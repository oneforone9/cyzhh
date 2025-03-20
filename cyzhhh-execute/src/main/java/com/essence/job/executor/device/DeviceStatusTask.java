package com.essence.job.executor.device;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.entity.*;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DeviceStatusTask {

    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Resource
    private StWaterRateDao stWaterRateDao;
    @Resource
    private DeviceQualifiedDao deviceQualifiedDao;
    @Resource
    private DeviceStatusDao deviceStatusDao;
    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Resource
    private VideoStatusRecordDao videoStatusRecordDao;
    @Resource
    private StStbprpAlarmDao stStbprpAlarmDao;

    /**
     * 定时更新在线状态
     */
    @XxlJob("DeviceStatusTask")
    public void demoJobHandler() throws Exception {
        System.out.println("设备在线状态更新");
        syncDeviceStatus();
    }

    public void syncDeviceStatus() {//如果 场站警戒状态不为空 则
        //水位流量状态
        deviceZZZQStatus();
        System.out.println("设备在线 DeviceStatusTask 采集完成" + new Date());
        //摄像头状态
        videoStatus();
        System.out.println("摄像头在线 DeviceStatusTask 采集完成" + new Date());
    }

    /**
     * 水位流量状态
     */
    public void deviceZZZQStatus() {
        Map<String, StStbprpAlarmDto> stcdWarningMap = new HashMap<>();
        List<StStbprpAlarmDto> stStbprpAlarmDtos = stStbprpAlarmDao.selectList(new QueryWrapper<>());
        if (CollUtil.isNotEmpty(stStbprpAlarmDtos)) {
            stcdWarningMap = stStbprpAlarmDtos.parallelStream().collect(Collectors.toMap(StStbprpAlarmDto::getStcd, Function.identity(), (o1, o2) -> o2));
        }

        Date toDay = new Date();
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();

        stationWrapper.eq("sttp", "ZZ").or().eq("sttp", "ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        List<StStbprpBEntityDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stStbprpBEntities)) {
            QueryWrapper<StSnConvertEntity> convertWrapper = new QueryWrapper();
            List<StSnConvertEntity> list1 = stSnConvertDao.selectList(convertWrapper);
            Map<String, String> stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StStbprpAlarmDto stStbprpAlarmDto = stcdWarningMap.get(stStbprpBEntity.getStcd());
                StStbprpBEntityDTO stStbprpBEntityDTO1 = new StStbprpBEntityDTO();
                BeanUtils.copyProperties(stStbprpBEntity, stStbprpBEntityDTO1);
                //水位站
                if (stStbprpBEntity.getSttp().equals("ZZ")) {
                    stStbprpBEntityDTO1.setSttp("ZZ");
                    String endZZ = DateUtil.format(toDay, "yyyy/MM/dd HH:mm:ss");
                    String startZZ = DateUtil.format(DateUtil.offsetDay(toDay, -1), "yyyy/MM/dd HH:mm:ss");
                    StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                    stStbprpBEntityDTO.setSttp("ZZ");
                    stStbprpBEntityDTO.setStartTime(startZZ);
                    stStbprpBEntityDTO.setEndTime(endZZ);
                    stStbprpBEntityDTO.setStcd(stStbprpBEntity.getStcd());
                    stStbprpBEntityDTO.setStcdMap(stcdMap);
                    List<StWaterRateEntityDTO> stationDataListZZ = getStationDataList(stStbprpBEntityDTO);
                    if (CollectionUtil.isNotEmpty(stationDataListZZ)) {
                        //设置在线状态
                        stStbprpBEntityDTO1.setOnlineStatus("1");
                        //水位值
                        BigDecimal water = new BigDecimal(0);
                        StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZZ.get(stationDataListZZ.size() - 1);
                        if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())) {
                            water = new BigDecimal(stWaterRateEntityDTO.getAddrv()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
                        }
                        //高程
                        BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();


                        if (stStbprpAlarmDto != null) {
                            BigDecimal alarmLevelWater = stStbprpAlarmDto.getAlarmLevelWater();
                            if (alarmLevelWater != null && water.add(dtmel).compareTo(alarmLevelWater) >= 0) {
                                stStbprpBEntityDTO1.setWarningStatus(2);
                            } else {
                                stStbprpBEntityDTO1.setWarningStatus(1);
                            }
                        } else {
                            stStbprpBEntityDTO1.setWarningStatus(1);
                        }
                        stStbprpBEntityDTO1.setWaterPosition(water);

                    } else {
                        //设置离线状态
                        stStbprpBEntityDTO1.setOnlineStatus("2");
                        //离线需要计算离线时长 用当前之前减去两周时间内最后一条数据 ,如果没有的话
                        String startWeekTimeZZ = DateUtil.format(DateUtil.offsetWeek(toDay, -2), "yyyy/MM/dd HH:mm:ss");
                        stStbprpBEntityDTO.setEndTime(startWeekTimeZZ);
                        List<StWaterRateEntityDTO> weekListZZ = getStationDataList(stStbprpBEntityDTO);
                        if (CollUtil.isNotEmpty(weekListZZ)) {
                            StWaterRateEntityDTO stWaterRateEntityDTO = weekListZZ.get(weekListZZ.size() - 1);
                            String ctime = stWaterRateEntityDTO.getCtime();
                            DateTime dateTime = DateUtil.parseDate(ctime);
                            //获取离线时长
                            String distanceDateTime = TimeUtil.getDistanceDateTime(dateTime, toDay);
                            stStbprpBEntityDTO1.setLineDown(distanceDateTime);
                        } else {
                            String distanceDateTime = TimeUtil.getDistanceDateTime(DateUtil.offsetWeek(toDay, -2), toDay);
                            stStbprpBEntityDTO1.setLineDown(distanceDateTime);
                        }
                    }
                }

                //流量站
                if (stStbprpBEntity.getSttp().equals("ZQ")) {
                    stStbprpBEntityDTO1.setSttp("ZQ");
                    String endZQ = DateUtil.format(toDay, "yyyy-MM-dd HH:mm:ss");
                    String startZQ = DateUtil.format(DateUtil.offsetDay(toDay, -1), "yyyy-MM-dd HH:mm:ss");
                    StStbprpBEntityDTO stStbprpBEntityZQDTO = new StStbprpBEntityDTO();
                    stStbprpBEntityZQDTO.setSttp("ZQ");
                    stStbprpBEntityZQDTO.setStartTime(startZQ);
                    stStbprpBEntityZQDTO.setEndTime(endZQ);
                    stStbprpBEntityZQDTO.setStcd(stStbprpBEntity.getStcd());
                    List<StWaterRateEntityDTO> stationDataListZQ = getStationDataList(stStbprpBEntityZQDTO);
                    if (CollectionUtil.isNotEmpty(stationDataListZQ)) {
                        stStbprpBEntityDTO1.setOnlineStatus("1");
                        //水位值
                        BigDecimal water = new BigDecimal(0);
                        StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZQ.get(stationDataListZQ.size() - 1);
                        if (!StrUtil.isEmpty(stWaterRateEntityDTO.getMomentRiverPosition())) {
                            water = new BigDecimal(stWaterRateEntityDTO.getMomentRiverPosition());
                        }
                        BigDecimal rate = new BigDecimal(0);
                        if (!StrUtil.isEmpty(stWaterRateEntityDTO.getMomentRate())) {
                            //流量
                            rate = new BigDecimal(stWaterRateEntityDTO.getMomentRate());
                            stStbprpBEntityDTO1.setWaterRate(rate);
                        }
                        if (stStbprpAlarmDto != null) {
                            BigDecimal alarmFlow = stStbprpAlarmDto.getAlarmFlow();
                            if (!StrUtil.isEmpty(stWaterRateEntityDTO.getMomentRate())) {
                                if (alarmFlow != null && rate.compareTo(alarmFlow) <= 0) {
                                    stStbprpBEntityDTO1.setWarningStatus(3);
                                } else {
                                    stStbprpBEntityDTO1.setWarningStatus(1);
                                }
                            } else {
                                stStbprpBEntityDTO1.setWarningStatus(1);
                            }
                        } else {
                            stStbprpBEntityDTO1.setWarningStatus(1);
                        }
                        stStbprpBEntityDTO1.setWaterPosition(water);
                    } else {
                        stStbprpBEntityDTO1.setOnlineStatus("2");
                        //离线需要计算离线时长 用当前之前减去两周时间内最后一条数据 ,如果没有的话
                        String startWeekTimeZZ = DateUtil.format(DateUtil.offsetWeek(toDay, -2), "yyyy-MM-dd HH:mm:ss");
                        stStbprpBEntityZQDTO.setEndTime(startWeekTimeZZ);
                        List<StWaterRateEntityDTO> weekListZZ = getStationDataList(stStbprpBEntityZQDTO);
                        if (CollUtil.isNotEmpty(weekListZZ)) {
                            StWaterRateEntityDTO stWaterRateEntityDTO = weekListZZ.get(weekListZZ.size() - 1);
                            String ctime = stWaterRateEntityDTO.getCtime();
                            DateTime dateTime = DateUtil.parseDate(ctime);
                            //获取离线时长
                            String distanceDateTime = TimeUtil.getDistanceDateTime(dateTime, toDay);
                            stStbprpBEntityDTO1.setLineDown(distanceDateTime);
                        } else {
                            String distanceDateTime = TimeUtil.getDistanceDateTime(DateUtil.offsetWeek(toDay, -2), toDay);
                            stStbprpBEntityDTO1.setLineDown(distanceDateTime);
                        }
                    }
                }
                list.add(stStbprpBEntityDTO1);
            }
        }
        String yyyyMMdd = DateUtil.format(new Date(), "yyyyMMdd");
        for (StStbprpBEntityDTO stStbprpBEntityDTO : list) {
            DeviceStatusEntity deviceStatusEntity = new DeviceStatusEntity();
            BeanUtil.copyProperties(stStbprpBEntityDTO, deviceStatusEntity);
            deviceStatusEntity.setTime(yyyyMMdd);
            String stcd = deviceStatusEntity.getStcd();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("stcd", stcd);
            DeviceStatusEntity deviceStatusEntity1 = deviceStatusDao.selectOne(queryWrapper);
            if (deviceStatusEntity1 != null) {
                deviceStatusDao.update(deviceStatusEntity, queryWrapper);
            } else {
                deviceStatusDao.insert(deviceStatusEntity);
            }
        }
    }


    /**
     * 摄像头状态
     */
    public void videoStatus() {
        Date toDay = new Date();
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        List<StStbprpBEntityDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(videoInfoBases)) {
            List<VideoStatusRecord> stationDataList = Optional.ofNullable(videoStatusRecordDao.selectList(new QueryWrapper<>())).orElse(Lists.newArrayList());
            for (VideoInfoBase stStbprpBEntity : videoInfoBases) {
                StStbprpBEntityDTO stStbprpBEntityDTO1 = new StStbprpBEntityDTO();
                BeanUtils.copyProperties(stStbprpBEntity, stStbprpBEntityDTO1);
                stStbprpBEntityDTO1.setStcd(stStbprpBEntity.getId().toString());
                if (CollectionUtil.isNotEmpty(stationDataList)) {
                    List<VideoStatusRecord> videoStatusRecordList = stationDataList.stream().filter(x -> x.getVideoId() == stStbprpBEntity.getId()).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(videoStatusRecordList)) {
                        stStbprpBEntityDTO1.setOnlineStatus("1");
                    } else {
                        stStbprpBEntityDTO1.setOnlineStatus("2");
                    }
                }else {
                    stStbprpBEntityDTO1.setOnlineStatus("2");
                }
                list.add(stStbprpBEntityDTO1);
            }
        }

        for (StStbprpBEntityDTO stStbprpBEntityDTO : list) {
            DeviceStatusEntity deviceStatusEntity = new DeviceStatusEntity();
            BeanUtil.copyProperties(stStbprpBEntityDTO, deviceStatusEntity);
            String stcd = deviceStatusEntity.getStcd();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("stcd", stcd);
            DeviceStatusEntity deviceStatusEntity1 = deviceStatusDao.selectOne(queryWrapper);
            if (deviceStatusEntity1 != null) {
                deviceStatusDao.update(deviceStatusEntity, queryWrapper);
            } else {
                deviceStatusDao.insert(deviceStatusEntity);
            }
        }
    }

    public List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")) {
            Map<String, String> stcdMap = stStbprpBEntityDTO.getStcdMap();
            if (stcdMap != null) {
                QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("did", stcdMap.get(stStbprpBEntityDTO.getStcd()));
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime())) {
                    wrapper.le("ctime", stStbprpBEntityDTO.getEndTime());
                }
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime())) {
                    wrapper.ge("ctime", stStbprpBEntityDTO.getStartTime());
                }
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                    for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                        StWaterRateEntityDTO stStbprpBEntityDTO1 = new StWaterRateEntityDTO();
                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                            String ctime = stWaterRateEntity.getCtime();
                            DateTime dateTime = DateUtil.parseDate(ctime);
                            String format = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(stWaterRateEntity.getCtime().replace("/", "-"));
                            stStbprpBEntityDTO1.setMDh(format.substring(5, 16));
                        }
                        list.add(stStbprpBEntityDTO1);
                    }
                }
            }

        } else {
            QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
            if (stStbprpBEntityDTO.getStcd().length() <= 3) {
                return null;
            }
            wrapper.eq("did", stStbprpBEntityDTO.getStcd().substring(2));
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime())) {
                wrapper.le("ctime", stStbprpBEntityDTO.getEndTime());
            }
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime())) {
                wrapper.ge("ctime", stStbprpBEntityDTO.getStartTime());
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5, 16));
                    }
                    list.add(stWaterRateEntityDTO);
                }
            }

        }
        if (CollUtil.isNotEmpty(list)) {
            list = list.stream().sorted(Comparator.comparing(StWaterRateEntityDTO::getCtime)).collect(Collectors.toList());
        }
        return list;
    }
}
