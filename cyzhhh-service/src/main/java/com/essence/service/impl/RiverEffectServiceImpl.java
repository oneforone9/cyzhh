package com.essence.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.PageUtils;
import com.essence.dao.*;
import com.essence.dao.entity.EventBase;
import com.essence.dao.entity.WorkorderNewest;
import com.essence.dao.entity.WorkorderRecordGeom;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.RiverEffectService;
import com.essence.interfaces.dot.*;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RiverEffectServiceImpl implements RiverEffectService {
    @Resource
    private EventBaseDao eventBaseDao;
    @Resource
    private WorkorderBaseDao workorderBaseDao;
    @Resource
    private WorkorderProcessDao workorderProcessDao;
    @Resource
    private WorkorderNewestDao workorderNewestDao;
    @Resource
    private WorkorderRecordGeomDao workorderRecordGeomDao;

    @Override
    public List<EffectCaseStatisticDto> getCaseTypeStatistic(EffectRequestDto effectRequestDto) {
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        if (effectRequestDto.getDateType().equals(1)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.YEAR, -1);

            DateTime lastStart = DateUtil.beginOfYear(offset);
            DateTime lastEnd = DateUtil.endOfYear(offset);

            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            List<EffectCaseStatisticDto> effectList = getEffectList(lastStart, lastEnd, start, end, effectRequestDto.getUnitId());
            return effectList;
        }
        if (effectRequestDto.getDateType().equals(2)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.MONTH, -1);

            DateTime lastStart = DateUtil.beginOfMonth(offset);
            DateTime lastEnd = DateUtil.endOfMonth(offset);

            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            List<EffectCaseStatisticDto> effectList = getEffectList(lastStart, lastEnd, start, end, effectRequestDto.getUnitId());
            return effectList;
        }
        if (effectRequestDto.getDateType().equals(3)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.DAY_OF_YEAR, -1);

            DateTime lastStart = DateUtil.beginOfDay(offset);
            DateTime lastEnd = DateUtil.endOfDay(offset);

            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            List<EffectCaseStatisticDto> effectList = getEffectList(lastStart, lastEnd, start, end, effectRequestDto.getUnitId());
            return effectList;
        } else {
            return null;
        }

    }

    @Override
    public PageUtil<EffectMarkStatistic> getCaseMarkStatistic(EffectRequestDto effectRequestDto) {
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        List<EffectMarkStatistic> effectData = new ArrayList<>();
        PageUtil<EffectMarkStatistic> pageUtil = null;
        if (effectRequestDto.getDateType().equals(1)) {

            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);

        }
        if (effectRequestDto.getDateType().equals(2)) {

            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);

        }
        if (effectRequestDto.getDateType().equals(3)) {

            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);
        }
        if (effectRequestDto.getDateType().equals(4)) {

            effectData = getEffectData(effectRequestDto.getStartTime(), effectRequestDto.getEndTime());
            effectData = getFilterCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);
        }

        return pageUtil;
    }

    @Override
    public List<EffectMarkStatistic> getCaseMarkStatisticList(EffectRequestDto effectRequestDto) {
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        List<EffectMarkStatistic> effectData = new ArrayList<>();
        PageUtil<EffectMarkStatistic> pageUtil = null;
        if (effectRequestDto.getDateType().equals(1)) {

            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);

        }
        if (effectRequestDto.getDateType().equals(2)) {

            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);

        }
        if (effectRequestDto.getDateType().equals(3)) {

            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            effectData = getEffectData(start, end);
            effectData = getFilterCondition(effectRequestDto, effectData);

        }
        if (effectRequestDto.getDateType().equals(4)) {

            effectData = getEffectData(effectRequestDto.getStartTime(), effectRequestDto.getEndTime());
            effectData = getFilterCondition(effectRequestDto, effectData);

        }

        return effectData;
    }

    @Override
    public List<EffectCaseStatisticDto> getCaseChannelStatistic(EffectRequestDto effectRequestDto) {
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        List<EffectCaseStatisticDto> effectList = new ArrayList<>();
        if (effectRequestDto.getDateType().equals(1)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.YEAR, -1);

            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            effectList = getEffectChannelList(start, end);
            return effectList;
        }
        if (effectRequestDto.getDateType().equals(2)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.MONTH, -1);


            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            effectList = getEffectChannelList(start, end);
            return effectList;
        }
        if (effectRequestDto.getDateType().equals(3)) {
            DateTime offset = DateUtil.offset(dateTime, DateField.DAY_OF_YEAR, -1);

            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            effectList = getEffectChannelList(start, end);
            return effectList;
        }
        if (effectRequestDto.getDateType().equals(4)) {

            effectList = getEffectChannelList(effectRequestDto.getStartTime(), effectRequestDto.getEndTime());
        }
        return effectList;

    }

    @Override
    public PageUtils<EffectRiverListDto> getRiverStatisticList(EffectRequestDto effectRequestDto) {
        List<EffectRiverListDto> res = new ArrayList<>();
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        if (effectRequestDto.getDateType().equals(1)) {
            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        if (effectRequestDto.getDateType().equals(2)) {
            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        if (effectRequestDto.getDateType().equals(3)) {
            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        PageUtils<EffectRiverListDto> pageUtil = new PageUtils(res, effectRequestDto.getCurrent(), effectRequestDto.getSize());
        return pageUtil;
    }

    @Override
    public PageUtil<EffectGeomStatisticDto> getReaGeomMarkStatistic(EffectRequestDto effectRequestDto) {
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        List<EffectGeomStatisticDto> effectData = new ArrayList<>();
        PageUtil<EffectGeomStatisticDto> pageUtil = null;
        if (effectRequestDto.getDateType().equals(1)) {

            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            effectData = getGeomEffectData(start, end);
            effectData = getFilterReaCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);

        }
        if (effectRequestDto.getDateType().equals(2)) {

            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            effectData = getGeomEffectData(start, end);
            effectData = getFilterReaCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);

        }
        if (effectRequestDto.getDateType().equals(3)) {

            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            effectData = getGeomEffectData(start, end);
            effectData = getFilterReaCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);
        }
        if (effectRequestDto.getDateType().equals(4)) {

            effectData = getGeomEffectData(effectRequestDto.getStartTime(), effectRequestDto.getEndTime());
            effectData = getFilterReaCondition(effectRequestDto, effectData);
            pageUtil = new PageUtil(effectData, effectRequestDto.getCurrent(), effectRequestDto.getSize(), null, null);
        }

        return pageUtil;
    }

    @Override
    public List<EffectExportRiverListDto> getRiverStatisticExportList(EffectRequestDto effectRequestDto) {
        List<EffectExportRiverListDto> list = new ArrayList<>();
        List<EffectRiverListDto> res = new ArrayList<>();
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        if (effectRequestDto.getDateType().equals(1)) {
            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        if (effectRequestDto.getDateType().equals(2)) {
            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        if (effectRequestDto.getDateType().equals(3)) {
            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            res = getRiverEffectLists(start, end, effectRequestDto);
        }
        for (EffectRiverListDto re : res) {
            String riverName = re.getRiverName();
            String date = re.getDate();
            for (EffectCaseStatisticListDto datum : re.getData()) {
                EffectExportRiverListDto effectExportRiverListDto = new EffectExportRiverListDto();
                Integer caseNumber = datum.getCaseNumber();
                String eventType = datum.getEventType();
                String unitName = datum.getUnitName();
                Integer disposeNumber = datum.getDisposeNumber();
                effectExportRiverListDto.setDate(date);
                effectExportRiverListDto.setRiverName(riverName);
                effectExportRiverListDto.setEventNum(caseNumber);
                effectExportRiverListDto.setUnitName(unitName);
                effectExportRiverListDto.setEventDeal(disposeNumber);
                /// 案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )
                if (eventType.equals("1")){
                    effectExportRiverListDto.setEventType("水环境案件");
                } else if (eventType.equals("2")) {
                    effectExportRiverListDto.setEventType("涉河工程和有关活动案件");
                }else {
                    effectExportRiverListDto.setEventType("河道附属设施案件");
                }
                list.add(effectExportRiverListDto);
            }
        }
        return list;
    }

    public List<EffectGeomStatisticDto> getFilterReaCondition(EffectRequestDto effectRequestDto, List<EffectGeomStatisticDto> res) {
        List<EffectGeomStatisticDto> collect = new ArrayList<>();
        if (CollUtil.isNotEmpty(res)) {
            collect = res.parallelStream().filter(effectMarkStatistic -> {
                if (StrUtil.isNotEmpty(effectRequestDto.getUserName())) {
                    return effectMarkStatistic.getUserName().contains(effectRequestDto.getUserName());
                }
                return true;
            }).filter(
                    effectMarkStatistic -> {
                        if (StrUtil.isNotEmpty(effectRequestDto.getReaId())) {
                            return effectMarkStatistic.getReaId().equals(effectRequestDto.getReaId());
                        }
                        return true;
                    }
            ).filter(
                    effectMarkStatistic -> {
                        if (StrUtil.isNotEmpty(effectRequestDto.getUnitName())) {
                            return effectMarkStatistic.getUnitId().contains(effectRequestDto.getUnitName());
                        }
                        return true;
                    }
            ).sorted(Comparator.comparing(EffectGeomStatisticDto::getFactReaNum).thenComparing(EffectGeomStatisticDto::getSelfCase).reversed()).collect(Collectors.toList());
        }
        return collect;
    }

    public List<EffectGeomStatisticDto> getGeomEffectData(Date start, Date end) {
        List<EffectGeomStatisticDto> res = new ArrayList<>();
        QueryWrapper<WorkorderNewest> wrapper = new QueryWrapper();
        wrapper.lambda()
                .ge(WorkorderNewest::getStartTime, start)
                .le(WorkorderNewest::getStartTime, end)
                .eq(WorkorderNewest::getOrderType, "1");
        List<WorkorderNewest> workorderBases = workorderNewestDao.selectList(wrapper);
        Map<String, List<WorkorderNewest>> orderMap = new HashMap<>();
        List<String> orderIds = new ArrayList<>();
        //工单对应的重点位置
        Map<String, List<WorkorderRecordGeom>> orderGeom = new HashMap<>();
        if (CollUtil.isNotEmpty(workorderBases)) {
            orderIds = workorderBases.parallelStream().filter(workorderNewest -> {
                return StrUtil.isNotEmpty(workorderNewest.getId());

            }).map(WorkorderNewest::getId).collect(Collectors.toList());
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("order_id",orderIds);
            List<WorkorderRecordGeom> workorderRecordGeoms = workorderRecordGeomDao.selectList(queryWrapper);
            orderGeom= workorderRecordGeoms.parallelStream().collect(Collectors.groupingBy(WorkorderRecordGeom::getFocusPosition));


            orderMap = workorderBases.parallelStream().collect(Collectors.groupingBy(
                    workorderNewest -> {
                        return workorderNewest.getId();
                    }));
        }


        List<EventBase> eventBases = new ArrayList<>();
        Map<String, List<EventBase>> eventList = new HashMap<>();
        if (CollUtil.isNotEmpty(orderIds)) {
            QueryWrapper<EventBase> eventWrapper = new QueryWrapper();
            eventWrapper.in("order_id", orderIds);
            eventBases = eventBaseDao.selectList(eventWrapper);
            eventList = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getOrderId));
        }

        //每个工单代表一个 巡河河段
        for (String key : orderGeom.keySet()) {
            List<WorkorderRecordGeom> workorderRecordGeom = orderGeom.get(key);
            if (workorderRecordGeom == null){
                continue;
            }
            String focusPosition = workorderRecordGeom.get(0).getFocusPosition();
            String reaId = workorderRecordGeom.get(0).getReaId();
            String reaName = workorderRecordGeom.get(0).getReaName();
            //未巡查河段数量
            int unPortalNum = 0;
            //自查案件
            int unApplyNum = 0;
            String userName = "";
            String unitName = "";
            String unitId = "";
            int total = 0;
            EffectGeomStatisticDto effectMarkStatistic = new EffectGeomStatisticDto();
            for (WorkorderRecordGeom recordGeom : workorderRecordGeom) {
                List<WorkorderNewest> workorderNewests1 = orderMap.get(recordGeom.getOrderId());
                total += workorderNewests1.size();
                for (WorkorderNewest workorderNewest : workorderNewests1) {
                    List<EventBase> eventBases1 = eventList.get(workorderNewest.getId());
                    userName = workorderNewest.getPersonName();
                    unitName = workorderNewest.getUnitName();
                    unitId = workorderNewest.getUnitId();
                    List<WorkorderNewest> workorderNewests = orderMap.get(key);

                    if (CollUtil.isNotEmpty(workorderNewests)) {
                        if (workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_EXAMINNING) < 0 && workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_NO_START) >0   )  {
                            //当天有 未巡查工单
                            ++unPortalNum;
                        }
                    }
                    // 未诉先办 数量
                    List<EventBase> unApply = new ArrayList<>();
                    if (CollUtil.isNotEmpty(eventBases1)) {
                        unApply = eventBases1.parallelStream().filter(eventBase -> {
                            return eventBase.getEventChannel().equals(ItemConstant.EVENT_CHANNEL_NO_APPEAL);
                        }).collect(Collectors.toList());
                    }
                    unApplyNum += unApply.size();

                }

            }
            effectMarkStatistic.setAbsenceNum(unPortalNum);
            effectMarkStatistic.setPortalNum(total);
            BigDecimal subtract = new BigDecimal(effectMarkStatistic.getPortalNum()).subtract(new BigDecimal(effectMarkStatistic.getAbsenceNum()));
            effectMarkStatistic.setRea(reaName);
            effectMarkStatistic.setReaId(reaId);
            effectMarkStatistic.setGeomPosition(focusPosition);
            effectMarkStatistic.setUserName(userName);
            effectMarkStatistic.setFactReaNum(subtract);
            effectMarkStatistic.setSelfCase(unApplyNum);
            effectMarkStatistic.setUnitName(unitName);
            effectMarkStatistic.setUnitId(unitId);
            res.add(effectMarkStatistic);

        }
        return res;
    }

    public List<EffectRiverListDto> getRiverEffectLists(DateTime start, DateTime end, EffectRequestDto effectRequestDto) {
        List<EffectRiverListDto> res = new ArrayList<>();
        QueryWrapper<EventBase> wrapper = new QueryWrapper();
        if (StringUtil.isNotBlank(effectRequestDto.getEventType())) {
            wrapper.lambda().eq(EventBase::getEventType, effectRequestDto.getEventType());
        }
        if (StringUtil.isNotBlank(effectRequestDto.getUnitName())) {
            wrapper.lambda().eq(EventBase::getUnitName, effectRequestDto.getUnitName());
        }
        wrapper.lambda()
                .ge(EventBase::getEventTime, start)
                .le(EventBase::getEventTime, end);
        List<EventBase> eventBases = eventBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(eventBases)) {
            Map<String, List<EventBase>> rvNameMap = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getReaName));
            for (String rvName : rvNameMap.keySet()) {
                EffectRiverListDto effectRiverDto = new EffectRiverListDto();
                List<EventBase> eventBases1 = rvNameMap.get(rvName);
                //当前河道下的类型数据统计
                Integer dateType = effectRequestDto.getDateType();
                effectRiverDto.setDate(effectRequestDto.getDate());
                effectRiverDto.setRiverName(rvName);
                Map<String, List<EventBase>> eventTypeMap = eventBases1.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
                List<EffectCaseStatisticListDto> list = new ArrayList<>();
                for (String type : eventTypeMap.keySet()) {
                    List<EventBase> typeList = Optional.ofNullable(eventTypeMap.get(type)).orElse(Lists.newArrayList());
                    EffectCaseStatisticListDto effectCaseStatisticListDto = new EffectCaseStatisticListDto();
                    effectCaseStatisticListDto.setEventType(type);
                    effectCaseStatisticListDto.setCaseNumber(CollUtil.isNotEmpty(typeList) ? typeList.size() : 0);
                    //增加返回案件的经纬度
                    effectCaseStatisticListDto.setEventBaseList(typeList);
                    //增加返回案件的经纬度
                    if (!CollectionUtils.isEmpty(typeList)) {
                        effectCaseStatisticListDto.setUnitName(typeList.get(0).getUnitName());
                        //处理过得集合
                        List<EventBase> disposeStatus = Optional.ofNullable(typeList.stream().filter(x -> StrUtil.isNotEmpty(x.getStatus())).filter(x -> x.getStatus().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        effectCaseStatisticListDto.setDisposeNumber(CollUtil.isNotEmpty(disposeStatus) ? disposeStatus.size() : 0);
                        //处理率
                        Double disposeRate = 0.0;
                        if (effectCaseStatisticListDto.getCaseNumber() > 0) {
                            disposeRate = Double.valueOf(effectCaseStatisticListDto.getDisposeNumber().toString()) / Double.valueOf(effectCaseStatisticListDto.getCaseNumber().toString()) * 100;
                        }
                        effectCaseStatisticListDto.setDisposeRate(disposeRate);
                    }
                    list.add(effectCaseStatisticListDto);
                }
                effectRiverDto.setData(list);
                res.add(effectRiverDto);
            }
        }
        return res;
    }

    @Override
    public List<EffectRiverDto> getRiverStatistic(EffectRequestDto effectRequestDto) {
        List<EffectRiverDto> res = new ArrayList<>();
        DateTime dateTime = DateUtil.parse(effectRequestDto.getDate());
        if (effectRequestDto.getDateType().equals(1)) {
            DateTime start = DateUtil.beginOfYear(dateTime);
            DateTime end = DateUtil.endOfYear(dateTime);
            res = getRiverEffectList(start, end);
            return res;
        }
        if (effectRequestDto.getDateType().equals(2)) {


            DateTime start = DateUtil.beginOfMonth(dateTime);
            DateTime end = DateUtil.endOfMonth(dateTime);
            res = getRiverEffectList(start, end);
            return res;
        }
        if (effectRequestDto.getDateType().equals(3)) {


            DateTime start = DateUtil.beginOfDay(dateTime);
            DateTime end = DateUtil.endOfDay(dateTime);
            res = getRiverEffectList(start, end);
            return res;
        }
        return res;
    }


    public List<EffectRiverDto> getRiverEffectList(DateTime start, DateTime end) {
        List<EffectRiverDto> res = new ArrayList<>();
        QueryWrapper<EventBase> wrapper = new QueryWrapper();
        wrapper.lambda()
                .ge(EventBase::getEventTime, start)
                .le(EventBase::getEventTime, end);
        List<EventBase> eventBases = eventBaseDao.selectList(wrapper);


        if (CollUtil.isNotEmpty(eventBases)) {
            Map<String, List<EventBase>> rvNameMap = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getReaName));
            for (String rvName : rvNameMap.keySet()) {
                EffectRiverDto effectRiverDto = new EffectRiverDto();
                List<EventBase> eventBases1 = rvNameMap.get(rvName);
                //当前河道下的类型数据统计
                Map<String, List<EventBase>> eventTypeMap = eventBases1.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
                List<EffectCaseStatisticDto> list = new ArrayList<>();
                for (String type : eventTypeMap.keySet()) {
                    List<EventBase> typeList = eventTypeMap.get(type);
                    EffectCaseStatisticDto effectCaseStatisticDto = new EffectCaseStatisticDto();
                    effectCaseStatisticDto.setEventType(type);
                    effectCaseStatisticDto.setCaseNumber(CollUtil.isNotEmpty(typeList) ? typeList.size() : 0);
                    BigDecimal now = CollUtil.isNotEmpty(typeList) ? new BigDecimal(typeList.size()) : new BigDecimal(0);
                    effectCaseStatisticDto.setSelfTotal(eventBases.size());
                    list.add(effectCaseStatisticDto);
                }
                effectRiverDto.setRiverName(rvName);
                effectRiverDto.setDate(list);
                res.add(effectRiverDto);
            }
        }
        return res;
    }

    public List<EffectMarkStatistic> getFilterCondition(EffectRequestDto effectRequestDto, List<EffectMarkStatistic> res) {
        List<EffectMarkStatistic> collect = new ArrayList<>();
        if (CollUtil.isNotEmpty(res)) {
            collect = res.parallelStream().filter(effectMarkStatistic -> {
                if (StrUtil.isNotEmpty(effectRequestDto.getUserName())) {
                    return effectMarkStatistic.getUserName().contains(effectRequestDto.getUserName());
                }
                return true;
            }).filter(
                    effectMarkStatistic -> {
                        if (StrUtil.isNotEmpty(effectRequestDto.getUnitName())) {
                            return effectMarkStatistic.getUnitId().contains(effectRequestDto.getUnitName());
                        }
                        return true;
                    }
            ).sorted(Comparator.comparing(EffectMarkStatistic::getPortalNum).thenComparing(EffectMarkStatistic::getSelfCase).reversed()).collect(Collectors.toList());
        }
        return collect;
    }


    public List<EffectMarkStatistic> getEffectData(Date start, Date end) {
        List<EffectMarkStatistic> res = new ArrayList<>();
        QueryWrapper<WorkorderNewest> wrapper = new QueryWrapper();
        wrapper.lambda()
                .ge(WorkorderNewest::getStartTime, start)
                .le(WorkorderNewest::getStartTime, end)
                .eq(WorkorderNewest::getOrderType, "1");
        List<WorkorderNewest> workorderBases = workorderNewestDao.selectList(wrapper);
        Map<String, List<WorkorderNewest>> orderMap = new HashMap<>();
        List<String> orderIds = new ArrayList<>();

        if (CollUtil.isNotEmpty(workorderBases)) {
            orderIds = workorderBases.parallelStream().filter(workorderNewest -> {
                return StrUtil.isNotEmpty(workorderNewest.getId());

            }).map(WorkorderNewest::getId).collect(Collectors.toList());

            orderMap = workorderBases.parallelStream().collect(Collectors.groupingBy(
                    workorderNewest -> {
                        return workorderNewest.getPersonId();
                    }));
        }
        List<EventBase> eventBases = new ArrayList<>();
        Map<String, List<EventBase>> eventList = new HashMap<>();
        if (CollUtil.isNotEmpty(orderIds)) {
            QueryWrapper<EventBase> eventWrapper = new QueryWrapper();
            eventWrapper.in("order_id", orderIds);
            eventBases = eventBaseDao.selectList(eventWrapper);
            eventList = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getOrderId));
        }


        for (String key : orderMap.keySet()) {
            EffectMarkStatistic effectMarkStatistic = new EffectMarkStatistic();
            List<WorkorderNewest> workorderNewests1 = orderMap.get(key);
            int unPortalNum = 0;
            int unApplyNum = 0;
            String userName = "";
            String unitName = "";
            String unitId = "";
            for (WorkorderNewest workorderNewest : workorderNewests1) {
                List<EventBase> eventBases1 = eventList.get(workorderNewest.getId());
                userName = workorderNewest.getPersonName();
                unitName = workorderNewest.getUnitName();
                unitId = workorderNewest.getUnitId();
                List<WorkorderNewest> workorderNewests = orderMap.get(key);

                if (CollUtil.isNotEmpty(workorderNewests)) {
                    if (workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_EXAMINNING) < 0 && workorderNewest.getOrderStatus().compareTo(ItemConstant.ORDER_STATUS_NO_START) >0   )  {
                        //当天有 未巡查工单
                        ++unPortalNum;
                    }
                }
                // 未诉先办 数量
                List<EventBase> unApply = new ArrayList<>();
                if (CollUtil.isNotEmpty(eventBases1)) {
                    unApply = eventBases1.parallelStream().filter(eventBase -> {
                        return eventBase.getEventChannel().equals(ItemConstant.EVENT_CHANNEL_NO_APPEAL);
                    }).collect(Collectors.toList());
                }
                unApplyNum += unApply.size();

            }
            effectMarkStatistic.setAbsenceNum(unPortalNum);
            effectMarkStatistic.setPortalNum(workorderNewests1.size());
            BigDecimal subtract = new BigDecimal(effectMarkStatistic.getPortalNum()).subtract(new BigDecimal(effectMarkStatistic.getAbsenceNum()));
            BigDecimal multiply = subtract.divide(new BigDecimal(effectMarkStatistic.getPortalNum()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            effectMarkStatistic.setPortalPercent(multiply);
            effectMarkStatistic.setUserName(userName);
            effectMarkStatistic.setPortalRiver(null);
            effectMarkStatistic.setSelfCase(unApplyNum);
            effectMarkStatistic.setUnitName(unitName);
            effectMarkStatistic.setUnitId(unitId);
            res.add(effectMarkStatistic);

        }
        return res;
    }


    public List<EffectCaseStatisticDto> getEffectChannelList(Date start, Date end) {
        List<EffectCaseStatisticDto> res = new ArrayList<>();
        QueryWrapper<EventBase> wrapper = new QueryWrapper();
        wrapper.lambda()
                .ge(EventBase::getEventTime, start)
                .le(EventBase::getEventTime, end);
        List<EventBase> eventBases = eventBaseDao.selectList(wrapper);

        Map<String, List<EventBase>> eventChannelTypeMap = new HashMap<>();
        if (CollUtil.isNotEmpty(eventBases)) {
            int size = eventBases.size();
            eventChannelTypeMap = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getEventChannel));
        }
        //案件渠道(案件渠道(1市级交办 2 区级交办 3 未诉先办 4 12345 5 网络上报 6 摄像头抓拍))
        List<EventBase> eventBases1 = eventChannelTypeMap.get("1");
        EffectCaseStatisticDto effectCaseStatisticDto = new EffectCaseStatisticDto();
        effectCaseStatisticDto.setCaseChannelType("1");
        effectCaseStatisticDto.setCaseNumber(CollUtil.isNotEmpty(eventBases1) ? eventBases1.size() : 0);
        effectCaseStatisticDto.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));

        List<EventBase> eventBases2 = eventChannelTypeMap.get("2");
        EffectCaseStatisticDto effectCaseStatisticDto2 = new EffectCaseStatisticDto();
        effectCaseStatisticDto2.setCaseChannelType("2");
        effectCaseStatisticDto2.setCaseNumber(CollUtil.isNotEmpty(eventBases2) ? eventBases2.size() : 0);
        effectCaseStatisticDto2.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto2.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));

        List<EventBase> eventBases3 = eventChannelTypeMap.get("3");
        EffectCaseStatisticDto effectCaseStatisticDto3 = new EffectCaseStatisticDto();
        effectCaseStatisticDto3.setCaseChannelType("3");
        effectCaseStatisticDto3.setCaseNumber(CollUtil.isNotEmpty(eventBases3) ? eventBases3.size() : 0);
        effectCaseStatisticDto3.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto3.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));

        List<EventBase> eventBases4 = eventChannelTypeMap.get("4");
        EffectCaseStatisticDto effectCaseStatisticDto4 = new EffectCaseStatisticDto();
        effectCaseStatisticDto4.setCaseChannelType("4");
        effectCaseStatisticDto4.setCaseNumber(CollUtil.isNotEmpty(eventBases4) ? eventBases4.size() : 0);
        effectCaseStatisticDto4.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto4.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));


        List<EventBase> eventBases5 = eventChannelTypeMap.get("5");
        EffectCaseStatisticDto effectCaseStatisticDto5 = new EffectCaseStatisticDto();
        effectCaseStatisticDto5.setCaseChannelType("5");
        effectCaseStatisticDto5.setCaseNumber(CollUtil.isNotEmpty(eventBases5) ? eventBases5.size() : 0);
        effectCaseStatisticDto5.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto5.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));

        List<EventBase> eventBases6 = eventChannelTypeMap.get("6");
        EffectCaseStatisticDto effectCaseStatisticDto6 = new EffectCaseStatisticDto();
        effectCaseStatisticDto6.setCaseChannelType("6");
        effectCaseStatisticDto6.setCaseNumber(CollUtil.isNotEmpty(eventBases6) ? eventBases6.size() : 0);
        effectCaseStatisticDto6.setPercent(CollUtil.isNotEmpty(eventBases) ? new BigDecimal(effectCaseStatisticDto6.getCaseNumber()).divide(new BigDecimal(eventBases.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));


        res.add(effectCaseStatisticDto);
        res.add(effectCaseStatisticDto2);
        res.add(effectCaseStatisticDto3);
        res.add(effectCaseStatisticDto4);
        res.add(effectCaseStatisticDto5);
        res.add(effectCaseStatisticDto6);

        return res;
    }


    public List<EffectCaseStatisticDto> getEffectList(Date lastStart, Date lastEnd, DateTime start, DateTime end, String unitId) {
        List<EffectCaseStatisticDto> res = new ArrayList<>();
        QueryWrapper<EventBase> wrapper = new QueryWrapper();

        wrapper.ge("event_time", start);
        wrapper.le("event_time", end);
        if (StrUtil.isNotEmpty(unitId)) {
            wrapper.eq("unit_id", unitId);
        }
        List<EventBase> eventBases = eventBaseDao.selectList(wrapper);

        QueryWrapper<EventBase> lastWrapper = new QueryWrapper();
        lastWrapper.lambda()
                .ge(EventBase::getEventTime, lastStart)
                .le(EventBase::getEventTime, lastEnd);
        List<EventBase> lastEventBases = eventBaseDao.selectList(lastWrapper);
        if (CollUtil.isNotEmpty(eventBases)) {
            int size = eventBases.size();
            Map<String, List<EventBase>> eventTypeMap = eventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
            Map<String, List<EventBase>> lastTypeMap = new HashMap<>();
            if (CollUtil.isNotEmpty(lastEventBases)) {
                lastTypeMap = lastEventBases.parallelStream().collect(Collectors.groupingBy(EventBase::getEventType));
            }

            for (String type : eventTypeMap.keySet()) {
                List<EventBase> lastTypeList = lastTypeMap.get(type);
                List<EventBase> typeList = eventTypeMap.get(type);
                EffectCaseStatisticDto effectCaseStatisticDto = new EffectCaseStatisticDto();
                effectCaseStatisticDto.setEventType(type);
                effectCaseStatisticDto.setCaseNumber(CollUtil.isNotEmpty(typeList) ? typeList.size() : 0);
                effectCaseStatisticDto.setPercent(CollUtil.isNotEmpty(typeList) ? new BigDecimal(typeList.size()).divide(new BigDecimal(size), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) : new BigDecimal(0));
                BigDecimal last = CollUtil.isNotEmpty(lastTypeList) ? new BigDecimal(lastTypeList.size()) : new BigDecimal(0);
                BigDecimal now = CollUtil.isNotEmpty(typeList) ? new BigDecimal(typeList.size()) : new BigDecimal(0);
                BigDecimal lastPercent = new BigDecimal(0);
                if (!last.toString().equals("0")) {
                    lastPercent = now.subtract(last).divide(last, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                }

                effectCaseStatisticDto.setLastPercent(lastPercent);
                BigDecimal percent = new BigDecimal(0);
                if (lastEventBases.size()>0) {
                    percent = new BigDecimal(eventBases.size() - lastEventBases.size()).divide(new BigDecimal(lastEventBases.size()), 2, RoundingMode.HALF_UP);
                }
                effectCaseStatisticDto.setCurrentPercent(percent);
                effectCaseStatisticDto.setSelfTotal(eventBases.size());
                if (StrUtil.isNotEmpty(unitId)) {
                    BigDecimal lastPercentValue = new BigDecimal(0);
                    if (lastEventBases.size()>0) {
                         lastPercentValue = new BigDecimal(eventBases.size() - lastEventBases.size()).divide(new BigDecimal(lastEventBases.size()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    }
                    effectCaseStatisticDto.setCurrentPercent(lastPercentValue);
                }
                res.add(effectCaseStatisticDto);
            }
        } else {
            EffectCaseStatisticDto dto = new EffectCaseStatisticDto();
            dto.setSelfTotal(0);
            dto.setPercent(new BigDecimal(0));
            res.add(dto);
        }
        return res;
    }
}
