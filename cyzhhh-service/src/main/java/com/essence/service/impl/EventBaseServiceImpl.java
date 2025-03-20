package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.EventBaseDao;
import com.essence.dao.FileBaseDao;
import com.essence.dao.TWorkorderProcessNewestDao;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.entity.EventBase;
import com.essence.dao.entity.FileBase;
import com.essence.dao.entity.TWorkorderProcessNewestDto;
import com.essence.dao.entity.WorkorderBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.CodeGenerateService;
import com.essence.interfaces.api.EventBaseService;
import com.essence.interfaces.api.EventBaseStatusService;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.EventBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterEventBaseEtoT;
import com.essence.service.converter.ConverterEventBaseTtoR;
import com.essence.service.utils.DataUtils;
import com.google.common.collect.Lists;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class EventBaseServiceImpl extends BaseApiImpl<EventBaseEsu, EventBaseEsp, EventBaseEsr, EventBase> implements EventBaseService {

    @Autowired
    private EventBaseDao eventBaseDao;
    @Autowired
    private ConverterEventBaseEtoT converterEventBaseEtoT;
    @Autowired
    private ConverterEventBaseTtoR converterEventBaseTtoR;
    @Autowired
    private CodeGenerateService codeGenerateService;
    @Autowired
    private EventBaseStatusService eventBaseStatusService;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private FileBaseDao fileBaseDao;
    @Resource
    private WorkorderBaseDao workorderBaseDao;
    @Autowired
    private EventBaseService eventBaseService;
    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;

    public EventBaseServiceImpl(EventBaseDao eventBaseDao, ConverterEventBaseEtoT converterEventBaseEtoT, ConverterEventBaseTtoR converterEventBaseTtoR) {
        super(eventBaseDao, converterEventBaseEtoT, converterEventBaseTtoR);
    }
    @Transactional
    @Override
    public String insertEventBase(EventBaseEsu eventBaseEsu) {
        String id =UuidUtil.get32UUIDStr();
        eventBaseEsu.setId(id);
        // 案件编号 (日期+案件分类编码+顺序码)
        if (StringUtils.isEmpty(eventBaseEsu.getEventCode())) {
            // 日期
            String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 案件分类编码
            String eventClass = eventBaseEsu.getEventClass();
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd + eventClass, 1);
            eventBaseEsu.setEventCode(codes.get(0));
        }
        // 案件时间
        if (null == eventBaseEsu.getEventTime()) {
            eventBaseEsu.setEventTime(new Date());
        }
        // 未删除
        eventBaseEsu.setIsDelete(ItemConstant.EVENT_NO_DELETE);
        // 新增
        int insert = super.insert(eventBaseEsu);
        // 如果传入图片信息关联图片
        List<String> fileIds = eventBaseEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return id;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.QUES_FILE_PREFIX + eventBaseEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });
        return id;
    }

    /**
     * 根据条件分页查询作业记录
     * @param param
     * @return
     */
    @Override
    public Paginator<EventBaseEsr> findByPaginatorBase(PaginatorParam param) {
        Paginator<EventBaseEsr> paginator = super.findByPaginator(param);
        List<EventBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return paginator;
        }

        // 获取关联建
        List<String> typeIds = items.stream().map(p -> {
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
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(typeIds);
        currency.add(criterion);
        fileparam.setCurrency(currency);
        Paginator<FileBaseEsr> filePaginator = fileBaseService.findByPaginator(fileparam);
        List<FileBaseEsr> fileItems = filePaginator.getItems();
        if (CollectionUtils.isEmpty(fileItems)) {
            return paginator;
        }

        Map<String, List<FileBaseEsr>> fileMap = fileItems.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        items.forEach(p -> {
            p.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + p.getId()));
            p.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + p.getId()));
        });
        items = items.stream().sorted(Comparator.comparing(EventBaseEsr::getEventTime).reversed()).collect(Collectors.toList());
        paginator.setItems(items);
        return paginator;
    }

    @Transactional
    @Override
    public int insert(EventBaseEsu eventBaseEsu) {
        String id =UuidUtil.get32UUIDStr();
        eventBaseEsu.setId(id);
        // 案件编号 (日期+案件分类编码+顺序码)
        if (StringUtils.isEmpty(eventBaseEsu.getEventCode())) {
            // 日期
            String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 案件分类编码
            String eventClass = eventBaseEsu.getEventClass();
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd + eventClass, 1);
            eventBaseEsu.setEventCode(codes.get(0));
        }
        // 案件时间
        if (null == eventBaseEsu.getEventTime()) {
            eventBaseEsu.setEventTime(new Date());
        }
        // 未删除
        eventBaseEsu.setIsDelete(ItemConstant.EVENT_NO_DELETE);
        // 新增
        int insert = super.insert(eventBaseEsu);
        // 如果传入图片信息关联图片
        List<String> fileIds = eventBaseEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return insert;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.QUES_FILE_PREFIX + eventBaseEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });
        return insert;
    }



    @Transactional
    @Override
    public int update(@RequestBody EventBaseEsu eventBaseEsu) {
        //控制并发不能重复处理
        EventBase eventBase = eventBaseDao.selectById(eventBaseEsu.getId());
        String status = eventBase.getStatus();
        if("1".equals(status)){
            throw new BusinessException("案件状态为已办，不能重复处理");
        }
        //控制并发不能重复处理
        // 修改
        int insert = super.update(eventBaseEsu);
        // 如果传入图片信息关联图片
        List<String> fileIds = eventBaseEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return insert;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.ORDER_FILE_PREFIX + eventBaseEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });
        return insert;
    }

    @Override
    public int add(EventBaseEsu eventBaseEsu) {
        return super.insert(eventBaseEsu);
    }

    @Override
    public EventAnalysisDto getEventAnalysis(String unitId) {
        EventAnalysisDto eventAnalysisDto = new EventAnalysisDto();
        DateTime start = cn.hutool.core.date.DateUtil.beginOfMonth(new Date());

        DateTime end = cn.hutool.core.date.DateUtil.endOfMonth(new Date());
        QueryWrapper wrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            wrapper.eq("unit_id", unitId);
        }

        wrapper.le("event_time", end);
        wrapper.ge("event_time", start);
        List<EventBase> eventBases = eventBaseDao.selectList(wrapper);

        Map<String, List<EventBase>> eventTypeNow = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
        List<EventBase> eventBasesN1 = eventTypeNow.get("1") == null ? new ArrayList<>() : eventTypeNow.get("1");
        List<EventBase> eventBasesN2 = eventTypeNow.get("2") == null ? new ArrayList<>() : eventTypeNow.get("2");
        List<EventBase> eventBasesN3 = eventTypeNow.get("3") == null ? new ArrayList<>() : eventTypeNow.get("3");
        eventAnalysisDto.setWaterEnv(CollUtil.isEmpty(eventBasesN1) ? 0 : eventBasesN1.size());
        eventAnalysisDto.setRiverProject(CollUtil.isEmpty(eventBasesN2) ? 0 : eventBasesN2.size());
        eventAnalysisDto.setRiverRoundDevice(CollUtil.isEmpty(eventBasesN3) ? 0 : eventBasesN3.size());
        if (CollUtil.isEmpty(eventBases)) {
            eventAnalysisDto.setWaterEnvPercent(new BigDecimal(0));
            eventAnalysisDto.setRiverRoundDevicePercent(new BigDecimal(0));
            eventAnalysisDto.setRiverProjectPercent(new BigDecimal(0));

        } else {
            eventAnalysisDto.setWaterEnvPercent(new BigDecimal(eventAnalysisDto.getWaterEnv()).divide(new BigDecimal(eventBases.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            eventAnalysisDto.setRiverRoundDevicePercent(new BigDecimal(eventAnalysisDto.getRiverRoundDevice()).divide(new BigDecimal(eventBases.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            eventAnalysisDto.setRiverProjectPercent(new BigDecimal(eventAnalysisDto.getRiverProject()).divide(new BigDecimal(eventBases.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));

        }


        //获取上月
        DateTime lastStart = cn.hutool.core.date.DateUtil.beginOfMonth(cn.hutool.core.date.DateUtil.offsetMonth(new Date(), -1));
        DateTime lastEnd = cn.hutool.core.date.DateUtil.endOfMonth(cn.hutool.core.date.DateUtil.offsetMonth(new Date(), -1));
        QueryWrapper wrapperLast = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            wrapperLast.eq("unit_id", unitId);
        }
        wrapperLast.le("event_time", lastEnd);
        wrapperLast.ge("event_time", lastStart);
        List<EventBase> eventBasesLast = eventBaseDao.selectList(wrapperLast);

        Map<String, List<EventBase>> eventType = eventBasesLast.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
        List<EventBase> eventBases1 = eventType.get("1") == null ? new ArrayList<>() : eventType.get("1");
        List<EventBase> eventBases2 = eventType.get("2") == null ? new ArrayList<>() : eventType.get("2");
        List<EventBase> eventBases3 = eventType.get("3") == null ? new ArrayList<>() : eventType.get("3");
        int env = eventAnalysisDto.getWaterEnv() - eventBases1.size();
        if (eventBases1.size() == 0) {
            eventAnalysisDto.setWaterEnvPercentPre(new BigDecimal(0));
        } else {
            eventAnalysisDto.setWaterEnvPercentPre(new BigDecimal(env).divide(new BigDecimal(eventBases1.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }

        int device = eventAnalysisDto.getRiverRoundDevice() - eventBases2.size();
        if (eventBases2.size() == 0) {
            eventAnalysisDto.setRiverRoundDevicePercentPre(new BigDecimal(0));
        } else {
            eventAnalysisDto.setRiverRoundDevicePercentPre(new BigDecimal(device).divide(new BigDecimal(eventBases2.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }

        int pro = eventAnalysisDto.getRiverProject() - eventBases3.size();
        if (eventBases3.size() == 0) {
            eventAnalysisDto.setRiverProjectPercentPre(new BigDecimal(0));
        } else {
            eventAnalysisDto.setRiverProjectPercentPre(new BigDecimal(pro).divide(new BigDecimal(eventBases3.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }


        return eventAnalysisDto;
    }

    @Override
    public EventChannelDto getEventChannel(String unitId) {
        EventChannelDto eventChannelDto = new EventChannelDto();
        //案件数量
        List<Integer> caseNum = DataUtils.getInitList(6, 0);
        // 案件占比
        List<BigDecimal> casePercent = DataUtils.getInitBigdecimalList(6, new BigDecimal(0));
        //上月环比
        List<BigDecimal> compareLast = DataUtils.getInitBigdecimalList(6, new BigDecimal(0));
        DateTime start = cn.hutool.core.date.DateUtil.beginOfMonth(new Date());
        DateTime end = cn.hutool.core.date.DateUtil.endOfMonth(new Date());
        QueryWrapper wrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            wrapper.eq("unit_id", unitId);
        }

        wrapper.le("event_time", end);
        wrapper.ge("event_time", start);
        List<EventBase> eventBases = new ArrayList<>();
        eventBases = eventBaseDao.selectList(wrapper);

        //获取上月
        DateTime lastStart = cn.hutool.core.date.DateUtil.beginOfMonth(cn.hutool.core.date.DateUtil.offsetMonth(new Date(), -1));
        DateTime lastEnd = cn.hutool.core.date.DateUtil.endOfMonth(cn.hutool.core.date.DateUtil.offsetMonth(new Date(), -1));
        QueryWrapper wrapperLast = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            wrapperLast.eq("unit_id", unitId);
        }
        wrapperLast.le("event_time", lastEnd);
        wrapperLast.ge("event_time", lastStart);
        List<EventBase> eventBasesLast = new ArrayList<>();
        eventBasesLast = eventBaseDao.selectList(wrapperLast);


        caseNum.set(1, eventBases.size());
        //TODO  后续接入其它类型的数据可以 进行汇总求百分比 目前手动写入100%
        casePercent.set(1, new BigDecimal(100));
        if (eventBasesLast.size() == 0) {
            compareLast.set(1, new BigDecimal(0));
        } else {
            int current = eventBases.size() - eventBasesLast.size();
            BigDecimal percent = new BigDecimal(current).divide(new BigDecimal(eventBasesLast.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            compareLast.set(1, percent);
        }

        eventChannelDto.setCaseNum(caseNum);
        eventChannelDto.setCasePercent(casePercent);
        eventChannelDto.setCompareLast(compareLast);
        return eventChannelDto;
    }


    @Override
    public int deleteById(Serializable id) {
        EventBase eventBase = new EventBase();
        eventBase.setId((String) id);
        eventBase.setIsDelete(ItemConstant.EVENT_IS_DELETE);
        return eventBaseDao.updateById(eventBase);
    }

    @Override
    public EventBaseEsr findById(Serializable id) {
        EventBaseEsr eventBaseEsr = super.findById(id);
        if (null == eventBaseEsr) {
            return eventBaseEsr;
        }

        // 查询图片
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(Arrays.asList(ItemConstant.ORDER_FILE_PREFIX + eventBaseEsr.getId(), ItemConstant.QUES_FILE_PREFIX + eventBaseEsr.getId()));
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
        List<FileBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return eventBaseEsr;
        }

        Map<String, List<FileBaseEsr>> fileMap = items.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        eventBaseEsr.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + eventBaseEsr.getId()));
        eventBaseEsr.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + eventBaseEsr.getId()));

        return eventBaseEsr;
    }

    @Override
    public Paginator<EventBaseEsr> findByPaginator(PaginatorParam param) {
        //20230803优化
        //原因案件状态和orderManager 是从视图关联取到的，优化为业务逻辑取
        Paginator<EventBaseEsr> res = super.findByPaginator(param);
        List<EventBaseEsr> eventBaseEsrs = res.getItems();
        //20230803优化
        //对状态查询
//        Paginator<EventBaseStatusEsr> paginator = eventBaseStatusService.findByPaginator(param);
//        Paginator<EventBaseEsr> res = new Paginator<>();
//        List<EventBaseStatusEsr> statusEsrList = paginator.getItems();

        if (CollectionUtils.isEmpty(eventBaseEsrs)) {
            return res;
        }
//        List<EventBaseEsr> eventBaseEsrs = BeanUtil.copyToList(eventBaseEsrList, EventBaseEsr.class);
//        eventBaseEsrs = eventBaseEsrs.stream().sorted(Comparator.comparing(EventBaseEsr::getEventTime).reversed()).collect(Collectors.toList());
//        res.setItems(eventBaseEsrs);
        List<EventBaseEsr> items = res.getItems();
        items = items.stream().map(eventBaseEsr -> {
            if (StrUtil.isNotEmpty(eventBaseEsr.getProblemDesc())) {
                List<String> strings = new ArrayList<>();
                try {
                    strings = JSONObject.parseArray(eventBaseEsr.getProblemDesc(), String.class);
                } catch (Exception e) {
                    strings.add(eventBaseEsr.getProblemDesc());
                }
                eventBaseEsr.setProblemDescList(strings);
            }
            return eventBaseEsr;
        }).collect(Collectors.toList());
        List<String> typeIdsr = new ArrayList<>();
        // 获取关联建
        List<String> typeIds = items.stream().map(p -> {
            Map<String, String> map = new HashMap<>();
            map.put(ItemConstant.QUES_FILE_PREFIX, ItemConstant.QUES_FILE_PREFIX + p.getId());
            map.put(ItemConstant.ORDER_FILE_PREFIX, ItemConstant.ORDER_FILE_PREFIX + p.getId());
            typeIdsr.add(p.getId());
            return map;
        }).flatMap(m -> m.values().stream()).collect(Collectors.toList());
        // 查询图片
        PaginatorParam fileparam = new PaginatorParam();
        fileparam.setPageSize(0);
        fileparam.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(typeIds);
        currency.add(criterion);
        fileparam.setCurrency(currency);
        Paginator<FileBaseEsr> filePaginator = fileBaseService.findByPaginator(fileparam);
        List<FileBaseEsr> fileItems = filePaginator.getItems();
//        if (CollectionUtils.isEmpty(fileItems)) {
//            return res;
//        }
        ////20230803关联处理案件的状态和orderManager
        List<WorkorderBase> list = workorderBaseDao.selectAll(typeIdsr);

        Map<String, List<WorkorderBase>> collect = list.stream().filter(WorkorderBase -> {
            return StrUtil.isNotEmpty(WorkorderBase.getEventId());
        }).collect(Collectors.groupingBy(WorkorderBase::getEventId));

        Map<String, List<FileBaseEsr>> fileMap = fileItems.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        items.forEach(p -> {
            p.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + p.getId()));
            p.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + p.getId()));
            //20230803关联处理案件的状态和orderManager
            p.setOrderManager(collect.get(p.getId()) == null ? "" : collect.get(p.getId()).get(0).getOrderManager());
            if (collect.get(p.getId()) != null) {
                String orderStatus = collect.get(p.getId()).get(0).getOrderStatus();
                if("4".equals(orderStatus) || "5".equals(orderStatus)){
                    p.setEventStatus(1);
                }else{
                    p.setEventStatus(2);
                }
            } else {
                p.setEventStatus(2);
            }

            //20230803关联处理案件的状态和orderManager
        });
        res.setPageSize(param.getPageSize());
        res.setCurrentPage(param.getCurrentPage());
        res.setTotalCount(res.getTotalCount());
        res.setPageCount(res.getPageCount());

        return res;
    }

    @Override
    public String uploadPictures(EventBasePictures eventBasePictures) {
        List<String> fileId = eventBasePictures.getFile();
        List<FileBase> fileBases = fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().in(FileBase::getId, fileId));
        if (!CollectionUtils.isEmpty(fileBases)) {
            for (FileBase fileBase : fileBases) {
                if (StringUtils.isNotBlank(eventBasePictures.getType()) && eventBasePictures.getType().equals("1")) {
                    fileBase.setTypeId(ItemConstant.QUES_FILE_PREFIX + eventBasePictures.getId());
                } else {
                    fileBase.setTypeId(ItemConstant.ORDER_FILE_PREFIX + eventBasePictures.getId());
                }
                fileBaseDao.updateById(fileBase);
            }
        }
        return "上传成功！";
    }
//获取案件数据
    @Override
    public List<EventBase> selectEventBase( Date start, Date end,String flag) {
        QueryWrapper<EventBase> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(EventBase::getSendStatus,"1");
        queryWrapper.lambda().eq(EventBase::getDealUnit,"1");
        if (StringUtils.isNotBlank(flag)){
            List<String>list=new ArrayList<>();
            if (flag.equals("1")){
                list.add("2");
                list.add("3");
            }else {
                list.add("4");
                list.add("5");
            }
            queryWrapper.lambda().in(EventBase::getOrderType,list);
        }
        queryWrapper.lambda().ge(EventBase::getGmtCreate,start);
        queryWrapper.lambda().le(EventBase::getGmtCreate,end);
        List<EventBase> eventBases = Optional.ofNullable(eventBaseDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        return eventBases;
    }
}
