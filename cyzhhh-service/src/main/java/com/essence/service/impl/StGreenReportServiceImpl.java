package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.StGreenReportDao;
import com.essence.dao.entity.StGreenReportDto;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StGreenReportEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStGreenReportEtoT;
import com.essence.service.converter.ConverterStGreenReportTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 绿化保洁工作日志上报表(StGreenReport)业务层
 *
 * @author liwy
 * @since 2023-03-14 15:34:18
 */
@Service
public class StGreenReportServiceImpl extends BaseApiImpl<StGreenReportEsu, StGreenReportEsp, StGreenReportEsr, StGreenReportDto> implements StGreenReportService {

    @Autowired
    private StGreenReportDao stGreenReportDao;
    @Autowired
    private ConverterStGreenReportEtoT converterStGreenReportEtoT;
    @Autowired
    private ConverterStGreenReportTtoR converterStGreenReportTtoR;
    @Autowired
    private StGreenReportService stGreenReportService;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private StCompanyBaseService stCompanyBaseService;

    @Autowired
    private StGreenReportRelationService stGreenReportRelationService;

    public StGreenReportServiceImpl(StGreenReportDao stGreenReportDao, ConverterStGreenReportEtoT converterStGreenReportEtoT, ConverterStGreenReportTtoR converterStGreenReportTtoR) {
        super(stGreenReportDao, converterStGreenReportEtoT, converterStGreenReportTtoR);
    }

