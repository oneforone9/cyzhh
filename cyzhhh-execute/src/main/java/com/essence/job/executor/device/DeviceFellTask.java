package com.essence.job.executor.device;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.QualifiedConfigDTO;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.common.utils.ConvertUtil;
import com.essence.dao.*;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.entity.*;
import com.essence.service.StationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DeviceFellTask {

    @Autowired
    private StationService stationService;

    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Resource
    private DeviceFellDao deviceFellDao;

    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;

    @Resource
    private VideoStatusRecordDao videoStatusRecordDao;
    @Resource
    private VideoStatusRecordHistoryDao videoStatusRecordHistoryDao;

    @Resource
    private DeviceQualifiedDao deviceQualifiedDao;


    @XxlJob("DeviceFellTask")
    public void demoJobHandler() throws Exception {
        run();
        System.out.println("设备年月日 状态 DeviceFellTask 采集完成" + new Date());
    }


    /**
     * 定时更新视频在线状态
     */
    public void run() throws ParseException {
        //获取当前时间月份的

        List<DeviceQualifiedEntity> deviceQualifiedEntities = deviceQualifiedDao.selectList(new QueryWrapper<>());
        Map<String, DeviceQualifiedEntity> deviceMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceQualifiedEntities)) {
            deviceMap = deviceQualifiedEntities.parallelStream().collect(Collectors.toMap(DeviceQualifiedEntity::getDeviceType, Function.identity(), (o1, o2) -> o2));
        }

       DeviceQualifiedEntity station = deviceMap.get("ZZ");
        getMonthStatistic(station);
        //视频配置
        DeviceQualifiedEntity video = deviceMap.get("VIDEO");
        //分年月日 配置
        //视频 年 月 日
        List<String> typeList1 = new ArrayList<>();
        typeList1.add("HUAWEI");
        typeList1.add("HIV");

        getMonthVideoStatistic(video, "VIDEO", typeList1);
        List<String> typeList2 = new ArrayList<>();
        typeList2.add("ZX");
        typeList2.add("NW");
        typeList2.add("YSY");
        getMonthVideoStatistic(video, "LJ", typeList2);
    }

    public void getMonthStatistic(DeviceQualifiedEntity qualifiedConfigDTO) throws ParseException {
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
        List<String> typeList = new ArrayList<>();
        typeList.add("ZZ");
        typeList.add("ZQ");
        stationWrapper.in("sttp", typeList);
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item -> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        //当月起始时间 当月结束时间
        Date date = new Date();
        DateTime startMonth = DateUtil.beginOfMonth(date);
        DateTime endMonth = DateUtil.endOfMonth(date);
        //30 天 或者 31 天
        List<DateTime> dateTimes = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);
        //1.根据类型 ZZ OR ZQ
        for (String type : sttpMap.keySet()) {
            List<Integer> upValue = new ArrayList<>();
            List<Integer> downValue = new ArrayList<>();
            List<BigDecimal> percent = new ArrayList<>();
            for (int i = 0; i < dateTimes.size(); i++) {
                DateTime dateTime = dateTimes.get(i);
                //每小时在线设备数量
                int onlineCount = 0;
                int size = sttpMap.get(type).size();
                for (StStbprpBEntity stStbprpBEntity : sttpMap.get(type)) {
                    DateTime day = DateUtil.beginOfDay(dateTime);
                    //取昨天时间的设备数量
                    DateTime start = DateUtil.offsetDay(day, -1);
                    String endZZ;
                    String startZZ;
                    if (type.equals("ZZ")) {
                        endZZ = DateUtil.format(day, "yyyy/MM/dd HH:mm:ss");
                        startZZ = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                    } else {
                        endZZ = DateUtil.format(day, "yyyy-MM-dd HH:mm:ss");
                        startZZ = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
                    }
                    StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                    stStbprpBEntityDTO.setSttp(type);
                    stStbprpBEntityDTO.setStartTime(startZZ);
                    stStbprpBEntityDTO.setEndTime(endZZ);
                    stStbprpBEntityDTO.setStcd(stStbprpBEntity.getStcd());
                    List<StWaterRateEntityDTO> stationDataListZZ = stationService.getDataList(stStbprpBEntityDTO);
                    if (CollUtil.isNotEmpty(stationDataListZZ)) {
                        //按照配置的小时数 决定这天算不算达标 ， fix 使用已小时 map 一天只有24个大小 目前达标只要满足20 则代表 在线达标 在线达标+1 否则 不在线 +1
                        Map<String, List<StWaterRateEntityDTO>> timeMap = stationDataListZZ.parallelStream().collect(Collectors.groupingBy(stWaterRateEntityDTO -> {
                            return stWaterRateEntityDTO.getCtime().substring(0, 13);
                        }));
                        if (qualifiedConfigDTO == null && stationDataListZZ.size() >= 20) {
                            onlineCount++;
                        } else if (qualifiedConfigDTO != null) {
                            Integer condNum = qualifiedConfigDTO.getCondNum();
                            if (condNum != null && timeMap.keySet().size() > condNum) {
                                onlineCount++;
                            }
                        }


                    }

                }
                if (i == 0) {
                    String time = DateUtil.format(DateUtil.offsetDay(dateTime, -1), "yyyy-MM-dd HH:mm:ss").substring(0, 7);
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("type", type);
                    wrapper.eq("date_type", 2);
                    wrapper.eq("date", time);
                    DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);
                    if (null != deviceFellEntity1 && StringUtils.isNotBlank(deviceFellEntity1.getUpValue())) {
                        String percent1 = deviceFellEntity1.getPercent().substring(1, deviceFellEntity1.getPercent().length() - 1);
                        String upValue1 = deviceFellEntity1.getUpValue().substring(1, deviceFellEntity1.getUpValue().length() - 1);
                        String downValue1 = deviceFellEntity1.getDownValue().substring(1, deviceFellEntity1.getDownValue().length() - 1);
                        BigDecimal multiply = new BigDecimal(onlineCount).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        String[] split = percent1.split(",");
                        List<String> list = Arrays.asList(split);
                        List<BigDecimal> collect = list.stream().map(x -> new BigDecimal(x)).collect(Collectors.toList());
                        collect.set(collect.size() - 1, multiply);
                        deviceFellEntity1.setPercent(JSONObject.toJSONString(collect));
                        deviceFellEntity1.setUpValue(JSONObject.toJSONString(save(upValue1, onlineCount)));
                        deviceFellEntity1.setDownValue(JSONObject.toJSONString(save(downValue1, size - onlineCount)));
                        deviceFellDao.update(deviceFellEntity1, wrapper);
                    }
                } else {
                    upValue.add(onlineCount);
                    downValue.add(size - onlineCount);
                    percent.add(new BigDecimal(onlineCount).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                    String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
                    String substring = format.substring(0, 7);
                    saveDeviceFell(type, substring, upValue, downValue, percent);
                    if (i == dateTimes.size() - 1) {
                        upValue.add(0);
                        downValue.add(0);
                        percent.add(new BigDecimal(0));
                        saveDeviceFell(type, substring, upValue, downValue, percent);
                    }
                }
            }
        }

    }

    private List<Integer> save(String percent1, Integer count) {
        String[] split = percent1.split(",");
        List<String> list = Arrays.asList(split);
        List<Integer> collect = list.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        collect.set(collect.size() - 1, count);
        return collect;
    }

    private void saveDeviceFell(String type, String substring, List<Integer> upValue, List<Integer> downValue, List<BigDecimal> percent) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", type);
        wrapper.eq("date_type", 2);
        wrapper.eq("date", substring);
        DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);

        DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
        deviceFellEntity.setType(type);
        deviceFellEntity.setDateType(2);
        deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
        deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
        deviceFellEntity.setUpValue(JSONObject.toJSONString(upValue));

        deviceFellEntity.setDate(substring);
        if (deviceFellEntity1 != null) {
            String percent1 = deviceFellEntity1.getPercent();
            String upValue1 = deviceFellEntity1.getUpValue();
            String downValue1 = deviceFellEntity1.getDownValue();

            List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
            List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue1, JSONObject.toJSONString(upValue), 1);
            List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);

            deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
            deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
            deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
            deviceFellDao.update(deviceFellEntity, wrapper);
        } else {
            deviceFellDao.insert(deviceFellEntity);
        }
    }

    /**
     * 普通 月 每天的达标个数
     *
     * @throws ParseException
     */
    public void getMonthVideoStatistic(DeviceQualifiedEntity qualifiedConfigDTO, String type, List<String> typeList) throws ParseException {
        QueryWrapper wrapperType = new QueryWrapper<>();
        wrapperType.in("source", typeList);
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(wrapperType);
        Date date = new Date();
        DateTime startMonth = DateUtil.beginOfMonth(date);
        DateTime endMonth = DateUtil.endOfMonth(date);
        //30 天 或者 31 天
        List<DateTime> dateTimes = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);

        List<Integer> upValue = new ArrayList<>();
        List<Integer> downValue = new ArrayList<>();
        List<BigDecimal> percent = new ArrayList<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            DateTime dateTime = dateTimes.get(i);
            //每小时在线设备数量
            int onlineCount = 0;
            int size = videoInfoBases.size();
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                DateTime end = DateUtil.beginOfDay(dateTime);
                //取昨天时间的设备数量
                DateTime start = DateUtil.offsetDay(end, -1);

                QueryWrapper<VideoStatusRecordHistory> wrapper = new QueryWrapper();
                wrapper.le("tm", end);
                wrapper.ge("tm", start);
                wrapper.eq("status", "1");
                wrapper.eq("video_id", videoInfoBase.getId());

                List<VideoStatusRecordHistory> stationDataListZZ = videoStatusRecordHistoryDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stationDataListZZ)) {

                    Map<String, List<VideoStatusRecordHistory>> timeMap = stationDataListZZ.parallelStream().collect(Collectors.groupingBy(videoStatusRecordHistory -> {
                        return videoStatusRecordHistory.getTm().substring(0, 13);
                    }));
                    //按照配置的比例 决定这天算不算达标
                    if (qualifiedConfigDTO == null && timeMap.keySet().size() >= 20) {
                        onlineCount++;
                    } else if (qualifiedConfigDTO != null) {
                        Integer condNum = qualifiedConfigDTO.getCondNum();
                        if (condNum != null && timeMap.keySet().size() > condNum) {
                            onlineCount++;
                        }
                    }
                }
            }
            if (i == 0) {
                String time = DateUtil.format(DateUtil.offsetDay(dateTime, -1), "yyyy-MM-dd HH:mm:ss").substring(0, 7);
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("type", type);
                wrapper.eq("date_type", 2);
                wrapper.eq("date", time);
                DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);
                if (null != deviceFellEntity1 && StringUtils.isNotBlank(deviceFellEntity1.getUpValue())) {
                    String percent1 = deviceFellEntity1.getPercent().substring(1, deviceFellEntity1.getPercent().length() - 1);
                    String upValue1 = deviceFellEntity1.getUpValue().substring(1, deviceFellEntity1.getUpValue().length() - 1);
                    String downValue1 = deviceFellEntity1.getDownValue().substring(1, deviceFellEntity1.getDownValue().length() - 1);
                    BigDecimal multiply = new BigDecimal(onlineCount).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    String[] split = percent1.split(",");
                    List<String> list = Arrays.asList(split);
                    List<BigDecimal> collect = list.stream().map(x -> new BigDecimal(x)).collect(Collectors.toList());
                    collect.set(collect.size() - 1, multiply);
                    deviceFellEntity1.setPercent(JSONObject.toJSONString(collect));
                    deviceFellEntity1.setUpValue(JSONObject.toJSONString(save(upValue1, onlineCount)));
                    deviceFellEntity1.setDownValue(JSONObject.toJSONString(save(downValue1, size - onlineCount)));
                    deviceFellDao.update(deviceFellEntity1, wrapper);
                }
            } else {
                upValue.add(onlineCount);
                downValue.add(size - onlineCount);
                percent.add(new BigDecimal(onlineCount).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                if (i == dateTimes.size() - 1) {
                    upValue.add(0);
                    downValue.add(0);
                    percent.add(new BigDecimal(0));
                }
            }
        }
        String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String substring = format.substring(0, 7);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", type);
        wrapper.eq("date_type", 2);
        wrapper.eq("date", substring);
        DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);

        DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
        deviceFellEntity.setType(type);
        deviceFellEntity.setDateType(2);
        deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
        deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
        deviceFellEntity.setUpValue(JSONObject.toJSONString(upValue));

        deviceFellEntity.setDate(substring);
        if (deviceFellEntity1 != null) {
            String percent1 = deviceFellEntity1.getPercent();
            String upValue1 = deviceFellEntity1.getUpValue();
            String downValue1 = deviceFellEntity1.getDownValue();

            List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
            List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue1, JSONObject.toJSONString(upValue), 1);
            List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);

            deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
            deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
            deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
            deviceFellDao.update(deviceFellEntity, wrapper);
        } else {
            deviceFellDao.insert(deviceFellEntity);
        }
    }
    public void getYearStatistic(QualifiedConfigDTO qualifiedConfigDTO) {

        //获取当前时间的年 开始结束 内的月份 每月的天数在线设备数量/总数 >= 0.9 则计为1天
//        Date date = new Date();
//        DateTime startYear = DateUtil.beginOfYear(date);
//        DateTime endYear = DateUtil.endOfYear(date);
//        //一年的12 个月
//        List<DateTime> dateTimes = DateUtil.rangeToList(startYear, endYear, DateField.MONTH);
//        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
//        List<String> typeList = new ArrayList<>();
//        typeList.add("ZZ");
//        typeList.add("ZQ");
//        stationWrapper.in("sttp",typeList);
//        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
//        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item-> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
//
//        //按照配置的比例 决定这天算不算达标
//        BigDecimal minPercent = null;
//        if (qualifiedConfigDTO != null){
//            BigDecimal minValue = qualifiedConfigDTO.getMinValue();
//             minPercent = minValue.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
//
//        }
//        for (String type : sttpMap.keySet()) {
//            //设备总数
//            int size = sttpMap.keySet().size();
//            List<Integer> onValue = new ArrayList<>();
//            List<Integer> downValue = new ArrayList<>();
//            List<BigDecimal> percent = new ArrayList<>();
//            for (DateTime month : dateTimes) {
//                DateTime startMonth = DateUtil.beginOfMonth(month);
//                DateTime endMonth = DateUtil.endOfMonth(month);
//                List<DateTime> times = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);
//                String format = DateUtil.format(month, "yyyy-MM-dd HH:mm:ss");
//                QueryWrapper wrapper = new QueryWrapper();
//                wrapper.eq("date_type",2);
//                wrapper.eq("date",format.substring(0,7));
//                wrapper.eq("type",type);
//                DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
//                Integer days = 0;
//                //设备达标的天数
//                if(deviceFellEntity !=null){
//                    String upValueStr = deviceFellEntity.getUpValue();
//                    List<Integer> upValue = JSONObject.parseArray(upValueStr,Integer.class);
//                    for (Integer integer : upValue) {
//                        BigDecimal value =   new BigDecimal(integer).divide(new BigDecimal(size),2,BigDecimal.ROUND_HALF_UP);
//                        if (minPercent != null){
//                            if (value.compareTo(minPercent) >= 0){
//                                days ++;
//                            }
//                        }
//                        else if (value.compareTo(new BigDecimal(0.9)) >= 0){
//                            days ++;
//                        }
//                    }
//                }
//                onValue.add(days);
//                downValue.add(times.size()-days);
//                percent.add(new BigDecimal(days).divide(new BigDecimal(times.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
//            }
//            String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
//            String substring = format.substring(0, 4);
//            QueryWrapper wrapper = new QueryWrapper();
//            wrapper.eq("type",type);
//            wrapper.eq("date_type",1);
//            wrapper.eq("date",substring);
//            DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);
//
//            DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
//            deviceFellEntity.setType(type);
//            deviceFellEntity.setDateType(1);
//            deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
//            deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
//            deviceFellEntity.setUpValue(JSONObject.toJSONString(onValue));
//
//            deviceFellEntity.setDate(substring);
//            if (deviceFellEntity1 != null){
//                String percent1 = deviceFellEntity1.getPercent();
//                String upValue = deviceFellEntity1.getUpValue();
//                String downValue1 = deviceFellEntity1.getDownValue();
//
//                List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
//                List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue, JSONObject.toJSONString(onValue), 1);
//                List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);
//
//                deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
//                deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
//                deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
//                deviceFellDao.update(deviceFellEntity,wrapper);
//            }else {
//
//                deviceFellDao.insert(deviceFellEntity);
//            }
//
//        }
    }

    public void getDayStatistic(QualifiedConfigDTO qualifiedConfigDTO) throws ParseException {
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();
        List<String> typeList = new ArrayList<>();
        typeList.add("ZZ");
        typeList.add("ZQ");
        stationWrapper.in("sttp", typeList);
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item -> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
        Map<String, String> stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
        Date date = DateUtil.offsetDay(new Date(), -1);
        DateTime startDay = DateUtil.beginOfDay(date);
        DateTime endDay = DateUtil.endOfDay(date);
        //24小时的时间点
        List<DateTime> dateTimes = DateUtil.rangeToList(startDay, endDay, DateField.HOUR_OF_DAY);
        //1.根据类型 ZZ OR ZQ
        for (String type : sttpMap.keySet()) {
            List<Integer> upValue = new ArrayList<>();
            List<Integer> downValue = new ArrayList<>();
            List<BigDecimal> percent = new ArrayList<>();
            List<StStbprpBEntity> zz = sttpMap.get(type);
            int count = zz.size();
            for (DateTime dateTime : dateTimes) {
                //每小时在线设备数量
                int onlineCount = 0;
                for (StStbprpBEntity stStbprpBEntity : zz) {
                    //以当前时间 上下1小时为开始结束时间
                    DateTime start = DateUtil.offsetHour(dateTime, -1);
                    DateTime end = dateTime;
                    String endZZ;
                    String startZZ;
                    if (type.equals("ZZ")) {
                        endZZ = DateUtil.format(end, "yyyy/MM/dd HH:mm:ss");
                        startZZ = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                    } else {
                        endZZ = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");
                        startZZ = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
                    }
                    StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                    stStbprpBEntityDTO.setSttp(type);
                    stStbprpBEntityDTO.setStartTime(startZZ);
                    stStbprpBEntityDTO.setEndTime(endZZ);
                    stStbprpBEntityDTO.setStcdMap(stcdMap);
                    stStbprpBEntityDTO.setStcd(stStbprpBEntity.getStcd());
                    List<StWaterRateEntityDTO> stationDataListZZ = stationService.getDataList(stStbprpBEntityDTO);
                    if (CollUtil.isNotEmpty(stationDataListZZ)) {
                        onlineCount++;
                    }
                }
                upValue.add(onlineCount);
                downValue.add(count - onlineCount);
                percent.add(new BigDecimal(onlineCount).divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }
            String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String substring = format.substring(0, 10);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("type", type);
            wrapper.eq("date_type", 3);
            wrapper.eq("date", substring);
            DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);

            DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
            deviceFellEntity.setType(type);
            deviceFellEntity.setDateType(3);
            deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
            deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
            deviceFellEntity.setUpValue(JSONObject.toJSONString(upValue));

            deviceFellEntity.setDate(substring);
            if (deviceFellEntity1 != null) {
                String percent1 = deviceFellEntity1.getPercent();
                String upValue1 = deviceFellEntity1.getUpValue();
                String downValue1 = deviceFellEntity1.getDownValue();

                List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
                List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue1, JSONObject.toJSONString(upValue), 1);
                List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);

                deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
                deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
                deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
                deviceFellDao.update(deviceFellEntity, wrapper);
            } else {

                deviceFellDao.insert(deviceFellEntity);
            }
        }
    }

    /**
     * 视频统计
     *
     * @throws ParseException
     */
    public void getDayVideoStatistic(QualifiedConfigDTO qualifiedConfigDTO) throws ParseException {
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        Date date = DateUtil.offsetDay(new Date(), -1);
        DateTime startDay = DateUtil.beginOfDay(date);
        DateTime endDay = DateUtil.endOfDay(date);
        //24小时的时间点
        List<DateTime> dateTimes = DateUtil.rangeToList(startDay, endDay, DateField.HOUR_OF_DAY);

        List<Integer> upValue = new ArrayList<>();
        List<Integer> downValue = new ArrayList<>();
        List<BigDecimal> percent = new ArrayList<>();

        int count = videoInfoBases.size();
        for (DateTime dateTime : dateTimes) {
            //每小时在线设备数量
            int onlineCount = 0;
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                //以当前时间 上下1小时为开始结束时间
                DateTime start = DateUtil.offsetHour(dateTime, -1);
                DateTime end = dateTime;

                QueryWrapper<VideoStatusRecordHistory> wrapper = new QueryWrapper();
                wrapper.le("tm", end);
                wrapper.ge("tm", start);
                wrapper.ge("video_id", videoInfoBase.getId());

                List<VideoStatusRecordHistory> stationDataListZZ = videoStatusRecordHistoryDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stationDataListZZ)) {
                    onlineCount++;
                }
            }
            upValue.add(onlineCount);
            downValue.add(count - onlineCount);
            percent.add(new BigDecimal(onlineCount).divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }
        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        String substring = format.substring(0, 10);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type", "VIDEO");
        wrapper.eq("date_type", 3);
        wrapper.eq("date", substring);
        DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);

        DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
        deviceFellEntity.setType("VIDEO");
        deviceFellEntity.setDateType(3);
        deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
        deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
        deviceFellEntity.setUpValue(JSONObject.toJSONString(upValue));

        deviceFellEntity.setDate(substring);
        if (deviceFellEntity1 != null) {
            String percent1 = deviceFellEntity1.getPercent();
            String upValue1 = deviceFellEntity1.getUpValue();
            String downValue1 = deviceFellEntity1.getDownValue();

            List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
            List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue1, JSONObject.toJSONString(upValue), 1);
            List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);

            deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
            deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
            deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
            deviceFellDao.update(deviceFellEntity, wrapper);
        } else {
            deviceFellDao.insert(deviceFellEntity);
        }
    }


    public void getYearVideoStatistic(QualifiedConfigDTO qualifiedConfigDTO) {
        //获取当前时间的年 开始结束 内的月份 每月的天数在线设备数量/总数 >= 0.9 则计为1天
//        Date date = new Date();
//        DateTime startYear = DateUtil.beginOfYear(date);
//        DateTime endYear = DateUtil.endOfYear(date);
//        //一年的12 个月
//        List<DateTime> dateTimes = DateUtil.rangeToList(startYear, endYear, DateField.MONTH);
//
//        List<Integer> onValue = new ArrayList<>();
//        List<Integer> downValue = new ArrayList<>();
//        List<BigDecimal> percent = new ArrayList<>();
//        //按照配置的比例 决定这天算不算达标
//        BigDecimal minPercent = null;
//        if (qualifiedConfigDTO != null){
//            BigDecimal minValue = qualifiedConfigDTO.getMinValue();
//            minPercent = minValue.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
//
//        }
//        for (DateTime month : dateTimes) {
//            DateTime startMonth = DateUtil.beginOfMonth(month);
//            DateTime endMonth = DateUtil.beginOfMonth(month);
//            List<DateTime> times = DateUtil.rangeToList(startMonth, endMonth, DateField.DAY_OF_MONTH);
//            String format = DateUtil.format(month, "yyyy-MM-dd HH:mm:ss");
//            QueryWrapper wrapper = new QueryWrapper();
//            wrapper.eq("date_type",2);
//            wrapper.eq("date",format.substring(0,7));
//            wrapper.eq("type","VIDEO");
//            DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
//            Integer days = 0;
//            if(deviceFellEntity !=null){
//                String upValueStr = deviceFellEntity.getUpValue();
//                List<Integer> upValue = JSONObject.parseArray(upValueStr,Integer.class);
//                for (Integer integer : upValue) {
//                    BigDecimal value =   new BigDecimal(integer).divide(new BigDecimal(12),2,BigDecimal.ROUND_HALF_UP);
//                    if (minPercent != null){
//                        if (value.compareTo(minPercent) >= 0){
//                            days ++;
//                        }
//                    }
//                    if (value.compareTo(new BigDecimal(0.9)) >= 0){
//                        days ++;
//                    }
//                }
//            }
//            onValue.add(days);
//            downValue.add(times.size()-days);
//            percent.add(new BigDecimal(days).divide(new BigDecimal(times.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
//        }
//        String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
//        String substring = format.substring(0, 4);
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("type","VIDEO");
//        wrapper.eq("date_type",1);
//        wrapper.eq("date",substring);
//        DeviceFellEntity deviceFellEntity1 = deviceFellDao.selectOne(wrapper);
//
//        DeviceFellEntity deviceFellEntity = new DeviceFellEntity();
//        deviceFellEntity.setType("VIDEO");
//        deviceFellEntity.setDateType(1);
//        deviceFellEntity.setPercent(JSONObject.toJSONString(percent));
//        deviceFellEntity.setDownValue(JSONObject.toJSONString(downValue));
//        deviceFellEntity.setUpValue(JSONObject.toJSONString(onValue));
//
//        deviceFellEntity.setDate(substring);
//        if (deviceFellEntity1 != null){
//            String percent1 = deviceFellEntity1.getPercent();
//            String upValue = deviceFellEntity1.getUpValue();
//            String downValue1 = deviceFellEntity1.getDownValue();
//
//            List<BigDecimal> bigDecimal = ConvertUtil.convertBigDecimalListValue(percent1, JSONObject.toJSONString(percent), new BigDecimal(0));
//            List<Integer> integerUpdates = ConvertUtil.convertIntegerListValue(upValue, JSONObject.toJSONString(onValue), 1);
//            List<Integer> integerDowns = ConvertUtil.convertIntegerListValue(downValue1, JSONObject.toJSONString(downValue), 1);
//
//            deviceFellEntity.setPercent(JSONObject.toJSONString(bigDecimal));
//            deviceFellEntity.setDownValue(JSONObject.toJSONString(integerDowns));
//            deviceFellEntity.setUpValue(JSONObject.toJSONString(integerUpdates));
//            deviceFellDao.update(deviceFellEntity,wrapper);
//        }else {
//            deviceFellDao.insert(deviceFellEntity);
//        }
    }


}
