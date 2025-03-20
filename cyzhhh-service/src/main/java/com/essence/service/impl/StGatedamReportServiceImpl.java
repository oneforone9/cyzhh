package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StGatedamReportEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStGatedamReportEtoT;
import com.essence.service.converter.ConverterStGatedamReportTtoR;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 闸坝运行维保日志上报表(StGatedamReport)业务层
 *
 * @author liwy
 * @since 2023-03-15 11:55:54
 */
@Service
@Log4j2
public class StGatedamReportServiceImpl extends BaseApiImpl<StGatedamReportEsu, StGatedamReportEsp, StGatedamReportEsr, StGatedamReportDto> implements StGatedamReportService {

    @Autowired
    private WorkorderBaseService workorderBaseService;
    @Autowired
    private StGatedamReportDao stGatedamReportDao;
    @Autowired
    private ConverterStGatedamReportEtoT converterStGatedamReportEtoT;
    @Autowired
    private ConverterStGatedamReportTtoR converterStGatedamReportTtoR;
    @Autowired
    private StGatedamReportService stGatedamReportService;
    @Autowired
    private StGatedamReportRelationService stGatedamReportRelationService;
    @Autowired
    private StCompanyBaseService stCompanyBaseService;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private StGatedamReportRelationDao stGatedamReportRelationDao;
    @Resource
    private WorkorderBaseDao workorderBaseDao;
    @Resource
    private WorkorderProcessDao workorderProcessDao;
    @Autowired
    private StPlanOperateDao stPlanOperateDao;
    @Autowired
    private EventCompanyService eventCompanyService;
    @Autowired
    private StPlanPersonService stPlanPersonService;
    @Resource
    private StPlanOperateRejectDao stPlanOperateRejectDao;

    public StGatedamReportServiceImpl(StGatedamReportDao stGatedamReportDao, ConverterStGatedamReportEtoT converterStGatedamReportEtoT, ConverterStGatedamReportTtoR converterStGatedamReportTtoR) {
        super(stGatedamReportDao, converterStGatedamReportEtoT, converterStGatedamReportTtoR);
    }

    /**
     * 新增养维护修日志
     *
     * @param stGatedamReportEsu
     * @return
     */
    @Transactional
    @Override
    public Object addStGatedamReport(StGatedamReportEsu stGatedamReportEsu) {
        //闸坝运行维保日志上报表 主表id
        String baseId = UUID.randomUUID().toString().replace("-", "");
        stGatedamReportEsu.setIsDelete(ItemConstant.REPORT_NO_DELETE);
        stGatedamReportEsu.setId(baseId);
        String userId = stGatedamReportEsu.getCreator(); //当前登录人的userid
        //根据userId去第三方公司基础表中获取到相关联的信息
        StCompanyBaseEsr stCompanyBaseEsr = stCompanyBaseService.findById(userId);
        if (!"".equals(stCompanyBaseEsr) && stCompanyBaseEsr != null) {
            stGatedamReportEsu.setWorkUnit(stCompanyBaseEsr.getCompany()); //作业单位
            stGatedamReportEsu.setWorkManage(stCompanyBaseEsr.getManageName()); //负责人
            stGatedamReportEsu.setManagePhone(stCompanyBaseEsr.getManagePhone()); //负责人联系方式
        }

        //先增加主表
        int insert = stGatedamReportService.insert(stGatedamReportEsu);
        String workType = stGatedamReportEsu.getWorkType(); //（ 4维保 5运行)）
        if ("5".equals(workType)) {
            //将上传的图片检查记录表进行关联
            List<String> fileIds1 = stGatedamReportEsu.getFileIds();
            log.info("111111111111111fileIds" + fileIds1.size());

            if (CollectionUtils.isEmpty(fileIds1)) {
                return insert;
            }
            fileIds1.forEach(p -> {
                FileBaseEsu fileBaseEsu = new FileBaseEsu();
                fileBaseEsu.setId(p);
                fileBaseEsu.setTypeId(ItemConstant.GATEBASE_FILE_PREFIX + stGatedamReportEsu.getId());
                fileBaseService.update(fileBaseEsu);
                log.info("22222222fileIds----------" + p);
            });
        }

        //再增加关联表
        List<StGatedamReportRelationEsu> workList = stGatedamReportEsu.getWorkList();
        log.info("333333333333333fileIds" + workList.size());
        for (int i = 0; i < workList.size(); i++) {
            StGatedamReportRelationEsu stGatedamReportRelationEsu = workList.get(i);
            stGatedamReportRelationEsu.setStGatedamId(stGatedamReportEsu.getId());
            stGatedamReportRelationEsu.setGmtCreate(new Date());
            String id = UUID.randomUUID().toString().replace("-", "");
            stGatedamReportRelationEsu.setId(id);
            stGatedamReportRelationService.insert(stGatedamReportRelationEsu);
            //将上传的图片进行关联
            List<String> fileIds = stGatedamReportRelationEsu.getFileIds();
            if (CollectionUtils.isEmpty(fileIds)) {
                continue;
            }
            fileIds.forEach(p -> {
                FileBaseEsu fileBaseEsu = new FileBaseEsu();
                fileBaseEsu.setId(p);
                fileBaseEsu.setTypeId(ItemConstant.GATE_FILE_PREFIX + stGatedamReportRelationEsu.getId());
                fileBaseService.update(fileBaseEsu);
            });
        }

        return insert;
    }