    /**
     * 新增日志
     *
     * @param stGreenReportEsu
     * @return
     */
    @Transactional
    @Override
    public Object addStGreenReport(StGreenReportEsu stGreenReportEsu) {
        String baseId = UUID.randomUUID().toString().replace("-", "");
        stGreenReportEsu.setIsDelete(ItemConstant.REPORT_NO_DELETE);
        stGreenReportEsu.setId(baseId);

        //根据userId去第三方公司基础表中获取到相关联的信息
        StCompanyBaseEsr stCompanyBaseEsr = stCompanyBaseService.findById(stGreenReportEsu.getCreator());
        if(!"".equals(stCompanyBaseEsr) && stCompanyBaseEsr !=null){
            stGreenReportEsu.setWorkUnit(stCompanyBaseEsr.getCompany()); //作业单位
            stGreenReportEsu.setWorkManage(stCompanyBaseEsr.getManageName()); //负责人
            stGreenReportEsu.setManagePhone(stCompanyBaseEsr.getManagePhone()); //负责人联系方式
        }

        int insert = stGreenReportService.insert(stGreenReportEsu);


        //再增加关联表
        List<StGreenReportRelationEsu> workList = stGreenReportEsu.getWorkList();
        for (int i = 0; i < workList.size(); i++) {
            StGreenReportRelationEsu stGreenReportRelationEsu = workList.get(i);
            stGreenReportRelationEsu.setStGreenId(stGreenReportEsu.getId());
            stGreenReportRelationEsu.setGmtCreate(new Date());
            String id = UUID.randomUUID().toString().replace("-", "");
            stGreenReportRelationEsu.setId(id);
            stGreenReportRelationService.insert(stGreenReportRelationEsu);
            //将上传的图片进行关联
            List<String> fileIds = stGreenReportRelationEsu.getFileIds();
            if (CollectionUtils.isEmpty(fileIds)) {
                continue;
            }
            fileIds.forEach(p -> {
                FileBaseEsu fileBaseEsu = new FileBaseEsu();
                fileBaseEsu.setId(p);
                fileBaseEsu.setTypeId(ItemConstant.GREEN_FILE_PREFIX + stGreenReportRelationEsu.getId());
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
        StGreenReportEsr stGreenReportEsr = super.findById(id);
        if (stGreenReportEsr == null) {
            return stGreenReportEsr;
        }

        //获取关联表信息
        Paginator<StGreenReportRelationEsr> byPaginator = this.getRelation(id);
        List<StGreenReportRelationEsr> items = byPaginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return stGreenReportEsr;
        }

        //关联获取到相关联的图片附件
        for (int i = 0; i < items.size(); i++) {
            StGreenReportRelationEsr stGreenReportRelationEsr = items.get(i);
            PaginatorParam param = new PaginatorParam();
            param.setPageSize(0);
            param.setCurrentPage(0);
            List currency = new ArrayList<>();
            Criterion criterion = new Criterion();
            criterion.setFieldName("typeId");
            criterion.setOperator(Criterion.IN);
            criterion.setValue(Arrays.asList(ItemConstant.GREEN_FILE_PREFIX + stGreenReportRelationEsr.getId()));
            currency.add(criterion);
            param.setCurrency(currency);
            Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
            List<FileBaseEsr> items2 = paginator.getItems();
            if (CollectionUtils.isEmpty(items2)) {
                continue;
            }
            Map<String, List<FileBaseEsr>> fileMap = items2.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
            stGreenReportRelationEsr.setReportFileList(fileMap.get(ItemConstant.GREEN_FILE_PREFIX + stGreenReportRelationEsr.getId()));
        }
        stGreenReportEsr.setStGreenReportRelationEsrList(items);
        return stGreenReportEsr;

    }

    /**
     * 根据主表id获取关联表信息
     *
     * @param id
     * @return
     */
    private Paginator<StGreenReportRelationEsr> getRelation(String id) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("stGreenId");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(id);
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<StGreenReportRelationEsr> byPaginator = stGreenReportRelationService.findByPaginator(param);
        return byPaginator;
    }

    /**
     * 绿化保洁工作报告汇总列表
     *
     * @param stGreenReportParam
     * @return
     */
    @Override
    public Paginator<StGreenReportEsr>  searchAll(StGreenReportParam stGreenReportParam) {
        PaginatorParam param = stGreenReportParam.getParam();
        int currentPage = param.getCurrentPage();
        int pageSize = param.getPageSize();
        String startTime = stGreenReportParam.getStartTime();
        String endTime = stGreenReportParam.getEndTime();
        String workUnit = stGreenReportParam.getWorkUnit();
        if("".equals(workUnit)){
            workUnit = null;
        }
        String stBRiverId = stGreenReportParam.getStBRiverId();
        if("".equals(stBRiverId)){
            stBRiverId = null;
        }
        String workType = stGreenReportParam.getWorkType();
        if("".equals(workType)){
            workType = null;
        }
        String unitId = stGreenReportParam.getUnitId();
        if("".equals(unitId)){
            unitId = null;
        }
        List<StGreenReportDto> list = stGreenReportDao.searchAll(startTime, endTime, workUnit, stBRiverId, unitId, workType);
        //手动进行分页
        PageUtil<StGreenReportDto> pageUtil = new PageUtil(list, currentPage, pageSize, null, null);
        List<StGreenReportDto> recordsList = pageUtil.getRecords();
        List<StGreenReportEsr> stGreenReportEsrList = BeanUtil.copyToList(recordsList, StGreenReportEsr.class);

        Paginator<StGreenReportEsr> paginator = new Paginator<>();
        paginator.setPageSize(pageUtil.getPageSize());
        paginator.setCurrentPage(pageUtil.getCurrent());
        paginator.setTotalCount(pageUtil.getTotal());
        paginator.setPageCount(pageUtil.getPages());
        paginator.setItems(stGreenReportEsrList);

        return paginator;
    }

    /**
     * 作业日历
     * @param stGreenReportParam
     * @return
     */
    @Override
    public Object searchByCondition(StGreenReportParam2 stGreenReportParam) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("workUnit");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(stGreenReportParam.getWorkUnit());
        currency.add(criterion);

        Criterion criterion2 = new Criterion();
        criterion2.setFieldName("stBRiverId");
        criterion2.setOperator(Criterion.EQ);
        criterion2.setValue(stGreenReportParam.getStBRiverId());
        currency.add(criterion2);

        Criterion criterion3 = new Criterion();
        criterion3.setFieldName("workType");
        criterion3.setOperator(Criterion.EQ);
        criterion3.setValue(stGreenReportParam.getWorkType());
        currency.add(criterion3);

        Criterion criterion4 = new Criterion();
        criterion4.setFieldName("gmtCreate");
        criterion4.setOperator(Criterion.GTE);
        criterion4.setValue(stGreenReportParam.getStartTime());
        currency.add(criterion4);

        String endTime = stGreenReportParam.getEndTime();
        DateTime parse = DateUtil.parse(endTime);
        endTime = TimeUtil.getPreDayDate(parse, 1);

        Criterion criterion5 = new Criterion();
        criterion5.setFieldName("gmtCreate");
        criterion5.setOperator(Criterion.LTE);
        criterion5.setValue(endTime);
        currency.add(criterion5);
        param.setCurrency(currency);


        Paginator<StGreenReportEsr> paginator = stGreenReportService.findByPaginator(param);
        List<StGreenReportEsr> list = paginator.getItems();
        for (int i = 0; i < list.size(); i++) {
            StGreenReportEsr stGreenReportEsr = list.get(i);
            //获取关联表信息
            Paginator<StGreenReportRelationEsr> byPaginator = this.getRelation(stGreenReportEsr.getId());
            List<StGreenReportRelationEsr> items = byPaginator.getItems();
            if (CollectionUtils.isEmpty(items)) {
                return list;
            }
            //关联获取到相关联的图片附件
            for (int j = 0; j < items.size(); j++) {
                StGreenReportRelationEsr stGreenReportRelationEsr = items.get(j);
                PaginatorParam param2 = new PaginatorParam();
                param2.setPageSize(0);
                param2.setCurrentPage(0);
                List currency2 = new ArrayList<>();
                Criterion criterionf = new Criterion();
                criterionf.setFieldName("typeId");
                criterionf.setOperator(Criterion.IN);
                criterionf.setValue(Arrays.asList(ItemConstant.GREEN_FILE_PREFIX + stGreenReportRelationEsr.getId()));
                currency2.add(criterionf);
                param.setCurrency(currency2);
                Paginator<FileBaseEsr> byPaginator1 = fileBaseService.findByPaginator(param);
                List<FileBaseEsr> items2 = byPaginator1.getItems();
                if (CollectionUtils.isEmpty(items2)) {
                    continue;
                }
                Map<String, List<FileBaseEsr>> fileMap = items2.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
                stGreenReportRelationEsr.setReportFileList(fileMap.get(ItemConstant.GREEN_FILE_PREFIX + stGreenReportRelationEsr.getId()));
            }
            stGreenReportEsr.setStGreenReportRelationEsrList(items);

        }

        return list;
    }
}
