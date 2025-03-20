package com.essence.service.impl.alg;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.StringUtil;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.StWaterDispatchService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StWaterDispatchEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterDispatchEtoT;
import com.essence.service.converter.ConverterStWaterDispatchTtoR;
import com.essence.service.utils.DataUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (StWaterDispatch)业务层
 *
 * @author majunjie
 * @since 2023-05-08 14:26:16
 */
@Service
public class StWaterDispatchServiceImpl extends BaseApiImpl<StWaterDispatchEsu, StWaterDispatchEsp, StWaterDispatchEsr, StWaterDispatchDto> implements StWaterDispatchService {

    @Autowired
    private StWaterDispatchDao stWaterDispatchDao;
    @Autowired
    private ConverterStWaterDispatchEtoT converterStWaterDispatchEtoT;
    @Autowired
    private ConverterStWaterDispatchTtoR converterStWaterDispatchTtoR;
    @Autowired
    private StPumpDataDao stPumpDataDao;
    @Autowired
    private StSideGateDao stSideGateDao;
    @Autowired
    private StWaterRateDao stWaterRateDao;
    @Autowired
    private StSnConvertDao stSnConvertDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private GateStationRelatedDao gateStationRelatedDao;
    @Resource
    private StWaterRateLatestDao waterRateLatestDao;

    public StWaterDispatchServiceImpl(StWaterDispatchDao stWaterDispatchDao, ConverterStWaterDispatchEtoT converterStWaterDispatchEtoT, ConverterStWaterDispatchTtoR converterStWaterDispatchTtoR) {
        super(stWaterDispatchDao, converterStWaterDispatchEtoT, converterStWaterDispatchTtoR);
    }

    @Override
    public StWaterDispatchEsr addWaterPlan(StWaterDispatchEsu stWaterDispatchEsu) {
        StWaterDispatchDto stWaterDispatchDto = converterStWaterDispatchEtoT.toBean(stWaterDispatchEsu);
        stWaterDispatchDto.setId(UuidUtil.get32UUIDStr());
        stWaterDispatchDto.setGmtCreate(new Date());
        stWaterDispatchDao.insert(stWaterDispatchDto);
        return converterStWaterDispatchTtoR.toBean(stWaterDispatchDto);
    }

