package com.essence.job.backjob.order;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.PersonBaseDao;
import com.essence.dao.StHolidayCountryDao;
import com.essence.dao.TWorkorderProcessNewestDao;
import com.essence.dao.entity.PersonBase;
import com.essence.dao.entity.StHolidayCountryDto;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.essence.service.converter.ConverterrecordGeom;
import com.essence.service.converter.ConverterrecordPoint;
import com.essence.service.utils.ExtractOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * 创建巡河工单定时任务
 *
 * @author zhy
 * @since 2022/10/31 16:07
 */
@Component
@Log4j2
public class OrderTask {

    @Autowired
    private RosteringInfoService rosteringInfoService;
    @Autowired
    private WorkorderBaseService workorderBaseService;
    @Autowired
    private WorkorderProcessService workorderProcessService;
    @Autowired
    private ReaFocusGeomService reaFocusGeomService;
    @Autowired
    private WorkorderRecordGeomService workorderRecordGeomService;
    @Autowired
    private OrderRosteringRecordService orderRosteringRecordService;
    @Autowired
    private ConverterrecordGeom converterrecordGeom;
    @Resource
    private PersonBaseDao personBaseDao;
    @Autowired
    private TReaFocusPointService tReaFocusPointService;
    @Autowired
    private TWorkorderRecordPointService tWorkorderRecordPointService;
    @Autowired
    private TWorkorderFlowService tWorkorderFlowService;
    @Autowired
    private ConverterrecordPoint converterrecordPoint;
    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;
    @Autowired
    private TWorkorderProcessNewestService tWorkorderProcessNewestService;
    @Resource
    private StHolidayCountryDao holidayCountryDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Transactional
    @Scheduled(cron = "0/20 * * * * ?")
    public void syncCreateOrderTask() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-创建巡河工单任务,结束.", BACK_END);
            return;
        }
        log.info("创建巡河工单开始....");
        // 1 获取值班表
        List<RosteringInfoEsr> rosteringList = rostering();
        if (CollectionUtils.isEmpty(rosteringList)) {
            log.info("创建巡河工单终止-缺少值班表信息");
            return;
        }
        // 2 获取今日已经生成的工单
        List<OrderRosteringRecordEsr> orderRosterList = orderRecord();

        // 3 筛选当天未生成工单的人员
        String week = TimeUtil.week(new Date());
        List<RosteringInfoEsr> unDoList = rosteringList.stream()
                .filter(p -> p.getWorkTime().contains(week) && !existOrder(orderRosterList, p))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unDoList)) {
            log.info("创建巡河工单终止-今日工单已全部生成.");
            return;
        }
        // 4 获取重点位置
        List<String> focusIds = rosteringList.stream().map(RosteringInfoEsr::getFocusPositionId).collect(Collectors.toList());
        Map<String, ReaFocusGeomEsr> reaFocusGeomMap = reaFocusGeomMap(focusIds);

        //获取打卡点位
        Map<String, List<TReaFocusPointEsr>> tReaFocusPointEsrMap = tReaFocusPointEsrMap(focusIds);

        // 5 获取节假日
        Date date = new Date();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("year", cn.hutool.core.date.DateUtil.format(date, "yyyy"));
        List<StHolidayCountryDto> stHolidayCountryDtos = holidayCountryDao.selectList(queryWrapper);

        // 5 生成巡河工单
        Date startTime = DateUtil.getDateByStringNormal(DateUtil.dateToStringDay(new Date()) + " 08:30:00");
        Date endTime = DateUtil.getDateByStringNormal(DateUtil.dateToStringDay(new Date()) + " 17:30:00");
        // 处理时长
        Integer timeSpan = 9 * 60;
        for (RosteringInfoEsr p : unDoList) {
            boolean flag = false;
            //是否跨过节假日
            if (p.getCrossw() != null) {
                if (CollUtil.isNotEmpty(stHolidayCountryDtos) && p.getCrossw() == 1) {
                    for (StHolidayCountryDto stHolidayCountryDto : stHolidayCountryDtos) {
                        Date start = stHolidayCountryDto.getStart();
                        Date end = stHolidayCountryDto.getEnd();
                        if (start != null && end != null) {
                            if (date.getTime() >= start.getTime() && date.getTime() <= end.getTime()) {
                                //在这个节假日时间范围内 则跳过 同时配置 cross ==1
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
            //如果符合条件 则跳过
            if (flag) {
                continue;
            }
            //否则生成工单
            //查询当年节假日日期
            createrXCOrder(p, reaFocusGeomMap.get(p.getFocusPositionId()), startTime, endTime, timeSpan, tReaFocusPointEsrMap.get(p.getFocusPositionId()));
        }
        log.info("创建巡河工单结束");
    }

    // 获取值班表
    private List<RosteringInfoEsr> rostering() {
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
        wrapper.eq("status", "1");
        List<PersonBase> personBases = personBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(personBases)) {
            List<String> noUsePersonIds = personBases.parallelStream().map(PersonBase::getId).collect(Collectors.toList());
            List<RosteringInfoEsr> collect = paginator.getItems().parallelStream().filter(rosteringInfoEsr -> {
                return !noUsePersonIds.contains(rosteringInfoEsr.getPersonId());
            }).collect(Collectors.toList());
            return collect;
        }
        return paginator.getItems();
    }

    // 获取今日已经生成的工单
    private List<OrderRosteringRecordEsr> orderRecord() {
        PaginatorParam param = new PaginatorParam();
        // 条件
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("tm");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(DateUtil.dateToStringDay(new Date()));
        currency.add(criterion);
        param.setCurrency(currency);

        Paginator<OrderRosteringRecordEsr> paginator = orderRosteringRecordService.findByPaginator(param);
        return paginator.getItems();
    }

    private boolean existOrder(List<OrderRosteringRecordEsr> list, RosteringInfoEsr rosteringInfoEsr) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        long count = list.stream().filter(p -> p.getRosteringId().equals(rosteringInfoEsr.getId())).count();
        if (count > 0) {
            return true;
        }
        return false;
    }

    // 获取重点位置geom
    private Map<String, ReaFocusGeomEsr> reaFocusGeomMap(List<String> focusIds) {
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
        if (CollectionUtils.isEmpty(items)) {
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

    //生成工单
    private void createrXCOrder(RosteringInfoEsr rosteringInfoEsr, ReaFocusGeomEsr reaFocusGeomEsr, Date startTime, Date endTime, Integer timeSpan,
                                List<TReaFocusPointEsr> tReaFocusPointEsrList) {
        // 1 根据值班表生成工单
        WorkorderBaseEsu workorderBaseEsu = ExtractOrder.xhOrderFromRostering(rosteringInfoEsr, UuidUtil.get32UUIDStr(), startTime, endTime, timeSpan);
        // 2 工单入库
        workorderBaseService.insert(workorderBaseEsu);
        // 3 记录工单重点巡河位置
        if (null != reaFocusGeomEsr) {
            WorkorderRecordGeomEsu workorderRecordGeomEsu = converterrecordGeom.toBean(reaFocusGeomEsr);
            workorderRecordGeomEsu.setOrderId(workorderBaseEsu.getId());
            workorderRecordGeomService.insert(workorderRecordGeomEsu);
        }
        // 4 记录工单的打卡点位
        if (null != tReaFocusPointEsrList && tReaFocusPointEsrList.size() > 0) {
            for (int i = 0; i < tReaFocusPointEsrList.size(); i++) {
                TReaFocusPointEsr tReaFocusPointEsr = tReaFocusPointEsrList.get(i);
                TWorkorderRecordPointEsu tWorkorderRecordPointEsu = converterrecordPoint.toBean(tReaFocusPointEsr);
                tWorkorderRecordPointEsu.setOrderId(workorderBaseEsu.getId());
                tWorkorderRecordPointEsu.setId(UuidUtil.get32UUIDStr());
                tWorkorderRecordPointEsu.setIsCompleteClock(0);
                int insert = tWorkorderRecordPointService.insert(tWorkorderRecordPointEsu);
            }
        }
        // 5 记录工单入库
        OrderRosteringRecordEsu orderRosteringRecordEsu = new OrderRosteringRecordEsu();
        orderRosteringRecordEsu.setOrderId(workorderBaseEsu.getId());
        orderRosteringRecordEsu.setRosteringId(rosteringInfoEsr.getId());
        orderRosteringRecordEsu.setTm(DateUtil.dateToStringDay(new Date()));
        orderRosteringRecordEsu.setGmtCreate(new Date());
        orderRosteringRecordService.insert(orderRosteringRecordEsu);
        // 6 派发工单
        WorkorderProcessEsu workorderProcessEsu = new WorkorderProcessEsu();
        workorderProcessEsu.setOrderTime(DateUtil.getNextSecond(startTime, 1));
        workorderProcessEsu.setPersonId(workorderBaseEsu.getPersonId());
        workorderProcessEsu.setPersonName(workorderBaseEsu.getPersonName());
        workorderProcessEsu.setOrderId(workorderBaseEsu.getId());
        workorderProcessEsu.setOrderStatus(ItemConstant.ORDER_STATUS_NO_START);
        workorderProcessService.insert(workorderProcessEsu);
        //20230802 保存最新的工单状态表
        //再处理工单流程的最新记录表只保留最新记录一条数据
        String orderId = workorderProcessEsu.getOrderId();
        //2删除orderId数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_id", orderId);
        int delete = tWorkorderProcessNewestDao.delete(wrapper);
        //3添加最新工单记录
        //组装数据
        TWorkorderProcessNewestEsu esu = new TWorkorderProcessNewestEsu();
        BeanUtil.copyProperties(workorderProcessEsu, esu);

        int insert1 = tWorkorderProcessNewestService.insert(esu);
        //20230802增加工单的最新状态到工单最新记录状态表中

        //系统生成并派发给指定人
        TWorkorderFlowEsu tWorkorderFlowEsu = new TWorkorderFlowEsu();
        tWorkorderFlowEsu.setOrderId(workorderBaseEsu.getId());
        tWorkorderFlowService.addTWorkOrderFlows(tWorkorderFlowEsu);
    }

}
