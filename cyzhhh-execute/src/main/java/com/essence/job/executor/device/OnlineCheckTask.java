package com.essence.job.executor.device;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.QualifiedConfigDTO;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.dao.*;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.entity.*;
import com.essence.service.StationService;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OnlineCheckTask {
    @Resource
    private StationService stationService;
    @Resource
    private StSnConvertDao stSnConvertDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Resource
    private OnlineCheckDeviceDao onlineCheckDeviceDao;
    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Resource
    private VideoStatusRecordHistoryDao videoStatusRecordHistoryDao;
    @Resource
    private DeviceQualifiedDao deviceQualifiedDao;


    /**
     * 设备达标统计
     */

    @XxlJob("OnlineCheckTask")
    public void demoJobHandler() throws Exception {
        System.out.println("在线达标状态 采集开始" + new Date());
        syncDeviceStatus();
        System.out.println("在线达标状态 采集完成" + new Date());
    }

    public void syncDeviceStatus() throws ParseException {
        //获取当前时间月份的
        List<DeviceQualifiedEntity> deviceQualifiedEntities = deviceQualifiedDao.selectList(new QueryWrapper<>());
        Map<String, DeviceQualifiedEntity> deviceMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceQualifiedEntities)) {
            deviceMap = deviceQualifiedEntities.parallelStream().collect(Collectors.toMap(DeviceQualifiedEntity::getDeviceType, Function.identity(), (o1, o2) -> o2));
        }
        //水位站
        DeviceQualifiedEntity station = deviceMap.get("ZZ");
        if (station != null) {
            //水位 流量
            getDayDeviceStatisticJob(station);
        } else {
            //水位 流量
            DeviceQualifiedEntity station1 = new DeviceQualifiedEntity();
            station1.setCondNum(19);
            getDayDeviceStatisticJob(station1);
        }
        //视频配置
        DeviceQualifiedEntity video = deviceMap.get("VIDEO");
        System.out.println("在线达标水位流量状态 采集完成" + new Date());
        if (video != null) {
            //视频
            getDayVideoStatisticJob(video);
        } else {
            //视频
            DeviceQualifiedEntity video1 = new DeviceQualifiedEntity();
            video1.setCondNum(20);
            getDayVideoStatisticJob(video1);
        }
        System.out.println("在线达标视频状态 采集完成" + new Date());
    }

    private void getDayDeviceStatisticJob(DeviceQualifiedEntity station) throws ParseException {
        //获取达标基数
        Integer condNum = station.getCondNum();
        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
        List<String> typeList = new ArrayList<>();
        typeList.add("ZZ");
        typeList.add("ZQ");
        stationWrapper.in("sttp", typeList);
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        List<StSnConvertEntity> convertEntities = stSnConvertDao.selectList(new QueryWrapper<>());
        Map<String, String> stcdMap = convertEntities.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
        //获取 -1天数据
        Date date = DateUtil.offsetDay(new Date(), -1);
        DateTime startDay = DateUtil.beginOfDay(date);
        DateTime endDay = DateUtil.endOfDay(date);
        //24小时的时间点
        List<DateTime> dateTimes = DateUtil.rangeToList(startDay, endDay, DateField.HOUR_OF_DAY);
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            // 用于-设备感知-设备在线-达标统计分析 1在线 2 离线
            List<Integer> status = new ArrayList<>();
            //每小时在线设备数量
            int onlineCount = 0;
            for (DateTime dateTime : dateTimes) {
                //以上一天的时间 上下1小时为开始结束时间
                DateTime start = DateUtil.offsetHour(dateTime, -2);
                DateTime end = dateTime;
                String endZZ;
                String startZZ;
                if (stStbprpBEntity.getSttp().equals("ZZ")) {
                    endZZ = DateUtil.format(end, "yyyy/MM/dd HH:mm:ss");
                    startZZ = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                } else {
                    endZZ = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
                    startZZ = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
                }
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                stStbprpBEntityDTO.setSttp(stStbprpBEntity.getSttp());
                stStbprpBEntityDTO.setStartTime(startZZ);
                stStbprpBEntityDTO.setEndTime(endZZ);
                stStbprpBEntityDTO.setStcd(stStbprpBEntity.getStcd());
                stStbprpBEntityDTO.setStcdMap(stcdMap);
                List<StWaterRateEntityDTO> stationDataListZZ = stationService.getDataList(stStbprpBEntityDTO);
                if (CollUtil.isNotEmpty(stationDataListZZ)) {
                    onlineCount++;
                    status.add(1);
                } else {
                    status.add(2);
                }
            }
            BigDecimal percent = new BigDecimal(onlineCount).divide(new BigDecimal(24), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            /**
             * 用于-设备感知-设备在线-决策分析
             */
            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
            decisionAnalysisDTO.setTotal(24);
            decisionAnalysisDTO.setOnline(onlineCount);
            decisionAnalysisDTO.setOnlinePercent(percent);
            decisionAnalysisDTO.setStcd(stStbprpBEntity.getStcd());
            String statusStr = JSONObject.toJSONString(status);
            decisionAnalysisDTO.setStatus(statusStr);
            decisionAnalysisDTO.setDate(DateUtil.format(date, "yyyy-MM-dd"));
            decisionAnalysisDTO.setDateType(3);
            int size = status.parallelStream().filter(integer -> {
                return integer == 1;
            }).collect(Collectors.toList()).size();
            if (null != condNum) {
                decisionAnalysisDTO.setChecked(condNum > size ? 0 : 1);
            } else {
                decisionAnalysisDTO.setChecked(20 > size ? 0 : 1);
            }
            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
        }
        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)) {
            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
                String dateStr = onlineCheckDeviceEntity.getDate();
                String stcd = onlineCheckDeviceEntity.getStcd();
                Integer dateType = 3;
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", dateStr);
                wrapper.eq("date_type", dateType);
                wrapper.eq("stcd", stcd);
                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity1 != null && StringUtils.isNotBlank(onlineCheckDeviceEntity1.getDate())) {
                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity, wrapper);
                } else {
                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
                }
            }
        }
    }

    /**
     * 视频监控
     */
    private void getDayVideoStatisticJob(DeviceQualifiedEntity station) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //达标基数
        Integer condNum = station.getCondNum();
        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
        List<VideoInfoBase> videoInfoBases = Optional.ofNullable(videoInfoBaseDao.selectList(new QueryWrapper<>())).orElse(Lists.newArrayList());
        Date date = DateUtil.offsetDay(new Date(), -1);
        DateTime startDay = DateUtil.beginOfDay(date);
        DateTime endDay = DateUtil.endOfDay(date);
        QueryWrapper<VideoStatusRecordHistory> queryWrapper = new QueryWrapper();
        queryWrapper.ge("tm", startDay);
        queryWrapper.le("tm", endDay);
        List<VideoStatusRecordHistory> videoStatusRecordHistoryList = Optional.ofNullable(videoStatusRecordHistoryDao.selectList(queryWrapper)).orElse(Lists.newArrayList());
        Map<Integer, List<VideoStatusRecordHistory>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(videoStatusRecordHistoryList)) {
            map = videoStatusRecordHistoryList.stream().collect(Collectors.groupingBy(VideoStatusRecordHistory::getVideoId));
        }

        //24小时的时间点
        List<DateTime> dateTimes = DateUtil.rangeToList(startDay, endDay, DateField.HOUR_OF_DAY);
        for (VideoInfoBase videoInfoBase : videoInfoBases) {
            //获取设备数据
            List<VideoStatusRecordHistory> videoStatusRecordHistoryList1 = Optional.ofNullable(map.get(videoInfoBase.getId())).orElse(Lists.newArrayList());
            // 用于-设备感知-设备在线-达标统计分析 1在线 2 离线
            List<Integer> status = new ArrayList<>();
            //每小时在线设备数量
            int onlineCount = 0;
            for (int i = 0; i < dateTimes.size() - 1; i++) {
                if (!CollectionUtils.isEmpty(videoStatusRecordHistoryList1)) {
                    int finalI = i;
                    List<VideoStatusRecordHistory> list = videoStatusRecordHistoryList1.stream().filter(x -> {
                        Boolean type = false;
                        try {
                            Date parse = simpleDateFormat.parse(x.getTm());
                            if (parse.getTime() >= dateTimes.get(finalI).getTime() && parse.getTime() <= dateTimes.get(finalI + 1).getTime()&& x.getStatus().equals("1") ) {
                                type = true;
                            }
                        } catch (Exception e) {
                        }
                        return type;
                    }).collect(Collectors.toList());

                    if (CollUtil.isNotEmpty(list)) {
                        onlineCount++;
                        status.add(1);
                    } else {
                        status.add(2);
                    }
                    if (i + 1 == dateTimes.size() - 1) {
                        List<VideoStatusRecordHistory> lists = videoStatusRecordHistoryList1.stream().filter(x -> {
                            Boolean type = false;
                            try {
                                Date parse = simpleDateFormat.parse(x.getTm());
                                if (parse.getTime() >= dateTimes.get(finalI + 1).getTime() && parse.getTime() <= DateUtil.endOfDay(dateTimes.get(finalI + 1)).getTime()&& x.getStatus().equals("1")) {
                                    type = true;
                                }
                            } catch (Exception e) {

                            }
                            return type;
                        }).collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(lists)) {
                            onlineCount++;
                            status.add(1);
                        } else {
                            status.add(2);
                        }
                    }
                }else {
                    status.add(2);
                    if (i + 1 == dateTimes.size() - 1) {
                        status.add(2);
                    }
                }
            }
            BigDecimal percent = new BigDecimal(onlineCount).divide(new BigDecimal(24), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            /**
             * 用于-设备感知-设备在线-决策分析
             */
            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
            decisionAnalysisDTO.setTotal(24);
            decisionAnalysisDTO.setOnline(onlineCount);
            decisionAnalysisDTO.setOnlinePercent(percent);
            decisionAnalysisDTO.setStcd(videoInfoBase.getId().toString());
            String statusStr = JSONObject.toJSONString(status);
            decisionAnalysisDTO.setStatus(statusStr);
            decisionAnalysisDTO.setDate(DateUtil.format(date, "yyyy-MM-dd"));
            decisionAnalysisDTO.setDateType(3);
            int size = status.parallelStream().filter(integer -> {
                return integer == 1;
            }).collect(Collectors.toList()).size();
            if (null != condNum) {
                decisionAnalysisDTO.setChecked(condNum > size ? 0 : 1);
            } else {
                decisionAnalysisDTO.setChecked(20 > size ? 0 : 1);
            }
            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
        }

        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)) {
            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
                String dateStr = onlineCheckDeviceEntity.getDate();
                String stcd = onlineCheckDeviceEntity.getStcd();
                Integer dateType = 3;
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", dateStr);
                wrapper.eq("date_type", dateType);
                wrapper.eq("stcd", stcd);
                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity1 != null) {
                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity, wrapper);
                } else {
                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
                }
            }
        }

    }

    public void getMonthStatisticJob(DeviceQualifiedEntity qualifiedConfigDTO) {
        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();

        stationWrapper.eq("sttp", "ZZ").or().eq("sttp", "ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        Date date = new Date();
        DateTime startMonth = DateUtil.beginOfMonth(date);
        DateTime endMonth = DateUtil.endOfMonth(date);

        //30 天 或者 31 天  fix: 改成当天减去1天
        List<DateTime> dateTimes = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);

        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            // 用于-设备感知-设备在线-达标统计分析 1在线 2 离线
            List<Integer> status = new ArrayList<>();
            //在线天数
            int onlineCount = 0;
            for (DateTime dateTime : dateTimes) {
                DateTime startDay = DateUtil.beginOfDay(dateTime);
                //取昨天时间的设备数量
                String startZZ = DateUtil.format(startDay, "yyyy-MM-dd");

                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", startZZ);
                wrapper.eq("date_type", 3);
                wrapper.eq("stcd", stStbprpBEntity.getStcd());
                OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity != null) {
                    String dbStatus = onlineCheckDeviceEntity.getStatus();
                    List<Integer> integers = JSONObject.parseArray(dbStatus, Integer.class);
                    List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
                        return integer.equals(1);
                    }).collect(Collectors.toList());
                    //按照配置的比例 决定这天算不算达标
                    Integer condNum = qualifiedConfigDTO.getCondNum();
                    if (condNum != null && onlineStatus.size() >= condNum) {
                        onlineCount++;
                        status.add(1);
                    } else if (onlineStatus.size() >= 20) {
                        onlineCount++;
                        status.add(1);
                    } else {
                        status.add(2);
                    }
                } else {
                    status.add(2);
                }
            }

            BigDecimal percent = new BigDecimal(onlineCount).divide(new BigDecimal(status.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            /**
             * 用于-设备感知-设备在线-决策分析
             */
            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
            decisionAnalysisDTO.setTotal(status.size());
            decisionAnalysisDTO.setOnline(onlineCount);
            decisionAnalysisDTO.setOnlinePercent(percent);
            decisionAnalysisDTO.setStcd(stStbprpBEntity.getStcd());
            String statusStr = JSONObject.toJSONString(status);
            decisionAnalysisDTO.setStatus(statusStr);
            decisionAnalysisDTO.setDate(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss").substring(0, 7));
            decisionAnalysisDTO.setDateType(2);
            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
        }
        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)) {
            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
                String dateStr = onlineCheckDeviceEntity.getDate();
                String stcd = onlineCheckDeviceEntity.getStcd();
                Integer dateType = 2;
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", dateStr);
                wrapper.eq("date_type", dateType);
                wrapper.eq("stcd", stcd);
                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity1 != null) {
                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity, wrapper);
                } else {
                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
                }
            }
        }
    }

    public void getYearStatistic(QualifiedConfigDTO qualifiedConfigDTO) {
//        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
//        Date date = new Date();
//        DateTime startYear = DateUtil.beginOfYear(date);
//        DateTime endYear = DateUtil.endOfYear(date);
//        //一年的12 个月
//        List<DateTime> dateTimes = DateUtil.rangeToList(startYear, endYear, DateField.MONTH);
//        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
//
//        stationWrapper.eq("sttp","ZZ").or().eq("sttp","ZQ");
//        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
//
//        //按照配置的比例 决定这天算不算达标
//
//        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
//            String stcd = stStbprpBEntity.getStcd();
//            List<Integer> statusList = new ArrayList<>();
//            Integer onValue = 0;
//            Integer downValue = 0;
//            BigDecimal percent = new BigDecimal(0);
//            for (DateTime month : dateTimes) {
//                String monthDate = DateUtil.format(month, "yyyy-MM-dd HH:mm:ss").substring(0, 7);
//                Integer dateType = 2;
//                QueryWrapper monthWrapper = new QueryWrapper();
//                monthWrapper.eq("date_type",dateType);
//                monthWrapper.eq("date",monthDate);
//                monthWrapper.eq("stcd",stcd);
//                OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(monthWrapper);
//                if (onlineCheckDeviceEntity != null){
//                    String status = onlineCheckDeviceEntity.getStatus();
//                    List<Integer> integers = JSONObject.parseArray(status, Integer.class);
//                    List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
//                        return integer.equals(1);
//                    }).collect(Collectors.toList());
//                    BigDecimal value =   new BigDecimal(onlineStatus.size()).divide(new BigDecimal(integers.size()),2,BigDecimal.ROUND_HALF_UP);
//                    if (minPercent != null){
//                        if (value.compareTo(minPercent) >= 0){
//                            statusList.add(1);
//                        }
//                    }
//                    else if (value.compareTo(new BigDecimal(0.9)) >= 0){
//                        statusList.add(1);
//                    }else {
//                        statusList.add(2);
//                    }
//                }else {
//                    statusList.add(2);
//                }
//            }
//             List<Integer> onlineStatus = statusList.parallelStream().filter(integer -> {
//                return integer.equals(1);
//            }).collect(Collectors.toList());
//            percent = new BigDecimal(onlineStatus.size()).divide(new BigDecimal(statusList.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
//            /**
//             * 用于-设备感知-设备在线-决策分析
//             */
//            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
//            decisionAnalysisDTO.setTotal(statusList.size());
//            decisionAnalysisDTO.setOnline(onlineStatus.size());
//            decisionAnalysisDTO.setOnlinePercent(percent);
//            decisionAnalysisDTO.setStcd(stStbprpBEntity.getStcd());
//            String statusStr = JSONObject.toJSONString(statusList);
//            decisionAnalysisDTO.setStatus(statusStr);
//            decisionAnalysisDTO.setDate(DateUtil.format(date,"yyyy-MM-dd HH:mm:ss").substring(0,4));
//            decisionAnalysisDTO.setDateType(1);
//            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
//
//        }
//        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)){
//            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
//                String dateStr = onlineCheckDeviceEntity.getDate();
//                String stcd = onlineCheckDeviceEntity.getStcd();
//                Integer dateType = 1;
//                QueryWrapper wrapper = new QueryWrapper();
//                wrapper.eq("date",dateStr);
//                wrapper.eq("date_type",dateType);
//                wrapper.eq("stcd",stcd);
//                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
//                if (onlineCheckDeviceEntity1 != null){
//                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity,wrapper);
//                }else {
//                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
//                }
//            }
//        }
    }

    public void getMonthVideoStatisticJob(DeviceQualifiedEntity qualifiedConfigDTO) {
        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        Date date = new Date();
        DateTime startMonth = DateUtil.beginOfMonth(date);
        DateTime endMonth = DateUtil.endOfMonth(date);

        //30 天 或者 31 天 fix:改为当天减去一天 因为今天之后的时间还不存在
        List<DateTime> dateTimes = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);
        for (VideoInfoBase videoInfoBase : videoInfoBases) {
            // 用于-设备感知-设备在线-达标统计分析 1在线 2 离线
            List<Integer> status = new ArrayList<>();
            //在线天数
            int onlineCount = 0;
            for (DateTime dateTime : dateTimes) {
                DateTime startDay = DateUtil.beginOfDay(dateTime);
                //取昨天时间的设备数量
                String startZZ = DateUtil.format(startDay, "yyyy-MM-dd");

                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", startZZ);
                wrapper.eq("date_type", 3);
                wrapper.eq("stcd", videoInfoBase.getId());
                OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity != null) {
                    String dbStatus = onlineCheckDeviceEntity.getStatus();
                    List<Integer> integers = JSONObject.parseArray(dbStatus, Integer.class);
                    List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
                        return integer.equals(1);
                    }).collect(Collectors.toList());
                    //按照配置的比例 决定这天算不算达标
                    if (qualifiedConfigDTO.getCondNum() != null && onlineStatus.size() >= qualifiedConfigDTO.getCondNum()) {
                        onlineCount++;
                        status.add(1);
                    } else if (onlineStatus.size() >= 20) {
                        onlineCount++;
                        status.add(1);
                    } else {
                        status.add(2);
                    }
                } else {
                    status.add(2);
                }
            }

            BigDecimal percent = new BigDecimal(onlineCount).divide(new BigDecimal(status.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            /**
             * 用于-设备感知-设备在线-决策分析
             */
            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
            decisionAnalysisDTO.setTotal(status.size());
            decisionAnalysisDTO.setOnline(onlineCount);
            decisionAnalysisDTO.setOnlinePercent(percent);
            decisionAnalysisDTO.setStcd(videoInfoBase.getId().toString());
            String statusStr = JSONObject.toJSONString(status);
            decisionAnalysisDTO.setStatus(statusStr);
            decisionAnalysisDTO.setDate(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss").substring(0, 7));
            decisionAnalysisDTO.setDateType(2);
            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
        }
        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)) {
            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
                String dateStr = onlineCheckDeviceEntity.getDate();
                String stcd = onlineCheckDeviceEntity.getStcd();
                Integer dateType = 2;
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date", dateStr);
                wrapper.eq("date_type", dateType);
                wrapper.eq("stcd", stcd);
                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
                if (onlineCheckDeviceEntity1 != null) {
                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity, wrapper);
                } else {
                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
                }
            }
        }
    }

    public void getYearVideoStatistic(QualifiedConfigDTO qualifiedConfigDTO) {
//        List<OnlineCheckDeviceEntity> onlineCheckDeviceEntities = new ArrayList<>();
//        Date date = new Date();
//        DateTime startYear = DateUtil.beginOfYear(date);
//        DateTime endYear = DateUtil.endOfYear(date);
//        //一年的12 个月
//        List<DateTime> dateTimes = DateUtil.rangeToList(startYear, endYear, DateField.MONTH);
//        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
//        //按照配置的比例 决定这天算不算达标
//        BigDecimal minPercent = null;
//        if (qualifiedConfigDTO != null){
//            BigDecimal minValue = qualifiedConfigDTO.getMinValue();
//            minPercent = minValue.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
//
//        }
//        for (VideoInfoBase videoInfoBase : videoInfoBases) {
//            String stcd = videoInfoBase.getId().toString();
//            List<Integer> statusList = new ArrayList<>();
//            Integer onValue = 0;
//            Integer downValue = 0;
//            BigDecimal percent = new BigDecimal(0);
//            for (DateTime month : dateTimes) {
//                String monthDate = DateUtil.format(month, "yyyy-MM-dd HH:mm:ss").substring(0, 7);
//                Integer dateType = 2;
//                QueryWrapper monthWrapper = new QueryWrapper();
//                monthWrapper.eq("date_type",dateType);
//                monthWrapper.eq("date",monthDate);
//                monthWrapper.eq("stcd",stcd);
//                OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(monthWrapper);
//                if (onlineCheckDeviceEntity != null){
//                    String status = onlineCheckDeviceEntity.getStatus();
//                    List<Integer> integers = JSONObject.parseArray(status, Integer.class);
//                    List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
//                        return integer.equals(1);
//                    }).collect(Collectors.toList());
//                    BigDecimal value =   new BigDecimal(onlineStatus.size()).divide(new BigDecimal(integers.size()),2,BigDecimal.ROUND_HALF_UP);
//                    if (minPercent != null){
//                        if (value.compareTo(minPercent) >= 0){
//                            statusList.add(1);
//                        }
//                    }
//                    else if (value.compareTo(new BigDecimal(0.9)) >= 0){
//                        statusList.add(1);
//                    }else {
//                        statusList.add(2);
//                    }
//                }else {
//                    statusList.add(2);
//                }
//            }
//            List<Integer> onlineStatus = statusList.parallelStream().filter(integer -> {
//                return integer.equals(1);
//            }).collect(Collectors.toList());
//            percent = new BigDecimal(onlineStatus.size()).divide(new BigDecimal(statusList.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
//            /**
//             * 用于-设备感知-设备在线-决策分析
//             */
//            OnlineCheckDeviceEntity decisionAnalysisDTO = new OnlineCheckDeviceEntity();
//            decisionAnalysisDTO.setTotal(statusList.size());
//            decisionAnalysisDTO.setOnline(onlineStatus.size());
//            decisionAnalysisDTO.setOnlinePercent(percent);
//            decisionAnalysisDTO.setStcd(stcd);
//            String statusStr = JSONObject.toJSONString(statusList);
//            decisionAnalysisDTO.setStatus(statusStr);
//            decisionAnalysisDTO.setDate(DateUtil.format(date,"yyyy-MM-dd HH:mm:ss").substring(0,4));
//            decisionAnalysisDTO.setDateType(1);
//            onlineCheckDeviceEntities.add(decisionAnalysisDTO);
//
//        }
//        if (CollUtil.isNotEmpty(onlineCheckDeviceEntities)){
//            for (OnlineCheckDeviceEntity onlineCheckDeviceEntity : onlineCheckDeviceEntities) {
//                String dateStr = onlineCheckDeviceEntity.getDate();
//                String stcd = onlineCheckDeviceEntity.getStcd();
//                Integer dateType = 1;
//                QueryWrapper wrapper = new QueryWrapper();
//                wrapper.eq("date",dateStr);
//                wrapper.eq("date_type",dateType);
//                wrapper.eq("stcd",stcd);
//                OnlineCheckDeviceEntity onlineCheckDeviceEntity1 = onlineCheckDeviceDao.selectOne(wrapper);
//                if (onlineCheckDeviceEntity1 != null){
//                    onlineCheckDeviceDao.update(onlineCheckDeviceEntity,wrapper);
//                }else {
//                    onlineCheckDeviceDao.insert(onlineCheckDeviceEntity);
//                }
//            }
//        }
    }
}