    @Override
    public List<StWaterDispatchFlow> stWaterDispatchFlowList(String stcd) {
        List<StWaterDispatchFlow> list = new ArrayList<>();
        //设置时间为整点时间
        Date endTime = new Date();
        Date startTime = DateUtil.beginOfDay(endTime);
        List<Date> timeSplit = DataUtils.getTimeSplit(startTime, endTime, 60, DateField.MINUTE);
        //2.获取泵站数据
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtil.isNotBlank(stcd)) {
            List<StPumpDataDto> stPumpDataDtos = Optional.ofNullable(stPumpDataDao.selectList(new QueryWrapper<StPumpDataDto>().lambda().eq(StPumpDataDto::getDeviceAddr, stcd).ge(StPumpDataDto::getDate, startTime).le(StPumpDataDto::getDate, endTime).orderByAsc(StPumpDataDto::getDate))).orElse(Lists.newArrayList());
            if (CollectionUtils.isEmpty(stPumpDataDtos)) {
                for (int i = 0; i < timeSplit.size(); i++) {
                    StWaterDispatchFlow stWaterDispatchFlow = new StWaterDispatchFlow();
                    stWaterDispatchFlow.setAddFlowRate("0");
                    stWaterDispatchFlow.setMomentRate("0");
                    stWaterDispatchFlow.setTime(simple.format(timeSplit.get(i)));
                    list.add(stWaterDispatchFlow);
                }
            } else {
                stPumpDataDtos.stream().forEach(x -> x.setFlowRate(StringUtils.isNotBlank(x.getFlowRate()) ? x.getFlowRate() : "0"));
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StWaterDispatchFlow stWaterDispatchFlow = new StWaterDispatchFlow();
                    Date start = timeSplit.get(i);
                    Date end = timeSplit.get(i + 1);
                    Double sum = 0.0;
                    Double flowRate = 0.0;
                    List<StPumpDataDto> collect = stPumpDataDtos.stream().filter(x -> x.getDate().getTime() >= start.getTime() && x.getDate().getTime() <= end.getTime()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        sum = collect.stream().mapToDouble(x -> Double.valueOf(x.getFlowRate())).sum();
                        flowRate = Double.valueOf(collect.get(0).getFlowRate());
                    }
                    stWaterDispatchFlow.setAddFlowRate(sum.toString());
                    stWaterDispatchFlow.setMomentRate(flowRate.toString());
                    stWaterDispatchFlow.setTime(simple.format(timeSplit.get(i)));
                    list.add(stWaterDispatchFlow);
                }
            }
        } else {
            for (int i = 0; i < timeSplit.size(); i++) {
                StWaterDispatchFlow stWaterDispatchFlow = new StWaterDispatchFlow();
                stWaterDispatchFlow.setAddFlowRate("0");
                stWaterDispatchFlow.setMomentRate("0");
                stWaterDispatchFlow.setTime(simple.format(timeSplit.get(i)));
                list.add(stWaterDispatchFlow);
            }
        }
        return list;
    }

    @Override
    public List<StWaterDispatchDP> stWaterDispatchDp(String riverId) {
        List<StWaterDispatchDP> stWaterDispatchDPList = new ArrayList<>();
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "DP").isNotNull(StSideGateDto::getTransferId).eq(StSideGateDto::getRiverId, Integer.valueOf(riverId)));
        if (!CollectionUtils.isEmpty(stSideGateDtos)) {
            for (StSideGateDto stSideGateDto : stSideGateDtos) {
                StWaterDispatchDP stWaterDispatchDP = new StWaterDispatchDP();
                stWaterDispatchDP.setStcd(stSideGateDto.getStcd());
                stWaterDispatchDP.setStnm(stSideGateDto.getStnm());
                stWaterDispatchDPList.add(stWaterDispatchDP);
            }
        }
        return stWaterDispatchDPList;
    }


    @Override
    public List<StWaterDispatchLevelList> stWaterLevelByZB(String riverId) {
        List<StWaterDispatchLevelList> stWaterDispatchLevelLists = new ArrayList<>();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        Date startTime = DateUtil.beginOfDay(endTime);
        //1.查询高水位中水位
        List<StSideGateDto> stSideGateDtosZ = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "DD").eq(StSideGateDto::getRiverId, Integer.valueOf(riverId)).isNotNull(StSideGateDto::getHighWaterLevel))).orElse(Lists.newArrayList());
        List<StSideGateDto> stSideGateDtosB = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "SB").eq(StSideGateDto::getRiverId, Integer.valueOf(riverId)).isNotNull(StSideGateDto::getHighWaterLevel))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stSideGateDtosB)) {
            stSideGateDtosZ.addAll(stSideGateDtosB);
        }
        if (!CollectionUtils.isEmpty(stSideGateDtosZ)) {
            //2.数据首先填充
            stWaterDispatchLevelLists = stSideGateDtosZ.stream().map(x -> {
                StWaterDispatchLevelList stWaterDispatchLevelList = new StWaterDispatchLevelList();
                stWaterDispatchLevelList.setStcdId(x.getId());
                stWaterDispatchLevelList.setStcd(x.getStcd());
                stWaterDispatchLevelList.setLgtd(x.getLgtd());
                stWaterDispatchLevelList.setLttd(x.getLttd());
                stWaterDispatchLevelList.setDType(x.getDType());
                stWaterDispatchLevelList.setDOrder(x.getDOrder());
                stWaterDispatchLevelList.setStnm(x.getStnm());
                stWaterDispatchLevelList.setHighWaterLevel(x.getHighWaterLevel());
                stWaterDispatchLevelList.setMiddleWaterLevel(x.getMiddleWaterLevel());
                stWaterDispatchLevelList.setWaterLevel("0");
                return stWaterDispatchLevelList;
            }).collect(Collectors.toList());
            //3.集中查询水位数据
            List<StSideGateDto> stSideGateDtoListStcd = Optional.ofNullable(stSideGateDtosZ.stream().filter(x -> StringUtil.isNotBlank(x.getStnm())).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(stSideGateDtoListStcd)) {
                List<String> stcdList = stSideGateDtoListStcd.stream().map(x -> x.getStnm()).collect(Collectors.toList());
                //4.查询闸坝关联 流量水位数据
                List<GateStationRelatedDto> stSideRelationDtos = Optional.ofNullable(gateStationRelatedDao.selectList(new QueryWrapper<GateStationRelatedDto>().lambda().in(GateStationRelatedDto::getGateName, stcdList))).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(stSideRelationDtos)) {
                    //水位站数据台账
                    List<GateStationRelatedDto> zz = Optional.ofNullable(stSideRelationDtos.stream().filter(x -> x.getSttp().equals("ZZ")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(zz)) {
                        List<String> stStbprpBList = Optional.ofNullable(zz.stream().map(x -> x.getStcd()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        List<StSnConvertEntity> stSnConvertEntities = Optional.ofNullable(stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().in(StSnConvertEntity::getStcd, stStbprpBList))).orElse(Lists.newArrayList());
                        if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                            List<String> getSnList = stSnConvertEntities.stream().map(x -> x.getSn()).collect(Collectors.toList());
//                            List<StWaterRateEntity> stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).in(StWaterRateEntity::getDid, getSnList))).orElse(Lists.newArrayList());
                            List<StWaterRateLatestDto> stWaterRateLatestDtos = waterRateLatestDao.selectList(new QueryWrapper<>());

                            if (!CollectionUtils.isEmpty(stWaterRateLatestDtos)) {
                                Map<String, List<StWaterRateLatestDto>> didmap = stWaterRateLatestDtos.parallelStream().collect(Collectors.groupingBy(StWaterRateLatestDto::getDid));
                                for (StWaterDispatchLevelList stWaterDispatchLevelList : stWaterDispatchLevelLists) {
                                    if (StringUtil.isNotBlank(stWaterDispatchLevelList.getStnm())) {
                                        List<GateStationRelatedDto> sideRelationDtos = zz.stream().filter(x -> x.getGateName().equals(stWaterDispatchLevelList.getStnm())).collect(Collectors.toList());
                                        if (!CollectionUtils.isEmpty(sideRelationDtos)) {
                                            for (GateStationRelatedDto stSideRelationDto : sideRelationDtos) {
                                                List<StSnConvertEntity> snConvertEntities = stSnConvertEntities.stream().filter(x -> x.getStcd().equals(stSideRelationDto.getStcd())).collect(Collectors.toList());
                                                if (!CollectionUtils.isEmpty(snConvertEntities)) {
                                                    StSnConvertEntity stSnConvertEntity = snConvertEntities.get(0);
                                                    List<StWaterRateLatestDto> stWaterRateEntityList = didmap.get(stSnConvertEntity.getSn());

                                                    if (!CollectionUtils.isEmpty(stWaterRateEntityList)) {
                                                        StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectById(stSideRelationDto.getStcd());
                                                        if (null != stStbprpBEntity) {
                                                            for (StWaterRateLatestDto stWaterRateEntity : stWaterRateEntityList) {
                                                                if (null != stStbprpBEntity.getDtmel()) {
                                                                    BigDecimal multiply = stStbprpBEntity.getDtmel().multiply(new BigDecimal("1000"));
                                                                    stWaterRateEntity.setAddrv(new BigDecimal(StringUtils.isNotBlank(stWaterRateEntity.getAddrv()) ? stWaterRateEntity.getAddrv() : "0").add(multiply).toString());
                                                                }
                                                            }
                                                        }
                                                        stWaterRateEntityList = stWaterRateEntityList.stream().sorted(Comparator.comparing(StWaterRateLatestDto::getCtime).reversed()).collect(Collectors.toList());
                                                        StWaterRateLatestDto stWaterRateEntity = stWaterRateEntityList.get(0);
                                                        if (StringUtil.isNotBlank(stWaterRateEntity.getAddrv())) {
                                                            stWaterDispatchLevelList.setWaterLevel(new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).toString());
                                                        }
                                                    }
                                                }
                                                if (!stWaterDispatchLevelList.getWaterLevel().equals("0")) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //流量站数据
                    List<GateStationRelatedDto> zq = Optional.ofNullable(stSideRelationDtos.stream().filter(x -> x.getSttp().equals("ZQ")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(zq)) {
                        List<String> stStbprpBList = Optional.ofNullable(zq.stream().map(x -> x.getStcd().substring(2)).collect(Collectors.toList())).orElse(Lists.newArrayList());
//                        List<StWaterRateEntity> stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).in(StWaterRateEntity::getDid, stStbprpBList))).orElse(Lists.newArrayList());
                        List<StWaterRateLatestDto> stWaterRateLatestDtos = waterRateLatestDao.selectList(new QueryWrapper<>());
                        if (!CollectionUtils.isEmpty(stWaterRateLatestDtos)) {
                            Map<String, List<StWaterRateLatestDto>> didmap = stWaterRateLatestDtos.parallelStream().collect(Collectors.groupingBy(StWaterRateLatestDto::getDid));
                            for (StWaterDispatchLevelList stWaterDispatchLevelList : stWaterDispatchLevelLists) {
                                if (StringUtil.isNotBlank(stWaterDispatchLevelList.getStnm()) && stWaterDispatchLevelList.getWaterLevel().equals("0")) {
                                    //获取关联关系
                                    List<GateStationRelatedDto> stSideRelationDtos1 = zq.stream().filter(x -> x.getGateName().equals(stWaterDispatchLevelList.getStnm())).collect(Collectors.toList());
                                    if (!CollectionUtils.isEmpty(stSideRelationDtos1)) {
                                        for (GateStationRelatedDto stSideRelationDto : stSideRelationDtos1) {
                                            //水位数据添加高程
                                            StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectById(stSideRelationDto.getStcd());
//                                            List<StWaterRateEntity> stWaterRateEntityList = stRainDateDtos.stream().filter(x -> x.getDid().equals(stSideRelationDto.getStcd().substring(2))).collect(Collectors.toList());
                                            List<StWaterRateLatestDto> stWaterRateEntityList = didmap.get(stSideRelationDto.getStcd().substring(2));
                                            if (!CollectionUtils.isEmpty(stWaterRateEntityList)) {
                                                stWaterRateEntityList = stWaterRateEntityList.stream().sorted(Comparator.comparing(StWaterRateLatestDto::getCtime).reversed()).collect(Collectors.toList());
                                                StWaterRateLatestDto stWaterRateEntity = stWaterRateEntityList.get(0);
                                                if (StringUtil.isNotBlank(stWaterRateEntity.getMomentRiverPosition())) {
                                                    if (null != stStbprpBEntity && null != stStbprpBEntity.getDtmel()) {
                                                        stWaterDispatchLevelList.setWaterLevel(new BigDecimal(StringUtils.isNotBlank(stWaterRateEntity.getMomentRiverPosition()) ? stWaterRateEntity.getMomentRiverPosition() : "0").add(stStbprpBEntity.getDtmel()).toString());
                                                    } else {
                                                        stWaterDispatchLevelList.setWaterLevel(stWaterRateEntity.getMomentRiverPosition());
                                                    }
                                                }
                                            }
                                            if (!stWaterDispatchLevelList.getWaterLevel().equals("0")) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return stWaterDispatchLevelLists;
    }

    @Override
    public List<StWaterDispatchLevel> stWaterLevelByStcd(Integer stcdId) {
        List<StWaterDispatchLevel> list = new ArrayList<>();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        Date startTime = DateUtil.beginOfDay(endTime);
        List<Date> timeSplit = DataUtils.getTimeSplit(startTime, endTime, 60, DateField.MINUTE);
        //1.获取闸坝高中水位线
        StSideGateDto stSideGateDto = stSideGateDao.selectById(stcdId);
        if (null == stSideGateDto) {
            return list;
        }
        List<StWaterRateEntity> stRainDateDtos = new ArrayList<>();
        //2.查询闸坝关联水位站流量站
        List<GateStationRelatedDto> stSideRelationDtos = Optional.ofNullable(gateStationRelatedDao.selectList(new QueryWrapper<GateStationRelatedDto>().lambda().eq(GateStationRelatedDto::getGateName, stSideGateDto.getStnm()))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stSideRelationDtos)) {
            String type = "1";
            for (GateStationRelatedDto stSideRelationDto : stSideRelationDtos) {
                //3.水位站数据
                //水位数据加高程
                StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectById(stSideRelationDto.getStcd());
                if (stSideRelationDto.getSttp().equals("ZZ")) {
                    List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stSideRelationDto.getStcd()));
                    if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                        type = "1";
                        stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(Lists.newArrayList());
                        if (!CollectionUtils.isEmpty(stRainDateDtos) && null != stStbprpBEntity) {
                            for (StWaterRateEntity stRainDateDto : stRainDateDtos) {
                                if (null != stStbprpBEntity.getDtmel()) {
                                    BigDecimal multiply = stStbprpBEntity.getDtmel().multiply(new BigDecimal("1000"));
                                    stRainDateDto.setAddrv(new BigDecimal(StringUtils.isNotBlank(stRainDateDto.getAddrv()) ? stRainDateDto.getAddrv() : "0").add(multiply).toString());
                                }
                            }
                        }
                    }
                } else {
                    //4流量站数据
                    type = "2";
                    stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stSideRelationDto.getStcd().substring(2)))).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(stRainDateDtos) && null != stStbprpBEntity) {
                        for (StWaterRateEntity stRainDateDto : stRainDateDtos) {
                            if (null != stStbprpBEntity.getDtmel()) {
                                stRainDateDto.setMomentRiverPosition(new BigDecimal(StringUtils.isNotBlank(stRainDateDto.getMomentRiverPosition()) ? stRainDateDto.getMomentRiverPosition() : "0").add(stStbprpBEntity.getDtmel()).toString());
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                    break;
                }
            }
            //null 直接填充
            if (CollectionUtils.isEmpty(stRainDateDtos)) {
                for (int i = 0; i < timeSplit.size(); i++) {
                    StWaterDispatchLevel stWaterDispatchLevel = new StWaterDispatchLevel();
                    stWaterDispatchLevel.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                    stWaterDispatchLevel.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                    stWaterDispatchLevel.setWaterLevel("0");
                    stWaterDispatchLevel.setTime(simple.format(timeSplit.get(i)));
                    list.add(stWaterDispatchLevel);
                }
            } else {
                //5.时间转换
                List<StWaterRateRelation> stWaterRateRelationList = new ArrayList<>();
                //6.水位站数据填充
                if (type.equals("1")) {
                    try {
                        for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                            StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                            stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                            stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                            stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                            stWaterRateRelationList.add(stWaterRateRelation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //水位站数据
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchLevel stWaterDispatchLevel = new StWaterDispatchLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = stWaterRateRelationList.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                        }
                        stWaterDispatchLevel.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                        stWaterDispatchLevel.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                        stWaterDispatchLevel.setWaterLevel(new BigDecimal(String.valueOf(sum)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).toString());
                        stWaterDispatchLevel.setTime(simple.format(timeSplit.get(i)));
                        list.add(stWaterDispatchLevel);
                    }
                } else {
                    try {
                        for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                            StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                            stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                            stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                            stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                            stWaterRateRelationList.add(stWaterRateRelation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //6.流量站数据填充
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchLevel stWaterDispatchLevel = new StWaterDispatchLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                        }
                        stWaterDispatchLevel.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                        stWaterDispatchLevel.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                        stWaterDispatchLevel.setWaterLevel(String.valueOf(sum));
                        stWaterDispatchLevel.setTime(simple.format(timeSplit.get(i)));
                        list.add(stWaterDispatchLevel);
                    }
                }
            }
            //7.未查询到闸坝关联水位站流量站
        } else {
            for (int i = 0; i < timeSplit.size(); i++) {
                StWaterDispatchLevel stWaterDispatchLevel = new StWaterDispatchLevel();
                stWaterDispatchLevel.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                stWaterDispatchLevel.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                stWaterDispatchLevel.setWaterLevel("0");
                stWaterDispatchLevel.setTime(simple.format(timeSplit.get(i)));
                list.add(stWaterDispatchLevel);
            }
        }
        return list;
    }

    @Override
    public List<StWaterDispatchZBLevel> stWaterLevelZBByStcd(StWaterDispatchZBLevelQuery stWaterDispatchZBLevelQuery) {
        List<StWaterDispatchZBLevel> stWaterDispatchZBLevelList = new ArrayList<>();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        Date startTime = DateUtil.beginOfDay(endTime);
        List<Date> timeSplit = DataUtils.getTimeSplit(startTime, endTime, 60, DateField.MINUTE);
        StSideGateDto stSideGateDto = stSideGateDao.selectById(stWaterDispatchZBLevelQuery.getId());
        if (null == stSideGateDto) {
            return stWaterDispatchZBLevelList;
        }
        List<GateStationRelatedDto> gateStationRelatedDtoList = gateStationRelatedDao.selectList(new QueryWrapper<GateStationRelatedDto>().lambda().eq(GateStationRelatedDto::getGateName, stSideGateDto.getStnm()).ne(GateStationRelatedDto::getSttp, "CA"));

        if (!CollectionUtils.isEmpty(gateStationRelatedDtoList)) {
            List<GateStationRelatedDto> sideRelationDtosQ = Optional.ofNullable(gateStationRelatedDtoList.stream().filter(x -> x.getStreamLoc().equals("0")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            List<GateStationRelatedDto> sideRelationDtosH = Optional.ofNullable(gateStationRelatedDtoList.stream().filter(x -> x.getStreamLoc().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //获取闸前水位
            if (!CollectionUtils.isEmpty(sideRelationDtosQ)) {
                if (!CollectionUtils.isEmpty(sideRelationDtosH)) {
                    stWaterDispatchZBLevelList = selectStWaterRateRelations(sideRelationDtosQ, sideRelationDtosH, startTime, endTime, timeSplit);
                } else {
                    stWaterDispatchZBLevelList = selectStWaterRateRelation(sideRelationDtosQ, startTime, endTime, timeSplit);
                }
            } else {
                //判断下游
                if (!CollectionUtils.isEmpty(sideRelationDtosH)) {
                    stWaterDispatchZBLevelList = selectStWaterRateRelationH(sideRelationDtosH, startTime, endTime, timeSplit);
                }
            }
        }

        return stWaterDispatchZBLevelList;
    }

    private List<StWaterDispatchZBLevel> selectStWaterRateRelationH(List<GateStationRelatedDto> sideRelationDtosH, Date startTime, Date endTime, List<Date> timeSplit) {

        List<StWaterDispatchZBLevel> stRainDateDtoss = new ArrayList<>();
        try {
            List<StWaterRateEntity> stRainDateDtos = new ArrayList<>();
            String type = "1";
            String stcdH  = "";
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (GateStationRelatedDto stSideRelationDto : sideRelationDtosH) {
                //3.水位站数据
                if (stSideRelationDto.getSttp().equals("ZZ")) {
                    List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stSideRelationDto.getStcd()));
                    if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                        type = "1";
                        stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(Lists.newArrayList());
                    }
                } else {
                    //4流量站数据
                    type = "2";
                    stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stSideRelationDto.getStcd().substring(2)))).orElse(Lists.newArrayList());
                }
                if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                    stcdH = stSideRelationDto.getStcd();
                    break;
                }
            }

            if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                String h = "0";
                if (StringUtils.isNotBlank(stcdH)) {
                    StStbprpBEntity stStbprpBEntityQ = stStbprpBDao.selectById(stcdH);
                    h = stStbprpBEntityQ.getDtmel() != null ? stStbprpBEntityQ.getDtmel().toString() : "0";
                }
                //5.时间转换
                List<StWaterRateRelation> stWaterRateRelationList = new ArrayList<>();
                if (type.equals("1")) {
                    //水位站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationList.add(stWaterRateRelation);
                    }
                    //水位站数据
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                        }
                        stWaterDispatchZBLevel.setWaterLevelQ("0.0");
                        stWaterDispatchZBLevel.setWaterLevelH(new BigDecimal(String.valueOf(sum)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).add(new BigDecimal(h)).toString());
                        stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                        stRainDateDtoss.add(stWaterDispatchZBLevel);
                    }
                } else {
                    //流量站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationList.add(stWaterRateRelation);
                    }
                    //6.流量站数据填充
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                        }
                        stWaterDispatchZBLevel.setWaterLevelQ("0.0");
                        stWaterDispatchZBLevel.setWaterLevelH(String.valueOf(sum+Double.valueOf(h)));
                        stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                        stRainDateDtoss.add(stWaterDispatchZBLevel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stRainDateDtoss;
    }

    private List<StWaterDispatchZBLevel> selectStWaterRateRelations(List<GateStationRelatedDto> stSideRelationDtoListQ, List<GateStationRelatedDto> stSideRelationDtoListH, Date startTime, Date endTime, List<Date> timeSplit) {
        List<StWaterDispatchZBLevel> stRainDateDtoss = new ArrayList<>();
        try {
            List<StWaterRateEntity> stRainDateDtos = new ArrayList<>();
            List<StWaterRateEntity> stRainDateDtoH = new ArrayList<>();
            String type = "1";
            String typeH = "1";
            String stcdQ = "";
            String stcdH = "";
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //1.获取闸前数据
            for (GateStationRelatedDto stSideRelationDto : stSideRelationDtoListQ) {
                //水位站数据
                if (stSideRelationDto.getSttp().equals("ZZ")) {
                    List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stSideRelationDto.getStcd()));
                    if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                        type = "1";
                        stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(Lists.newArrayList());
                    }
                } else {
                    //流量站数据
                    type = "2";
                    stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stSideRelationDto.getStcd().substring(2)))).orElse(Lists.newArrayList());
                }
                if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                    stcdQ = stSideRelationDto.getStcd();
                    break;
                }
            }
            //2.获取闸后数据
            for (GateStationRelatedDto stSideRelationDto : stSideRelationDtoListH) {
                //水位站数据
                if (stSideRelationDto.getSttp().equals("ZZ")) {
                    List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stSideRelationDto.getStcd()));
                    if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                        typeH = "1";
                        stRainDateDtoH = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(Lists.newArrayList());
                    }
                } else {
                    //流量站数据
                    typeH = "2";
                    stRainDateDtoH = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stSideRelationDto.getStcd().substring(2)))).orElse(Lists.newArrayList());
                }
                if (!CollectionUtils.isEmpty(stRainDateDtoH)) {
                    stcdH = stSideRelationDto.getStcd();
                    break;
                }
            }
            //5.时间转换
            List<StWaterRateRelation> stWaterRateRelationList = new ArrayList<>();
            String q = "0";
            String h = "0";
            if (StringUtils.isNotBlank(stcdQ)) {
                StStbprpBEntity stStbprpBEntityQ = stStbprpBDao.selectById(stcdQ);
                q = stStbprpBEntityQ.getDtmel() != null ? stStbprpBEntityQ.getDtmel().toString() : "0";
            }
            if (StringUtils.isNotBlank(stcdH)) {
                StStbprpBEntity stStbprpBEntityH = stStbprpBDao.selectById(stcdH);
                h = stStbprpBEntityH.getDtmel() != null ? stStbprpBEntityH.getDtmel().toString() : "0";
            }
            if (type.equals("1")) {
                //闸前水位站数据格式转化
                for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                    StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                    stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                    stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                    stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                    stWaterRateRelationList.add(stWaterRateRelation);
                }
                //判断闸后水位站数据格式转化
                List<StWaterRateRelation> stWaterRateRelationListH = new ArrayList<>();
                if (typeH.equals("1")) {
                    //水位站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtoH) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationListH.add(stWaterRateRelation);
                    }
                } else {
                    //闸后流量站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtoH) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationListH.add(stWaterRateRelation);
                    }
                }
                //水位站数据
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                    Date start = timeSplit.get(i);
                    Date end = timeSplit.get(i + 1);
                    Double sum = 0.0;
                    if (!CollectionUtils.isEmpty(stWaterRateRelationList)) {
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                        }
                    }
                    stWaterDispatchZBLevel.setWaterLevelQ(new BigDecimal(String.valueOf(sum)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).add(new BigDecimal(q)).toString());
                    stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                    if (!CollectionUtils.isEmpty(stWaterRateRelationListH)) {
                        if (typeH.equals("1")) {
                            Double sumH = 0.0;
                            List<StWaterRateRelation> collectS = stWaterRateRelationListH.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collectS)) {
                                List<StWaterRateRelation> collect1 = collectS.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                                sumH = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                            }
                            stWaterDispatchZBLevel.setWaterLevelH(new BigDecimal(String.valueOf(sumH)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).add(new BigDecimal(h)).toString());
                        } else {
                            Double sumH = 0.0;
                            List<StWaterRateRelation> collects = stWaterRateRelationListH.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collects)) {
                                List<StWaterRateRelation> collect1 = collects.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                                sumH = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                            }
                            stWaterDispatchZBLevel.setWaterLevelH(String.valueOf(sumH+Double.valueOf(h)));
                        }
                    }
                    stRainDateDtoss.add(stWaterDispatchZBLevel);
                }
            } else {
                //闸前流量站数据
                for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                    StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                    stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                    stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                    stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                    stWaterRateRelationList.add(stWaterRateRelation);
                }
                //判断闸后水位
                List<StWaterRateRelation> stWaterRateRelationListH = new ArrayList<>();
                if (typeH.equals("1")) {
                    //水位站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtoH) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationListH.add(stWaterRateRelation);
                    }
                } else {
                    //闸后流量站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtoH) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationListH.add(stWaterRateRelation);
                    }
                }
                //6.流量站数据填充
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                    Date start = timeSplit.get(i);
                    Date end = timeSplit.get(i + 1);
                    Double sum = 0.0;
                    if (!CollectionUtils.isEmpty(stWaterRateRelationList)) {
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                        }
                    }
                    stWaterDispatchZBLevel.setWaterLevelQ(String.valueOf(sum+Double.valueOf(q)));
                    stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                    if (!CollectionUtils.isEmpty(stWaterRateRelationListH)) {
                        if (typeH.equals("1")) {
                            Double sumH = 0.0;
                            List<StWaterRateRelation> collectS = stWaterRateRelationListH.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collectS)) {
                                List<StWaterRateRelation> collect1 = collectS.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                                sumH = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                            }
                            stWaterDispatchZBLevel.setWaterLevelH(new BigDecimal(String.valueOf(sumH)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).add(new BigDecimal(h)).toString());
                        } else {
                            Double sumH = 0.0;
                            List<StWaterRateRelation> collects = stWaterRateRelationListH.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collects)) {
                                List<StWaterRateRelation> collect1 = collects.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                                sumH = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                            }
                            stWaterDispatchZBLevel.setWaterLevelH(String.valueOf(sumH+Double.valueOf(h)));
                        }
                    }
                    stRainDateDtoss.add(stWaterDispatchZBLevel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stRainDateDtoss;
    }

    private List<StWaterDispatchZBLevel> selectStWaterRateRelation(List<GateStationRelatedDto> stSideRelationDtoList, Date startTime, Date endTime, List<Date> timeSplit) {

        List<StWaterDispatchZBLevel> stRainDateDtoss = new ArrayList<>();
        try {
            List<StWaterRateEntity> stRainDateDtos = new ArrayList<>();
            String type = "1";
            String stcdQ = "";
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (GateStationRelatedDto stSideRelationDto : stSideRelationDtoList) {
                //3.水位站数据
                if (stSideRelationDto.getSttp().equals("ZZ")) {
                    List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stSideRelationDto.getStcd()));
                    if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                        type = "1";
                        stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(Lists.newArrayList());
                    }
                } else {
                    //4流量站数据
                    type = "2";
                    stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stSideRelationDto.getStcd().substring(2)))).orElse(Lists.newArrayList());
                }
                if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                    stcdQ = stSideRelationDto.getStcd();
                    break;
                }
            }

            if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                String q = "0";
                if (StringUtils.isNotBlank(stcdQ)) {
                    StStbprpBEntity stStbprpBEntityQ = stStbprpBDao.selectById(stcdQ);
                    q = stStbprpBEntityQ.getDtmel() != null ? stStbprpBEntityQ.getDtmel().toString() : "0";
                }
                //5.时间转换
                List<StWaterRateRelation> stWaterRateRelationList = new ArrayList<>();
                if (type.equals("1")) {
                    //水位站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(simple.format(DateUtil.parse(stWaterRateEntity.getCtime(), "yyyy/MM/dd HH:mm:ss"))));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationList.add(stWaterRateRelation);
                    }
                    //水位站数据
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                        }
                        stWaterDispatchZBLevel.setWaterLevelH("0.0");
                        stWaterDispatchZBLevel.setWaterLevelQ(new BigDecimal(String.valueOf(sum)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).add(new BigDecimal(q)).toString());
                        stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                        stRainDateDtoss.add(stWaterDispatchZBLevel);
                    }
                } else {
                    //流量站数据
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelationList.add(stWaterRateRelation);
                    }
                    //6.流量站数据填充
                    for (int i = 0; i < timeSplit.size() - 1; i++) {
                        StWaterDispatchZBLevel stWaterDispatchZBLevel = new StWaterDispatchZBLevel();
                        Date start = timeSplit.get(i);
                        Date end = timeSplit.get(i + 1);
                        Double sum = 0.0;
                        List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                            sum = Double.valueOf(StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                        }
                        stWaterDispatchZBLevel.setWaterLevelH("0.0");
                        stWaterDispatchZBLevel.setWaterLevelQ(String.valueOf(sum+Double.valueOf(q)));
                        stWaterDispatchZBLevel.setTime(simple.format(timeSplit.get(i)));
                        stRainDateDtoss.add(stWaterDispatchZBLevel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stRainDateDtoss;
    }

}