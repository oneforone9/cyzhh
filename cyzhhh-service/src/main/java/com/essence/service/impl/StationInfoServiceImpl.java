package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.common.dto.*;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StationService;
import com.essence.interfaces.dot.ReaBaseDto;
import com.essence.interfaces.model.OnlineDeviceDetailDVo;
import com.essence.interfaces.vo.WaterFlowIncreaseVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cuirx
 * @Classname StationInfoServiceImpl
 * @Description TODO
 * @Date 2022/10/14 16:14
 * @Created by essence
 */

@Service
public class StationInfoServiceImpl implements StationService {
    @Resource
    private DeviceDataStatisticDao deviceDataStatisticDao;
    @Resource
    private WaterPositionStatisticDao waterPositionStatisticDao;

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
    private OnlineCheckDeviceDao onlineCheckDeviceDao;
    @Resource
    private DeviceFellDao deviceFellDao;
    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private StStbprpAlarmDao stStbprpAlarmDao;
    @Resource
    private OrganismRiverRecordDao organismRiverRecordDao;
    @Resource
    private StRainDateDao stRainDateDao;
    /**
     * 边闸
     */
    @Resource
    private StSideGateDao stSideGateDao;

    @Override
    public ResponseResult getStationInfoList(StStbprpBEntityDTO dto) throws ParseException {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        List<ReaBase> baseList = reaBaseDao.selectList(new LambdaQueryWrapper<ReaBase>()
                .le(ReaBase::getId, 31)
                .eq(StringUtils.isNotBlank(dto.getUnitId()), ReaBase::getUnitId, dto.getUnitId())
        );
        Map<String, ReaBase> riverMap = baseList.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));

        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(dto.getSttp())) {
            wrapper.eq("sttp", dto.getSttp());
        }
        if (StrUtil.isNotEmpty(dto.getUnitId())) {
            List<String> collect = baseList.parallelStream().map(ReaBase::getId).collect(Collectors.toList());
            wrapper.in("rvnm", collect);
        }
        if (StringUtil.isNotEmpty(dto.getStnm())) {
            wrapper.like("stnm", dto.getStnm());
        }
        if (StringUtil.isNotEmpty(dto.getArea())) {
            wrapper.like("area", dto.getArea());
        }
        if (StringUtil.isNotEmpty(dto.getRvnm())) {
            wrapper.eq("rvnm", dto.getRvnm());
        }
        if (StringUtil.isNotEmpty(dto.getStlc())) {
            wrapper.like("stlc", dto.getStlc());
        }
        if (StringUtil.isNotEmpty(dto.getSttp())) {
            wrapper.eq("sttp", dto.getSttp());
        }
        if (CollUtil.isNotEmpty(dto.getSttps())) {
            wrapper.in("sttp", dto.getSttps());
        }
        if (StringUtil.isNotBlank(dto.getFlowTypeG()) && dto.getFlowTypeG().equals("1")) {
            wrapper.ne("flow_type", "运营需求监测点");
        } else {
            if (StrUtil.isNotEmpty(dto.getFlowType())) {
                wrapper.in("flow_type", dto.getFlowType());
            }
        }
        if (StrUtil.isNotEmpty(dto.getStcd())) {
            wrapper.in("stcd", dto.getStcd());
        }
        //测站基本信息
        List<StStbprpBEntity> stationList = stStbprpBDao.selectList(wrapper);

        //预警阀值
        Map<String, StStbprpAlarmDto> alarmMap = stStbprpAlarmDao.selectList(null)
                .parallelStream().collect(Collectors.toMap(StStbprpAlarmDto::getStcd, Function.identity()));

        List<StStbprpBEntityDTO> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stationList)) {
            //设备状态
            Map<String, DeviceStatusEntity> deviceStatusMap = deviceStatusDao.selectList(null)
                    .parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
            for (StStbprpBEntity station : stationList) {
                StStbprpBEntityDTO vo = new StStbprpBEntityDTO();
                BeanUtil.copyProperties(station, vo);
                //河流名称
                if (StrUtil.isNotEmpty(station.getRvnm())) {
                    ReaBase reaBase = riverMap.get(vo.getRvnm());
                    vo.setRiverId(Integer.valueOf(reaBase.getId()));
                    vo.setRvnm(reaBase.getReaName());
                    vo.setUnitId(reaBase.getUnitId());
                    vo.setUnitName(reaBase.getUnitName());
                }
                DeviceStatusEntity deviceStatusEntity = deviceStatusMap.get(station.getStcd());
                if (deviceStatusEntity != null) {
                    BeanUtil.copyProperties(deviceStatusEntity, vo);
                } else {
                    vo.setOnlineStatus("2");
                }
                BigDecimal dtmel = vo.getDtmel() == null ? new BigDecimal(0) : vo.getDtmel();
                BigDecimal waterPosition = vo.getWaterPosition() == null ? new BigDecimal(0) : vo.getWaterPosition();
                BigDecimal add = dtmel.add(waterPosition);
                //实时水位
                vo.setPosition(add);
                if (dto.getRequireProvide() && station.getSttp().equals("ZQ")) {
                    setDatProvideWater(vo);
                }
                //报警后阀值
                StStbprpAlarmDto alarmDto = alarmMap.get(vo.getStcd());
                if (alarmDto != null) {
                    vo.setAlarmLevelWater(alarmDto.getAlarmLevelWater());
                    vo.setAlarmFlow(alarmDto.getAlarmFlow());
                    //水位状态: 1 正常  2 超警戒 3 超保证
                    Integer waterStatus = 1;
                    if (vo.getPosition() != null && vo.getBhtz() != null) {
                        if (vo.getPosition().compareTo(vo.getBhtz()) > 0) {
                            waterStatus = 3;
                        } else if (vo.getPosition().compareTo(vo.getWrz()) > 0) {
                            waterStatus = 2;
                        }
                    }
                    vo.setWaterStatus(waterStatus);
                    //流量状态: 1 正常 2 五年一遇 3 二十年一遇 4 五十年一遇 5 一百年一遇
                    Integer flowStatus = 1;
                    if (vo.getWaterRate() != null) {
                        BigDecimal once5Flow = alarmDto.getOnce5Flow();
                        BigDecimal once20Flow = alarmDto.getOnce20Flow();
                        BigDecimal once50Flow = alarmDto.getOnce50Flow();
                        BigDecimal once100Flow = alarmDto.getOnce100Flow();
                        if (once5Flow != null && vo.getWaterRate().compareTo(once5Flow) < 0) {
                            flowStatus = 1;
                        }
                        if (once5Flow != null
                                && once20Flow != null
                                && vo.getWaterRate().compareTo(once5Flow) >= 0
                                && vo.getWaterRate().compareTo(once20Flow) < 0) {
                            flowStatus = 2;
                        }
                        if (once20Flow != null
                                && once5Flow != null
                                && vo.getWaterRate().compareTo(once20Flow) >= 0
                                && vo.getWaterRate().compareTo(once50Flow) < 0) {
                            flowStatus = 3;
                        }
                        if (once50Flow != null
                                && once100Flow != null
                                && vo.getWaterRate().compareTo(once50Flow) >= 0
                                && vo.getWaterRate().compareTo(once100Flow) < 0) {
                            flowStatus = 4;
                        }
                        if (once100Flow != null && vo.getWaterRate().compareTo(once100Flow) >= 0) {
                            flowStatus = 5;
                        }
                    }
                    vo.setFlowStatus(flowStatus);
                }
                voList.add(vo);
            }
        }
        //如果 场站警戒状态不为空 则
        if (dto.getWarningStatus() != null) {
            voList = voList.parallelStream().filter(stStbprpBEntityDTO -> {
                return stStbprpBEntityDTO.getWarningStatus() != null && stStbprpBEntityDTO.getWarningStatus().equals(dto.getWarningStatus());
            }).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(voList)) {
            voList = voList.stream().map(stStbprpBEntityDTO -> {
                if (stStbprpBEntityDTO.getWarningStatus() == null) {
                    stStbprpBEntityDTO.setWarningStatus(1);
                }
                return stStbprpBEntityDTO;
            }).sorted(Comparator.comparing(StStbprpBEntityDTO::getWarningStatus).reversed()).collect(Collectors.toList());
        }

        PageUtil pages = new PageUtil(voList, dto.getCurrent(), dto.getSize(), null, null);
        return ResponseResult.success("获取成功", pages);
    }

    @Override
    public List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO dto) throws ParseException {
        List<StWaterRateEntityDTO> voList = new ArrayList<>();
        Map<String, StStbprpAlarmDto> alarmMap = stStbprpAlarmDao.selectList(new QueryWrapper<>()).
                parallelStream().collect(Collectors.toMap(StStbprpAlarmDto::getStcd, Function.identity()));
        Map<String, StStbprpBEntity> stationMap = stStbprpBDao.selectList(new QueryWrapper<>())
                .parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity()));
        StStbprpBEntity station = stationMap.get(dto.getStcd());
        //水位
        if (dto.getSttp().equals("ZZ")) {
            Map<String, String> stcdMap = dto.getStcdMap();
            if (CollUtil.isEmpty(stcdMap)) {
                stcdMap = stSnConvertDao.selectList(new QueryWrapper<>())
                        .parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            }
            QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("did", stcdMap.get(dto.getStcd()));
            if (StrUtil.isNotEmpty(dto.getEndTime())) {
                String replace = dto.getEndTime().replace("-", "/");
                wrapper.le("ctime", replace);
            }
            if (StrUtil.isNotEmpty(dto.getStartTime())) {
                String replace = dto.getStartTime().replace("-", "/");
                wrapper.ge("ctime", replace);
            }
            //采集数据
            List<StWaterRateEntity> waterRateList = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(waterRateList)) {
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = waterRateList.parallelStream()
                        .collect(Collectors.groupingBy(StWaterRateEntity::getCtime));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO vo = new StWaterRateEntityDTO();
                    //水位 从毫米 转换为m
                    String addrv = stWaterRateEntity.getAddrv();
                    if (StrUtil.isEmpty(addrv)) {
                        addrv = "0";
                    }
                    BigDecimal meter = new BigDecimal(addrv).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
                    BeanUtils.copyProperties(stWaterRateEntity, vo);
                    vo.setWaterDeep(meter);
                    String ctime = stWaterRateEntity.getCtime();
                    if (StrUtil.isNotEmpty(ctime)) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date parse = formatter.parse(ctime);
                        String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                        vo.setCtime(format);
                        vo.setMDh(format.substring(5, 16));
                        //高程
                        BigDecimal dtmel = station.getDtmel() == null ? new BigDecimal(0) : station.getDtmel();
                        vo.setAddrv(meter.add(dtmel).toString());
                        vo.setDtmel(dtmel.toString());
                        StStbprpAlarmDto stStbprpAlarmDto = alarmMap.get(dto.getStcd());
                        //水位站是否 报警超过伐值
                        if (stStbprpAlarmDto != null && vo.getAddr() != null && stStbprpAlarmDto.getAlarmLevelWater() != null) {
                            if (new BigDecimal(vo.getAddrv()).compareTo(stStbprpAlarmDto.getAlarmLevelWater()) > 0) {
                                vo.setFlowStatus(2);
                            } else {
                                vo.setFlowStatus(1);
                            }
                        } else {
                            vo.setFlowStatus(1);
                        }
                    }
                    vo.setStationName(station.getStnm());
                    voList.add(vo);
                }
            }
        }
        //流量
        else {
            QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
            if (dto.getStcd().length() <= 3) {
                return null;
            }
            wrapper.eq("did", dto.getStcd().substring(2));
            if (StrUtil.isNotEmpty(dto.getEndTime())) {
                String replace = dto.getEndTime();
                wrapper.le("ctime", replace);
            }
            if (StrUtil.isNotEmpty(dto.getStartTime())) {
                String replace = dto.getStartTime();
                wrapper.ge("ctime", replace);
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream()
                        .collect(Collectors.groupingBy(StWaterRateEntity::getCtime));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO vo = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, vo);
                    String ctime = stWaterRateEntity.getCtime();
                    if (StrUtil.isNotEmpty(ctime)) {
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date parse = formatter.parse(ctime);
//                        String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                        vo.setCtime(ctime);
                        vo.setMDh(stWaterRateEntity.getCtime().substring(5, 16));
                        vo.setAddrv(stWaterRateEntity.getMomentRate() == null ? "0" : stWaterRateEntity.getMomentRate());
                        StStbprpAlarmDto stStbprpAlarmDto = alarmMap.get(dto.getStcd());
                        if (stStbprpAlarmDto != null
                                && stStbprpAlarmDto.getAlarmFlow() != null
                                && stWaterRateEntity.getMomentRate() != null
                                && new BigDecimal(stWaterRateEntity.getMomentRate()).compareTo(stStbprpAlarmDto.getAlarmFlow()) < 0) {
                            vo.setFlowStatus(2);
                        } else {
                            vo.setFlowStatus(1);
                        }
                        BigDecimal dtmel = station.getDtmel() == null ? new BigDecimal(0) : station.getDtmel();
                        vo.setWaterDeep(new BigDecimal(vo.getAddrv()).add(dtmel));
                        //河底高程
                        vo.setDtmel(dtmel.toString());
                    }
                    vo.setStationName(station.getStnm());
                    voList.add(vo);
                }
            }
        }
        if (CollUtil.isNotEmpty(voList)) {
            voList = voList.stream().sorted(Comparator.comparing(StWaterRateEntityDTO::getCtime)).collect(Collectors.toList());
        }
        return voList;
    }

    @Override
    public List<StWaterRateExcelDTO> getStationExcelDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateExcelDTO> list = new ArrayList<>();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, StStbprpBEntity> stationMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity()));
        StStbprpBEntity stStbprpBEntity = stationMap.get(stStbprpBEntityDTO.getStcd());
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")) {
            Map<String, String> stcdMap = stStbprpBEntityDTO.getStcdMap();
            if (CollUtil.isEmpty(stcdMap)) {
                List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
                stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            }
            if (stcdMap != null) {
                QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("did", stcdMap.get(stStbprpBEntityDTO.getStcd()));
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime())) {
                    String replace = stStbprpBEntityDTO.getEndTime().replace("-", "/");
                    wrapper.le("ctime", replace);
                }
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime())) {

                    String replace = stStbprpBEntityDTO.getStartTime().replace("-", "/");
                    wrapper.ge("ctime", replace);
                }
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                    // 以小时为分组 取最后一个小时的数据
                    Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                        return stWaterRateEntity.getCtime();
                    }));
                    for (String ct : ctTimeMapList.keySet()) {
                        StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                        StWaterRateExcelDTO stStbprpBEntityDTO1 = new StWaterRateExcelDTO();
                        //水位 从毫米 转换为m
                        String addrv = stWaterRateEntity.getAddrv();
                        if (StrUtil.isEmpty(addrv)) {
                            addrv = "0";
                        }
                        BigDecimal meter = new BigDecimal(addrv).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
//                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        stStbprpBEntityDTO1.setWaterDeep(meter.toString());
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                            String ctime = stWaterRateEntity.getCtime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date parse = formatter.parse(ctime);
                            stStbprpBEntityDTO1.setDate(parse);

                            //高程
                            BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                            stStbprpBEntityDTO1.setDtml(dtmel.toString());
                            stStbprpBEntityDTO1.setWaterPosition(meter.add(dtmel).toString());
                            String stnm = stStbprpBEntity.getStnm();
                            stStbprpBEntityDTO1.setStationName(stnm);
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
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                    return stWaterRateEntity.getCtime();
                }));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateExcelDTO stWaterRateEntityDTO = new StWaterRateExcelDTO();
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                        stWaterRateEntityDTO.setWaterRate(stWaterRateEntity.getPreMomentRate() == null ? "0" : stWaterRateEntity.getPreMomentRate());
                        //高程
                        BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                        stWaterRateEntityDTO.setDtml(dtmel.toString());
                        stWaterRateEntityDTO.setWaterDeep(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateEntityDTO.setWaterPosition(new BigDecimal(StrUtil.isEmpty(stWaterRateEntity.getMomentRiverPosition()) ? "0" : stWaterRateEntity.getMomentRiverPosition()).add(dtmel).toString());
                        stWaterRateEntityDTO.setWaterRate(stWaterRateEntity.getMomentRate() == null ? "0" : stWaterRateEntity.getMomentRate());
                        stWaterRateEntityDTO.setDate(DateUtil.parseDate(stWaterRateEntity.getCtime()));
                        String stnm = stStbprpBEntity.getStnm();
                        stWaterRateEntityDTO.setStationName(stnm);
                    }
                    list.add(stWaterRateEntityDTO);
                }
            }
        }
        if (CollUtil.isNotEmpty(list)) {
            list = list.stream().sorted(Comparator.comparing(StWaterRateExcelDTO::getDate)).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public WaterFlowIncreaseVo getWaterFlowIncrease() {
        List<StStbprpBEntity> stationList = stStbprpBDao.selectList(null);
        //流量站
        List<StStbprpBEntity> flowList = stationList.stream().filter(v -> v.getSttp().equalsIgnoreCase("ZQ")).collect(Collectors.toList());
        //水位站(需要sn转换)
        List<StStbprpBEntity> waterList = stationList.stream().filter(v -> v.getSttp().equalsIgnoreCase("ZZ")).collect(Collectors.toList());
        Map<String, String> map = stSnConvertDao.selectList(null)
                .stream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0);
        //水位格式
        DateTimeFormatter waterFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        //流量格式
        DateTimeFormatter flowFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //采集数据
        List<StWaterRateEntity> nowList;
        List<StWaterRateEntity> beforeOneHourList;
        List<StWaterRateEntity> beforeOneDayList;
        nowList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                .eq(StWaterRateEntity::getCtime, waterFormat.format(now))
                .or()
                .eq(StWaterRateEntity::getCtime, flowFormat.format(now))
        );
        beforeOneHourList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                .eq(StWaterRateEntity::getCtime, waterFormat.format(now.minusHours(1)))
                .or()
                .eq(StWaterRateEntity::getCtime, waterFormat.format(now.minusHours(1)))
        );
        beforeOneDayList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                .eq(StWaterRateEntity::getCtime, waterFormat.format(now.minusDays(1)))
                .or()
                .eq(StWaterRateEntity::getCtime, flowFormat.format(now.minusDays(1)))
        );
        if (CollectionUtils.isEmpty(nowList)) {
            nowList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                    .or(
                            i -> i.and(j -> j.ge(StWaterRateEntity::getCtime, waterFormat.format(now.withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, waterFormat.format(now.withSecond(20)))
                                    )
                                    .or(j -> j.ge(StWaterRateEntity::getCtime, flowFormat.format(now.withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, flowFormat.format(now.withSecond(20)))
                                    )
                    )
                    .orderByDesc(StWaterRateEntity::getCtime)
            );
            beforeOneHourList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                    .or(
                            i -> i.and(j -> j.ge(StWaterRateEntity::getCtime, waterFormat.format(now.minusHours(1).withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, waterFormat.format(now.minusHours(1).withSecond(20)))
                                    )
                                    .or(j -> j.ge(StWaterRateEntity::getCtime, flowFormat.format(now.minusHours(1).withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, flowFormat.format(now.minusHours(1).withSecond(20)))
                                    )
                    )
                    .orderByDesc(StWaterRateEntity::getCtime)
            );
            beforeOneDayList = stWaterRateDao.selectList(new LambdaQueryWrapper<StWaterRateEntity>()
                    .or(
                            i -> i.and(j -> j.ge(StWaterRateEntity::getCtime, waterFormat.format(now.minusDays(1).withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, waterFormat.format(now.minusDays(1).withSecond(20)))
                                    )
                                    .or(j -> j.ge(StWaterRateEntity::getCtime, flowFormat.format(now.minusDays(1).withSecond(1)))
                                            .le(StWaterRateEntity::getCtime, flowFormat.format(now.minusDays(1).withSecond(20)))
                                    )
                    )
                    .orderByDesc(StWaterRateEntity::getCtime)
            );
        }
        List<StWaterRateEntity> finalBeforeOneHourList = beforeOneHourList;
        List<StWaterRateEntity> finalBeforeOneDayList = beforeOneDayList;
        HashMap<BigDecimal, String> flowOneHourMap = new HashMap<>();
        HashMap<BigDecimal, String> flowOneDayMap = new HashMap<>();
        HashMap<BigDecimal, String> flowWaterOneHourMap = new HashMap<>();
        HashMap<BigDecimal, String> flowWaterOneDayMap = new HashMap<>();
        HashMap<BigDecimal, String> waterWaterOneHourMap = new HashMap<>();
        HashMap<BigDecimal, String> waterWaterOneDayMap = new HashMap<>();
        nowList.forEach(v -> {
            String did = v.getDid();
            //流量
            if (did.length() == 8) {
                StWaterRateEntity oneHour = finalBeforeOneHourList.stream()
                        .filter(one -> did.equals(one.getDid()))
                        .findFirst().orElse(new StWaterRateEntity());
                StWaterRateEntity oneDay = finalBeforeOneDayList.stream()
                        .filter(one -> did.equals(one.getDid()) && StringUtils.isNotBlank(one.getAddrv()))
                        .findFirst().orElse(new StWaterRateEntity());
                //瞬时流量
                if (StringUtils.isNotBlank(v.getMomentRate())) {
                    if (StringUtils.isNotBlank(oneHour.getMomentRate())) {
                        if (new BigDecimal(v.getMomentRate()).compareTo(new BigDecimal(oneHour.getMomentRate())) > 0) {
                            BigDecimal subtract = new BigDecimal(v.getMomentRate()).subtract(new BigDecimal(oneHour.getMomentRate()));
                            flowOneHourMap.put(subtract, did);
                        }
                    }
                    if (StringUtils.isNotBlank(oneDay.getMomentRate())) {
                        if (new BigDecimal(v.getMomentRate()).compareTo(new BigDecimal(oneDay.getMomentRate())) > 0) {
                            BigDecimal subtract = new BigDecimal(v.getMomentRate()).subtract(new BigDecimal(oneDay.getMomentRate()));
                            flowOneDayMap.put(subtract, did);
                        }
                    }
                }
                //瞬时河道水深
                if (StringUtils.isNotBlank(v.getMomentRiverPosition())) {
                    if (StringUtils.isNotBlank(oneHour.getMomentRiverPosition())) {
                        if (new BigDecimal(v.getMomentRiverPosition()).compareTo(new BigDecimal(oneHour.getMomentRiverPosition())) > 0) {
                            BigDecimal subtract = new BigDecimal(v.getMomentRiverPosition()).subtract(new BigDecimal(oneHour.getMomentRiverPosition()));
                            flowWaterOneHourMap.put(subtract, did);
                        }
                    }
                    if (StringUtils.isNotBlank(oneDay.getMomentRiverPosition())) {
                        if (new BigDecimal(v.getMomentRiverPosition()).compareTo(new BigDecimal(oneDay.getMomentRiverPosition())) > 0) {
                            BigDecimal subtract = new BigDecimal(v.getMomentRiverPosition()).subtract(new BigDecimal(oneDay.getMomentRiverPosition()));
                            flowWaterOneDayMap.put(subtract, did);
                        }
                    }
                }
            }
            //水位
            else {
                if (StringUtils.isNotBlank(v.getAddrv())) {
                    StWaterRateEntity oneHour = finalBeforeOneHourList.stream()
                            .filter(one -> did.equals(one.getDid()) && StringUtils.isNotBlank(one.getAddrv()))
                            .findFirst().orElse(new StWaterRateEntity());
                    StWaterRateEntity oneDay = finalBeforeOneDayList.stream()
                            .filter(one -> did.equals(one.getDid()) && StringUtils.isNotBlank(one.getAddrv()))
                            .findFirst().orElse(new StWaterRateEntity());

                    if (new BigDecimal(v.getAddrv()).compareTo(new BigDecimal(oneHour.getAddrv())) > 0) {
                        BigDecimal subtract = new BigDecimal(v.getAddrv()).subtract(new BigDecimal(oneHour.getAddrv()));
                        waterWaterOneHourMap.put(subtract.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP), did);
                    }

                    if (new BigDecimal(v.getAddrv()).compareTo(new BigDecimal(oneDay.getAddrv())) > 0) {
                        BigDecimal subtract = new BigDecimal(v.getAddrv()).subtract(new BigDecimal(oneDay.getAddrv()));
                        waterWaterOneDayMap.put(subtract.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP), did);
                    }
                }
            }
        });

        BigDecimal flowOneHourMax = flowOneHourMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        BigDecimal flowOneDayMax = flowOneDayMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        BigDecimal flowWaterOneHourMax = flowWaterOneHourMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        BigDecimal flowWaterOneDayMax = flowWaterOneDayMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        BigDecimal waterWaterOneHourMax = waterWaterOneHourMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        BigDecimal waterWaterOneDayMax = waterWaterOneDayMap.keySet().stream().max(BigDecimal::compareTo).orElse(new BigDecimal("0"));

        BigDecimal waterOneHourMax = flowWaterOneHourMax.compareTo(waterWaterOneHourMax) > 0 ? flowWaterOneHourMax : waterWaterOneHourMax;
        BigDecimal waterOneDayMax = flowWaterOneDayMax.compareTo(waterWaterOneDayMax) > 0 ? flowWaterOneDayMax : waterWaterOneDayMax;

        String flowOneHourDid = flowOneHourMap.get(flowOneHourMax);
        String flowOneDayDid = flowOneDayMap.get(flowOneDayMax);
        String flowWaterOneHourDid = flowWaterOneHourMap.get(waterOneHourMax);
        String waterWaterOneHourDid = waterWaterOneHourMap.get(waterOneHourMax);
        String flowWaterOneDayDid = flowWaterOneDayMap.get(waterOneDayMax);
        String waterWaterOneDayDid = waterWaterOneDayMap.get(waterOneDayMax);

        AtomicReference<BigDecimal> waterMaxOneHour = new AtomicReference<>();
        AtomicReference<BigDecimal> waterMaxOneDay = new AtomicReference<>();
        AtomicReference<BigDecimal> flowMaxOneHour = new AtomicReference<>();
        AtomicReference<BigDecimal> flowMaxOneDay = new AtomicReference<>();

        String waterStationNameOneHour = null;
        String waterStationNameOneDay = null;
        AtomicReference<String> flowStationNameOneHour = new AtomicReference<>();
        AtomicReference<String> flowStationNameOneDay = new AtomicReference<>();
        //流量站取流量
        if (StringUtils.isNotEmpty(flowOneHourDid)) {
            flowList.stream()
                    .filter(v -> flowOneHourDid.equals(v.getStcd().substring(2)))
                    .findFirst()
                    .ifPresent(v -> flowStationNameOneHour.set(v.getStnm()));
            nowList.stream()
                    .filter(v -> v.getDid().equals(flowOneHourDid))
                    .findFirst()
                    .ifPresent(v -> flowMaxOneHour.set(new BigDecimal(v.getMomentRate())));
        }
        //流量站取流量
        if (StringUtils.isNotEmpty(flowOneDayDid)) {
            flowList.stream()
                    .filter(v -> flowOneDayDid.equals(v.getStcd().substring(2)))
                    .findFirst()
                    .ifPresent(v -> flowStationNameOneDay.set(v.getStnm()));
            nowList.stream()
                    .filter(v -> v.getDid().equals(flowOneDayDid))
                    .findFirst()
                    .ifPresent(v -> flowMaxOneDay.set(new BigDecimal(v.getMomentRate())));
        }
        //流量站取水位
        if (StringUtils.isNotEmpty(flowWaterOneHourDid)) {
            BigDecimal dtmel = new BigDecimal(0);
            Optional<StStbprpBEntity> first = flowList.stream()
                    .filter(v -> flowWaterOneHourDid.equals(v.getStcd().substring(2)))
                    .findFirst();
            if (first.isPresent()) {
                waterStationNameOneHour = first.get().getStnm();
                dtmel = first.get().getDtmel();
            }
            BigDecimal finalDtmel = dtmel;
            nowList.stream()
                    .filter(v -> v.getDid().equals(flowWaterOneHourDid))
                    .findFirst()
                    .ifPresent(v -> waterMaxOneHour.set(new BigDecimal(v.getMomentRiverPosition()).add(finalDtmel)));

        }
        //水位站站取水位
        if (StringUtils.isNotEmpty(waterWaterOneHourDid)) {
            BigDecimal dtmel = new BigDecimal(0);
            Optional<StStbprpBEntity> first = flowList.stream()
                    .filter(v -> waterWaterOneHourDid.equals(map.get(v.getStcd())))
                    .findFirst();
            if (first.isPresent()) {
                waterStationNameOneHour = first.get().getStnm();
                dtmel = first.get().getDtmel();
            }
            BigDecimal finalDtmel = dtmel;
            nowList.stream()
                    .filter(v -> v.getDid().equals(waterWaterOneHourDid))
                    .findFirst()
                    .ifPresent(v -> waterMaxOneHour.set(new BigDecimal(v.getAddrv())
                            .divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP)
                            .add(finalDtmel)
                    ));
        }
        //流量站取水位
        if (StringUtils.isNotEmpty(flowWaterOneDayDid)) {
            BigDecimal dtmel = new BigDecimal(0);
            Optional<StStbprpBEntity> first = flowList.stream()
                    .filter(v -> flowWaterOneDayDid.equals(v.getStcd().substring(2)))
                    .findFirst();
            if (first.isPresent()) {
                waterStationNameOneDay = first.get().getStnm();
                dtmel = first.get().getDtmel();
            }
            BigDecimal finalDtmel = dtmel;
            nowList.stream()
                    .filter(v -> v.getDid().equals(flowWaterOneDayDid))
                    .findFirst()
                    .ifPresent(v -> waterMaxOneDay.set(new BigDecimal(v.getMomentRiverPosition()).add(finalDtmel)));

        }
        //水位站站取水位
        if (StringUtils.isNotEmpty(waterWaterOneDayDid)) {
            BigDecimal dtmel = new BigDecimal(0);
            Optional<StStbprpBEntity> first = waterList.stream()
                    .filter(v -> waterWaterOneDayDid.equals(map.get(v.getStcd())))
                    .findFirst();
            if (first.isPresent()) {
                waterStationNameOneDay = first.get().getStnm();
                dtmel = first.get().getDtmel();
            }
            BigDecimal finalDtmel = dtmel;
            nowList.stream()
                    .filter(v -> v.getDid().equals(waterWaterOneDayDid))
                    .findFirst()
                    .ifPresent(v -> waterMaxOneDay.set(new BigDecimal(v.getAddrv())
                            .divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP)
                            .add(finalDtmel)
                    ));
        }

        WaterFlowIncreaseVo vo = new WaterFlowIncreaseVo();
        vo.setWaterIncreaseNmOneHour(flowWaterOneHourMap.values().size() + waterWaterOneHourMap.values().size());
        vo.setWaterIncreaseNmOneDay(flowWaterOneDayMap.values().size() + waterWaterOneDayMap.values().size());
        vo.setWaterMaxOneHour(waterMaxOneHour.get());
        vo.setWaterMaxOneDay(waterMaxOneDay.get());
        vo.setWaterStationNameOneHour(waterStationNameOneHour);
        vo.setWaterStationNameOneDay(waterStationNameOneDay);
        vo.setWaterValueOneHour(waterOneHourMax);
        vo.setWaterValueOneDay(waterOneDayMax);

        vo.setFlowIncreaseNmOneHour(flowOneHourMap.values().size());
        vo.setFlowIncreaseNmOneDay(flowOneDayMap.values().size());
        vo.setFlowMaxOneHour(flowMaxOneHour.get());
        vo.setFlowMaxOneDay(flowMaxOneDay.get());
        vo.setFlowStationNameOneHour(flowStationNameOneHour.get());
        vo.setFlowStationNameOneDay(flowStationNameOneDay.get());
        vo.setFlowValueOneHour(flowOneHourMax);
        vo.setFlowValueOneDay(flowOneDayMax);
        return vo;
    }

    public void setDatProvideWater(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        BigDecimal avg = this.dealDayProvide(stStbprpBEntityDTO);
        Date date = new Date();
        if (avg == null) {
            avg = new BigDecimal("0");
        }
        long l = DateUtil.betweenMs(DateUtil.beginOfDay(date), date) / 1000;
        BigDecimal provide = new BigDecimal(l).multiply(avg).divide(new BigDecimal("1"), 2, RoundingMode.HALF_UP);
        stStbprpBEntityDTO.setAvgRate(avg);
        stStbprpBEntityDTO.setDayProvideWater(provide);

    }

    @Override
    public List<StWaterRateEntityDTO> getStationDataMomentList(StStbprpBEntityDTO stStbprpBEntityDTO) {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, StStbprpBEntity> stationMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity()));
        StStbprpBEntity stStbprpBEntity = stationMap.get(stStbprpBEntityDTO.getStcd());
        if (stStbprpBEntity == null) {
            return null;
        }
        BigDecimal bhtz = stStbprpBEntity.getBhtz() == null ? BigDecimal.ZERO : stStbprpBEntity.getBhtz();
        BigDecimal wrz = stStbprpBEntity.getWrz() == null ? BigDecimal.ZERO : stStbprpBEntity.getWrz();
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")) {

            Map<String, String> stcdMap = stStbprpBEntityDTO.getStcdMap();
            if (CollUtil.isEmpty(stcdMap)) {
                List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
                stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            }
            if (stcdMap != null) {
                QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("did", stcdMap.get(stStbprpBEntityDTO.getStcd()));
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime())) {
                    String replace = stStbprpBEntityDTO.getEndTime().replace("-", "/");
                    wrapper.le("ctime", replace);
                }
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime())) {
                    String replace = stStbprpBEntityDTO.getStartTime().replace("-", "/");
                    wrapper.ge("ctime", replace);
                }
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                    // 以小时为分组 取最后一个小时的数据
                    Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                        return stWaterRateEntity.getCtime().substring(0, 16);
                    }));
                    for (String ct : ctTimeMapList.keySet()) {
                        StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                        StWaterRateEntityDTO stStbprpBEntityDTO1 = new StWaterRateEntityDTO();
                        //水位 从毫米 转换为m
                        String addrv = stWaterRateEntity.getAddrv();
                        if (StrUtil.isEmpty(addrv)) {
                            addrv = "0";
                        }
                        BigDecimal meter = new BigDecimal(addrv).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        stStbprpBEntityDTO1.setWaterDeep(meter);
                        stStbprpBEntityDTO1.setWrz(wrz);
                        stStbprpBEntityDTO1.setBhtz(bhtz);
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                            String ctime = stWaterRateEntity.getCtime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date parse = null;
                            try {
                                parse = formatter.parse(ctime);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(format);
                            stStbprpBEntityDTO1.setMDh(format.substring(5, 16));
                            //高程
                            BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                            stStbprpBEntityDTO1.setAddrv(meter.add(dtmel).toString());
                            stStbprpBEntityDTO1.setWaterDeep(meter);
                            stStbprpBEntityDTO1.setDtmel(dtmel.toString());
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
                String replace = stStbprpBEntityDTO.getEndTime().replace("-", "/");
                wrapper.le("ctime", replace);
            }
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime())) {
                String replace = stStbprpBEntityDTO.getStartTime().replace("-", "/");
                wrapper.ge("ctime", replace);
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                    return stWaterRateEntity.getCtime().substring(0, 16);
                }));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    stWaterRateEntityDTO.setWrz(wrz);
                    stWaterRateEntityDTO.setBhtz(bhtz);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5, 16));

                        //高程
                        BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                        stWaterRateEntityDTO.setWaterDeep(stWaterRateEntity.getMomentRiverPosition() == null ? new BigDecimal(0) : new BigDecimal(stWaterRateEntity.getMomentRiverPosition()));
                        stWaterRateEntityDTO.setAddrv(stWaterRateEntityDTO.getWaterDeep().add(dtmel).toString());
                        stWaterRateEntityDTO.setDtmel(dtmel.toString());
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


    @Override
    public void updateById(StStbprpBEntityDTO stStbprpB) {
        StStbprpBEntity stbprpBEntity = new StStbprpBEntity();
        BeanUtils.copyProperties(stStbprpB, stbprpBEntity);
        stStbprpBDao.updateById(stbprpBEntity);
    }

    @Override
    public void removeByIds(List<String> asList) {
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.in("stcd", asList);
        stStbprpBDao.delete(wrapper);
    }

    @Override
    public void add(StStbprpBEntityDTO stStbprpB) {
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper();
        wrapper.eq("stcd", stStbprpB.getStcd());
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stStbprpBEntities)) {
            throw new RuntimeException("测站id不能重复");
        }
        StStbprpBEntity stbprpBEntity = new StStbprpBEntity();
        BeanUtils.copyProperties(stStbprpB, stbprpBEntity);

        stStbprpBDao.insert(stbprpBEntity);
    }

    @Override
    public List<StStbprpBEntityDTO> getStationExcelList(StStbprpBEntityDTO requestStStbprpBEntityDTO) {

        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(requestStStbprpBEntityDTO.getStnm())) {
            wrapper.eq("stnm", requestStStbprpBEntityDTO.getStnm());

        }
        if (StringUtil.isNotEmpty(requestStStbprpBEntityDTO.getRvnm())) {
            wrapper.eq("rvnm", requestStStbprpBEntityDTO.getRvnm());
        }
        if (StringUtil.isNotEmpty(requestStStbprpBEntityDTO.getStlc())) {
            wrapper.eq("stlc", requestStStbprpBEntityDTO.getStlc());
        }
        if (StringUtil.isNotEmpty(requestStStbprpBEntityDTO.getSttp())) {
            wrapper.eq("sttp", requestStStbprpBEntityDTO.getSttp());
        } else {
            wrapper.eq("sttp", "ZZ").or().eq("sttp", "ZQ");
        }
        IPage pageInfo = new Page(requestStStbprpBEntityDTO.getCurrent(), requestStStbprpBEntityDTO.getSize());
        IPage pages = stStbprpBDao.selectPage(pageInfo, wrapper);
        List<StStbprpBEntity> stStbprpBEntities = pages.getRecords();
        List<StStbprpBEntityDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stStbprpBEntities)) {
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                BeanUtils.copyProperties(stStbprpBEntity, stStbprpBEntityDTO);
                list.add(stStbprpBEntityDTO);
            }
        }
        return list;
    }

    @Override
    public DeviceStatusDTO getDeviceRunStatus() {
        List<DeviceDataStatisticEntity> deviceDataStatisticEntities = deviceDataStatisticDao.selectList(new QueryWrapper<>());
        Map<String, DeviceDataStatisticEntity> typeMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceDataStatisticEntities)) {
            typeMap = deviceDataStatisticEntities.parallelStream().collect(Collectors.toMap(DeviceDataStatisticEntity::getType, Function.identity()));
        }
        DeviceStatusDTO deviceStatusDTO = new DeviceStatusDTO();
        //水位
        DeviceDataStatisticEntity zz = typeMap.get("ZZ");
        if (zz != null) {
            DeviceDataStatisticDTO water = new DeviceDataStatisticDTO();
            water.setOnline(zz.getOnline());
            water.setType("ZZ");
            water.setTotal(zz.getTotal());
            deviceStatusDTO.setWaterPosition(water);
        }

        //流量
        DeviceDataStatisticEntity zq = typeMap.get("ZQ");
        if (zq != null) {
            DeviceDataStatisticDTO flowRate = new DeviceDataStatisticDTO();
            flowRate.setOnline(zq.getOnline());
            flowRate.setType("ZQ");
            flowRate.setTotal(zq.getTotal());
            deviceStatusDTO.setFlowRate(flowRate);
        }
        //流量
        DeviceDataStatisticEntity pp = typeMap.get("PP");
        if (pp != null) {
            DeviceDataStatisticDTO rainRate = new DeviceDataStatisticDTO();
            rainRate.setOnline(pp.getOnline());
            rainRate.setType("PP");
            rainRate.setTotal(pp.getTotal());
            deviceStatusDTO.setRainRate(rainRate);
        }
        return deviceStatusDTO;
    }

    @Override
    public WaterPositionStatisticDTO getStationStatus() {
        WaterPositionStatisticEntity waterPositionStatisticEntity = waterPositionStatisticDao.selectOne(new QueryWrapper<>());
        WaterPositionStatisticDTO waterPositionStatisticDTO = new WaterPositionStatisticDTO();
        if (waterPositionStatisticEntity != null) {
            BeanUtil.copyProperties(waterPositionStatisticEntity, waterPositionStatisticDTO);
        }
        return waterPositionStatisticDTO;
    }

    @Override
    public List<DeviceFellStatusDTO> getDeviceFellStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        List<DeviceFellStatusDTO> result = new ArrayList<>();