    /**
     * 日志详情
     *
     * @param id
     * @return
     */
    @Override
    public Object searchById(String id) {
        //获取主表信息
        StGatedamReportEsr stGatedamReportEsr = super.findById(id);
        if (stGatedamReportEsr == null) {
            return stGatedamReportEsr;
        }
        //获取主表关联的检查记录表图片
        Paginator<FileBaseEsr> paginatorFile = this.getRelationUrl(id);
        stGatedamReportEsr.setReportFileList(paginatorFile.getItems());

        //获取关联表信息
        Paginator<StGatedamReportRelationEsr> byPaginator = this.getRelation(id);
        List<StGatedamReportRelationEsr> items = byPaginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return stGatedamReportEsr;
        }

        //关联获取到相关联的图片附件
        for (int i = 0; i < items.size(); i++) {
            StGatedamReportRelationEsr stGatedamReportRelationEsr = items.get(i);
            PaginatorParam param = new PaginatorParam();
            param.setPageSize(0);
            param.setCurrentPage(0);
            List currency = new ArrayList<>();
            Criterion criterion = new Criterion();
            criterion.setFieldName("typeId");
            criterion.setOperator(Criterion.IN);
            criterion.setValue(Arrays.asList(ItemConstant.GATE_FILE_PREFIX + stGatedamReportRelationEsr.getId()));
            currency.add(criterion);
            param.setCurrency(currency);
            Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
            List<FileBaseEsr> items2 = paginator.getItems();
            if (CollectionUtils.isEmpty(items2)) {
                continue;
            }
            Map<String, List<FileBaseEsr>> fileMap = items2.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
            stGatedamReportRelationEsr.setReportFileList(fileMap.get(ItemConstant.GATE_FILE_PREFIX + stGatedamReportRelationEsr.getId()));
        }
        stGatedamReportEsr.setStGatedamReportRelationEsrList(items);
        return stGatedamReportEsr;
    }

    /**
     * PC端-闸坝运行养护工作报告汇总列表
     *
     * @param stGatedamReportParam
     * @return
     */
    @Override
    public Paginator<StGatedamReportEsr> searchAll(StGatedamReportParam stGatedamReportParam) {
        PaginatorParam param = stGatedamReportParam.getParam();
        int currentPage = param.getCurrentPage();
        int pageSize = param.getPageSize();
        String startTime = stGatedamReportParam.getStartTime();
        String endTime = stGatedamReportParam.getEndTime();
        String workUnit = stGatedamReportParam.getWorkUnit();
        if ("".equals(workUnit)) {
            workUnit = null;
        }
        String stBRiverId = stGatedamReportParam.getStBRiverId();
        if ("".equals(stBRiverId)) {
            stBRiverId = null;
        }
        String workType = stGatedamReportParam.getWorkType();
        if ("".equals(workType)) {
            workType = null;
        }
        String unitId = stGatedamReportParam.getUnitId();
        if ("".equals(unitId)) {
            unitId = null;
        }
        String sttp = stGatedamReportParam.getSttp();
        if ("".equals(sttp)) {
            sttp = null;
        }
        List<StGatedamReportDto> list = stGatedamReportDao.searchAll(startTime, endTime, workUnit, stBRiverId, unitId, workType, sttp);
        //手动进行分页
        PageUtil<StGatedamReportDto> pageUtil = new PageUtil(list, currentPage, pageSize, null, null);
        List<StGatedamReportDto> recordsList = pageUtil.getRecords();
        List<StGatedamReportEsr> stGatedamReportEsrList = BeanUtil.copyToList(recordsList, StGatedamReportEsr.class);


        //运行的工单关联到检查记录表
        for (int i = 0; i < stGatedamReportEsrList.size(); i++) {
            StGatedamReportEsr stGatedamReportEsr = stGatedamReportEsrList.get(i);
            //获取主表关联的检查记录表图片
            Paginator<FileBaseEsr> paginatorFile = this.getRelationUrl(stGatedamReportEsr.getId());
            stGatedamReportEsr.setReportFileList(paginatorFile.getItems());
            //stGatedamReportEsrList.set(i,stGatedamReportEsr);

        }
        Paginator<StGatedamReportEsr> paginator = new Paginator<>();
        paginator.setPageSize(pageUtil.getPageSize());
        paginator.setCurrentPage(pageUtil.getCurrent());
        paginator.setTotalCount(pageUtil.getTotal());
        paginator.setPageCount(pageUtil.getPages());
        paginator.setItems(stGatedamReportEsrList);

        return paginator;
    }

    /**
     * 作业日历
     *
     * @param stGatedamReportParam
     * @return
     */
    @Override
    public Object searchByCondition(StGatedamReportParam2 stGatedamReportParam) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("workUnit");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(stGatedamReportParam.getWorkUnit());
        currency.add(criterion);

        Criterion criterion2 = new Criterion();
        criterion2.setFieldName("stBRiverId");
        criterion2.setOperator(Criterion.EQ);
        criterion2.setValue(stGatedamReportParam.getStBRiverId());
        currency.add(criterion2);

        Criterion criterion3 = new Criterion();
        criterion3.setFieldName("workType");
        criterion3.setOperator(Criterion.EQ);
        criterion3.setValue(stGatedamReportParam.getWorkType());
        currency.add(criterion3);

        Criterion criterion4 = new Criterion();
        criterion4.setFieldName("sttp");
        criterion4.setOperator(Criterion.EQ);
        criterion4.setValue(stGatedamReportParam.getSttp());
        currency.add(criterion4);

        Criterion criterion5 = new Criterion();
        criterion5.setFieldName("gmtCreate");
        criterion5.setOperator(Criterion.GTE);
        criterion5.setValue(stGatedamReportParam.getStartTime());
        currency.add(criterion5);

        String endTime = stGatedamReportParam.getEndTime();
        DateTime parse = DateUtil.parse(endTime);
        endTime = TimeUtil.getPreDayDate(parse, 1);

        Criterion criterion6 = new Criterion();
        criterion6.setFieldName("gmtCreate");
        criterion6.setOperator(Criterion.LTE);
        criterion6.setValue(endTime);
        currency.add(criterion6);

        Criterion criterion7 = new Criterion();
        criterion7.setFieldName("stnm");
        criterion7.setOperator(Criterion.EQ);
        criterion7.setValue(stGatedamReportParam.getStnm());
        currency.add(criterion7);

        param.setCurrency(currency);
        Paginator<StGatedamReportEsr> paginator = stGatedamReportService.findByPaginator(param);
        List<StGatedamReportEsr> list = paginator.getItems();
        for (int i = 0; i < list.size(); i++) {
            StGatedamReportEsr stGatedamReportEsr = list.get(i);

            //获取主表关联的检查记录表图片
            Paginator<FileBaseEsr> paginatorFile = this.getRelationUrl(stGatedamReportEsr.getId());
            stGatedamReportEsr.setReportFileList(paginatorFile.getItems());

            //获取关联表信息
            Paginator<StGatedamReportRelationEsr> byPaginator = this.getRelation(stGatedamReportEsr.getId());
            List<StGatedamReportRelationEsr> items = byPaginator.getItems();
//            if (CollectionUtils.isEmpty(items)) {
//                return list;
//            }
            //关联获取到相关联的图片附件
            for (int j = 0; j < items.size(); j++) {
                StGatedamReportRelationEsr stGatedamReportRelationEsr = items.get(j);
                PaginatorParam param2 = new PaginatorParam();
                param2.setPageSize(0);
                param2.setCurrentPage(0);
                List currency2 = new ArrayList<>();
                Criterion criterionf = new Criterion();
                criterionf.setFieldName("typeId");
                criterionf.setOperator(Criterion.IN);
                criterionf.setValue(Arrays.asList(ItemConstant.GATE_FILE_PREFIX + stGatedamReportRelationEsr.getId()));
                currency2.add(criterionf);
                param.setCurrency(currency2);
                Paginator<FileBaseEsr> byPaginator1 = fileBaseService.findByPaginator(param);
                List<FileBaseEsr> items2 = byPaginator1.getItems();
                if (CollectionUtils.isEmpty(items2)) {
                    continue;
                }
                Map<String, List<FileBaseEsr>> fileMap = items2.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
                stGatedamReportRelationEsr.setReportFileList(fileMap.get(ItemConstant.GATE_FILE_PREFIX + stGatedamReportRelationEsr.getId()));
            }
            stGatedamReportEsr.setStGatedamReportRelationEsrList(items);
        }

        return list;
    }

    /**
     * 闸坝运行养护统计
     *
     * @param stBRiverId
     * @return
     */
    @Override
    public Object searchCount(String stBRiverId, String unitId) {
        //stBRiverId 为空时查询所有河道和，不为空时查询此河道上的
        //
        String year = TimeUtil.getYear(new Date());
        String mouth = TimeUtil.getYearAndMonth(new Date());
        if ("".equals(stBRiverId) || ("null").equals(stBRiverId)) {
            stBRiverId = null;
        }
        if ("".equals(unitId) || ("null").equals(unitId)) {
            unitId = null;
        }
        //当月统计
        List<Map> list = stGatedamReportDao.searchCount(stBRiverId, mouth, unitId);
        //当年统计
        List<Map> yearList = stGatedamReportDao.searchCountYear(stBRiverId, year, unitId);
        for (int i = 0; i < yearList.size(); i++) {
            Map map = yearList.get(i);
            yearList.get(i).put("mouthCount", null);
            String sttp = (String) map.get("sttp");

            for (int j = 0; j < list.size(); j++) {

                String sttp2 = (String) list.get(j).get("sttp");
                if (sttp.equals(sttp2)) {
                    Object mouthCount = list.get(j).get("mouthCount");
                    yearList.get(i).put("mouthCount", mouthCount);
                    break;
                }
            }
        }
        return yearList;
    }

    /**
     * 闸坝运行养护统计
     *
     * @param stBRiverId
     * @return
     */
    @Override
    public Object searchCountNew(String stBRiverId, String unitId) {
        //stBRiverId 为空时查询所有河道和，不为空时查询此河道上的
        //当月统计
        Date startTime = cn.hutool.core.date.DateUtil.beginOfMonth(new Date());
        Date endTime = cn.hutool.core.date.DateUtil.endOfMonth(new Date());

        QueryWrapper queryWrapper = new QueryWrapper();
        if (!"".equals(stBRiverId) && null != stBRiverId && !"null".equals(stBRiverId)) {
            queryWrapper.eq("real_id", stBRiverId);
        }

        if (!"".equals(unitId) && null != unitId &&  !"null".equals(unitId) ) {
            queryWrapper.eq("unit_id", unitId);
        }
        queryWrapper.eq("order_type", "6");
        queryWrapper.between("start_time", startTime, endTime);

        List<WorkorderBase> list = workorderBaseDao.selectList(queryWrapper);
        List<WorkorderBase> resList = new ArrayList<>();

        //获取工单的最新状态
        for (int k = 0; k < list.size(); k++) {
            WorkorderBase workorderBase = list.get(k);
            String id = workorderBase.getId(); //工单的id
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id", id);
            wrapper.orderByDesc("order_time");
            wrapper.last(" limit 1");

            List<WorkorderProcess> processList = workorderProcessDao.selectList(wrapper);
            String orderStatus = processList.get(0).getOrderStatus();
            if ("4".equals(orderStatus) || "5".equals(orderStatus) || "8".equals(orderStatus)) {
                //属于已完成的工单
                resList.add(workorderBase);
            } else {
                log.info("养护工单未完成，不作统计 ");
            }

        }

        //当年统计
        Date startTimeY = cn.hutool.core.date.DateUtil.beginOfYear(new Date());
        Date endTimeY = cn.hutool.core.date.DateUtil.endOfYear(new Date());


        QueryWrapper queryWrapperYear = new QueryWrapper();
        if (!"".equals(stBRiverId) && null != stBRiverId && !"null".equals(stBRiverId)) {
            queryWrapperYear.eq("real_id", stBRiverId);
        }

        if (!"".equals(unitId) && null != unitId  &&  !"null".equals(unitId) ) {
            queryWrapperYear.eq("unit_id", unitId);
        }
        queryWrapperYear.eq("order_type", "6");
        queryWrapperYear.between("start_time", startTimeY, endTimeY);

        List<WorkorderBase> yearList = workorderBaseDao.selectList(queryWrapperYear);
        List<WorkorderBase> restYearList = new ArrayList<>();

        //获取工单的最新状态
        for (int k = 0; k < yearList.size(); k++) {
            WorkorderBase workorderBase = yearList.get(k);
            String id = workorderBase.getId(); //工单的id
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id", id);
            wrapper.orderByDesc("order_time");
            wrapper.last(" limit 1");

            List<WorkorderProcess> processList = workorderProcessDao.selectList(wrapper);
            String orderStatus = processList.get(0).getOrderStatus();
            if ("4".equals(orderStatus) || "5".equals(orderStatus) || "8".equals(orderStatus)) {
                //属于已完成的工单
                restYearList.add(workorderBase);
            } else {
                log.info("养护工单未完成，不作统计 ");
            }

        }
        //按类型进行分组
        Map<String, List<WorkorderBase> > resListMouth = resList.stream().collect(Collectors.groupingBy(WorkorderBase::getSttp));
        Map<String, List<WorkorderBase> > resListYear = restYearList.stream().collect(Collectors.groupingBy(WorkorderBase::getSttp));

        List<WorkorderBase> listDP = (List<WorkorderBase>) resListMouth.get("DP");//泵站
        List<WorkorderBase> listDD = (List<WorkorderBase>) resListMouth.get("DD"); //水闸
        List<WorkorderBase> listBZ = (List<WorkorderBase>) resListMouth.get("BZ");  //边闸
        List<WorkorderBase> listSB = (List<WorkorderBase>) resListMouth.get("SB");  // 水坝

        List<WorkorderBase> listDPYear = (List<WorkorderBase>) resListYear.get("DP");//泵站
        List<WorkorderBase> listDDYear = (List<WorkorderBase>) resListYear.get("DD"); //水闸
        List<WorkorderBase> listBZYear = (List<WorkorderBase>) resListYear.get("BZ");  //边闸
        List<WorkorderBase> listSBYear = (List<WorkorderBase>) resListYear.get("SB");  // 水坝

        //组装返回的数据
        List<Map> yearResList = new ArrayList<>();
        Map<String, Object> mapDP = new HashMap<>();
        mapDP.put("sttp", "DP");
        mapDP.put("mouthCount", listDP == null ? 0 : listDP.size());
        mapDP.put("yearCount", listDPYear == null ? 0 : listDPYear.size());
        yearResList.add(mapDP);

        Map<String, Object> mapDD = new HashMap<>();
        mapDD.put("sttp", "DD");
        mapDD.put("mouthCount", listDD == null ? 0 : listDD.size());
        mapDD.put("yearCount", listDDYear == null ? 0 : listDDYear.size());
        yearResList.add(mapDD);

        Map<String, Object> mapBZ = new HashMap<>();
        mapBZ.put("sttp", "BZ");
        mapBZ.put("mouthCount", listBZ == null ? 0 : listBZ.size());
        mapBZ.put("yearCount", listBZYear == null ? 0 : listBZYear.size());
        yearResList.add(mapBZ);

        Map<String, Object> mapSB = new HashMap<>();
        mapSB.put("sttp", "SB");
        mapSB.put("mouthCount", listSB == null ? 0 : listSB.size());
        mapSB.put("yearCount", listSBYear == null ? 0 : listSBYear.size());
        yearResList.add(mapSB);

        return yearResList;
    }

    /**
     * 获取单个测站最新运行养护记录
     *
     * @param stcd
     * @param stnm
     * @return
     */
    @Override
    public Map<String, Object> searchReportNew(String stcd, String stnm) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        //queryWrapper.eq("stcd",stcd);
        queryWrapper.eq("stnm", stnm);
        queryWrapper.eq("is_delete", ItemConstant.REPORT_NO_DELETE);
        queryWrapper.eq("work_type", 4);
        queryWrapper.orderByDesc("gmt_create");
        List<StGatedamReportDto> list = stGatedamReportDao.selectList(queryWrapper);
        StGatedamReportDto stGatedamReportDto = null;
        StGatedamReportEsr stGatedamReportEsr = null;
        if (list.size() > 0) {
            stGatedamReportDto = list.get(0);
            stGatedamReportEsr = (StGatedamReportEsr) stGatedamReportService.searchById(stGatedamReportDto.getId());
        }
        map.put("workType4", stGatedamReportEsr);

        //（ 4维保 5运行)）
        QueryWrapper queryWrapper2 = new QueryWrapper();
        //queryWrapper.eq("stcd",stcd);
        queryWrapper2.eq("stnm", stnm);
        queryWrapper2.eq("is_delete", ItemConstant.REPORT_NO_DELETE);
        queryWrapper2.eq("work_type", 5);
        queryWrapper2.orderByDesc("gmt_create");
        List<StGatedamReportDto> list2 = stGatedamReportDao.selectList(queryWrapper2);
        StGatedamReportDto stGatedamReportDto2 = null;
        StGatedamReportEsr stGatedamReportEsr2 = null;
        if (list2.size() > 0) {
            stGatedamReportDto2 = list2.get(0);
            stGatedamReportEsr2 = (StGatedamReportEsr) stGatedamReportService.searchById(stGatedamReportDto2.getId());
        }

        map.put("workType5", stGatedamReportEsr2);

        return map;
    }

    /**
     * 获取单个测站最新运行养护记录_从闸坝养护计划工单获取数据
     *
     * @param stcd
     * @param stnm
     * @return
     */
    @Override
    public  Object searchNewReportYh(String stcd, String stnm) {
        //当年统计
        Date startTimeY = cn.hutool.core.date.DateUtil.beginOfYear(new Date());
        Date endTimeY = cn.hutool.core.date.DateUtil.endOfYear(new Date());

        Map<String, Object> map = new HashMap<>();
        //从工单表中获取到查询测站的工单ids
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stnm", stnm);
        queryWrapper.eq("is_delete", ItemConstant.REPORT_NO_DELETE);
        queryWrapper.eq("order_type", 6);
        queryWrapper.between("start_time", startTimeY, endTimeY);
        List<WorkorderBase> list = workorderBaseDao.selectList(queryWrapper);
        log.info("list:"+list.size());
        if(list.size()>0){
            String[] ids = new String[list.size()];
            for (int k = 0; k < list.size(); k++) {
                String id = list.get(k).getId();
                ids[k] =id;
            }

            //去流程表中查询这些工单中已经完成的工单 4待审核  5已归档 8 待审核
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.in("order_id",ids);
            wrapper.in("order_status","4","5","8");
            wrapper.orderByDesc("order_time");
            wrapper.last("LIMIT 1");
            List<WorkorderProcess> processList = workorderProcessDao.selectList(wrapper);
            log.info("养护工单processList:"+processList.size());
            WorkorderBaseEsrYh workorderBaseEsrYh = null;
            if(processList.size()>0){
                WorkorderProcess workorderProcess = processList.get(0);
                String orderId = workorderProcess.getOrderId();

                QueryWrapper wrapperOperate = new QueryWrapper();
                wrapperOperate.eq("order_id",orderId);

                List<StPlanOperateDto> operateList = stPlanOperateDao.selectList(wrapperOperate);

                QueryWrapper wrapperReject = new QueryWrapper();
                wrapperReject.eq("order_id", orderId);
                List<StPlanOperateRejectDto> stPlanOperateRejectDtos = stPlanOperateRejectDao.selectList(wrapperReject);

                queryWrapper.eq("id",orderId);
                WorkorderBaseEsr workorderBaseEsr = workorderBaseService.findById(orderId);
                workorderBaseEsrYh = BeanUtil.copyProperties(workorderBaseEsr, WorkorderBaseEsrYh.class);
                workorderBaseEsrYh.setOperateList(operateList);
                workorderBaseEsrYh.setOperateRejectList(stPlanOperateRejectDtos);
                workorderBaseEsrYh.setOrderTime(workorderProcess.getOrderTime());
                String orderManager = workorderBaseEsrYh.getOrderManager();
                if(!"".equals(orderManager) && null !=orderManager){
                    List<EventCompanyDto> eventCompanyDtoList = eventCompanyService.selectManagerPhone(orderManager);
                    if(eventCompanyDtoList.size()>0){
                        workorderBaseEsrYh.setPhone(eventCompanyDtoList.get(0).getPhone());
                    }else{
                        //
                        List<StPlanPersonDto> stPlanPersonDtosList = stPlanPersonService.selectStPlanPerson(orderManager);
                        if(stPlanPersonDtosList.size()>0){
                            workorderBaseEsrYh.setPhone(stPlanPersonDtosList.get(0).getPlanPhone());
                        }
                    }

                }
            }
            return workorderBaseEsrYh;
        }else{
            return null;
        }
    }

    private Paginator<FileBaseEsr> getRelationUrl(String id) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(Arrays.asList(ItemConstant.GATEBASE_FILE_PREFIX + id));
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
        return paginator;
    }

    /**
     * 根据主表id获取关联表信息
     *
     * @param id
     * @return
     */
    private Paginator<StGatedamReportRelationEsr> getRelation(String id) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("stGatedamId");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(id);
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<StGatedamReportRelationEsr> byPaginator = stGatedamReportRelationService.findByPaginator(param);
        return byPaginator;
    }
}
