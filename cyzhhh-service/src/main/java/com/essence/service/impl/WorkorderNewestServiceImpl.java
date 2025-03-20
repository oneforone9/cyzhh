package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.api.WorkorderNewestService;
import com.essence.interfaces.dot.*;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.EventBaseDTO;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.WorkorderNewestEsr;
import com.essence.interfaces.model.WorkorderNewestEsu;
import com.essence.interfaces.param.WorkorderNewestEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderNewestEtoT;
import com.essence.service.converter.ConverterWorkorderNewestTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhy
 * @since 2022/10/28 14:47
 */
@Service
public class WorkorderNewestServiceImpl extends BaseApiImpl<WorkorderNewestEsu, WorkorderNewestEsp, WorkorderNewestEsr, WorkorderNewest> implements WorkorderNewestService {
    @Resource
    private WorkorderRecordGeomDao workorderRecordGeomDao ;
    @Autowired
    private WorkorderNewestDao workorderNewestDao;
    @Autowired
    private ConverterWorkorderNewestEtoT converterWorkorderNewestEtoT;
    @Autowired
    private ConverterWorkorderNewestTtoR converterWorkorderNewestTtoR;
    @Autowired
    private FileBaseService fileBaseService;
    @Resource
    private WorkorderBaseDao workorderBaseDao;
    @Resource
    private EventBaseDao eventBaseDao;
    @Resource
    private ReaBaseDao reaBaseDao;
    @Resource
    private WorkorderProcessDao workorderProcessDao;
    @Resource
    private ReaFocusGeomDao reaFocusGeomDao;
    @Autowired
    private DepartmentBaseDao departmentBaseDao;
    @Autowired
    private RelPersonDepartmentDao relPersonDepartmentDao;

    public WorkorderNewestServiceImpl(WorkorderNewestDao workorderNewestDao, ConverterWorkorderNewestEtoT converterWorkorderNewestEtoT, ConverterWorkorderNewestTtoR converterWorkorderNewestTtoR) {
        super(workorderNewestDao, converterWorkorderNewestEtoT, converterWorkorderNewestTtoR);
    }