//        if (deviceFeelRequestDTO.getDateType().equals("1")) {
//            result = getYearStatistic(deviceFeelRequestDTO);
//        }
        if (deviceFeelRequestDTO.getDateType().equals("2")) {
            result = getMonthStatistic(deviceFeelRequestDTO);
        }
//        if (deviceFeelRequestDTO.getDateType().equals("3")) {
//            result = getDayStatistic(deviceFeelRequestDTO);
//        }
        return result;
    }

    @Override
    public OnlineCheckDeviceDTO getDeviceOnlineStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        OnlineCheckDeviceDTO result = new OnlineCheckDeviceDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd");
        if (deviceFeelRequestDTO.getDateType().equals("1")) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd", deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type", deviceFeelRequestDTO.getDateType());
            wrapper.eq("date", format.substring(0, 4));
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null) {
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity, result);
                result.setStatus(status);
            }
        }
        if (deviceFeelRequestDTO.getDateType().equals("2")) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd", deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type", deviceFeelRequestDTO.getDateType());
            wrapper.eq("date", format.substring(0, 7));
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null) {
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity, result);
                result.setStatus(status);
            }
        }
        if (deviceFeelRequestDTO.getDateType().equals("3")) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd", deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type", deviceFeelRequestDTO.getDateType());
            wrapper.eq("date", format);
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null) {
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity, result);
                result.setStatus(status);
            }
        }
        return result;
    }


    @Override
    public void setStationCheckConfig(List<QualifiedConfigDTO> qualifiedConfigDTOs) {
        if (CollUtil.isNotEmpty(qualifiedConfigDTOs)) {
            for (QualifiedConfigDTO qualifiedConfigDTO : qualifiedConfigDTOs) {
                DeviceQualifiedEntity deviceQualifiedEntity = new DeviceQualifiedEntity();
                BeanUtil.copyProperties(qualifiedConfigDTO, deviceQualifiedEntity);
                //1 通过类型 去查询之前是否有数 如果有则更新 没有则新增
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("device_type", deviceQualifiedEntity.getDeviceType());
                DeviceQualifiedEntity findOne = deviceQualifiedDao.selectOne(queryWrapper);
                if (findOne != null) {
                    //更新
                    deviceQualifiedDao.update(deviceQualifiedEntity, queryWrapper);
                    continue;
                }
                deviceQualifiedDao.insert(deviceQualifiedEntity);
            }
        }
    }

    @Override
    public PageUtil<OnlineDeviceDetailDTO> getStationStatusList(StationStatusRequestDTO stationStatusRequestDTO) {
        //1.查询台账数据
        List<OnlineDeviceDetailDTO> list = new ArrayList<>();
        Date date = stationStatusRequestDTO.getDate();
        String format = DateUtil.format(date, "yyyy-MM-dd");
        Integer checkStatus = stationStatusRequestDTO.getChecked();
        Integer dateType = stationStatusRequestDTO.getDateType();
        List<StStbprpBEntity> stStbprpBEntities = new ArrayList<>();
        if (StringUtil.isNotEmpty(stationStatusRequestDTO.getSttp())) {
            if (stationStatusRequestDTO.getSttp().equals("ZZ") || stationStatusRequestDTO.getSttp().equals("ZQ")) {
                QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getStnm())) {
                    wrapper.eq("stnm", stationStatusRequestDTO.getStnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getRvnm())) {
                    wrapper.eq("rvnm", stationStatusRequestDTO.getRvnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getSttp())) {
                    wrapper.eq("sttp", stationStatusRequestDTO.getSttp());
                }
                stStbprpBEntities = stStbprpBDao.selectList(wrapper);
            } else {
                //将视频列表加入
                QueryWrapper<VideoInfoBase> videoWrapper = new QueryWrapper<>();
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getStnm())) {
                    videoWrapper.like("name", stationStatusRequestDTO.getStnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getRvnm())) {
                    videoWrapper.eq("st_b_river_id", stationStatusRequestDTO.getRvnm());
                }
                if (stationStatusRequestDTO.getSttp().equals("VIDEO")) {
                    List<String> typeList1 = new ArrayList<>();
                    typeList1.add("HUAWEI");
                    typeList1.add("HIV");
                    videoWrapper.in("source", typeList1);
                }
                if (stationStatusRequestDTO.getSttp().equals("LJ")) {
                    List<String> typeList2 = new ArrayList<>();
                    typeList2.add("ZX");
                    typeList2.add("NW");
                    typeList2.add("YSY");
                    videoWrapper.in("source", typeList2);
                }
                List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(videoWrapper);
                if (CollUtil.isNotEmpty(videoInfoBases)) {
                    for (VideoInfoBase videoInfoBase : videoInfoBases) {
                        StStbprpBEntity stStbprpBEntity = new StStbprpBEntity();
                        stStbprpBEntity.setStcd(videoInfoBase.getId().toString());
                        stStbprpBEntity.setStnm(videoInfoBase.getName() == null ? null : videoInfoBase.getName());
                        stStbprpBEntity.setLgtd(videoInfoBase.getLgtd() == null ? null : new BigDecimal(videoInfoBase.getLgtd()));
                        stStbprpBEntity.setLttd(videoInfoBase.getLttd() == null ? null : new BigDecimal(videoInfoBase.getLttd()));
                        stStbprpBEntity.setLocality(videoInfoBase.getAddress() == null ? null : videoInfoBase.getAddress());
                        stStbprpBEntity.setSttp("VIDEO");
                        stStbprpBEntity.setRvnm(videoInfoBase.getStBRiverId());
                        stStbprpBEntities.add(stStbprpBEntity);
                    }
                }
            }
        }


        //查询 河流
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        if (CollUtil.isNotEmpty(stStbprpBEntities)) {
            List<String> stcdList = stStbprpBEntities.stream().map(x -> x.getStcd()).collect(Collectors.toList());
            List<OnlineCheckDeviceEntity> onlineCheckDeviceEntitys = new ArrayList<>();
            if (stationStatusRequestDTO.getDateType().equals(3)) {
                QueryWrapper<OnlineCheckDeviceEntity> onlineWrapper = new QueryWrapper();
                onlineWrapper.in("stcd", stcdList);
                onlineWrapper.eq("date_type", dateType);
                onlineWrapper.eq("date", format);
                onlineCheckDeviceEntitys = onlineCheckDeviceDao.selectList(onlineWrapper);
            }
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                OnlineDeviceDetailDTO onlineDeviceDetailDTO = new OnlineDeviceDetailDTO();
                BeanUtil.copyProperties(stStbprpBEntity, onlineDeviceDetailDTO);
                if (StrUtil.isNotEmpty(stStbprpBEntity.getRvnm())) {
                    ReaBase reaBase = riverMap.get(stStbprpBEntity.getRvnm());
                    onlineDeviceDetailDTO.setRvnm(reaBase.getReaName());
                }
                String stcd = stStbprpBEntity.getStcd();
//                if (stationStatusRequestDTO.getDateType().equals(1)) {
//                    onlineWrapper.eq("stcd", stcd);
//                    onlineWrapper.eq("date_type", dateType);
//                    onlineWrapper.eq("date", format.substring(0, 4));
//                }
//                if (stationStatusRequestDTO.getDateType().equals(2)) {
//                    onlineWrapper.eq("stcd", stcd);
//                    onlineWrapper.eq("date_type", dateType);
//                    onlineWrapper.eq("date", format.substring(0, 7));
//                }
                OnlineCheckDeviceEntity onlineCheckDeviceEntity = new OnlineCheckDeviceEntity();
                if (stationStatusRequestDTO.getDateType().equals(3)) {
                    List<OnlineCheckDeviceEntity> onlineCheckDeviceEntityList = Optional.ofNullable(onlineCheckDeviceEntitys.stream().filter(x -> x.getStcd().equals(stcd)).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(onlineCheckDeviceEntityList)) {
                        onlineCheckDeviceEntity = onlineCheckDeviceEntityList.get(0);
                        String status = onlineCheckDeviceEntity.getStatus();
                        List<Integer> integers = JSONObject.parseArray(status, Integer.class);
                        List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
                            return integer.equals(1);
                        }).collect(Collectors.toList());
                        BigDecimal value = new BigDecimal(onlineStatus.size() * 100).divide(new BigDecimal(integers.size()), 2, BigDecimal.ROUND_HALF_UP);
                        onlineDeviceDetailDTO.setTotal(integers.size());
                        onlineDeviceDetailDTO.setFactNum(onlineStatus.size());
                        onlineDeviceDetailDTO.setPercent(value);
                        onlineDeviceDetailDTO.setChecked(onlineCheckDeviceEntity.getChecked());
                    }
                }
                list.add(onlineDeviceDetailDTO);
            }
        }
        //总数
        Integer count = list.size();
        if (checkStatus != null) {
            if (checkStatus == 1) {
                list = list.stream().filter(x -> x.getChecked() != null && x.getChecked() == checkStatus).collect(Collectors.toList());
            } else {
                list = list.stream().filter(x -> x.getChecked() == null || x.getChecked() != 1).collect(Collectors.toList());
            }

        }
        //在线数
        Integer onlie = 0;
        if (!CollectionUtils.isEmpty(list)) {
            List<OnlineDeviceDetailDTO> collect = list.stream().filter(x -> x.getChecked() != null && x.getChecked() == 1).collect(Collectors.toList());
            onlie = collect.size();
        }
