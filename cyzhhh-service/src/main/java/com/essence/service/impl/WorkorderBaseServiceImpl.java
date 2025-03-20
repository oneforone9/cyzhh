package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.EventBaseDao;
import com.essence.dao.PersonBaseDao;
import com.essence.dao.TWorkorderProcessNewestDao;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.entity.EventBase;
import com.essence.dao.entity.PersonBase;
import com.essence.dao.entity.WorkorderBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.WorkorderBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderBaseEtoT;
import com.essence.service.converter.ConverterWorkorderBaseTtoR;
import com.essence.service.converter.ConverterrecordGeom;
import com.essence.service.converter.ConverterrecordPoint;
import com.essence.service.utils.ExtractProcess;
import com.essence.service.utils.OrderCodeGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Slf4j
@Service
public class WorkorderBaseServiceImpl extends BaseApiImpl<WorkorderBaseEsu, WorkorderBaseEsp, WorkorderBaseEsr, WorkorderBase> implements WorkorderBaseService {

    @Autowired
    private WorkorderBaseDao workorderBaseDao;
    @Autowired
    private WorkorderProcessService workorderProcessService;

    @Autowired
    private ConverterWorkorderBaseEtoT converterWorkorderBaseEtoT;
    @Autowired
    private ConverterWorkorderBaseTtoR converterWorkorderBaseTtoR;

    @Autowired
    private CodeGenerateService codeGenerateService;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private EventBaseService eventBaseService;
    @Autowired
    private WorkorderRecordGeomService workorderRecordGeomService;
    @Autowired
    private ConverterrecordGeom converterrecordGeom;
    @Autowired
    private RosteringInfoService rosteringInfoService;
    @Resource
    private PersonBaseDao personBaseDao;
    @Autowired
    private ReaFocusGeomService reaFocusGeomService;
    @Autowired
    private TWorkorderRecordPointService tWorkorderRecordPointService;
    @Autowired
    private ConverterrecordPoint converterrecordPoint;
    @Autowired
    private TReaFocusPointService tReaFocusPointService;
    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;
    @Autowired
    private TWorkorderProcessNewestService tWorkorderProcessNewestService;
    @Autowired
    private EventBaseDao eventBaseDao;


    public WorkorderBaseServiceImpl(WorkorderBaseDao workorderBaseDao, ConverterWorkorderBaseEtoT converterWorkorderBaseEtoT, ConverterWorkorderBaseTtoR converterWorkorderBaseTtoR) {
        super(workorderBaseDao, converterWorkorderBaseEtoT, converterWorkorderBaseTtoR);
    }
    @Transactional
    @Override
    public String insertWorkorder(WorkorderBaseEsu workorderBaseEsu) {
        String id=UuidUtil.get32UUIDStr();
        // 1 添加工单
        // 1） 设置主键
        workorderBaseEsu.setId(id);
        // 2）是否关注 默认不关注
        workorderBaseEsu.setIsAttention(ItemConstant.ORDER_NO_ATTENTION);
        // 3) 是否删除 默认不删除
        workorderBaseEsu.setIsDelete(ItemConstant.ORDER_NO_DELETE);
        // 4) 工单编码如果为空自动生成工单编码(日期 +拼音缩写 +顺序码)
        if (StringUtils.isEmpty(workorderBaseEsu.getOrderCode())) {
            // 日期
            String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 拼音缩写
            String type = OrderCodeGenerateUtil.renPing(workorderBaseEsu.getOrderType());
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd + type, 1);
            workorderBaseEsu.setOrderCode(codes.get(0));
        }
        //===20230324 案件一览里生成工单时根据eventId 管联到初始的orderId重点巡查位置
        String eventId = workorderBaseEsu.getEventId();
        if(!"".equals(eventId) && eventId !=null){
            EventBaseEsr eventBaseEsr = eventBaseService.findById(eventId);
            String status = eventBaseEsr.getStatus();
            String disposeName = workorderBaseEsu.getSendTo();
            String companyId = workorderBaseEsu.getCompanyId();
            //
            if("1".equals(status)){
                throw new BusinessException("案件状态为已办，不能重复生成工单");
            }
            EventBase eventBase = new EventBase();
            eventBase.setId(eventId);
            eventBase.setCompanyId(companyId);
            eventBase.setDisposeName(disposeName);
            log.info("oooooooooo开始:"+eventId);
            int i = eventBaseDao.updateById(eventBase);
            log.info("oooooooooo结束:"+i);

            WorkorderBase workorderBase = workorderBaseDao.selectById(eventBaseEsr !=null?eventBaseEsr.getOrderId():"");
            workorderBaseEsu.setLocation(workorderBase!=null?workorderBase.getLocation():"");
        }
        //===20230324 案件一览里生成工单时根据eventId 管联到初始的orderId重点巡查位置
        //