    @Override
    public WorkorderNewestEsr findById(Serializable id) {
        WorkorderNewestEsr workorderNewestEsr = super.findById(id);
        if (null == workorderNewestEsr) {
            return workorderNewestEsr;
        }
        //查看该工单的过程表是否有过程拥有 整改时间 如果有的话 则表示是 整改工单
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_id", workorderNewestEsr.getId());
        List<WorkorderProcess> workorderProcess = workorderProcessDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(workorderProcess)) {
            List<WorkorderProcess> collect = workorderProcess.parallelStream().filter(workorderProcess1 -> {
                return workorderProcess1.getFixTime() != null;
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(collect)) {
                //不为空表示拥有
                workorderNewestEsr.setRefused(true);
            }
        }


        // 查询图片
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("classId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(Arrays.asList(ItemConstant.ORDER_FILE_PREFIX + workorderNewestEsr.getId(), ItemConstant.QUES_FILE_PREFIX + workorderNewestEsr.getId()));
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
        List<FileBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return workorderNewestEsr;
        }

        Map<String, List<FileBaseEsr>> fileMap = items.stream().collect(Collectors.groupingBy(FileBaseEsr::getClassId));
        workorderNewestEsr.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + workorderNewestEsr.getId()));
        workorderNewestEsr.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + workorderNewestEsr.getId()));

        return workorderNewestEsr;
    }

    @Override
    public Paginator<WorkorderNewestEsr> findByPaginatorByCompanyId(PaginatorParam param) {
        //查询
        Paginator<WorkorderNewestEsr> paginator = super.findByPaginator(param);
        List<WorkorderNewestEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return paginator;
        }
        Date  date  = new Date();//系统当前时间
        //先判断当前系统时间是否大于截止时间

        items.forEach(p -> {
            if ( ! p.getOrderType().equals("7")){
                Date endTime = p.getEndTime();//截止时间
                int i = date.compareTo(endTime);
                if(i > 0){
                    String distanceDateTime = TimeUtil.getDistanceDateTime(endTime, date);
                    p.setExpirationTime("已超期："+ distanceDateTime);
                }else{
                    String distanceDateTime = TimeUtil.getDistanceDateTime(date, endTime);
                    p.setExpirationTime("距到期：" + distanceDateTime);
                }
            }


        });
        for (WorkorderNewestEsr item : items) {
            //查看该工单的过程表是否有过程拥有 整改时间 如果有的话 则表示是 整改工单
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id", item.getId());
            List<WorkorderProcess> workorderProcess = workorderProcessDao.selectList(wrapper);
            if (CollUtil.isNotEmpty(workorderProcess)) {
                List<WorkorderProcess> collect = workorderProcess.parallelStream().filter(workorderProcess1 -> {
                    return workorderProcess1.getFixTime() != null;
                }).sorted(Comparator.comparing(WorkorderProcess::getOrderTime).reversed()).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    //不为空表示拥有
                    item.setRefused(true);
                    item.setFixTime(collect.get(0).getFixTime());
                }
            }
        }

        // 判断整改 逾期 或者 按期
        items.forEach(p -> {
            //完成时间
            Date rejectTime = p.getRejectTime();
            //整改截止时间
            Date fixTime = p.getFixTime();
            if (null != rejectTime && null != fixTime) {
                int i = rejectTime.compareTo(fixTime);
                if (i > 0) {
                    String distanceDateTime = TimeUtil.getDistanceDateTime(fixTime, rejectTime);
                    p.setExpireFixTime("逾期：" + distanceDateTime);
                } else {
                    String distanceDateTime = TimeUtil.getDistanceDateTime(rejectTime, fixTime);
                    p.setExpireFixTime("按期：" + distanceDateTime);
                }
            }

        });
        // 获取关联建
        List<String> classIds = items.stream().map(p -> {
            Map<String, String> map = new HashMap<>();
            map.put(ItemConstant.QUES_FILE_PREFIX, ItemConstant.QUES_FILE_PREFIX + p.getId());
            map.put(ItemConstant.ORDER_FILE_PREFIX, ItemConstant.ORDER_FILE_PREFIX + p.getId());
            return map;
        }).flatMap(m -> m.values().stream()).collect(Collectors.toList());
        // 查询图片
        PaginatorParam fileparam = new PaginatorParam();
        fileparam.setPageSize(0);
        fileparam.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("classId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(classIds);
        currency.add(criterion);
        fileparam.setCurrency(currency);
        Paginator<FileBaseEsr> filePaginator = fileBaseService.findByPaginator(fileparam);
        List<FileBaseEsr> fileItems = filePaginator.getItems();
        if (CollectionUtils.isEmpty(fileItems)) {
            return paginator;
        }

        Map<String, List<FileBaseEsr>> fileMap = fileItems.stream().collect(Collectors.groupingBy(FileBaseEsr::getClassId));
        items.forEach(p -> {
            p.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + p.getId()));
            p.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + p.getId()));
        });
        return paginator;
    }

    @Override
    public Paginator<WorkorderNewestEsr> findByPaginator(PaginatorParam param) {
        //20240324兼容巡河员组长权限可以看到所在小组其他人已终止和待审核的工单
       List<Criterion> conditions = param.getConditions();
        Boolean flag = false;
        for (int q = 0; q < conditions.size(); q++) {
            String fieldName = conditions.get(q).getFieldName();
            //增加限制班长只能看到组员已终止和待审核的工单，其他的看不到
            if ("orderStatus".equals(fieldName)){
                ArrayList list = (ArrayList) conditions.get(q).getValue();
                for (int z = 0; z < list.size(); z++) {
                    Object o = list.get(z);
                    if(ItemConstant.ORDER_STATUS_STOP_FINAL.equals(o.toString()) ||ItemConstant.ORDER_STATUS_OVER.equals(o.toString())
                            || ItemConstant.ORDER_STATUS_EXAMINNING.equals(o.toString()) || ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(o.toString())){
                        //有权限
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            for (int i = 0; i < conditions.size(); i++) {
                String fieldName = conditions.get(i).getFieldName();
                if ("personId".equals(fieldName)){
                    String personId = (String) conditions.get(i).getValue();
                    //根据personId判断当前用户是否是巡河员组长，
                    QueryWrapper<DepartmentBase> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("person_base_id",personId);
                    List<DepartmentBase> departmentBases = departmentBaseDao.selectList(queryWrapper);

                    if(departmentBases.size()>0){
                        DepartmentBase departmentBase = departmentBases.get(0);
                        //获取所在小组所有人的personId
                        QueryWrapper<RelPersonDepartment> wrapper = new QueryWrapper();
                        wrapper.eq("department_id",departmentBase.getId());
                        List<RelPersonDepartment> list = relPersonDepartmentDao.selectList(wrapper);
                        String[] personIdArray = new String[list.size()];
                        for (int k = 0; k < list.size(); k++) {
                            String personId1 = list.get(k).getPersonId();
                            personIdArray[k] =personId1;
                        }
                        conditions.get(i).setValue(personIdArray);
                        conditions.get(i).setOperator("IN");
                        break;
                    }
                }
            }
        }
        //20240324兼容巡河员组长权限可以看到所在小组其他人已终止和待审核的工单

        Paginator<WorkorderNewestEsr> paginator = super.findByPaginator(param);
        List<WorkorderNewestEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return paginator;
        }
        //20230725增加已到期
        Date  date  = new Date();//系统当前时间
        //先判断当前系统时间是否大于截止时间  OrderType() == 7 的工单是群众上报的问题工单
        items.forEach(p -> {
            if ( ! p.getOrderType().equals("7")){
                Date endTime = p.getEndTime();//截止时间
                int i = date.compareTo(endTime);
                if(i > 0){
                    String distanceDateTime = TimeUtil.getDistanceDateTime(endTime, date);
                    p.setExpirationTime("已超期："+ distanceDateTime);
                }else{
                    String distanceDateTime = TimeUtil.getDistanceDateTime(date, endTime);
                    p.setExpirationTime("距到期：" + distanceDateTime);
                }
            }


        });

        //20230725增加已到期

        // 获取关联建
        List<String> classIds = items.stream().map(p -> {
            Map<String, String> map = new HashMap<>();
            map.put(ItemConstant.QUES_FILE_PREFIX, ItemConstant.QUES_FILE_PREFIX + p.getId());
            map.put(ItemConstant.ORDER_FILE_PREFIX, ItemConstant.ORDER_FILE_PREFIX + p.getId());
            return map;
        }).flatMap(m -> m.values().stream()).collect(Collectors.toList());
        for (WorkorderNewestEsr item : items) {
            //查看该工单的过程表是否有过程拥有 整改时间 如果有的话 则表示是 整改工单
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id", item.getId());
            List<WorkorderProcess> workorderProcess = workorderProcessDao.selectList(wrapper);
            if (CollUtil.isNotEmpty(workorderProcess)) {
                List<WorkorderProcess> collect = workorderProcess.parallelStream().filter(workorderProcess1 -> {
                    return workorderProcess1.getFixTime() != null;
                }).sorted(Comparator.comparing(WorkorderProcess::getOrderTime).reversed()).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    //不为空表示拥有
                    item.setRefused(true);
                    item.setFixTime(collect.get(0).getFixTime());
                }
            }
        }

        // 判断整改 逾期 或者 按期
        items.forEach(p -> {
            //完成时间
            Date rejectTime = p.getRejectTime();
            //整改截止时间
            Date fixTime = p.getFixTime();
            if (null != rejectTime && null != fixTime) {
                int i = rejectTime.compareTo(fixTime);
                if (i > 0) {
                    String distanceDateTime = TimeUtil.getDistanceDateTime(fixTime, rejectTime);
                    p.setExpireFixTime("逾期：" + distanceDateTime);
                } else {
                    String distanceDateTime = TimeUtil.getDistanceDateTime(rejectTime, fixTime);
                    p.setExpireFixTime("按期：" + distanceDateTime);
                }
            }

        });

        items = items.parallelStream().sorted(Comparator.comparing(WorkorderNewestEsr::isRefused)).collect(Collectors.toList());
        // 查询图片
        PaginatorParam fileparam = new PaginatorParam();
        fileparam.setPageSize(0);
        fileparam.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("classId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(classIds);
        currency.add(criterion);
        fileparam.setCurrency(currency);
        Paginator<FileBaseEsr> filePaginator = fileBaseService.findByPaginator(fileparam);
        List<FileBaseEsr> fileItems = filePaginator.getItems();
        if (CollectionUtils.isEmpty(fileItems)) {
            return paginator;
        }

        Map<String, List<FileBaseEsr>> fileMap = fileItems.stream().collect(Collectors.groupingBy(FileBaseEsr::getClassId));
        items.forEach(p -> {
            p.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + p.getId()));
            p.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + p.getId()));
        });
        return paginator;
    }

    @Override
    public Map<String,Object> findByPaginatorCount(PaginatorParam param) {
        Paginator<WorkorderNewestEsr> paginator = super.findByPaginator(param);
        List<WorkorderNewestEsr> items = paginator.getItems();
        //进行统计数据
        if (CollectionUtils.isEmpty(items)) {
            Map<String, Object> map = new HashMap<>();
            map.put("noHandout", 0);
            map.put("unStart", 0);
            map.put("over", 0);
            map.put("running", 0);
            map.put("audit", 0);
            map.put("end", 0);
            return map;
        }else{
            //进行统计数据
            // 未派发-1   未开始-2,7  已终止-6,-2  进行中-3  待审核-4,-1  已归档-5
            List<WorkorderNewestEsr> noHandout = items.parallelStream().filter(WorkorderNewestEsr -> WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_NO_HANDOUT)).collect(Collectors.toList());
            List<WorkorderNewestEsr> unStart = items.parallelStream().filter(WorkorderNewestEsr -> (WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_NO_START)) || WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_URGE) || WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_REFUSE)).collect(Collectors.toList());
            List<WorkorderNewestEsr> over = items.parallelStream().filter(WorkorderNewestEsr -> (WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_OVER)) || WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_STOP_FINAL)).collect(Collectors.toList());
            List<WorkorderNewestEsr> running = items.parallelStream().filter(WorkorderNewestEsr -> WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_RUNNING)).collect(Collectors.toList());
            List<WorkorderNewestEsr> audit = items.parallelStream().filter(WorkorderNewestEsr -> (WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_EXAMINNING)) || WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_STOP_AUDIT) || WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_YHWC)).collect(Collectors.toList());
            List<WorkorderNewestEsr> end = items.parallelStream().filter(WorkorderNewestEsr -> WorkorderNewestEsr.getOrderStatus().equals(ItemConstant.ORDER_STATUS_END)).collect(Collectors.toList());

            Map<String, Object> map = new HashMap<>();
            map.put("noHandout", noHandout.size());
            map.put("unStart", unStart.size());
            map.put("over", over.size());
            map.put("running", running.size());
            map.put("audit", audit.size());
            map.put("end", end.size());
            return map;
        }

    }

    /**
     * 查询日期下的工单轨迹
     * @param workMarkRequestDto
     * @return
     */
    @Override
    public  List<WorkMarkResDto> getWorkPortal(WorkMarkRequestDto workMarkRequestDto) {
        List<WorkMarkResDto> resDtos = new ArrayList<>();
        String personId = workMarkRequestDto.getPersonId();
        String personName = workMarkRequestDto.getPersonName();
        String orderStatus = workMarkRequestDto.getOrderStatus();
        String time = workMarkRequestDto.getTime();
        DateTime parse = DateUtil.parse(time);
        DateTime start = DateUtil.beginOfDay(parse);
        DateTime end = DateUtil.endOfDay(parse);
        // 获取当前节点的 巡查工单
        QueryWrapper<WorkorderNewest> queryWrapper = new QueryWrapper<>();
        if(orderStatus !=null && !"".equals(orderStatus)){
           // -1,-2,2,6,7 未巡
           // 4,5已巡
           //3-巡河中
            if("未巡".equals(orderStatus)){
                queryWrapper.in("orderStatus",ItemConstant.ORDER_STATUS_STOP_AUDIT,ItemConstant.ORDER_STATUS_STOP_FINAL,ItemConstant.ORDER_STATUS_NO_START,
                        ItemConstant.ORDER_STATUS_OVER,ItemConstant.ORDER_STATUS_URGE);
            }
            if("已巡".equals(orderStatus)){
                queryWrapper.in("orderStatus",ItemConstant.ORDER_STATUS_EXAMINNING,ItemConstant.ORDER_STATUS_END);
            }
            if("巡河中".equals(orderStatus)){
                queryWrapper.in("orderStatus", ItemConstant.ORDER_STATUS_RUNNING);
            }

        }
        if (!"".equals(personId) && personId != null) {
            queryWrapper.eq("person_id", personId);
        }

        if (!"".equals(personId) && personId != null) {
            queryWrapper.eq("person_id", personId);
        }
        if (!"".equals(personName) && personName != null) {
            queryWrapper.like("person_Name", personName);
        }
        if(!"".equals(workMarkRequestDto.getUnitId()) && workMarkRequestDto.getUnitId() != null){
            queryWrapper.eq("unit_id", workMarkRequestDto.getUnitId());
        }
        if(!"".equals(start)){
            queryWrapper.ge("start_time", start);
            queryWrapper.le("end_time", end);
        }
        queryWrapper.eq("order_type", 1);

        List<WorkorderNewest> workorderNewestList = workorderNewestDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(workorderNewestList)){
            for (WorkorderNewest workorderNewest : workorderNewestList) {
                WorkMarkResDto workMarkResDto = new WorkMarkResDto();
                BeanUtil.copyProperties(workorderNewest,workMarkResDto);
                String id = workorderNewest.getId();
                //获取每个工单下的 重点河段
                QueryWrapper<WorkorderRecordGeom> wrapper = new QueryWrapper<>();
                wrapper.eq("order_id", id);
                wrapper.eq(StrUtil.isNotEmpty(workMarkRequestDto.getReaId()) ,"rea_id", workMarkRequestDto.getReaId());
                List<WorkorderRecordGeom> workorderRecordGeoms = workorderRecordGeomDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(workorderRecordGeoms)){
                    List<WorkorderRecordGeomDto> workorderRecordGeomDtos = BeanUtil.copyToList(workorderRecordGeoms, WorkorderRecordGeomDto.class);
                    workMarkResDto.setWorkorderRecordGeomDtos(workorderRecordGeomDtos);
                    resDtos.add(workMarkResDto);
                }

            }
        }

        return resDtos;
    }

    @Override
    public PortalUserCountDto getPortalCount(String unitId) {
        PortalUserCountDto portalUserCountDto = new PortalUserCountDto();
        DateTime start = DateUtil.beginOfDay(new Date());
        DateTime end = DateUtil.endOfDay(new Date());
        QueryWrapper<WorkorderNewest> queryWrapper = new QueryWrapper();
        queryWrapper.ge("start_time",start);
        queryWrapper.le("start_time",end);
        queryWrapper.eq("order_type",ItemConstant.ORDER_TYPE_XC);
        if(unitId !=null && !"".equals(unitId)){
            queryWrapper.eq("unit_id",unitId);
        }
        List<WorkorderNewest> workorderNewests = workorderNewestDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(workorderNewests)){
            List<WorkorderNewest> running = workorderNewests.parallelStream().filter(workorderNewest -> workorderNewest.getOrderStatus().equals(ItemConstant.ORDER_STATUS_RUNNING)).collect(Collectors.toList());

            List<WorkorderNewest> complete = workorderNewests.parallelStream().filter(workorderNewest -> workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_EXAMINNING) >= 0).collect(Collectors.toList());
            portalUserCountDto.setRunningNum(running.size());
            portalUserCountDto.setCompleteNum(complete.size());
            portalUserCountDto.setPortalNum(workorderNewests.size());
        }
        return portalUserCountDto;
    }

    @Override
    public List<PortalEventCountDto> getRiverEventStatistic(String start, String end, String type) {
        List<PortalEventCountDto> res = new ArrayList<>();
        DateTime startTime = DateUtil.parse(start);
        DateTime emdTime = DateUtil.parse(end);
        if (type.equals("1")){
            //查询 当前时间下的事件
            QueryWrapper eventQuery = new QueryWrapper();
            eventQuery.ge("event_time",startTime);
            eventQuery.le("event_time",emdTime);
            List<EventBase> eventBases = eventBaseDao.selectList(eventQuery);
            Map<String, List<EventBaseDTO>> evenOrdertMap = new HashMap<>();
            if (CollUtil.isNotEmpty(eventBases)){
                List<EventBaseDTO> eventBaseDTOS = BeanUtil.copyToList(eventBases, EventBaseDTO.class);
                evenOrdertMap = eventBaseDTOS.parallelStream().collect(Collectors.groupingBy(eventBaseDTO -> {
                    return eventBaseDTO.getOrderId();
                }));
            }


            if (CollUtil.isNotEmpty(evenOrdertMap)){
                for (String key : evenOrdertMap.keySet()) {
                    PortalEventCountDto portalEventCountDto = new PortalEventCountDto();

                    int size = evenOrdertMap.get(key).size();
                    QueryWrapper<WorkorderRecordGeom> wrapper = new QueryWrapper<>();
                    wrapper.eq("order_id", key);
                    WorkorderRecordGeom workorderRecordGeoms = workorderRecordGeomDao.selectOne(wrapper);
                    String geom = workorderRecordGeoms.getGeom();
                    String reaId = workorderRecordGeoms.getReaId();
                    String reaName = workorderRecordGeoms.getReaName();
                    portalEventCountDto.setGeom(geom);
                    portalEventCountDto.setNum(size);
                    portalEventCountDto.setReaId(reaId);
                    portalEventCountDto.setReaName(reaName);
                    res.add(portalEventCountDto);
                }
            }
        }else {
            QueryWrapper<WorkorderProcess> processQueryWrapper = new QueryWrapper<>();
            processQueryWrapper.eq("order_status",ItemConstant.ORDER_STATUS_RUNNING);
            processQueryWrapper.ge("order_time",startTime);
            processQueryWrapper.le("order_time",emdTime);
            List<WorkorderProcess> workorderProcesses = workorderProcessDao.selectList(processQueryWrapper);
            if (CollUtil.isNotEmpty(workorderProcesses)){
                Map<String, List<WorkorderProcess>> orderMap = workorderProcesses.parallelStream().collect(Collectors.groupingBy(WorkorderProcess::getOrderId));
                List<String> orderIds = orderMap.keySet().parallelStream().collect(Collectors.toList());
                QueryWrapper<WorkorderRecordGeom> geomQueryWrapper = new QueryWrapper<>();
                geomQueryWrapper.in("order_id",orderIds);
                List<WorkorderRecordGeom> workorderRecordGeoms = workorderRecordGeomDao.selectList(geomQueryWrapper);
                Map<String, List<WorkorderRecordGeom>> orderGeomMap = workorderRecordGeoms.parallelStream().collect(Collectors.groupingBy(WorkorderRecordGeom::getReaId));
                for (String reaId : orderGeomMap.keySet()) {
                    // 该 重点位置下的 重点位置工单
                    List<WorkorderRecordGeom> workorderRecordGeoms1 = orderGeomMap.get(reaId);
                    WorkorderRecordGeom workorderRecordGeom1 = workorderRecordGeoms1.get(0);
                    int size = 0;
                    PortalEventCountDto portalEventCountDto = new PortalEventCountDto();
                    for (WorkorderRecordGeom workorderRecordGeom : workorderRecordGeoms1) {

                        String orderId = workorderRecordGeom.getOrderId();
                        List<WorkorderProcess> workorderProcesses1 = orderMap.get(orderId);
                        if (CollUtil.isNotEmpty(workorderProcesses1)){
                            List<WorkorderProcess> signPoints = workorderProcesses1.parallelStream().filter(workorderProcess -> {
                                return StrUtil.isNotEmpty(workorderProcess.getOperation());
                            }).filter(workorderProcess -> {
                                return workorderProcess.getOperation().contains("signPoints");
                            }).collect(Collectors.toList());
                            size += signPoints.size();
                        }
                    }
                    portalEventCountDto.setNum(size);
                    String geom = workorderRecordGeom1.getGeom();
                    String reaName = workorderRecordGeom1.getReaName();
                    portalEventCountDto.setGeom(geom);
                    portalEventCountDto.setReaId(workorderRecordGeom1.getReaId());
                    portalEventCountDto.setReaName(reaName);
                    res.add(portalEventCountDto);
                }
            }
        }
        return res;
    }

    @Override
    public List<PortalEventCountDto> getRiverSignPointStatistic(String start, String end) {

        return null;
    }

    @Override
    public PortalUserCountDto getPortalUserCount(String unitId) {
        PortalUserCountDto portalUserCountDto = new PortalUserCountDto();
        DateTime start = DateUtil.beginOfDay(new Date());
        DateTime end = DateUtil.endOfDay(new Date());
        QueryWrapper<WorkorderNewest> queryWrapper = new QueryWrapper();
        queryWrapper.ge("start_time",start);
        queryWrapper.le("start_time",end);
        queryWrapper.eq("order_type",ItemConstant.ORDER_TYPE_XC);
        if(unitId !=null && !"".equals(unitId)){
            queryWrapper.eq("unit_id",unitId);
        }
        List<WorkorderNewest> workorderNewests = workorderNewestDao.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(workorderNewests)){
            Map<String, List<WorkorderNewest>> personMap = workorderNewests.parallelStream().collect(Collectors.groupingBy(WorkorderNewest::getPersonId));
            //完成人数
            Integer completeNum = 0;
            //进行中数量
            Integer runningNum = 0;
            //未开始工单
            Integer unStartNum = 0;
            for (String personId : personMap.keySet()) {
                List<WorkorderNewest> workorderNewests1 = personMap.get(personId);
                List<WorkorderNewest> unStart = workorderNewests1.parallelStream().filter(workorderNewest -> workorderNewest.getOrderStatus().equals(ItemConstant.ORDER_STATUS_NO_START)).collect(Collectors.toList());
                List<WorkorderNewest> complete = workorderNewests1.parallelStream().filter(workorderNewest -> workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_EXAMINNING) >= 0).collect(Collectors.toList());

                if (unStart.size() == workorderNewests1.size()){
                    ++ unStartNum;
                }
                else if (complete.size() == workorderNewests1.size()){
                    ++ completeNum;
                }
            }
            runningNum = personMap.size()-completeNum-unStartNum;

            //巡河人数
            portalUserCountDto.setPortalNum(personMap.size());
            portalUserCountDto.setCompleteNum(completeNum);
            portalUserCountDto.setRunningNum(runningNum);
            portalUserCountDto.setUnStartNum(unStartNum);
        }
        return portalUserCountDto;
    }

    /**
     * 查询 未巡河河段
     * @param workMarkRequestDto
     * @return
     */
    @Override
    public List<WorkorderNewest> unRiver(WorkMarkRequestDto workMarkRequestDto) {
        String time = workMarkRequestDto.getTime();
        DateTime parse = DateUtil.parse(time);
        DateTime start = DateUtil.beginOfDay(parse);
        DateTime end = DateUtil.endOfDay(parse);

        // 获取当前节点的 巡查工单
        QueryWrapper<WorkorderNewest> queryWrapper = new QueryWrapper<>();

        if(!"".equals(workMarkRequestDto.getUnitId()) && workMarkRequestDto.getUnitId() != null){
            queryWrapper.eq("unit_id", workMarkRequestDto.getUnitId());
        }
        if(!"".equals(start)){
            queryWrapper.ge("start_time", start);
            queryWrapper.le("end_time", end);
        }
        queryWrapper.eq("order_type", ItemConstant.ORDER_TYPE_XC);
        //未完成巡河的 未开始和进行中
        queryWrapper.in("order_status", ItemConstant.ORDER_STATUS_NO_START, ItemConstant.ORDER_STATUS_RUNNING, ItemConstant.ORDER_STATUS_URGE);

        List<WorkorderNewest> workorderNewestList = workorderNewestDao.selectList(queryWrapper);

        return workorderNewestList;
    }

    @Override
    public List<WorkorderNewest> selectWorkorderBase(String flag, Date start, Date end) {

        QueryWrapper<WorkorderNewest> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();
        if (StringUtil.isNotBlank(flag)) {
            if (flag.equals("1")) {
                list.add("2");
                list.add("3");
            } else {
                list.add("4");
                list.add("5");
            }
            queryWrapper.lambda().in(WorkorderNewest::getOrderType, list);
        }
        queryWrapper.lambda().eq(WorkorderNewest::getIsDelete, "0");
        queryWrapper.lambda().ge(WorkorderNewest::getGmtCreate, start);
        queryWrapper.lambda().le(WorkorderNewest::getGmtCreate, end);
        queryWrapper.lambda().isNotNull(WorkorderNewest::getCompanyId);
        List<WorkorderNewest> workorderBases = Optional.ofNullable(workorderNewestDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        return workorderBases;
    }
}
