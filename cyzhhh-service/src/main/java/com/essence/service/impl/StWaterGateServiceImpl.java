package com.essence.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.exception.BusinessException;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.alg.StCaseResDto;
import com.essence.dao.entity.water.StQpModelDto;
import com.essence.dao.entity.water.StWaterRiskForecastDto;
import com.essence.interfaces.api.StSideGateService;
import com.essence.interfaces.api.StWaterGateService;
import com.essence.interfaces.dot.WaterInfoDto;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.model.StWaterGateEsr;
import com.essence.interfaces.model.StWaterGateEsu;
import com.essence.interfaces.model.StWaterRateRelation;
import com.essence.interfaces.param.StWaterGateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterGateEtoT;
import com.essence.service.converter.ConverterStWaterGateTtoR;
import com.essence.service.utils.DataUtils;
import com.essence.service.utils.MapReduceUtil;
import com.google.common.collect.Maps;
import com.google.common.math.DoubleMath;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (StWaterGate)业务层
 *
 * @author majunjie
 * @since 2023-04-20 15:36:26
 */
@Service
@Slf4j
public class StWaterGateServiceImpl extends BaseApiImpl<StWaterGateEsu, StWaterGateEsp, StWaterGateEsr, StWaterGateDto> implements StWaterGateService {
    @Autowired
    private StWaterRateDao stWaterRateDao;
    @Autowired
    private StWaterRateLatestDao stWaterRateLatestDao;
    @Autowired
    private StSnConvertDao stSnConvertDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private StSectionModelDao stSectionModelDao;
    @Autowired
    private StWaterRiskForecastDao stWaterRiskForecastDao;
    @Autowired
    private StQpModelDao stQpModelDao;
    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private StCaseResDao stCaseResDao;
    @Autowired
    StRainDateDao stRainDateDao;
    @Autowired
    StCaseBaseInfoDao stCaseBaseInfoDao;
    @Autowired
    GateStationRelatedDao gateStationRelatedDao;
    @Autowired
    StSideGateDao stSideGateDao;
    @Autowired
    StWaterRiskForecastGateDao stWaterRiskForecastGateDao;
    @Autowired
    private StSideGateService stSideGateService;
    @Autowired
    RedisService redisService;


    @Autowired
    private ConverterStWaterGateEtoT converterStWaterGateEtoT;
    @Autowired
    private ConverterStWaterGateTtoR converterStWaterGateTtoR;

    @Resource
    private StPumpDataDao pumpDataDao;

    public StWaterGateServiceImpl(StWaterGateDao stWaterGateDao, ConverterStWaterGateEtoT converterStWaterGateEtoT, ConverterStWaterGateTtoR converterStWaterGateTtoR) {
        super(stWaterGateDao, converterStWaterGateEtoT, converterStWaterGateTtoR);
    }

    @Override
    public List<RiverRiskPoint> selectRiverRiskPoint() {
        List<StSectionModelDto> allStSectionModelList = null != redisService.getCacheObject("AllStSectionModelList") ? redisService.getCacheObject("AllStSectionModelList") : stSectionModelDao.selectList(new QueryWrapper<>());
        List<StSnConvertEntity> allStSnConvertList = null != redisService.getCacheObject("AllStSnConvertList") ? redisService.getCacheObject("AllStSnConvertList") : stSnConvertDao.selectList(new QueryWrapper<>());

        List<StWaterRiskForecastDto> allStWaterRiskForecastList = null != redisService.getCacheObject("AllStWaterRiskForecastList") ? redisService.getCacheObject("AllStWaterRiskForecastList") : stWaterRiskForecastDao.selectList(new QueryWrapper<>());
        Map<String, List<StSnConvertEntity>> stSnMap = allStSnConvertList.stream().collect(Collectors.groupingBy(StSnConvertEntity::getStcd));
        Map<String, List<StSectionModelDto>> stSectionModelDtoMap = allStSectionModelList.stream().collect(Collectors.groupingBy(StSectionModelDto::getSectionName));
        Map<String, List<StWaterRiskForecastDto>> stWaterRiskForecastMap = allStWaterRiskForecastList.stream().collect(Collectors.groupingBy(StWaterRiskForecastDto::getStcd));
        List<StStbprpBEntity> stStbprpBEntities = null != redisService.getCacheObject("stvprp") ? redisService.getCacheObject("stvprp") : stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().in("sttp", "ZQ", "ZZ"));