/*

        BigDecimal percent = new BigDecimal(0);


        if (CollUtil.isNotEmpty(list)) {
            checked = list.parallelStream().filter(onlineDeviceDetailDTO -> {
                return (onlineDeviceDetailDTO.getChecked() != null) && (checkStatus != null) && (onlineDeviceDetailDTO.getChecked().equals(checkStatus));
            }).collect(Collectors.toList());
            percent = new BigDecimal(checked.size()).divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }

        if (checkStatus != null) {
            PageUtil<OnlineDeviceDetailDTO> pageUtil = new PageUtil(checked, stationStatusRequestDTO.getCurrent(), stationStatusRequestDTO.getSize(), percent, onlie);
            return pageUtil;
        }*/
        PageUtil<OnlineDeviceDetailDTO> pageUtil = new PageUtil(list, stationStatusRequestDTO.getCurrent(), stationStatusRequestDTO.getSize(), new BigDecimal(count), onlie);
        return pageUtil;
    }

    @Override
    public List<OnlineDeviceDetailDVo> getStationStatusLists(StationStatusRequestDTO stationStatusRequestDTO) {
        //1.查询台账数据
        List<OnlineDeviceDetailDVo> list = new ArrayList<>();
        Date date = stationStatusRequestDTO.getDate();
        String format = DateUtil.format(date, "yyyy-MM-dd");
        Integer checkStatus = stationStatusRequestDTO.getChecked();
        Integer dateType = stationStatusRequestDTO.getDateType();
        List<StStbprpBEntity> stStbprpBEntities = new ArrayList<>();
        if (StringUtil.isNotEmpty(stationStatusRequestDTO.getSttp())) {
            if (stationStatusRequestDTO.getSttp().equals("ZZ") || stationStatusRequestDTO.getSttp().equals("ZQ")) {
                QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getStnm())) {
                    wrapper.eq("stnm", stationStatusRequestDTO.getStnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getRvnm())) {
                    wrapper.eq("rvnm", stationStatusRequestDTO.getRvnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getSttp())) {
                    wrapper.eq("sttp", stationStatusRequestDTO.getSttp());
                }
                stStbprpBEntities = stStbprpBDao.selectList(wrapper);
            } else {
                //将视频列表加入
                QueryWrapper<VideoInfoBase> videoWrapper = new QueryWrapper<>();
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getStnm())) {
                    videoWrapper.like("name", stationStatusRequestDTO.getStnm());
                }
                if (StringUtil.isNotEmpty(stationStatusRequestDTO.getRvnm())) {
                    videoWrapper.eq("st_b_river_id", stationStatusRequestDTO.getRvnm());
                }
                if (stationStatusRequestDTO.getSttp().equals("VIDEO")) {
                    List<String> typeList1 = new ArrayList<>();
                    typeList1.add("HUAWEI");
                    typeList1.add("HIV");
                    videoWrapper.in("source", typeList1);
                }
                if (stationStatusRequestDTO.getSttp().equals("LJ")) {
                    List<String> typeList2 = new ArrayList<>();
                    typeList2.add("ZX");
                    typeList2.add("NW");
                    typeList2.add("YSY");
                    videoWrapper.in("source", typeList2);
                }
                List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(videoWrapper);
                if (CollUtil.isNotEmpty(videoInfoBases)) {
                    for (VideoInfoBase videoInfoBase : videoInfoBases) {
                        StStbprpBEntity stStbprpBEntity = new StStbprpBEntity();
                        stStbprpBEntity.setStcd(videoInfoBase.getId().toString());
                        stStbprpBEntity.setStnm(videoInfoBase.getName() == null ? null : videoInfoBase.getName());
                        stStbprpBEntity.setLgtd(videoInfoBase.getLgtd() == null ? null : new BigDecimal(videoInfoBase.getLgtd()));
                        stStbprpBEntity.setLttd(videoInfoBase.getLttd() == null ? null : new BigDecimal(videoInfoBase.getLttd()));
                        stStbprpBEntity.setLocality(videoInfoBase.getAddress() == null ? null : videoInfoBase.getAddress());
                        stStbprpBEntity.setSttp("VIDEO");
                        stStbprpBEntity.setRvnm(videoInfoBase.getStBRiverId());
                        stStbprpBEntities.add(stStbprpBEntity);
                    }
                }
            }
        }


        //查询 河流
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        if (CollUtil.isNotEmpty(stStbprpBEntities)) {
            List<String> stcdList = stStbprpBEntities.stream().map(x -> x.getStcd()).collect(Collectors.toList());
            List<OnlineCheckDeviceEntity> onlineCheckDeviceEntitys = new ArrayList<>();
            if (stationStatusRequestDTO.getDateType().equals(3)) {
                QueryWrapper<OnlineCheckDeviceEntity> onlineWrapper = new QueryWrapper();
                onlineWrapper.in("stcd", stcdList);
                onlineWrapper.eq("date_type", dateType);
                onlineWrapper.eq("date", format);
                onlineCheckDeviceEntitys = onlineCheckDeviceDao.selectList(onlineWrapper);
            }
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                OnlineDeviceDetailDVo onlineDeviceDetailDVo = new OnlineDeviceDetailDVo();
                onlineDeviceDetailDVo.setStnm(stStbprpBEntity.getStnm());
                if (stStbprpBEntity.getSttp().equals("ZZ")) {
                    onlineDeviceDetailDVo.setSttp("水位");
                } else {
                    if (stStbprpBEntity.getSttp().equals("ZQ")) {
                        onlineDeviceDetailDVo.setSttp("流量");
                    } else {
                        onlineDeviceDetailDVo.setSttp("视频");
                    }
                }

                if (StrUtil.isNotEmpty(stStbprpBEntity.getRvnm())) {
                    ReaBase reaBase = riverMap.get(stStbprpBEntity.getRvnm());
                    onlineDeviceDetailDVo.setRvnm(reaBase.getReaName());
                }
                String stcd = stStbprpBEntity.getStcd();
                OnlineCheckDeviceEntity onlineCheckDeviceEntity = new OnlineCheckDeviceEntity();
                if (stationStatusRequestDTO.getDateType().equals(3)) {
                    List<OnlineCheckDeviceEntity> onlineCheckDeviceEntityList = Optional.ofNullable(onlineCheckDeviceEntitys.stream().filter(x -> x.getStcd().equals(stcd)).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(onlineCheckDeviceEntityList)) {
                        onlineCheckDeviceEntity = onlineCheckDeviceEntityList.get(0);
                        String status = onlineCheckDeviceEntity.getStatus();
                        List<Integer> integers = JSONObject.parseArray(status, Integer.class);
                        List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
                            return integer.equals(1);
                        }).collect(Collectors.toList());
                        BigDecimal value = new BigDecimal(onlineStatus.size() * 100).divide(new BigDecimal(integers.size()), 2, BigDecimal.ROUND_HALF_UP);
                        onlineDeviceDetailDVo.setTotal(integers.size());
                        onlineDeviceDetailDVo.setFactNum(onlineStatus.size());
                        onlineDeviceDetailDVo.setPercent(value);
                        if (onlineCheckDeviceEntity.getChecked() != null && onlineCheckDeviceEntity.getChecked() == 1) {
                            onlineDeviceDetailDVo.setChecked("达标");
                        } else {
                            onlineDeviceDetailDVo.setChecked("未达标");
                        }

                    }
                }
                list.add(onlineDeviceDetailDVo);
            }
        }
        if (checkStatus != null) {
            if (checkStatus == 1) {
                list = list.stream().filter(x -> x.getChecked().equals("达标")).collect(Collectors.toList());
            } else {
                list = list.stream().filter(x -> x.getChecked().equals("未达标")).collect(Collectors.toList());
            }
        }
        return list;
    }


    @Override
    public List<QualifiedConfigDTO> getStationCheckConfig(QualifiedConfigDTO requestConfig) {
        List<QualifiedConfigDTO> result = new ArrayList<>();

        List<DeviceQualifiedEntity> deviceQualifiedEntities = deviceQualifiedDao.selectList(new QueryWrapper<>());
        if (CollUtil.isNotEmpty(deviceQualifiedEntities)) {
            for (DeviceQualifiedEntity deviceQualifiedEntity : deviceQualifiedEntities) {
                QualifiedConfigDTO qualifiedConfigDTO = new QualifiedConfigDTO();
                BeanUtil.copyProperties(deviceQualifiedEntity, qualifiedConfigDTO);
                result.add(qualifiedConfigDTO);
            }
        }
        return result;
    }

    @Override
    public DeviceCountDTO getDeviceCount(String unitId) {
        //查询单位所属的河段
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(unitId)) {
            QueryWrapper<ReaBase> queryWrapperRv = new QueryWrapper<>();
            queryWrapperRv.le("id", 31);
            queryWrapperRv.eq("unit_id", unitId);
            List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapperRv);
            List<String> rvids = reaBases.parallelStream().map(ReaBase::getId).collect(Collectors.toList());
            queryWrapper.eq("rvnm", rvids);
        }
        DeviceCountDTO res = new DeviceCountDTO();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(queryWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(stStbprpBEntity -> {
            return Objects.nonNull(stStbprpBEntity.getSttp());
        }).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        res.setZzCount(CollUtil.isNotEmpty(sttpMap.get("ZZ")) ? sttpMap.get("ZZ").size() : 0);
        res.setZqCount(CollUtil.isNotEmpty(sttpMap.get("ZQ")) ? sttpMap.get("ZQ").size() : 0);
        res.setPpCount(CollUtil.isNotEmpty(sttpMap.get("PP")) ? sttpMap.get("PP").size() : 0);


        QueryWrapper videoWrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            videoWrapper.eq("unit_id", unitId);
        }
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(videoWrapper);
        Map<String, List<VideoInfoBase>> functionMap = videoInfoBases.parallelStream().filter(videoInfoBase -> {
            return Objects.nonNull(videoInfoBase.getFunctionType());
        }).collect(Collectors.groupingBy(VideoInfoBase::getFunctionType));
        res.setFunctionMonitor(CollUtil.isNotEmpty(functionMap.get("1")) ? functionMap.get("1").size() : 0);
        res.setProtectMonitor(CollUtil.isNotEmpty(functionMap.get("2")) ? functionMap.get("2").size() : 0);
        res.setJfMonitor(CollUtil.isNotEmpty(functionMap.get("3")) ? functionMap.get("3").size() : 0);
        //摄像头 分类 普通摄像头 丽旧 摄像头
        List<String> typeList1 = new ArrayList<>();
        typeList1.add("HUAWEI");
        typeList1.add("HIV");

        List<String> typeList2 = new ArrayList<>();
        typeList2.add("ZX");
        typeList2.add("NW");
        typeList2.add("YSY");
        List<VideoInfoBase> commonCamera = videoInfoBases.parallelStream().filter(videoInfoBase -> {
            return videoInfoBase.getSource() != null && typeList1.contains(videoInfoBase.getSource());
        }).collect(Collectors.toList());
        res.setCommonCameraCount(CollUtil.isEmpty(commonCamera) ? 0 : commonCamera.size());
        //丽旧 摄像头
        List<VideoInfoBase> ljCamera = videoInfoBases.parallelStream().filter(videoInfoBase -> {
            return videoInfoBase.getSource() != null && typeList2.contains(videoInfoBase.getSource());
        }).collect(Collectors.toList());
        res.setLjCameraCount(CollUtil.isEmpty(ljCamera) ? 0 : ljCamera.size());

        //丽旧 + 智能摄像头 = commonCameraAll
        res.setCommonCameraAll(res.getCommonCameraCount() + res.getLjCameraCount());
        //丽旧 + 智能摄像头 = commonCameraAll

        //边闸
        QueryWrapper gateWrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(unitId)) {
            gateWrapper.eq("unit_id", unitId);
        }
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(gateWrapper);

        if (CollUtil.isNotEmpty(stSideGateDtos)) {
            Map<String, List<StSideGateDto>> collect = stSideGateDtos.parallelStream().filter(stSideGateDto -> {
                return StrUtil.isNotEmpty(stSideGateDto.getSttp());
            }).collect(Collectors.groupingBy(StSideGateDto::getSttp));
            res.setDdCount(CollUtil.isNotEmpty(collect.get("DD")) ? collect.get("DD").size() : 0);
            res.setDpCount(CollUtil.isNotEmpty(collect.get("DP")) ? collect.get("DP").size() : 0);
            res.setSbCount(CollUtil.isNotEmpty(collect.get("SB")) ? collect.get("DP").size() : 0);
            res.setSideGateCount(CollUtil.isNotEmpty(collect.get("BZ")) ? collect.get("BZ").size() : 0);
        }

        return res;
    }

    @Override
    public StationFlowStatisticDTO getFlowRiver(StationFlowDTO stationFlowDTO) {
        List<StStbprpAlarmDto> stStbprpAlarmDtos = stStbprpAlarmDao.selectList(new QueryWrapper<>());
        Map<String, StStbprpAlarmDto> stcdAlarmMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stStbprpAlarmDtos)) {
            stcdAlarmMap = stStbprpAlarmDtos.parallelStream().collect(Collectors.toMap(StStbprpAlarmDto::getStcd, Function.identity(), (o1, o2) -> o2));
        }
        StationFlowStatisticDTO res = new StationFlowStatisticDTO();
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        if (StrUtil.isNotEmpty(stationFlowDTO.getRvid())) {
            queryWrapper.eq("id", stationFlowDTO.getRvid());
        }
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        } else {
            return res;
        }
        QueryWrapper statusWrapper = new QueryWrapper();
        if (stationFlowDTO.getDate() != null) {
            statusWrapper.eq("time", DateUtil.format(stationFlowDTO.getDate(), "yyyyMMdd"));
        }
        List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(statusWrapper);
        Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceStatusEntities)) {
            stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
        }
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp", "ZQ");
        if (stationFlowDTO.getStnm() != null && !"".equals(stationFlowDTO.getStnm())) {
            wrapper.eq("stnm", stationFlowDTO.getStnm());
        }
        if (stationFlowDTO.getRvnm() != null && !"".equals(stationFlowDTO.getRvnm())) {
            wrapper.like("rvnm", stationFlowDTO.getRvnm());
        }
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);

        List<StationFlowDTO> stationFlowDTOS = new ArrayList<>();
        Integer normal = 0;
        Integer unNormal = 0;
        if (CollUtil.isNotEmpty(stStbprpBEntities)) {
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StationFlowDTO stationFlowDTO1 = new StationFlowDTO();
                BeanUtil.copyProperties(stStbprpBEntity, stationFlowDTO1);
                ReaBase reaBase = riverMap.get(stationFlowDTO1.getRvnm());
                if (reaBase == null) {
                    continue;
                }
                stationFlowDTO1.setRvnm(reaBase.getReaName());
                DeviceStatusEntity deviceStatusEntity = stcdMap.get(stationFlowDTO1.getStcd());
                if (deviceStatusEntity != null) {
                    BigDecimal waterRate = deviceStatusEntity.getWaterRate() == null ? null : new BigDecimal(deviceStatusEntity.getWaterRate());
                    stationFlowDTO1.setWaterPosition(deviceStatusEntity.getWaterPosition() == null ? null : new BigDecimal(deviceStatusEntity.getWaterPosition()));
                    stationFlowDTO1.setWaterRate(deviceStatusEntity.getWaterRate() == null ? null : new BigDecimal(deviceStatusEntity.getWaterRate()));
                    StStbprpAlarmDto stStbprpAlarmDto = stcdAlarmMap.get(stationFlowDTO1.getStcd());

                    //此处设置 在线离线
                    String onlineStatus = deviceStatusEntity.getOnlineStatus();
                    //在线
                    if (onlineStatus.equals("1")) {
                        if (waterRate != null
                                && stStbprpAlarmDto != null
                                && stStbprpAlarmDto.getAlarmFlow() != null
                                && waterRate.compareTo(stStbprpAlarmDto.getAlarmFlow()) <= 0) {
                            stationFlowDTO1.setFlowStatus(2);
                            ++unNormal;
                        } else {
                            stationFlowDTO1.setFlowStatus(1);
                            ++normal;
                        }
                    } else {
                        stationFlowDTO1.setFlowStatus(1);
                        ++normal;
                    }
                    stationFlowDTO1.setOnLineStatus(onlineStatus);

                } else {
                    stationFlowDTO1.setFlowStatus(1);
                    ++normal;
                }
                stationFlowDTOS.add(stationFlowDTO1);
            }
        }
        //对flowStatus 流量状态进行过滤
        if (stationFlowDTO.getFlowStatus() != null && !"".equals(stationFlowDTO.getFlowStatus())) {
            stationFlowDTOS = stationFlowDTOS.parallelStream().filter(StationFlowDTO -> {
                return StationFlowDTO.getFlowStatus() != null && StationFlowDTO.getFlowStatus().equals(stationFlowDTO.getFlowStatus());
            }).collect(Collectors.toList());
        }

        //对flowStatus 流量状态进行过滤

        //对 字段进行排序
        if (StrUtil.isNotEmpty(stationFlowDTO.getField()) && stationFlowDTO.getSort() != null) {
            stationFlowDTOS = getSortList(stationFlowDTOS, stationFlowDTO);
        }

        res.setNormalNum(normal);
        res.setUnNormalNum(unNormal);
        res.setStationFlowDTOS(stationFlowDTOS);
        return res;
    }

    @Override
    public List<ReaBaseDto> getFlowRiverMap() {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        QueryWrapper<OrganismRiverRecordEntity> orgQuery = new QueryWrapper();
        orgQuery.eq("year", DateUtil.format(new Date(), "yyyy-MM-dd").substring(0, 4));
        List<OrganismRiverRecordEntity> organismRiverRecordEntities = organismRiverRecordDao.selectList(orgQuery);
        Map<String, OrganismRiverRecordEntity> rvnmMap = new HashMap<>();
        if (CollUtil.isNotEmpty(organismRiverRecordEntities)) {
            rvnmMap = organismRiverRecordEntities.parallelStream().collect(Collectors.toMap(OrganismRiverRecordEntity::getRvnm, Function.identity()));
        }
        List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(new QueryWrapper<>());
        Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceStatusEntities)) {
            stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
        }
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp", "ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        Map<String, List<StStbprpBEntity>> stationRvIdMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stStbprpBEntities)) {
            stationRvIdMap = stStbprpBEntities.parallelStream().collect(Collectors.groupingBy(StStbprpBEntity::getRvnm));
        }
        List<ReaBaseDto> reaBaseDtoList = new ArrayList<>();
        for (ReaBase reaBase : reaBases) {
            ReaBaseDto reaBaseDto = new ReaBaseDto();
            BeanUtil.copyProperties(reaBase, reaBaseDto);
            List<StStbprpBEntity> stStbprpBEntities1 = stationRvIdMap.get(reaBase.getId());
            if (CollUtil.isNotEmpty(stStbprpBEntities1)) {
                BigDecimal rate = new BigDecimal(0);
                int size = stStbprpBEntities1.size();
                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities1) {
                    String stcd = stStbprpBEntity.getStcd();
                    DeviceStatusEntity deviceStatusEntity = stcdMap.get(stcd);
                    if (deviceStatusEntity == null) {
                        rate = rate.add(new BigDecimal(0));
                    } else {
                        String waterRate = deviceStatusEntity.getWaterRate();
                        rate = rate.add(new BigDecimal(StringUtil.isNotEmpty(waterRate) ? waterRate : "0"));

                    }

                }
                BigDecimal divide = rate.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
                if (divide.compareTo(BigDecimal.ZERO) >= 0 && divide.compareTo(BigDecimal.ONE) <= 0) {
                    reaBaseDto.setWaterRate(1);
                }
                if (divide.compareTo(BigDecimal.ONE) >= 0 && divide.compareTo(new BigDecimal(2)) <= 0) {
                    reaBaseDto.setWaterRate(2);
                }
                if (divide.compareTo(new BigDecimal(2)) >= 0 && divide.compareTo(new BigDecimal(3)) <= 0) {
                    reaBaseDto.setWaterRate(3);
                }
                OrganismRiverRecordEntity organismRiverRecordEntity = rvnmMap.get(reaBaseDto.getReaName());
                if (organismRiverRecordEntity != null) {
                    BigDecimal rvLong = organismRiverRecordEntity.getRvLong();
                    reaBaseDto.setReaLength(rvLong);
                    BigDecimal rvWide = organismRiverRecordEntity.getRvWide();
                    reaBaseDto.setReaWidth(rvWide);
                }
                reaBaseDtoList.add(reaBaseDto);
            }
        }

        return reaBaseDtoList;
    }

    @Override
    public RainComposeDto getStationRainNum(StStbprpBEntityDTO stStbprpBEntityDTO) {
        RainComposeDto res = new RainComposeDto();
        // 时段雨量
        List<BigDecimal> rainList = new ArrayList<>();
        // 累计雨量
        List<BigDecimal> addList = new ArrayList<>();
        //时间
        List<String> timeList = new ArrayList<>();
        //累计雨量
        BigDecimal addRainNum = new BigDecimal(0);

        String startTime = stStbprpBEntityDTO.getStartTime();
        String endTime = stStbprpBEntityDTO.getEndTime();
        DateTime parse = DateUtil.parse(endTime);
        String stcd = stStbprpBEntityDTO.getStcd();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("station_id", stcd);
        wrapper.ge("date", DateUtil.parse(startTime));
        wrapper.le("date", DateUtil.parse(endTime));
        wrapper.orderByAsc("date");
        List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(stRainDateDtos)) {
            Map<String, List<StRainDateDto>> hourMap = stRainDateDtos.stream().collect(Collectors.groupingBy(stRainDateDto -> {
                return DateUtil.format(stRainDateDto.getDate(), "yyyy-MM-dd HH:mm:ss").substring(0, 13);
            }));
            BigDecimal addNum = new BigDecimal(0);
            List<String> collect = hourMap.keySet().stream().sorted().collect(Collectors.toList());
            for (String hour : collect) {
                timeList.add(hour);
                List<StRainDateDto> stRainDateDtos1 = hourMap.get(hour);
                BigDecimal hourNum = stRainDateDtos1.parallelStream().map(stRainDateDto -> {
                    if (stRainDateDto.getHhRain().equals("9999")) {
                        return "0";
                    } else {
                        return stRainDateDto.getHhRain();
                    }
                }).map(s -> {
                    return new BigDecimal(s);
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
                addNum = addNum.add(hourNum);
                rainList.add(hourNum);
                addList.add(addNum);
            }

        }
        if (CollUtil.isNotEmpty(addList)) {
            BigDecimal reduce = addList.parallelStream().reduce(BigDecimal.ZERO, BigDecimal::add);
            addRainNum = reduce;
            res.setAddRainNum(addRainNum);
        }
        res.setAddList(addList);
        res.setTimeList(timeList);
        res.setRainList(rainList);
        return res;
    }

    @Override
    public FlowMonitorDto getFlowRateMonitor(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        FlowMonitorDto flowMonitorDto = new FlowMonitorDto();
        QueryWrapper flowWrapper = new QueryWrapper();
        flowWrapper.eq("stcd", stStbprpBEntityDTO.getStcd());
        StStbprpBEntity stStbprpBEntities = stStbprpBDao.selectOne(flowWrapper);

        QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
        if (stStbprpBEntityDTO.getStcd().length() <= 3) {
            return null;
        }
        Date date = new Date();
        if (StrUtil.isEmpty(stStbprpBEntityDTO.getStartTime()) && StrUtil.isEmpty(stStbprpBEntityDTO.getEndTime())) {
            stStbprpBEntityDTO.setEndTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            stStbprpBEntityDTO.setStartTime(DateUtil.format(DateUtil.beginOfDay(date), "yyyy-MM-dd HH:mm:ss"));
        }
        //流量站检测数据返回的数据id 缺失前两位 00 台账表中的台账ID 需要截取掉前两位进行查询
        wrapper.eq("did", stStbprpBEntityDTO.getStcd().substring(2));
        wrapper.le("ctime", stStbprpBEntityDTO.getEndTime());
        wrapper.ge("ctime", stStbprpBEntityDTO.getStartTime());

        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
            // 以小时为分组 取最后一个小时的数据
            Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                return stWaterRateEntity.getCtime().substring(0, 13);
            }));
            BigDecimal momentRate = new BigDecimal(0);

            List<BigDecimal> timelyFlow = new ArrayList<>();
            List<String> dates = new ArrayList<>();
            for (String ct : ctTimeMapList.keySet()) {
                List<StWaterRateEntity> stWaterRateEntities1 = ctTimeMapList.get(ct);
                StWaterRateEntity stWaterRateEntity = stWaterRateEntities1.get(0);
                if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                    BigDecimal value = new BigDecimal(stWaterRateEntity.getMomentRate());
                    timelyFlow.add(value);
                    momentRate = momentRate.add(value);
                } else {
                    timelyFlow.add(BigDecimal.ZERO);
                }
                dates.add(ct);
            }
            int size = ctTimeMapList.keySet().size();
            //平均流量
            BigDecimal divide = momentRate.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
            flowMonitorDto.setTimelyFlow(timelyFlow);
            flowMonitorDto.setAvgFlow(divide);
            flowMonitorDto.setDates(dates);
        } else {

        }
        return flowMonitorDto;
    }

    public BigDecimal dealDayProvide(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
/*        QueryWrapper flowWrapper = new QueryWrapper();
        flowWrapper.eq("stcd", stStbprpBEntityDTO.getStcd());*/
        // StStbprpBEntity stStbprpBEntities = stStbprpBDao.selectOne(new QueryWrapper<>());

        QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
        if (stStbprpBEntityDTO.getStcd().length() <= 3) {
            return null;
        }
        Date date = new Date();
        stStbprpBEntityDTO.setEndTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
        stStbprpBEntityDTO.setStartTime(DateUtil.format(DateUtil.beginOfDay(date), "yyyy-MM-dd HH:mm:ss"));
        //流量站检测数据返回的数据id 缺失前两位 00 台账表中的台账ID 需要截取掉前两位进行查询
        wrapper.eq("did", stStbprpBEntityDTO.getStcd().substring(2));
        wrapper.le("ctime", stStbprpBEntityDTO.getEndTime());
        wrapper.ge("ctime", stStbprpBEntityDTO.getStartTime());
        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
            // 以小时为分组 取最后一个小时的数据
            Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                return stWaterRateEntity.getCtime().substring(0, 13);
            }));
            BigDecimal momentRate = new BigDecimal(0);


            for (String ct : ctTimeMapList.keySet()) {
                StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                    BigDecimal value = new BigDecimal(stWaterRateEntity.getMomentRate() == null ? "0" : stWaterRateEntity.getMomentRate());
                    momentRate = momentRate.add(value);
                }
            }
            int size = ctTimeMapList.keySet().size();
            //平均流量
            BigDecimal divide = momentRate.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
            return divide;
        }
        return null;
    }


    /**
     * 排序
     *
     * @param stationFlowDTOS
     * @param stationFlowDTO
     * @return
     */
    public List<StationFlowDTO> getSortList(List<StationFlowDTO> stationFlowDTOS, StationFlowDTO stationFlowDTO) {
        List<StationFlowDTO> res = new ArrayList<>();
        List<StationFlowDTO> reverse = new ArrayList<>();
        if (stationFlowDTO.getField().equals("status")) {
            res = stationFlowDTOS.parallelStream().sorted(Comparator.comparing(StationFlowDTO::getFlowStatus)).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)) {
                return res;
            } else {
                res = stationFlowDTOS.parallelStream().sorted(Comparator.comparing(StationFlowDTO::getFlowStatus)).collect(Collectors.toList());
                reverse = CollUtil.reverse(res);
                return reverse;
            }
        } else if (stationFlowDTO.getField().equals("waterRate")) {
            res = stationFlowDTOS.parallelStream().sorted(Comparator.comparing(stationFlowDTO1 -> {
                return (stationFlowDTO1.getWaterRate() == null) ? new BigDecimal(0) : stationFlowDTO1.getWaterRate();
            })).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)) {

                return res;
            } else {

                reverse = CollUtil.reverse(res);
                return reverse;
            }
        } else if (stationFlowDTO.getField().equals("waterPosition")) {
            res = stationFlowDTOS.parallelStream().sorted(Comparator.comparing(stationFlowDTO1 -> {
                return (stationFlowDTO1.getWaterPosition() == null) ? new BigDecimal(0) : stationFlowDTO1.getWaterPosition();
            })).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)) {
                return res;
            } else {
                reverse = CollUtil.reverse(res);
                return reverse;
            }
        } else {
            return stationFlowDTOS;
        }
    }


    public DeviceFeelCountDTO getYearStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type", 1);
        wrapper.eq("date", format.substring(0, 4));
        wrapper.eq("type", deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null) {
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();

            result.setDateType("1");
            result.setUpValue(JSONObject.parseArray(onValue, Integer.class));
            result.setDownValue(JSONObject.parseArray(downValue, Integer.class));
            result.setPercent(JSONObject.parseArray(percent, BigDecimal.class));

        }

        return result;
    }

    public List<DeviceFellStatusDTO> getMonthStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