        int insert = super.insert(workorderBaseEsu);
        if (insert < 1) {
            throw new BusinessException("工单创建失败");

        }

        // 2 关联图片
        List<String> fileIds = workorderBaseEsu.getFileIds();
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileIds.forEach(p -> {
                FileBaseEsu fileBaseEsu = new FileBaseEsu();
                fileBaseEsu.setId(p);
                fileBaseEsu.setClassId(ItemConstant.QUES_FILE_PREFIX + workorderBaseEsu.getId());
                fileBaseService.update(fileBaseEsu);
            });

        }
        //===20230324增加处理兼容小程序的创建工单并且自动派发
        String orderStatus = ItemConstant.ORDER_STATUS_NO_HANDOUT;
        String flag = workorderBaseEsu.getOrderStatus();
        //orderStatus不为空时，表示新增工单并自动派单=发
        if (!"".equals(flag) && flag != null) {
            orderStatus = workorderBaseEsu.getOrderStatus();
        }
        //===20230324增加处理兼容小程序的创建工单并且自动派发

        // 记录工单重点巡河位置
        String focusPositionId = workorderBaseEsu.getFocusPositionId(); //重点巡查位置主键
        if (!"".equals(focusPositionId) && focusPositionId != null) {
            List<String> focusIds = new ArrayList<>();
            focusIds.add(focusPositionId);
            Map<String, ReaFocusGeomEsr> reaFocusGeomMap = this.reaFocusGeomMap(focusIds);
            ReaFocusGeomEsr reaFocusGeomEsr = reaFocusGeomMap.get(focusPositionId);
            if (null != reaFocusGeomEsr){
                WorkorderRecordGeomEsu workorderRecordGeomEsu = converterrecordGeom.toBean(reaFocusGeomEsr);
                workorderRecordGeomEsu.setOrderId(workorderBaseEsu.getId());
                workorderRecordGeomService.insert(workorderRecordGeomEsu);

            }
        }

        // 记录工单打卡点位
        if (!"".equals(focusPositionId) && focusPositionId != null) {
            List<String> focusIds = new ArrayList<>();
            focusIds.add(focusPositionId);
            Map<String, ReaFocusGeomEsr> reaFocusGeomMap = this.reaFocusGeomMap(focusIds);
            ReaFocusGeomEsr reaFocusGeomEsr = reaFocusGeomMap.get(focusPositionId);

            //获取打卡点位
            Map<String, List<TReaFocusPointEsr>> tReaFocusPointEsrMap = tReaFocusPointEsrMap(focusIds);
            List<TReaFocusPointEsr> tReaFocusPointEsrList = tReaFocusPointEsrMap.get(focusPositionId);
            if (null != tReaFocusPointEsrList && tReaFocusPointEsrList.size()>0) {
                for (int i = 0; i < tReaFocusPointEsrList.size(); i++) {
                    TReaFocusPointEsr tReaFocusPointEsr = tReaFocusPointEsrList.get(i);
                    TWorkorderRecordPointEsu tWorkorderRecordPointEsu =  converterrecordPoint.toBean(tReaFocusPointEsr);
                    tWorkorderRecordPointEsu.setOrderId(workorderBaseEsu.getId());
                    tWorkorderRecordPointEsu.setId(UuidUtil.get32UUIDStr());
                    tWorkorderRecordPointEsu.setIsCompleteClock(0);
                    tWorkorderRecordPointService.insert(tWorkorderRecordPointEsu);
                }
            }
        }
        // 记录工单打卡点位

        // 3 添加工单未派发流程
        //WorkorderProcessEsu workorderProcessEsu = ExtractProcess.fromOrderBase(workorderBaseEsu, UuidUtil.get32UUIDStr(), ItemConstant.ORDER_STATUS_NO_HANDOUT);
        WorkorderProcessEsu workorderProcessEsu = ExtractProcess.fromOrderBase(workorderBaseEsu, UuidUtil.get32UUIDStr(), orderStatus);
        if (null != workorderProcessEsu) {
            workorderProcessService.insert(workorderProcessEsu);
            //20230802 保存最新的工单状态表
            //再处理工单流程的最新记录表只保留最新记录一条数据
            String orderId = workorderProcessEsu.getOrderId();
            //2删除orderId数据
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id",orderId);
            int delete = tWorkorderProcessNewestDao.delete(wrapper);
            //3添加最新工单记录
            //组装数据
            TWorkorderProcessNewestEsu esu = new TWorkorderProcessNewestEsu();
            BeanUtil.copyProperties(workorderProcessEsu, esu);

            int insert1 = tWorkorderProcessNewestService.insert(esu);
            //20230802 保存最新的工单状态表

            return id;

        }
        throw new BusinessException("工单创建失败");
    }

    @Override
    public void updateRejectTime(WorkorderBaseEsu workorderBaseEsu) {
        workorderBaseDao.updateRejectTime(workorderBaseEsu.getId(), workorderBaseEsu.getRejectTime());
    }

    @Override
    public Integer updateWorkorder(WorkorderBaseEsu workorderBaseEsu) {
        WorkorderBase workorderBase = converterWorkorderBaseEtoT.toBean(workorderBaseEsu);
        int i = workorderBaseDao.updateById(workorderBase);
        return i;
    }

    @Transactional
    @Override
    public int insert(WorkorderBaseEsu workorderBaseEsu) {
        // 1 添加工单
        // 1） 设置主键
        workorderBaseEsu.setId(UuidUtil.get32UUIDStr());
        // 2）是否关注 默认不关注
        workorderBaseEsu.setIsAttention(ItemConstant.ORDER_NO_ATTENTION);
        // 3) 是否删除 默认不删除
        workorderBaseEsu.setIsDelete(ItemConstant.ORDER_NO_DELETE);
        // 4) 工单编码如果为空自动生成工单编码(日期 +拼音缩写 +顺序码)
        if (StringUtils.isEmpty(workorderBaseEsu.getOrderCode())) {
            // 日期
            String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 拼音缩写
            String type = OrderCodeGenerateUtil.renPing(workorderBaseEsu.getOrderType());
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd + type, 1);
            workorderBaseEsu.setOrderCode(codes.get(0));
        }
        //===20230324 案件一览里生成工单时根据eventId 管联到初始的orderId重点巡查位置
        String eventId = workorderBaseEsu.getEventId();
        if(!"".equals(eventId) && eventId !=null){
            EventBaseEsr eventBaseEsr = eventBaseService.findById(eventId);
            String status = eventBaseEsr.getStatus();
            //
            if("1".equals(status)){
                throw new BusinessException("案件状态为已办，不能重复生成工单");
            }
            WorkorderBase workorderBase = workorderBaseDao.selectById(eventBaseEsr !=null?eventBaseEsr.getOrderId():"");
            workorderBaseEsu.setLocation(workorderBase!=null?workorderBase.getLocation():"");
        }
        //===20230324 案件一览里生成工单时根据eventId 管联到初始的orderId重点巡查位置
        //

        int insert = super.insert(workorderBaseEsu);
        if (insert < 1) {
            throw new BusinessException("工单创建失败");

        }

        // 2 关联图片
        List<String> fileIds = workorderBaseEsu.getFileIds();
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileIds.forEach(p -> {
                FileBaseEsu fileBaseEsu = new FileBaseEsu();
                fileBaseEsu.setId(p);
                fileBaseEsu.setClassId(ItemConstant.QUES_FILE_PREFIX + workorderBaseEsu.getId());
                fileBaseService.update(fileBaseEsu);
            });

        }
        //===20230324增加处理兼容小程序的创建工单并且自动派发
        String orderStatus = ItemConstant.ORDER_STATUS_NO_HANDOUT;
        String flag = workorderBaseEsu.getOrderStatus();
        //orderStatus不为空时，表示新增工单并自动派单=发
        if (!"".equals(flag) && flag != null) {
            orderStatus = workorderBaseEsu.getOrderStatus();
        }
        //===20230324增加处理兼容小程序的创建工单并且自动派发

        // 记录工单重点巡河位置
        String focusPositionId = workorderBaseEsu.getFocusPositionId(); //重点巡查位置主键
        if (!"".equals(focusPositionId) && focusPositionId != null) {
            List<String> focusIds = new ArrayList<>();
            focusIds.add(focusPositionId);
            Map<String, ReaFocusGeomEsr> reaFocusGeomMap = this.reaFocusGeomMap(focusIds);
            ReaFocusGeomEsr reaFocusGeomEsr = reaFocusGeomMap.get(focusPositionId);
            if (null != reaFocusGeomEsr){
                WorkorderRecordGeomEsu workorderRecordGeomEsu = converterrecordGeom.toBean(reaFocusGeomEsr);
                workorderRecordGeomEsu.setOrderId(workorderBaseEsu.getId());
                workorderRecordGeomService.insert(workorderRecordGeomEsu);

            }
        }

        // 记录工单打卡点位
        if (!"".equals(focusPositionId) && focusPositionId != null) {
            List<String> focusIds = new ArrayList<>();
            focusIds.add(focusPositionId);
            Map<String, ReaFocusGeomEsr> reaFocusGeomMap = this.reaFocusGeomMap(focusIds);
            ReaFocusGeomEsr reaFocusGeomEsr = reaFocusGeomMap.get(focusPositionId);

            //获取打卡点位
            Map<String, List<TReaFocusPointEsr>> tReaFocusPointEsrMap = tReaFocusPointEsrMap(focusIds);
            List<TReaFocusPointEsr> tReaFocusPointEsrList = tReaFocusPointEsrMap.get(focusPositionId);
            if (null != tReaFocusPointEsrList && tReaFocusPointEsrList.size()>0) {
                for (int i = 0; i < tReaFocusPointEsrList.size(); i++) {
                    TReaFocusPointEsr tReaFocusPointEsr = tReaFocusPointEsrList.get(i);
                    TWorkorderRecordPointEsu tWorkorderRecordPointEsu =  converterrecordPoint.toBean(tReaFocusPointEsr);
                    tWorkorderRecordPointEsu.setOrderId(workorderBaseEsu.getId());
                    tWorkorderRecordPointEsu.setId(UuidUtil.get32UUIDStr());
                    tWorkorderRecordPointEsu.setIsCompleteClock(0);
                    tWorkorderRecordPointService.insert(tWorkorderRecordPointEsu);
                }
            }
        }
        // 记录工单打卡点位

        // 3 添加工单未派发流程
        //WorkorderProcessEsu workorderProcessEsu = ExtractProcess.fromOrderBase(workorderBaseEsu, UuidUtil.get32UUIDStr(), ItemConstant.ORDER_STATUS_NO_HANDOUT);
        WorkorderProcessEsu workorderProcessEsu = ExtractProcess.fromOrderBase(workorderBaseEsu, UuidUtil.get32UUIDStr(), orderStatus);
        if (null != workorderProcessEsu) {
            workorderProcessService.insert(workorderProcessEsu);
            return insert;

        }
        throw new BusinessException("工单创建失败");

    }


    // 获取值班表
    private List<RosteringInfoEsr> rostering(){
        PaginatorParam param = new PaginatorParam();
        // 条件
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.ROSTERING_NO_DELETE);
        currency.add(criterion);
        param.setCurrency(currency);

        Paginator<RosteringInfoEsr> paginator = rosteringInfoService.findByPaginator(param);

        //查询人员 人员是停用
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("status","1");
        List<PersonBase> personBases = personBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(personBases)){
            List<String> noUsePersonIds = personBases.parallelStream().map(PersonBase::getId).collect(Collectors.toList());
            List<RosteringInfoEsr> collect = paginator.getItems().parallelStream().filter(rosteringInfoEsr -> {
                return !noUsePersonIds.contains(rosteringInfoEsr.getPersonId());
            }).collect(Collectors.toList());
            return collect;
        }
        return paginator.getItems();
    }


    // 获取重点位置geom
    private Map<String, ReaFocusGeomEsr> reaFocusGeomMap(List<String> focusIds){
        PaginatorParam param = new PaginatorParam();
        // 条件
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("id");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(focusIds);
        currency.add(criterion);
        param.setCurrency(currency);

        Paginator<ReaFocusGeomEsr> paginator = reaFocusGeomService.findByPaginator(param);
        List<ReaFocusGeomEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)){
            return new HashMap();
        }
        return items.stream().collect(Collectors.toMap(ReaFocusGeomEsr::getId, Function.identity(), (key1, key2) -> key1));
    }

    // 获取打卡点位的数据
    private Map<String, List<TReaFocusPointEsr>> tReaFocusPointEsrMap(List<String> focusIds) {
        PaginatorParam param = new PaginatorParam();
        // 条件
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("focusId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(focusIds);
        currency.add(criterion);
        param.setCurrency(currency);

        Paginator<TReaFocusPointEsr> paginator = tReaFocusPointService.findByPaginator(param);
        List<TReaFocusPointEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return new HashMap();
        }
        return items.stream().collect(Collectors.groupingBy(TReaFocusPointEsr::getFocusId));
    }

}