        return null != redisService.getCacheObject("selectRiverRiskPoint") ? redisService.getCacheObject("selectRiverRiskPoint") : getRiverRiskPoints(stSnMap, stSectionModelDtoMap, stStbprpBEntities, stWaterRiskForecastMap);
    }

    @Override
    public List<Map<String, Object>> selectForecastWaterLevel(String caseId) {

        List<StQpModelDto> allStQpModelList = null != redisService.getCacheObject("AllStQpModelList") ? redisService.getCacheObject("AllStQpModelList") : stQpModelDao.selectList(new QueryWrapper<StQpModelDto>().orderByAsc("qp_id"));

        List<StCaseResDto> allStCaseResList = null != redisService.getCacheObject("AllStCaseResList" + caseId) ? redisService.getCacheObject("AllStCaseResList" + caseId) : stCaseResDao.selectList(new QueryWrapper<StCaseResDto>().select("section_name", "river_z", "step").eq("case_id", caseId));
        Map<Integer, List<StCaseResDto>> stCaseResDtoMap = allStCaseResList.stream().collect(Collectors.groupingBy(StCaseResDto::getStep));
        List<Map.Entry<Integer, List<StCaseResDto>>> entryList = new ArrayList<>();
        stCaseResDtoMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(integerListEntry -> {
            entryList.add(integerListEntry);
        });
        List<Map<String, Object>> maps = MapReduceUtil.handleList(entryList, entries -> {
            List<Map<String, Object>> list = Lists.newArrayList();
            entries.forEach(entry -> {
                Map<String, Object> map = new HashMap<>();

                Integer key = entry.getKey();
                map.put("step", key);
                List<StCaseResDto> value = entry.getValue();
                Map<String, StCaseResDto> sectionNameMap = value.stream().collect(Collectors.toMap(StCaseResDto::getSectionName, a -> a, (k1, k2) -> k1));
                Map<Integer, Object> stepResult = Maps.newHashMap();
                Double lastValue = null;
                for (StQpModelDto stQpModelDto : allStQpModelList) {
                    Map<String, Object> stepmap = new HashMap<>();
                    Integer qpId = stQpModelDto.getQpId();
                    String name = stQpModelDto.getName();
                    List<String> sectionNameList = split(name, ",");
                    if (sectionNameList.size() == 2) {
//                        Double mean = DoubleMath.mean(Double.valueOf(sectionNameMap.get(sectionNameList.get(0)).getRiverZ()), Double.valueOf(sectionNameMap.get(sectionNameList.get(1)).getRiverZ()));

                        StCaseResDto compare = compare((a, b) -> {
                            a.setRiverZ(String.valueOf(DoubleMath.mean(Double.valueOf(a.getRiverZ()), Double.valueOf(b.getRiverZ()))));
                            return a;
                        }, sectionNameMap.get(sectionNameList.get(0)), sectionNameMap.get(sectionNameList.get(1)));
                        if (compare != null) {
                            lastValue = Double.valueOf(compare.getRiverZ());
                            stepmap.put("value", Double.valueOf(compare.getRiverZ()));
                        } else {
                            stepmap.put("value", lastValue);
                        }

                    }
                    if (sectionNameList.size() == 1) {
                        String sectionName = sectionNameList.get(0);
                        StCaseResDto stCaseResDto = sectionNameMap.get(sectionName);
                        String riverZ = stCaseResDto.getRiverZ();
                        Double aDouble = Double.valueOf(riverZ);
                        lastValue = aDouble;
                        stepmap.put("value", aDouble);
                    }
                    stepmap.put("id", qpId);
//                    String sectionId = stQpModelDto.getSectionId();
//                    Integer riverId = stQpModelDto.getRiverId();

                    stepResult.put(qpId, stepmap);
                }
                map.put("stepResult", stepResult);
                list.add(map);
            });
            return list;
        });

        return maps;
    }


    public static <T> T compare(BinaryOperator<T> comparable, T... objects) {
        T res = null;
        for (T object : objects) {
            res = (res == null ? object : (object == null ? res : comparable.apply(res, object)));
        }
        return res;
    }

    @Override
    public WaterSituationCountVo getWaterSituation(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        if (StrUtil.isEmpty(stCaseBaseInfoEsu.getId())) {
            throw new BusinessException("案件id不能为空! 字段: id 类型: String");
        }
        QueryWrapper<StCaseBaseInfoDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", stCaseBaseInfoEsu.getId());
        StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectOne(queryWrapper);
        List<StSectionModelDto> allStSectionModelList = stSectionModelDao.selectList(new QueryWrapper<>());
        List<StSnConvertEntity> allStSnConvertList = stSnConvertDao.selectList(new QueryWrapper<>());
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().in("sttp", "ZQ", "ZZ"));
        Map<String, List<StSnConvertEntity>> stSnMap = allStSnConvertList.stream().collect(Collectors.groupingBy(StSnConvertEntity::getStcd));
        Map<String, List<StSectionModelDto>> stSectionModelDtoMap = allStSectionModelList.stream().collect(Collectors.groupingBy(StSectionModelDto::getSectionName));

        return getWaterSituation(stCaseBaseInfoDto, stSnMap, stSectionModelDtoMap, stStbprpBEntities);
    }

    @SneakyThrows
    @Override
    public List<RainSituationVo> getRainSituation(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        List<RainSituationVo> rainSituationVos = new ArrayList<>();
        // 异常传参
        if (StrUtil.isEmpty(stCaseBaseInfoEsu.getId())) {
            throw new BusinessException("案件id不能为空! 字段: id 类型: String");
        }
        QueryWrapper<StCaseBaseInfoDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", stCaseBaseInfoEsu.getId());
        StCaseBaseInfoDto modelCase = stCaseBaseInfoDao.selectOne(queryWrapper);
        //雨量站台账数据
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").eq(StStbprpBEntity::getIsModelUsed, 1).isNotNull(StStbprpBEntity::getSectionName).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            stStbprpBEntityList.forEach(stStbprpBEntity -> {
                List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(new QueryWrapper<StRainDateDto>().lambda().ge(StRainDateDto::getDate, modelCase.getPreHotTime()).le(StRainDateDto::getDate, modelCase.getForecastStartTime()).eq(StRainDateDto::getStationId, stStbprpBEntity.getSectionName()));
                BigDecimal sumRain = new BigDecimal(0);
                for (StRainDateDto stRainDateDto : stRainDateDtos) {
                    if (StrUtil.isNotEmpty(stRainDateDto.getHhRain())) {
                        sumRain = sumRain.add(BigDecimal.valueOf(Double.valueOf(stRainDateDto.getHhRain())));
                    }
                }
                RainSituationVo rainSituationVo = new RainSituationVo();
                rainSituationVo.setStnm(stStbprpBEntity.getStnm());
                rainSituationVo.setArea(stStbprpBEntity.getArea());
                rainSituationVo.setStreet(stStbprpBEntity.getStreet());
                rainSituationVo.setRain(sumRain);
                rainSituationVos.add(rainSituationVo);
            });
        }
        List<RainSituationVo> collect = rainSituationVos.stream().sorted(Comparator.comparing(RainSituationVo::getRain).reversed()).collect(Collectors.toList());
        return collect;
    }

    @SneakyThrows
    @Override
    public List<RiverRiskPointPC> selectRiverRiskPointPC() {
        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RiverRiskPointPC> resList = new ArrayList<>();
        // 闸坝泵站关联关系 因为查闸前水位 只查询上游
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZZ", "ZQ");
        queryWrapper.eq("stream_loc", "0");
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        Map<String, List<GateStationRelatedDto>> stCollect = gateStationRelatedDtos.stream().collect(Collectors.groupingBy(GateStationRelatedDto::getGateName));
        // 闸坝基础信息
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(new QueryWrapper<>());
        Map<String, List<StSideGateDto>> gateMap = stSideGateDtos.stream().collect(Collectors.groupingBy(StSideGateDto::getStnm));
        // 水位站流量站基础信息
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> stbMap = stStbprpBEntities.stream().collect(Collectors.groupingBy(StStbprpBEntity::getStcd));
        // 取实时水位
        List<StWaterRateLatestDto> stWaterRates = stWaterRateLatestDao.selectList(new QueryWrapper());
        Map<String, List<StWaterRateLatestDto>> stWaterRateMap = stWaterRates.stream().collect(Collectors.groupingBy(StWaterRateLatestDto::getDid));
        for (Map.Entry<String, List<GateStationRelatedDto>> stEntry : stCollect.entrySet()) {
            // 站点名称
            String stnm = stEntry.getKey();
            if (gateMap.get(stnm) == null){
                continue;
            }
            StSideGateDto stSideGate = gateMap.get(stnm).get(0);
            String sttp = stSideGate.getSttp();
            // 警戒水位
            BigDecimal wrz = stSideGate.getWrz();
            // 最高水位
            BigDecimal bhtz = stSideGate.getBhtz();
            // 查询水位站流量站实时水位实时水深, 都有数据的时候, 优先取水位站, 其次取流量站
            List<GateStationRelatedDto> stList = stEntry.getValue();
            boolean haveWaterStLevel = false;
            BigDecimal momentDepth = BigDecimal.ZERO;
            Date moment = new Date();
            // 河底高程
            BigDecimal bottom = null;
            // 左岸高程
            BigDecimal left = null;
            // 右岸高程
            BigDecimal right = null;
            for (GateStationRelatedDto st : stList) {
                if ("ZZ".equals(st.getSttp())) {
                    // 查询水位站断面数据
                    List<StStbprpBEntity> stStbprpBEntities1 = stbMap.get(st.getStcd());
                    if (CollUtil.isNotEmpty(stStbprpBEntities1 )){
                        if (stbMap.get(st.getStcd()) == null){
                            continue;
                        }
                        if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                            QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                            queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                            StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                            bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                            left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                            right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                        }

                        // 编码转换
                        QueryWrapper<StSnConvertEntity> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("stcd", st.getStcd());
                        StSnConvertEntity stSnConvertEntity = stSnConvertDao.selectOne(queryWrapper2);
                        if(null != stSnConvertEntity) {
                            List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(stSnConvertEntity.getSn());
                            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                                StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                                String addr = stWaterRateEntity.getAddrv();
                                if (StrUtil.isNotEmpty(addr)) {
                                    momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP);
                                    moment = zzFormat.parse(stWaterRateEntity.getCtime());
                                    haveWaterStLevel = true;
                                }
                            }
                        }
                    }
                } else if ("ZQ".equals(st.getSttp()) && !haveWaterStLevel) {
                    // 查询流量站断面数据
                    List<StStbprpBEntity> stStbprpBEntities1 = stbMap.get(st.getStcd());
                    if (CollUtil.isNotEmpty(stStbprpBEntities1 )){
                        if (stbMap.get(st.getStcd()) == null){
                            continue;
                        }
                        if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                            QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                            queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                            StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                            bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                            left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                            right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                        }
                        List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(st.getStcd().substring(2));
                        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                            StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                            String addr = stWaterRateEntity.getMomentRiverPosition();
                            if (StrUtil.isNotEmpty(addr)) {
                                momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).setScale(3, BigDecimal.ROUND_HALF_UP);
                                moment = zqFormat.parse(stWaterRateEntity.getCtime());
                            }
                        }
                    }

                }
                if (CollUtil.isEmpty(stbMap.get(st.getStcd()))){
                    bottom = BigDecimal.ZERO;
                }else {
                    bottom = null == stbMap.get(st.getStcd()).get(0).getDtmel() ? BigDecimal.ZERO : stbMap.get(st.getStcd()).get(0).getDtmel();
                }
            }
            BigDecimal momentLevel = null;
            if (null != bottom) {
                momentLevel = momentDepth.add(bottom);
            }
            String state = "";
            BigDecimal fromWaring = null;
            BigDecimal fromHighest = null;
            BigDecimal fromOverFlow = null;
            if (null != wrz && null != momentLevel) {
                fromWaring = momentLevel.subtract(wrz);
            }
            if (null != bhtz && null != momentLevel) {
                fromHighest = momentLevel.subtract(bhtz);
            }
            if (null != left && null != right && null != momentLevel) {
                fromOverFlow = momentLevel.subtract(left.min(right));
            }
            if (null != left && null != right && null != momentLevel && (momentLevel.compareTo(left) > 0 || momentLevel.compareTo(right) > 0)) {
                state = "漫堤";
            } else if (null != bhtz && null != momentLevel && momentLevel.compareTo(bhtz) > 0) {
                state = "超最高";
            } else if (null != wrz && null != momentLevel && momentLevel.compareTo(wrz) > 0) {
                state = "超警戒";
            } else if (null != left && null != right && null != wrz && null != bhtz && null != momentLevel) {
                state = "正常";
            }
            // 填充返回值
            RiverRiskPointPC e = new RiverRiskPointPC();
            e.setStnm(stnm);
            e.setSttp(sttp);
            e.setMomentRiverLevel(momentLevel);
            e.setMomentRiverDepth(momentDepth);
            e.setState(state);
            e.setTime(moment);
            e.setFromWarning(fromWaring);
            e.setFromHighest(fromHighest);
            e.setFromOverFlow(fromOverFlow);
            e.setWarning(wrz);
            e.setHighest(bhtz);
            if (null != left && null != right) {
                e.setOverFlow(left.min(right));
            }
            e.setLgtd(stSideGate.getLgtd());
            e.setLttd(stSideGate.getLttd());
            resList.add(e);
        }
        resList.sort(Comparator.comparing(RiverRiskPointPC::getMomentRiverLevel, Comparator.nullsLast(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        })));
        return resList;
    }

    @Override
    public List<GateCameraVo> getCameraByGateName(String name) {
        List<GateCameraVo> resList = new ArrayList<>();

        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "CA");
        queryWrapper.eq("gate_name", name);
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        for (GateStationRelatedDto gateStationRelatedDto : gateStationRelatedDtos) {
            GateCameraVo res = new GateCameraVo();
            res.setGateName(gateStationRelatedDto.getGateName());
            res.setCode(gateStationRelatedDto.getStcd());
            res.setName(gateStationRelatedDto.getStnm());
            res.setCameraType(gateStationRelatedDto.getStreamLoc());
            resList.add(res);
        }
        return resList;
    }



    @Override
    public WaterInfoDto getWaterInfoByGateName(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sttp","ZZ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        Map<String, BigDecimal> stcdInfoMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, StStbprpBEntity::getDtmel,(o1,o2)->o2));
        WaterInfoDto waterInfoDto = new WaterInfoDto();
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZQ","ZZ");
        queryWrapper.eq("gate_name", name);
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        Map<String, List<GateStationRelatedDto>> collect = gateStationRelatedDtos.parallelStream().collect(Collectors.groupingBy(GateStationRelatedDto::getSttp));
        List<GateStationRelatedDto> zz = collect.get("ZZ");
        List<GateStationRelatedDto> zq = collect.get("ZQ");
        List<StWaterRateLatestDto> stWaterRateLatestDtos = stWaterRateLatestDao.selectList(new QueryWrapper<>());
        Map<String, StWaterRateLatestDto> didMap = stWaterRateLatestDtos.parallelStream().collect(Collectors.toMap(StWaterRateLatestDto::getDid, Function.identity(), (o1, o2) -> o2));
        List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<>());
        Map<String, String> snMap = stSnConvertEntities.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn, (o1, o2) -> o2));
        if (CollUtil.isNotEmpty(zq)){
            GateStationRelatedDto gateStationRelatedDto = zq.get(0);
            String substring = gateStationRelatedDto.getStcd().substring(2);
            StWaterRateLatestDto stWaterRateLatestDto = didMap.get(substring);
            //水位
            String momentRiverPosition = stWaterRateLatestDto.getMomentRiverPosition();
            //流量
            String momentRate = stWaterRateLatestDto.getMomentRate();
            waterInfoDto.setWaterLevel(momentRiverPosition);
            waterInfoDto.setWaterRate(momentRate);
        }
        if (CollUtil.isNotEmpty(zz)){
            GateStationRelatedDto gateStationRelatedDto = zz.get(0);
            String s = snMap.get(gateStationRelatedDto.getStcd());
            BigDecimal bigDecimal = stcdInfoMap.get(gateStationRelatedDto.getStcd());
            if (StrUtil.isNotEmpty(s)){
                StWaterRateLatestDto stWaterRateLatestDto = didMap.get(s);
                if (StrUtil.isNotEmpty(stWaterRateLatestDto.getAddr())){
                    //水位
                    String addrv = stWaterRateLatestDto.getAddrv();
                    if (StrUtil.isNotEmpty(addrv) ){
                        BigDecimal waterDeep = new BigDecimal(addrv ).divide(new BigDecimal(1000),2,RoundingMode.HALF_UP);
                        if (bigDecimal != null){
                            waterInfoDto.setWaterLevel( waterDeep.add(bigDecimal).toString());
                        }
                    }

                }
            }
        }
        return waterInfoDto;
    }

    @Override
    public WaterInfoDto getWaterInfoByPumpName(String name) {
        WaterInfoDto waterInfoDto = new WaterInfoDto();
        Date date = new Date();
        DateTime start = DateUtil.offsetHour(date, -2);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sttp","DP");
        queryWrapper.eq("stnm",name);
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(stSideGateDtos)){
            StSideGateDto stSideGateDto = stSideGateDtos.get(0);
            String stcd = stSideGateDto.getStcd();
            String holesNumber = stSideGateDto.getHolesNumber();
            if (StrUtil.isNotEmpty(stcd)){
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.between("date",start,date);
                queryWrapper1.eq("device_addr",stcd);
                List<StPumpDataDto> stPumpDataDtos = pumpDataDao.selectList(queryWrapper1);
                if (CollUtil.isNotEmpty(stPumpDataDtos)){
                    StPumpDataDto stPumpDataDto = stPumpDataDtos.parallelStream().sorted(Comparator.comparing(StPumpDataDto::getDate).reversed()).collect(Collectors.toList()).get(0);
                    String flowRate = stPumpDataDto.getFlowRate();
                    waterInfoDto.setWaterRate(flowRate);
                }
            }
        }
        return waterInfoDto;
    }

    @Override
    public Object selectForecastWaterLevelByRiverId(String caseId, String riverId) {
        List<StQpModelDto> allStQpModelList = null != redisService.getCacheObject("AllStQpModelList" + riverId) ? redisService.getCacheObject("AllStQpModelList" + riverId) : stQpModelDao.selectList(new QueryWrapper<StQpModelDto>().eq("river_id", riverId).orderByAsc("qp_id"));

        List<StCaseResDto> allStCaseResList = null != redisService.getCacheObject("AllStCaseResList" + caseId + riverId) ? redisService.getCacheObject("AllStCaseResList" + caseId + riverId) : stCaseResDao.selectList(new QueryWrapper<StCaseResDto>().select("section_name", "river_z", "step").eq("case_id", caseId).eq("rv_id", riverId));
        Map<Integer, List<StCaseResDto>> stCaseResDtoMap = allStCaseResList.stream().collect(Collectors.groupingBy(StCaseResDto::getStep));
        List<Map.Entry<Integer, List<StCaseResDto>>> entryList = new ArrayList<>();
        stCaseResDtoMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(integerListEntry -> {
            entryList.add(integerListEntry);
        });
        List<Map<String, Object>> maps = MapReduceUtil.handleList(entryList, entries -> {
            List<Map<String, Object>> list = Lists.newArrayList();
            entries.forEach(entry -> {
                Map<String, Object> map = new HashMap<>();

                Integer key = entry.getKey();
                map.put("step", key);
                List<StCaseResDto> value = entry.getValue();
                Map<String, StCaseResDto> sectionNameMap = value.stream().collect(Collectors.toMap(StCaseResDto::getSectionName, a -> a, (k1, k2) -> k1));
                Map<Integer, Object> stepResult = Maps.newHashMap();
                Double lastValue = null;
                for (StQpModelDto stQpModelDto : allStQpModelList) {
                    Map<String, Object> stepmap = new HashMap<>();
                    Integer qpId = stQpModelDto.getQpId();
                    String name = stQpModelDto.getName();
                    List<String> sectionNameList = split(name, ",");
                    if (sectionNameList.size() == 2) {
//                        Double mean = DoubleMath.mean(Double.valueOf(sectionNameMap.get(sectionNameList.get(0)).getRiverZ()), Double.valueOf(sectionNameMap.get(sectionNameList.get(1)).getRiverZ()));

                        StCaseResDto compare = compare((a, b) -> {
                            a.setRiverZ(String.valueOf(DoubleMath.mean(Double.valueOf(a.getRiverZ()), Double.valueOf(b.getRiverZ()))));
                            return a;
                        }, sectionNameMap.get(sectionNameList.get(0)), sectionNameMap.get(sectionNameList.get(1)));
                        if (compare != null) {
                            lastValue = Double.valueOf(compare.getRiverZ());
                            stepmap.put("value", Double.valueOf(compare.getRiverZ()));
                        } else {
                            stepmap.put("value", lastValue);
                        }

                    }
                    if (sectionNameList.size() == 1) {
                        String sectionName = sectionNameList.get(0);
                        StCaseResDto stCaseResDto = sectionNameMap.get(sectionName);
                        String riverZ = stCaseResDto.getRiverZ();
                        Double aDouble = Double.valueOf(riverZ);
                        lastValue = aDouble;
                        stepmap.put("value", aDouble);
                    }
                    stepmap.put("id", qpId);
//                    String sectionId = stQpModelDto.getSectionId();
//                    Integer riverId = stQpModelDto.getRiverId();

                    stepResult.put(qpId, stepmap);
                }
                map.put("stepResult", stepResult);
                list.add(map);
            });
            return list;
        });

        return maps;
    }

    @SneakyThrows
    @Override
    public List<RiskByRiver> selectGateRiskByRiverId(String riverId) {

        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RiskByRiver> resList = new ArrayList<>();

        // 闸坝泵站关联关系 因为查闸前水位 只查询上游
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZZ", "ZQ");
        queryWrapper.eq("stream_loc", "0");
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        Map<String, List<GateStationRelatedDto>> stCollect = gateStationRelatedDtos.stream().collect(Collectors.groupingBy(GateStationRelatedDto::getGateName));
        // 查询河流对应的闸坝
        QueryWrapper<StSideGateDto> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("river_id", riverId);
        queryWrapper3.in("sttp", "DD", "SB");
        queryWrapper3.eq("is_model_used", 1);
        queryWrapper3.isNotNull("seria_name");
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper3);
        Map<String, List<StSideGateDto>> gateMap = stSideGateDtos.stream().collect(Collectors.groupingBy(StSideGateDto::getStnm));
        // 水位站流量站基础信息
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> stbMap = stStbprpBEntities.stream().collect(Collectors.groupingBy(StStbprpBEntity::getStcd));
        // 取实时水位
        List<StWaterRateLatestDto> stWaterRates = stWaterRateLatestDao.selectList(new QueryWrapper());
        Map<String, List<StWaterRateLatestDto>> stWaterRateMap = stWaterRates.stream().collect(Collectors.groupingBy(StWaterRateLatestDto::getDid));
        List<StSideGateDto> gates = new ArrayList<>();
        for (String s : gateMap.keySet()) {
            gates.add(gateMap.get(s).get(0));
        }
        for (Map.Entry<String, List<GateStationRelatedDto>> stEntry : stCollect.entrySet()) {
            // 站点名称
            String stnm = stEntry.getKey();
            if (null == gateMap.get(stnm)) {
                continue;
            } else {
                gates.remove(gateMap.get(stnm).get(0));
            }
            StSideGateDto stSideGate = gateMap.get(stnm).get(0);
            String sttp = stSideGate.getSttp();
            // 警戒水位
            BigDecimal wrz = stSideGate.getWrz();
            // 最高水位
            BigDecimal bhtz = stSideGate.getBhtz();
            // 查询水位站流量站实时水位实时水深, 都有数据的时候, 优先取水位站, 其次取流量站
            List<GateStationRelatedDto> stList = stEntry.getValue();
            boolean haveWaterStLevel = false;
            BigDecimal momentDepth = BigDecimal.ZERO;
            Date moment = new Date();
            // 河底高程
            BigDecimal bottom = null;
            // 左岸高程
            BigDecimal left = null;
            // 右岸高程
            BigDecimal right = null;
            for (GateStationRelatedDto st : stList) {
                if ("ZZ".equals(st.getSttp())) {
                    // 查询水位站断面数据
                    if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                        QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                        StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                        // 底面高程直接用站点高程
                        bottom = null == stbMap.get(st.getStcd()).get(0).getDtmel() ? BigDecimal.ZERO : stbMap.get(st.getStcd()).get(0).getDtmel();
//                        bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                        left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                        right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                    }

                    // 编码转换
                    QueryWrapper<StSnConvertEntity> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("stcd", st.getStcd());
                    StSnConvertEntity stSnConvertEntity = stSnConvertDao.selectOne(queryWrapper2);
                    if(null != stSnConvertEntity) {
                        List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(stSnConvertEntity.getSn());
                        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                            StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                            String addr = stWaterRateEntity.getAddrv();
                            if (StrUtil.isNotEmpty(addr)) {
                                momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP);
                                moment = zzFormat.parse(stWaterRateEntity.getCtime());
                                haveWaterStLevel = true;
                            }
                        }
                    }
                } else if ("ZQ".equals(st.getSttp()) && !haveWaterStLevel) {
                    // 查询流量站断面数据
                    if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                        QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                        StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                        // 底面高程直接用站点高程

//                        bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                        left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                        right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                    }
                    List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(st.getStcd().substring(2));
                    if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                        StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                        String addr = stWaterRateEntity.getMomentRiverPosition();
                        if (StrUtil.isNotEmpty(addr)) {
                            momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).setScale(3, BigDecimal.ROUND_HALF_UP);
                            moment = zqFormat.parse(stWaterRateEntity.getCtime());
                        }
                    }
                }
                bottom = null == stbMap.get(st.getStcd()).get(0).getDtmel() ? BigDecimal.ZERO : stbMap.get(st.getStcd()).get(0).getDtmel();
            }

            BigDecimal momentLevel = null;
            if (null != bottom) {
                momentLevel = momentDepth.add(bottom);
            }
            String state = "";
            BigDecimal fromWaring = null;
            BigDecimal fromHighest = null;
            BigDecimal fromOverFlow = null;
            if (null != wrz && null != momentLevel) {
                fromWaring = momentLevel.subtract(wrz);
            }
            if (null != bhtz && null != momentLevel) {
                fromHighest = momentLevel.subtract(bhtz);
            }
            if (null != left && null != right && null != momentLevel) {
                fromOverFlow = momentLevel.subtract(left.min(right));
            }
            if (null != left && null != right && null != momentLevel && (momentLevel.compareTo(left) > 0 || momentLevel.compareTo(right) > 0)) {
                state = "漫堤";
            } else if (null != bhtz && null != momentLevel && momentLevel.compareTo(bhtz) > 0) {
                state = "超最高";
            } else if (null != wrz && null != momentLevel && momentLevel.compareTo(wrz) > 0) {
                state = "超警戒";
            } else if (null != left && null != right && null != wrz && null != bhtz && null != momentLevel) {
                state = "正常";
            }else {
                state = "正常";
            }
            // 填充返回值
            RiskByRiver e = new RiskByRiver();
            e.setStnm(stnm);
            e.setSttp(sttp);
            e.setMomentRiverLevel(momentLevel);
            e.setMomentRiverDepth(momentDepth);
            e.setState(state);
            e.setTime(moment);
            e.setFromWarning(fromWaring);
            e.setFromHighest(fromHighest);
            e.setFromOverFlow(fromOverFlow);
            e.setWarning(wrz);
            e.setHighest(bhtz);
            e.setIsStationRelated(1);
            StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
            stStbprpBEntityDTO.setStnm(stnm);
            stStbprpBEntityDTO.setSttp(sttp);
            stStbprpBEntityDTO.setCurrent(1);
            stStbprpBEntityDTO.setSize(10);
            ResponseResult stationInfoList = stSideGateService.getStationInfoList(stStbprpBEntityDTO);
            PageUtil result = (PageUtil) stationInfoList.getResult();
            List<StStbprpBEntityDTO> baseInfo = result.getRecords();
            e.setBaseInfo(baseInfo.get(0));
            if (null != left && null != right) {
                e.setOverFlow(left.min(right));
            }
            e.setLgtd(stSideGate.getLgtd());
            e.setLttd(stSideGate.getLttd());
            resList.add(e);
        }
        for (StSideGateDto gate : gates) {
            RiskByRiver e = new RiskByRiver();
            e.setStnm(gate.getStnm());
            e.setSttp(gate.getSttp());
            e.setLgtd(gate.getLgtd());
            e.setLttd(gate.getLttd());
            e.setIsStationRelated(0);
            StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
            stStbprpBEntityDTO.setStnm(gate.getStnm());
            stStbprpBEntityDTO.setSttp(gate.getSttp());
            stStbprpBEntityDTO.setCurrent(1);
            stStbprpBEntityDTO.setSize(10);
            ResponseResult stationInfoList = stSideGateService.getStationInfoList(stStbprpBEntityDTO);
            PageUtil result = (PageUtil) stationInfoList.getResult();
            List<StStbprpBEntityDTO> baseInfo = result.getRecords();
            e.setBaseInfo(baseInfo.get(0));
            resList.add(e);
        }
        return resList;
    }

    @SneakyThrows
    @Override
    public List<RiverRiskPointGate> selectRiverRiskPointGate(String caseId) {
        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RiverRiskPointGate> resList = new ArrayList<>();
        // 闸坝泵站关联关系 因为查闸前水位 只查询上游
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZZ", "ZQ");
        queryWrapper.eq("stream_loc", "0");
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        Map<String, List<GateStationRelatedDto>> stCollect = gateStationRelatedDtos.stream().collect(Collectors.groupingBy(GateStationRelatedDto::getGateName));
        // 闸坝基础信息
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(new QueryWrapper<>());
        Map<String, List<StSideGateDto>> gateMap = stSideGateDtos.stream().collect(Collectors.groupingBy(StSideGateDto::getStnm));
        // 水位站流量站基础信息
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> stbMap = stStbprpBEntities.stream().collect(Collectors.groupingBy(StStbprpBEntity::getStcd));
        // 取实时水位
        List<StWaterRateLatestDto> stWaterRates = stWaterRateLatestDao.selectList(new QueryWrapper());
        Map<String, List<StWaterRateLatestDto>> stWaterRateMap = stWaterRates.stream().collect(Collectors.groupingBy(StWaterRateLatestDto::getDid));
        for (Map.Entry<String, List<GateStationRelatedDto>> stEntry : stCollect.entrySet()) {
            // 站点名称
            String stnm = stEntry.getKey();
            List<StSideGateDto> stSideGateDtos1 = gateMap.get(stnm);
            if (CollUtil.isEmpty(stSideGateDtos1)  ){
                continue;
            }
            StSideGateDto stSideGate = gateMap.get(stnm).get(0);
            String sttp = stSideGate.getSttp();
            // 警戒水位
            BigDecimal wrz = stSideGate.getWrz();
            // 最高水位
            BigDecimal bhtz = stSideGate.getBhtz();
            // 查询水位站流量站实时水位实时水深, 都有数据的时候, 优先取水位站, 其次取流量站
            List<GateStationRelatedDto> stList = stEntry.getValue();
            boolean haveWaterStLevel = false;
            BigDecimal momentDepth = BigDecimal.ZERO;
            Date moment = new Date();
            // 河底高程
            BigDecimal bottom = null;
            // 左岸高程
            BigDecimal left = null;
            // 右岸高程
            BigDecimal right = null;
            for (GateStationRelatedDto st : stList) {
                if ("ZZ".equals(st.getSttp())) {
                    if (CollUtil.isEmpty(stbMap.get(st.getStcd()))){
                        continue;
                    }
                    // 查询水位站断面数据
                    if (stbMap.get(st.getStcd()) == null){
                        continue;
                    }
                    if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                        QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                        StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                        bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                        left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                        right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                    }
                    if(null == bottom || BigDecimal.ZERO == bottom) {
                        if(null != stbMap.get(st.getStcd()).get(0).getDtmel()) {
                            bottom = stbMap.get(st.getStcd()).get(0).getDtmel();
                        }
                    }
                    // 编码转换
                    QueryWrapper<StSnConvertEntity> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("stcd", st.getStcd());
                    StSnConvertEntity stSnConvertEntity = stSnConvertDao.selectOne(queryWrapper2);
                    if(null != stSnConvertEntity) {
                        List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(stSnConvertEntity.getSn());
                        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                            StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                            String addr = stWaterRateEntity.getAddrv();
                            if (StrUtil.isNotEmpty(addr)) {
                                momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).divide(new BigDecimal(1000)).setScale(3, BigDecimal.ROUND_HALF_UP);
                                moment = zzFormat.parse(stWaterRateEntity.getCtime());
                                haveWaterStLevel = true;
                            }
                        }
                    }
                } else if ("ZQ".equals(st.getSttp()) && !haveWaterStLevel) {
                    if (CollUtil.isEmpty(stbMap.get(st.getStcd()))){
                        continue;
                    }
                    // 查询流量站断面数据
                    if (stbMap.get(st.getStcd()) == null){
                        continue;
                    }
                    if (StrUtil.isNotEmpty(stbMap.get(st.getStcd()).get(0).getSectionName())) {
                        QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                        queryWrapper1.eq("section_name", stbMap.get(st.getStcd()).get(0).getSectionName());
                        StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                        bottom = null == stSectionModelDto.getAltitudeBottom() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeBottom();
                        left = null == stSectionModelDto.getAltitudeLeft() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeLeft();
                        right = null == stSectionModelDto.getAltitudeRight() ? BigDecimal.ZERO : stSectionModelDto.getAltitudeRight();
                    }
                    if(null == bottom || BigDecimal.ZERO == bottom) {
                        if(null != stbMap.get(st.getStcd()).get(0).getDtmel()) {
                            bottom = stbMap.get(st.getStcd()).get(0).getDtmel();
                        }
                    }
                    List<StWaterRateLatestDto> stWaterRateEntities = stWaterRateMap.get(st.getStcd().substring(2));
                    if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                        StWaterRateLatestDto stWaterRateEntity = stWaterRateEntities.get(0);
                        String addr = stWaterRateEntity.getMomentRiverPosition();
                        if (StrUtil.isNotEmpty(addr)) {
                            momentDepth = BigDecimal.valueOf(Double.valueOf(addr)).setScale(3, BigDecimal.ROUND_HALF_UP);
                            moment = zqFormat.parse(stWaterRateEntity.getCtime());
                        }
                    }
                }
            }
            BigDecimal momentLevel = null;
            if (null != bottom) {
                momentLevel = momentDepth.add(bottom);
            }
            String state = "";
            BigDecimal fromWaring = null;
            BigDecimal fromHighest = null;
            BigDecimal fromOverFlow = null;
            if (null != wrz && null != momentLevel) {
                fromWaring = momentLevel.subtract(wrz);
            }
            if (null != bhtz && null != momentLevel) {
                fromHighest = momentLevel.subtract(bhtz);
            }
            if (null != left && null != right && null != momentLevel) {
                fromOverFlow = momentLevel.subtract(left.min(right));
            }
            if (null != left && null != right && null != momentLevel && (momentLevel.compareTo(left) > 0 || momentLevel.compareTo(right) > 0)) {
                state = "漫堤";
            } else if (null != bhtz && null != momentLevel && momentLevel.compareTo(bhtz) > 0) {
                state = "超最高";
            } else if (null != wrz && null != momentLevel && momentLevel.compareTo(wrz) > 0) {
                state = "超警戒";
            } else if (null != wrz && null != bhtz && null != momentLevel) {
                state = "正常";
            }
            // XXX 查询风险信息 暂时在表里造数据 后续改逻辑
            QueryWrapper<StWaterRiskForecastGateDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("case_id", caseId);
            queryWrapper1.eq("stnm", stnm);
            queryWrapper1.in("sttp", "DD", "SB");
            // 有的闸既是闸也是坝
            List<StWaterRiskForecastGateDto> stWaterRiskForecastGateDtos = stWaterRiskForecastGateDao.selectList(queryWrapper1);
            StWaterRiskForecastGateDto stWaterRiskForecastGateDto = new StWaterRiskForecastGateDto();
            if(!CollectionUtils.isEmpty(stWaterRiskForecastGateDtos)) {
                stWaterRiskForecastGateDto = stWaterRiskForecastGateDtos.get(0);
            }
            // 填充返回值
            RiverRiskPointGate e = new RiverRiskPointGate();
            e.setStnm(stnm);
            e.setSttp(sttp);
            e.setMomentRiverLevel(momentLevel);
            e.setMomentRiverDepth(momentDepth);
            e.setState(state);
            e.setTime(moment);
            e.setFromWarning(fromWaring);
            e.setFromHighest(fromHighest);
            e.setFromOverFlow(fromOverFlow);
            e.setWarning(wrz);
            e.setHighest(bhtz);
            e.setRisk(stWaterRiskForecastGateDto.getRisk());
            if (null != left && null != right) {
                e.setOverFlow(left.min(right));
            }
            e.setLgtd(stSideGate.getLgtd());
            e.setLttd(stSideGate.getLttd());
            resList.add(e);
        }
        return resList;
    }

    @Override
    public Object gateRiskModule(String caseId) {
        //模拟数据
        QueryWrapper<StStbprpBEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZZ", "ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(queryWrapper);
        stStbprpBEntities.forEach(stStbprpBEntity -> {
            StWaterRiskForecastGateDto s = new StWaterRiskForecastGateDto();
            s.setCaseId(caseId);
            s.setStcd(stStbprpBEntity.getStcd());
            s.setStnm(stStbprpBEntity.getStnm());
            s.setId(UUID.randomUUID().toString().replace("-",""));
            s.setRisk("无风险");
            s.setTime(new Date());
            s.setSttp(stStbprpBEntity.getSttp());
            stWaterRiskForecastGateDao.insert(s);
        });
        QueryWrapper<StSideGateDto> queryWrapper1 = new QueryWrapper<>();
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper1);
        stSideGateDtos.forEach(stSideGateDto -> {
            StWaterRiskForecastGateDto s = new StWaterRiskForecastGateDto();
            s.setCaseId(caseId);
            s.setStcd(stSideGateDto.getStcd());
            s.setStnm(stSideGateDto.getStnm());
            s.setId(UUID.randomUUID().toString().replace("-",""));
            s.setRisk("无风险");
            s.setTime(new Date());
            s.setSttp(stSideGateDto.getSttp());
            stWaterRiskForecastGateDao.insert(s);
        });
        return null;
    }

    @Override
    public List<WaterFlowStationVo> getStationByPumpName(String name) {
        List<WaterFlowStationVo> resList = new ArrayList<>();
        Date date = new Date();
        DateTime start = DateUtil.beginOfDay(date);
        // 主汛期开始结束时间
        Calendar now = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), 6, 19);
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), 7, 16);
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gate_name", name);
        queryWrapper.in("sttp", "ZZ", "ZQ");
        List<GateStationRelatedDto> pumpToSt = gateStationRelatedDao.selectList(queryWrapper);
        for (GateStationRelatedDto ptos : pumpToSt) {
            QueryWrapper<GateStationRelatedDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("stnm", ptos.getStnm());
            queryWrapper1.isNotNull("stream_loc");
            List<GateStationRelatedDto> stToGate = gateStationRelatedDao.selectList(queryWrapper1);
            List<String> collect = stToGate.stream().map(GateStationRelatedDto::getGateName).collect(Collectors.toList());
            QueryWrapper<StSideGateDto> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.in("stnm", collect);
            queryWrapper2.in("sttp", "DD", "SB");
            List<StSideGateDto> gates = stSideGateDao.selectList(queryWrapper2);
            List<Integer> ids = new ArrayList<>();
            for (StSideGateDto gate : gates) {
                if(!ids.contains(gate.getId())) {
                    if("DD".equals(gate.getSttp())) {
                        ids.add(gate.getId());
                    }
                }
            }
            List<StSideGateDto> gatesFilter = gates.stream().filter(x -> {
                return ids.contains(x.getId());
            }).collect(Collectors.toList());
            for (StSideGateDto gate : gatesFilter) {
                WaterFlowStationVo e = new WaterFlowStationVo();
                e.setStcd(ptos.getStcd());
                e.setStnm(ptos.getStnm());
                e.setGate(gate.getStnm());
                if(now.after(startDate) && now.before(endDate)) {
                    e.setLevelName("设计中水位");
                    e.setWaterLevel(gate.getMiddleWaterLevel());
                } else {
                    e.setLevelName("设计高水位");
                    e.setWaterLevel(gate.getHighWaterLevel());
                }
                QueryWrapper<StStbprpBEntity> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("stcd", ptos.getStcd());
                StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectOne(queryWrapper3);
                // 基面高程
                BigDecimal dtmel = null;
                String stcd = "";
                String stationType = "";
                List<StWaterRateEntity> stWaterRateEntities = new ArrayList<>();
                boolean haveZZ = false;
                switch (stStbprpBEntity.getSttp()) {
                    case "ZZ":
                        QueryWrapper<StSnConvertEntity> queryWrapper4 = new QueryWrapper<>();
                        queryWrapper4.eq("stcd", stStbprpBEntity.getStcd());
                        StSnConvertEntity stSnConvertEntity = stSnConvertDao.selectOne(queryWrapper4);
                        if(null != stSnConvertEntity) {
                            stcd = stSnConvertEntity.getSn();
                            String format1 = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                            String format2 = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
                            QueryWrapper<StWaterRateEntity> queryWrapper5 = new QueryWrapper<>();
                            queryWrapper5.eq("did", stcd);
                            queryWrapper5.ge("ctime",format1);
                            queryWrapper5.le("ctime",format2);
                            queryWrapper5.orderByDesc("ctime");
                            stWaterRateEntities = stWaterRateDao.selectList(queryWrapper5);
                        }
                        if(CollectionUtil.isNotEmpty(stWaterRateEntities)) {
                            // 基面高程
                            dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                            haveZZ = true;
                        }
                        stationType = "ZZ";
                        break;
                    case "ZQ":
                        stcd = stStbprpBEntity.getStcd().substring(2);
                        String format1 = DateUtil.format(start, "yyyy-MM-dd HH:mm:ss");
                        String format2 = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
                        QueryWrapper<StWaterRateEntity> queryWrapper6 = new QueryWrapper<>();
                        queryWrapper6.eq("did", stcd);
                        queryWrapper6.ge("ctime",format1);
                        queryWrapper6.le("ctime",format2);
                        queryWrapper6.orderByDesc("ctime");
                        stWaterRateEntities = stWaterRateDao.selectList(queryWrapper6);
                        stationType = "ZQ";
                        // 基面高程
                        dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                        break;
                }
                BigDecimal actualTimeLevel = new BigDecimal(0);
                if(!"".equals(stcd)) {
                    if("ZZ".equals(stationType) && haveZZ) {
                        actualTimeLevel = BigDecimal.valueOf(Double.valueOf(0 < stWaterRateEntities.size() && StrUtil.isNotEmpty(stWaterRateEntities.get(0).getAddrv())? stWaterRateEntities.get(0).getAddrv() : "0")).divide(new BigDecimal(1000));
                    } else if("ZQ".equals(stationType) && !haveZZ){
                        actualTimeLevel = BigDecimal.valueOf(Double.valueOf(0 < stWaterRateEntities.size() && StrUtil.isNotEmpty(stWaterRateEntities.get(0).getMomentRiverPosition()) ? stWaterRateEntities.get(0).getMomentRiverPosition() : "0"));
                    } else {
                        actualTimeLevel = BigDecimal.ZERO;
                    }
                    if(null != dtmel) {
                        actualTimeLevel = actualTimeLevel.add(dtmel);
                    }
                    if(0 == BigDecimal.ZERO.compareTo(actualTimeLevel)) {
                        e.setActualTimeWaterLevel(null);
                    } else {
                        e.setActualTimeWaterLevel(actualTimeLevel.setScale(2, RoundingMode.HALF_UP));
                    }
                    // 2023-06-25 需求变更 高于 (中/高) 水位展示"高于", 等于 (中/高) 水位展示"正常", 低于 (中/高) 水位展示"低于", 其他情况: 展示空
                    if(now.after(startDate) && now.before(endDate)) {
                        if("敞泄".equals(gate.getMiddleWaterLevel()) || "关闭".equals(gate.getMiddleWaterLevel()) || "/".equals(gate.getMiddleWaterLevel()) || StrUtil.isEmpty(gate.getMiddleWaterLevel()) ) {
                            e.setState("");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(gate.getMiddleWaterLevel()))) > 0) {
                            e.setState("高于");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(gate.getMiddleWaterLevel()))) == 0) {
                            // 正常水位的
                            e.setState("正常");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(gate.getMiddleWaterLevel()))) < 0) {
                            e.setState("低于");
                        }
                    } else {
                        if("敞泄".equals(gate.getHighWaterLevel()) || "关闭".equals(gate.getHighWaterLevel()) || "/".equals(gate.getHighWaterLevel()) || StrUtil.isEmpty(gate.getHighWaterLevel())) {
                            e.setState("");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf( gate.getHighWaterLevel()  ))) > 0) {
                            e.setState("高于");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf(gate.getHighWaterLevel()))) == 0) {
                            // 正常水位的
                            e.setState("正常");
                        } else if(null != e.getActualTimeWaterLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf(gate.getHighWaterLevel()))) < 0) {
                            e.setState("低于");
                        }
                    }
                }
                resList.add(e);
            }
        }
        return resList;
    }

    @Override
    public List<StationWaterFlowDataVo> getWaterFlowByStation(String stcd) {
        List<StationWaterFlowDataVo> resList = new ArrayList<>();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        Date startTime = DateUtil.beginOfDay(endTime);
        List<StWaterRateEntity> stRainDateDtos = new ArrayList<>();
        List<Date> timeSplit = DataUtils.getTimeSplit(startTime, endTime, 60, DateField.MINUTE);
        //水位数据加高程
        StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectById(stcd);
        QueryWrapper<GateStationRelatedDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stnm", stStbprpBEntity.getStnm());
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper);
        StSideGateDto stSideGateDto = new StSideGateDto();
        // 闸坝同名取闸
        if(CollectionUtil.isNotEmpty(gateStationRelatedDtos) && gateStationRelatedDtos.size() > 1) {
            for (GateStationRelatedDto x : gateStationRelatedDtos) {
                QueryWrapper<StSideGateDto> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("stnm", x.getGateName());
                queryWrapper1.eq("sttp", "DD");
                StSideGateDto stSideGateDto1 = stSideGateDao.selectOne(queryWrapper1);
                if(null != stSideGateDto1) {
                    stSideGateDto = stSideGateDto1;
                }
            }
        } else if(CollectionUtil.isNotEmpty(gateStationRelatedDtos)) {
            for (GateStationRelatedDto x : gateStationRelatedDtos) {
                QueryWrapper<StSideGateDto> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("stnm", x.getGateName());
                StSideGateDto stSideGateDto1 = stSideGateDao.selectOne(queryWrapper1);
                if(null != stSideGateDto1) {
                    stSideGateDto = stSideGateDto1;
                }
            }
        }
        String type = "1";
        if (stStbprpBEntity.getSttp().equals("ZZ")) {
            List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().eq(StSnConvertEntity::getStcd, stcd));
            if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                type = "1";
                stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(startTime, "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(endTime, "yyyy/MM/dd HH:mm:ss")).eq(StWaterRateEntity::getDid, stSnConvertEntities.get(0).getSn()))).orElse(com.google.common.collect.Lists.newArrayList());
                if (!CollectionUtils.isEmpty(stRainDateDtos) && null != stStbprpBEntity) {
                    for (StWaterRateEntity stRainDateDto : stRainDateDtos) {
                        if (null != stStbprpBEntity.getDtmel()) {
                            BigDecimal multiply = stStbprpBEntity.getDtmel().multiply(new BigDecimal("1000"));
                            stRainDateDto.setAddrv(new BigDecimal(org.apache.commons.lang.StringUtils.isNotBlank(stRainDateDto.getAddrv()) ? stRainDateDto.getAddrv() : "0").add(multiply).toString());
                        }
                    }
                }
            }
        } else {
            //流量站数据
            type = "2";
            stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, simple.format(startTime)).le(StWaterRateEntity::getCtime, simple.format(endTime)).eq(StWaterRateEntity::getDid, stcd.substring(2)))).orElse(com.google.common.collect.Lists.newArrayList());
            if (!CollectionUtils.isEmpty(stRainDateDtos) && null != stStbprpBEntity) {
                for (StWaterRateEntity stRainDateDto : stRainDateDtos) {
                    if (null != stStbprpBEntity.getDtmel()) {
                        stRainDateDto.setMomentRiverPosition(new BigDecimal(org.apache.commons.lang.StringUtils.isNotBlank(stRainDateDto.getMomentRiverPosition()) ? stRainDateDto.getMomentRiverPosition() : "0").add(stStbprpBEntity.getDtmel()).toString());
                    }
                }
            }
        }
        //null 直接填充
        if (CollectionUtils.isEmpty(stRainDateDtos)) {
            for (int i = 0; i < timeSplit.size(); i++) {
                StationWaterFlowDataVo stationWaterFlowDataVo = new StationWaterFlowDataVo();
                stationWaterFlowDataVo.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                stationWaterFlowDataVo.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                stationWaterFlowDataVo.setWaterLevel("0");
                stationWaterFlowDataVo.setFlow("0");
                stationWaterFlowDataVo.setTime(simple.format(timeSplit.get(i)));
                resList.add(stationWaterFlowDataVo);
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
                    StationWaterFlowDataVo stationWaterFlowDataVo = new StationWaterFlowDataVo();
                    Date start = timeSplit.get(i);
                    Date end = timeSplit.get(i + 1);
                    Double sum = 0.0;
                    List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        List<StWaterRateRelation> collect1 = stWaterRateRelationList.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                        sum = Double.valueOf(org.apache.commons.lang.StringUtils.isNotBlank(collect1.get(0).getAddrv()) ? collect1.get(0).getAddrv() : "0");
                    }
                    stationWaterFlowDataVo.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                    stationWaterFlowDataVo.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                    stationWaterFlowDataVo.setWaterLevel(new BigDecimal(String.valueOf(sum)).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).toString());
                    stationWaterFlowDataVo.setTime(simple.format(timeSplit.get(i)));
                    resList.add(stationWaterFlowDataVo);
                }
            } else {
                try {
                    for (StWaterRateEntity stWaterRateEntity : stRainDateDtos) {
                        StWaterRateRelation stWaterRateRelation = new StWaterRateRelation();
                        stWaterRateRelation.setCtime(simple.parse(stWaterRateEntity.getCtime()));
                        stWaterRateRelation.setAddrv(stWaterRateEntity.getAddrv());
                        stWaterRateRelation.setMomentRiverPosition(stWaterRateEntity.getMomentRiverPosition());
                        stWaterRateRelation.setMomentRate(stWaterRateEntity.getMomentRate());
                        stWaterRateRelationList.add(stWaterRateRelation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //6.流量站数据填充
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StationWaterFlowDataVo stationWaterFlowDataVo = new StationWaterFlowDataVo();
                    Date start = timeSplit.get(i);
                    Date end = timeSplit.get(i + 1);
                    Double sum = 0.0;
                    Double flow = 0.0;
                    List<StWaterRateRelation> collect = stWaterRateRelationList.stream().filter(x -> x.getCtime().getTime() >= start.getTime() && x.getCtime().getTime() <= end.getTime()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        List<StWaterRateRelation> collect1 = collect.stream().sorted(Comparator.comparing(StWaterRateRelation::getCtime).reversed()).collect(Collectors.toList());
                        sum = Double.valueOf(org.apache.commons.lang.StringUtils.isNotBlank(collect1.get(0).getMomentRiverPosition()) ? collect1.get(0).getMomentRiverPosition() : "0");
                        flow = Double.valueOf(org.apache.commons.lang.StringUtils.isNotBlank(collect1.get(0).getMomentRate()) ? collect1.get(0).getMomentRate() : "0");
                    }
                    stationWaterFlowDataVo.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
                    stationWaterFlowDataVo.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
                    stationWaterFlowDataVo.setWaterLevel(String.valueOf(sum));
                    stationWaterFlowDataVo.setFlow(String.valueOf(flow));
                    stationWaterFlowDataVo.setTime(simple.format(timeSplit.get(i)));
                    resList.add(stationWaterFlowDataVo);
                }
            }
        }
        return resList;
    }

    public WaterSituationCountVo getWaterSituation(StCaseBaseInfoDto stCaseBaseInfoDto, Map<String, List<StSnConvertEntity>> stSnMap, Map<String, List<StSectionModelDto>> stSectionModelDtoMap, List<StStbprpBEntity> stStbprpBEntities) {
        WaterSituationCountVo waterSituationCountVo = new WaterSituationCountVo();
        Integer overFlow = 0;
        Integer overWaring = 0;
        List<WaterSituationVo> list = new ArrayList<>();
        // 取实时水位
        List<StWaterRateEntity> stWaterRates = stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().ge("ctime", stCaseBaseInfoDto.getPreHotTime()).le("ctime", stCaseBaseInfoDto.getForecastStartTime()).orderByDesc("ctime"));
        Map<String, List<StWaterRateEntity>> stWaterRateMap = stWaterRates.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            QueryWrapper<StWaterRiskForecastDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("stcd", stStbprpBEntity.getStcd());
            StWaterRiskForecastDto stWaterRiskForecastDto = stWaterRiskForecastDao.selectOne(queryWrapper1);
            if (stWaterRiskForecastDto != null && StrUtil.isNotEmpty(stWaterRiskForecastDto.getType())  ){
                if ("漫堤".equals(stWaterRiskForecastDto.getType())) {
                    overFlow++;
                } else if ("超警戒".equals(stWaterRiskForecastDto.getType())) {
                    overWaring++;
                }
            }
            WaterSituationVo waterSituationVo = new WaterSituationVo();
            waterSituationVo.setStnm(stStbprpBEntity.getStnm());
            QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", stStbprpBEntity.getRvnm());
            ReaBase reaBase = reaBaseDao.selectOne(queryWrapper);
            waterSituationVo.setRiverName(null != reaBase && null != reaBase.getReaName() ? reaBase.getReaName() : "");
            String sttp = stStbprpBEntity.getSttp();
            String refSnId = "";
            if ("ZZ".equals(sttp) && !CollectionUtils.isEmpty(stSnMap.get(stStbprpBEntity.getStcd()))) {
                refSnId = stSnMap.get(stStbprpBEntity.getStcd()).get(0).getSn();
            } else {
                if (StrUtil.isNotEmpty(stStbprpBEntity.getStcd())) {
                    refSnId = stStbprpBEntity.getStcd().substring(2);
                }
            }
            if ("".equals(refSnId)) {
                continue;
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateMap.get(refSnId);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                BigDecimal momentRiverPosition = null;
                StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
                if ("ZZ".equals(sttp)) {
                    stWaterRateEntity = stWaterRateEntities.stream().max(Comparator.comparing(x -> new BigDecimal(Double.valueOf(StrUtil.isNotEmpty(x.getAddrv()) ? x.getAddrv() : "0")))).orElse(null);
                    if (null != stWaterRateEntity && StrUtil.isNotEmpty(stWaterRateEntity.getAddrv())) {
                        momentRiverPosition = BigDecimal.valueOf(Double.valueOf(stWaterRateEntity.getAddrv())).divide(new BigDecimal("1000"));
                    }
                } else {
                    stWaterRateEntity = stWaterRateEntities.stream().max(Comparator.comparing(x -> new BigDecimal(Double.valueOf(StrUtil.isNotEmpty(x.getMomentRiverPosition()) ? x.getMomentRiverPosition() : "0")))).orElse(null);
                    if (null != stWaterRateEntity && StrUtil.isNotEmpty(stWaterRateEntity.getMomentRiverPosition())) {
                        momentRiverPosition = BigDecimal.valueOf(Double.valueOf(stWaterRateEntity.getMomentRiverPosition()));
                    }
                }
                if (null != momentRiverPosition) {
                    // 水位加上基面高程
                    waterSituationVo.setHighestLevel(momentRiverPosition.add(null != stStbprpBEntity.getDtmel() ? stStbprpBEntity.getDtmel() : BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    SimpleDateFormat ZZTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat ZQTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        waterSituationVo.setHighestLevelTime(ZZTimeFormat.parse(stWaterRateEntity.getCtime()));
                    } catch (ParseException e) {
                        try {
                            waterSituationVo.setHighestLevelTime(ZQTimeFormat.parse(stWaterRateEntity.getCtime()));
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    List<StSectionModelDto> stSectionModelDtos = stSectionModelDtoMap.get(stStbprpBEntity.getSectionName());
                    if (stStbprpBEntity.getWrz() != null && stStbprpBEntity.getDtmel() != null) {
                        waterSituationVo.setFromWaring(momentRiverPosition.subtract(stStbprpBEntity.getWrz()).add(stStbprpBEntity.getDtmel()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                    if (CollectionUtils.isEmpty(stSectionModelDtos)) {
                        log.info(refSnId + "缺少对应stSectionModelDtos");
                    } else {
                        StSectionModelDto stSectionModelDto = stSectionModelDtos.get(0);
                        BigDecimal altitudeLeft = stSectionModelDto.getAltitudeLeft();
                        BigDecimal altitudeRight = stSectionModelDto.getAltitudeRight();
                        BigDecimal altitudeBottom = stSectionModelDto.getAltitudeBottom();
                        BigDecimal altitudeMin = altitudeLeft.subtract(altitudeRight).longValue() > 0 ? altitudeRight : altitudeLeft;
                        BigDecimal mandi = momentRiverPosition.add(altitudeBottom).subtract(altitudeMin);
                        waterSituationVo.setFromMandi(mandi.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            } else {
                log.info(refSnId + "缺少对应最新数据");
            }
            list.add(waterSituationVo);
        }
        waterSituationCountVo.setOverFlowCount(overFlow);
        waterSituationCountVo.setOverWaringCount(overWaring);
        waterSituationCountVo.setList(list);
        return waterSituationCountVo;
    }

    public static <T, R, U> Map<T, U> listGroupOne(List<R> list, Function<? super R, ? extends T> classifier, Function<? super
        R, ? extends U> valueMapper) {
        if (null == list || list.isEmpty()) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(classifier, valueMapper, (v1, v2) -> v2));
    }

    public List<RiverRiskPoint> getRiverRiskPoints(Map<String, List<StSnConvertEntity>> stSnMap, Map<String, List<StSectionModelDto>> stSectionModelDtoMap, List<StStbprpBEntity> stStbprpBEntities, Map<String, List<StWaterRiskForecastDto>> stWaterRiskForecastMap) {
        List<RiverRiskPoint> list = new CopyOnWriteArrayList<>();

        List<GateStationRelatedDto> gateStationRelatedDtos = null != redisService.getCacheObject("AllGateStationRelated") ? redisService.getCacheObject("AllGateStationRelated") : gateStationRelatedDao.selectList(new QueryWrapper<>());
        List<StSideGateDto> stSideGateDtos = null != redisService.getCacheObject("AllStSideGateList") ? redisService.getCacheObject("AllStSideGateList") : stSideGateDao.selectList(new QueryWrapper<>());
        Map<String, GateStationRelatedDto> gateStationRelatedDtoMapByStcd = listGroupOne(gateStationRelatedDtos, GateStationRelatedDto::getStcd, v -> v);
        Map<String, StSideGateDto> stSideGateDtoMapByStnm = listGroupOne(stSideGateDtos, StSideGateDto::getStnm, v -> v);


        StringBuilder logSb = new StringBuilder();
        logSb.append("总共有").append(stStbprpBEntities.size()).append("条数据---> 查询表 stStbprpB 条件 sttp IN('ZQ', 'ZZ')\n");
        MapReduceUtil.handle(stStbprpBEntities, stStbprpBEntityUnit -> {
            log.info(stStbprpBEntityUnit.size() + "");
            stStbprpBEntityUnit.forEach(stStbprpBEntity -> {
                String stcd = stStbprpBEntity.getStcd();


                //
                RiverRiskPoint riverRiskPoint = new RiverRiskPoint();
                riverRiskPoint.setAltitudeBottom(stStbprpBEntity.getDtmel());

                List<StWaterRiskForecastDto> stWaterRiskForecastDtos = stWaterRiskForecastMap.get(stcd);
                if (!CollectionUtils.isEmpty(stWaterRiskForecastDtos)) {
                    StWaterRiskForecastDto stWaterRiskForecastDto = stWaterRiskForecastDtos.get(0);
                    Date time = stWaterRiskForecastDto.getTime();
                    String type = stWaterRiskForecastDto.getType();
                    riverRiskPoint.setTime(time);
                    riverRiskPoint.setType(type);
                }


                riverRiskPoint.setStnm(stStbprpBEntity.getStnm());
                riverRiskPoint.setLgtd(stStbprpBEntity.getLgtd());
                riverRiskPoint.setLttd(stStbprpBEntity.getLttd());
                String sttp = stStbprpBEntity.getSttp();
                String refSnId;
                if ("ZZ".equals(sttp) && !CollectionUtils.isEmpty(stSnMap.get(stStbprpBEntity.getStcd()))) {
                    refSnId = stSnMap.get(stStbprpBEntity.getStcd()).get(0).getSn();
                } else {
                    if (stStbprpBEntity.getStcd().length() > 2) {
                        refSnId = stStbprpBEntity.getStcd().substring(2);
                    } else {
                        refSnId = "";
                    }
                }
                if ("".equals(refSnId)) {
                    logSb.append(stStbprpBEntity).append("缺失对应的refSnId \n");
                    return;
                }
                String key = stStbprpBEntity.getSectionName2() != null ? stStbprpBEntity.getSectionName2() : stStbprpBEntity.getStnm().contains("（") ? stStbprpBEntity.getStnm().substring(0, stStbprpBEntity.getStnm().indexOf("（")) : stStbprpBEntity.getStnm();
                long l = System.currentTimeMillis();

                List<StSectionModelDto> stSectionModelDtos = stSectionModelDtoMap.get(key);
                StSectionModelDto stSectionModelDto = null;
                if (CollectionUtils.isEmpty(stSectionModelDtos)) {
                    logSb.append(stStbprpBEntity).append("缺少对应stSectionModel\n");
                    log.info(key + "缺少对应stSectionModelDtos");
                } else {
                    stSectionModelDto = stSectionModelDtos.get(0);
                    BigDecimal altitudeLeft = stSectionModelDto.getAltitudeLeft();
                    BigDecimal altitudeRight = stSectionModelDto.getAltitudeRight();
                    BigDecimal altitudeBottom = stSectionModelDto.getAltitudeBottom();
                    riverRiskPoint.setAltitudeLeft(altitudeLeft);
                    riverRiskPoint.setAltitudeRight(altitudeRight);
                    if (riverRiskPoint.getAltitudeBottom() != null)
                        riverRiskPoint.setAltitudeBottom(altitudeBottom);
                }
                Date now = new Date();
//                now.setDate(0);
                now.setHours(0);
                List<StWaterRateEntity> stWaterRateEntities = null != redisService.getCacheObject("refSnId" + refSnId) ? redisService.getCacheObject("refSnId" + refSnId) : stWaterRateDao.selectByOneByDid(refSnId, now);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                    log.info(refSnId + "  " + (System.currentTimeMillis() - l) + "ms");
                    BigDecimal momentRiverPosition = null;
                    StWaterRateEntity stWaterGateDto = stWaterRateEntities.get(0);
                    if ("ZZ".equals(sttp)) {
                        String addrv = stWaterGateDto.getAddrv();
                        if (StringUtils.hasText(addrv)) {
                            BigDecimal bigDecimal = new BigDecimal(addrv);
                            momentRiverPosition = bigDecimal.divide(new BigDecimal("1000"));
                        }
                    } else {
                        if (StringUtils.hasText(stWaterGateDto.getMomentRiverPosition())) {
                            momentRiverPosition = new BigDecimal(stWaterGateDto.getMomentRiverPosition());
                        } else {
//                            logSb.append(stStbprpBEntity).append("缺少对应最新实时流量数据 \n");
                        }

                    }
                    if (momentRiverPosition != null) {
                        riverRiskPoint.setMomentRiverPosition(momentRiverPosition.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                        if (stStbprpBEntity.getWrz() != null && stStbprpBEntity.getDtmel() != null) {
                            riverRiskPoint.setFromWaring(momentRiverPosition.subtract(stStbprpBEntity.getWrz()).add(stStbprpBEntity.getDtmel()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        } else {
                            GateStationRelatedDto gateStationRelatedDto = gateStationRelatedDtoMapByStcd.get(stStbprpBEntity.getStcd());
                            if (gateStationRelatedDto != null) {
                                String gateName = gateStationRelatedDto.getGateName();
                                StSideGateDto stSideGateDto = stSideGateDtoMapByStnm.get(gateName);
                                if (stSideGateDto != null && stSideGateDto.getWrz() != null && stStbprpBEntity.getDtmel() != null) {
                                    riverRiskPoint.setFromWaring(momentRiverPosition.subtract(stSideGateDto.getWrz()).add(stStbprpBEntity.getDtmel()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                                } else {
                                    logSb.append(stStbprpBEntity).append("stSideGateDto stSideGateDto.getWrz() stStbprpBEntity.getDtmel() ").append(stSideGateDto + " " + stSideGateDto.getWrz()
                                            + " " + stStbprpBEntity.getDtmel() + "\n");
                                }

                            } else {
                                logSb.append(stStbprpBEntity).append("缺少对应gateStationRelatedByStcd \n");
                            }
                        }

                        if (CollectionUtils.isEmpty(stSectionModelDtos)) {
                            logSb.append(stStbprpBEntity).append("缺少对应stSectionModel \n");
                            log.info(refSnId + "缺少对应stSectionModelDtos");
                        } else {
                            stSectionModelDto = stSectionModelDtos.get(0);
                            BigDecimal altitudeLeft = stSectionModelDto.getAltitudeLeft();
                            BigDecimal altitudeRight = stSectionModelDto.getAltitudeRight();
                            BigDecimal altitudeBottom = stSectionModelDto.getAltitudeBottom();
                            BigDecimal altitudeMin = altitudeLeft.subtract(altitudeRight).longValue() > 0 ? altitudeRight : altitudeLeft;

                            BigDecimal mandi = momentRiverPosition.add(altitudeBottom).subtract(altitudeMin);
                            riverRiskPoint.setFromMandi(mandi.setScale(2, BigDecimal.ROUND_HALF_UP).toString());


                        }
                    } else {
                        logSb.append(stStbprpBEntity).append("缺少对应最新实时流量数据 \n");
                    }


                } else {
                    logSb.append(stStbprpBEntity).append("缺少对应最新实时数据 \n");
                    log.info(refSnId + "缺少对应最新数据");
                }
                list.add(riverRiskPoint);
            });
            return null;
        });

        List<RiverRiskPoint> collect = list.stream().sorted(((o1, o2) -> {

            int i1 = (StringUtils.hasText(o1.getFromMandi()) ? 1 : 0) + (StringUtils.hasText(o1.getFromWaring()) ? 1 : 0);
            int i2 = (StringUtils.hasText(o2.getFromMandi()) ? 1 : 0) + (StringUtils.hasText(o2.getFromWaring()) ? 1 : 0);
            return i1 > i2 ? -1 : (i1 == i2 ? 0 : 1);
        })).sorted((o1, o2) -> {
            String type1 = o1.getType();
            String type2 = o2.getType();
            if (type1 == null || type2 == null || type1.equals(type2)) {
                return 0;
            }
            if (type1.equals("漫堤")) {
                return -1;
            }
            if (type1.equals("超警戒")) {
                return type2.equals("无风险") ? -1 : 1;
            } else {
                return 1;
            }

        }).collect(Collectors.toList());
        log.info(logSb.toString());
        return collect;
    }

    private static List<String> split(String str, final String delim) {
        List<String> stringList = new ArrayList<>();
        while (true) {
            int index = str.indexOf(delim);
            if (index < 0) {
                stringList.add(str);
                break;
            }
            stringList.add(str.substring(0, index));
            str = str.substring(index + delim.length());
        }
        return stringList;
    }

    public static void main(String[] args) {
        Random random = new Random();
        String[] strings = new String[]{"无风险", "无风险", "无风险", "无风险", "无风险", "无风险", "超警戒", "超警戒", "漫堤", "漫堤"};
        for (int i = 0; i < 1000; i++) {
            int i1 = random.nextInt(10);
            if (i1 == 8) {
                System.out.println("9");
            }
            if (i1 == 0) {
                System.out.println("0");
            }
            System.out.println(i1);
            System.out.println(strings[i1]);
        }

    }
}