//        DeviceFeelCountDTO result = new DeviceFeelCountDTO();

        List<DeviceFellStatusDTO> res = new ArrayList<>();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type", 2);
        wrapper.eq("date", format.substring(0, 7));
        wrapper.eq("type", deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null) {
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();
            List<Integer> upValue = JSONObject.parseArray(onValue, Integer.class);
            List<Integer> downValues = JSONObject.parseArray(downValue, Integer.class);
            DateTime start = DateUtil.beginOfMonth(deviceFeelRequestDTO.getDate());
            DateTime end = DateUtil.endOfMonth(deviceFeelRequestDTO.getDate());
            List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
            for (int i = 0; i < dateTimes.size(); i++) {
                DeviceFellStatusDTO deviceFellStatusDTO = new DeviceFellStatusDTO();
                String format1 = DateUtil.format(dateTimes.get(i), "yyyy-MM-dd");
                deviceFellStatusDTO.setDate(format1);
                Integer up = upValue.get(i);
                Integer down = downValues.get(i);
                deviceFellStatusDTO.setOnline(up);
                deviceFellStatusDTO.setDown(down);
                deviceFellStatusDTO.setTotal(up + down);
                //增加判断
                String preDayDate = TimeUtil.getPreDayDate(new Date(), 0);
                // 返回正值是代表左侧日期大于参数日期，反之亦然，日期格式必须一致
                String selectDate = format1.substring(0, 7);
                String nowDate = preDayDate.substring(0, 7);
                if (selectDate.equals(nowDate)) { //选的是当月
                    if (format1.compareTo(preDayDate) < 0) {
                        //只添加当天以及之前的数据
                        res.add(deviceFellStatusDTO);
                    }
                } else {
                    //非当月 则返回所有的数据
                    res.add(deviceFellStatusDTO);
                }

            }
        }
        //按照时间倒序排序
        String flag = deviceFeelRequestDTO.getFlag();
        if ("desc".equals(flag)) {
            Collections.sort(res, Comparator.comparing(DeviceFellStatusDTO::getDate).reversed());
        }
        return res;
//        return result;
    }


    public DeviceFeelCountDTO getDayStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type", 3);
        wrapper.eq("date", format.substring(0, 10));
        wrapper.eq("type", deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null) {
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();

            result.setDateType("1");
            result.setUpValue(JSONObject.parseArray(onValue, Integer.class));
            result.setDownValue(JSONObject.parseArray(downValue, Integer.class));
            result.setPercent(JSONObject.parseArray(percent, BigDecimal.class));

        }

        return result;
    }


    public WaterPositionStatisticDTO getWaterPosition() throws ParseException {
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item -> StringUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        //水位
        List<StStbprpBEntity> zz = sttpMap.get("ZZ");
        Integer normalNum = 0;
        Integer moreForbidNum = 0;
        Integer mostPositionNum = 0;
        QueryWrapper<StSnConvertEntity> convertWrapper = new QueryWrapper();
        List<StSnConvertEntity> list1 = stSnConvertDao.selectList(convertWrapper);
        Map<String, String> stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
        for (StStbprpBEntity stbprpBEntity : zz) {
            String endZZ = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss");
            String startZZ = DateUtil.format(DateUtil.offsetDay(new Date(), -3), "yyyy/MM/dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
            stStbprpBEntityDTO.setSttp("ZZ");
            stStbprpBEntityDTO.setStartTime(startZZ);
            stStbprpBEntityDTO.setEndTime(endZZ);
            stStbprpBEntityDTO.setStcd(stbprpBEntity.getStcd());
            stStbprpBEntityDTO.setStcdMap(stcdMap);
            List<StWaterRateEntityDTO> stationDataListZZ = getStationDataList(stStbprpBEntityDTO);
            if (CollectionUtil.isNotEmpty(stationDataListZZ)) {
                StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZZ.get(stationDataListZZ.size() - 1);
                //水位值
                BigDecimal water = new BigDecimal(0);
                if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())) {
                    water = new BigDecimal(stWaterRateEntityDTO.getAddrv()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
                }
                //高程
                BigDecimal dtmel = stbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stbprpBEntity.getDtmel();
                //警戒水位
                BigDecimal wrz = stbprpBEntity.getWrz() == null ? new BigDecimal(0) : stbprpBEntity.getWrz();
                //最高水位
                BigDecimal bhtz = stbprpBEntity.getBhtz() == null ? new BigDecimal(0) : stbprpBEntity.getBhtz();
                if (water.add(dtmel).compareTo(bhtz) >= 0) {
                    mostPositionNum++;
                }
                if (water.add(dtmel).compareTo(wrz) >= 0) {
                    moreForbidNum++;
                } else {
                    normalNum++;
                }
            }
        }

        //流量
        List<StStbprpBEntity> zq = sttpMap.get("ZQ");
        for (StStbprpBEntity stbprpBEntity : zq) {
            String endZQ = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String startZQ = DateUtil.format(DateUtil.offsetDay(new Date(), -3), "yyyy-MM-dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityZQDTO = new StStbprpBEntityDTO();
            stStbprpBEntityZQDTO.setSttp("ZQ");
            stStbprpBEntityZQDTO.setStartTime(startZQ);
            stStbprpBEntityZQDTO.setEndTime(endZQ);
            stStbprpBEntityZQDTO.setStcd(stbprpBEntity.getStcd());
            List<StWaterRateEntityDTO> stationDataListZQ = getStationDataList(stStbprpBEntityZQDTO);
            if (CollectionUtil.isNotEmpty(stationDataListZQ)) {
                StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZQ.get(stationDataListZQ.size() - 1);
                //水位值
                BigDecimal water = new BigDecimal(0);
                if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())) {
                    water = new BigDecimal(stWaterRateEntityDTO.getAddrv()).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP);
                }
                //高程
                BigDecimal dtmel = stbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stbprpBEntity.getDtmel();
                //警戒水位
                BigDecimal wrz = stbprpBEntity.getWrz() == null ? new BigDecimal(0) : stbprpBEntity.getWrz();
                //最高水位
                BigDecimal bhtz = stbprpBEntity.getBhtz() == null ? new BigDecimal(0) : stbprpBEntity.getBhtz();
                if (water.add(dtmel).compareTo(bhtz) >= 0) {
                    mostPositionNum++;
                }
                if (water.add(dtmel).compareTo(wrz) >= 0) {
                    moreForbidNum++;
                } else {
                    normalNum++;
                }
            }
        }
        WaterPositionStatisticDTO waterPositionStatisticDTO = new WaterPositionStatisticDTO();
        waterPositionStatisticDTO.setNormalNum(normalNum);
        waterPositionStatisticDTO.setMoreForbidNum(moreForbidNum);
        waterPositionStatisticDTO.setMostPositionNum(mostPositionNum);
        int total = normalNum + moreForbidNum + mostPositionNum;
        waterPositionStatisticDTO.setNormalPercent(new BigDecimal(normalNum).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        waterPositionStatisticDTO.setMoreForbidPercent(new BigDecimal(moreForbidNum).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        waterPositionStatisticDTO.setMostPositionPercent(new BigDecimal(mostPositionNum).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        return waterPositionStatisticDTO;
    }
}
