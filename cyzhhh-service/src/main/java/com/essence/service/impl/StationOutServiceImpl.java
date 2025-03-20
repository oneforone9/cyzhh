package com.essence.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.exception.BusinessException;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.StationOutService;
import com.essence.interfaces.dot.GateStationOutVo;
import com.essence.interfaces.dot.StationOutVo;
import com.essence.interfaces.dot.TimeParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StationOutEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStationOutEtoT;
import com.essence.service.converter.ConverterStationOutTtoR;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.management.QueryEval;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 水位流量站(对外)(StationOut)业务层
 *
 * @author BINX
 * @since 2023-05-11 10:33:39
 */
@Service
public class StationOutServiceImpl extends BaseApiImpl<StationOutEsu, StationOutEsp, StationOutEsr, StationOutDto> implements StationOutService {

    @Autowired
    private StationOutDao stationOutDao;
    @Autowired
    StStbprpBDao stStbprpBDao;
    @Autowired
    StSnConvertDao stSnConvertDao;
    @Resource
    private StPumpConvertDao pumpConvertDao;
    @Autowired
    StWaterRateDao stWaterRateDao;
    @Autowired
    GateStationOutDao gateStationOutDao;
    @Autowired
    StWaterGateDao stWaterGateDao;
    @Autowired
    StGaConvertDao stGaConvertDao;
    @Autowired
    private ConverterStationOutEtoT converterStationOutEtoT;
    @Autowired
    private ConverterStationOutTtoR converterStationOutTtoR;
    @Autowired
    private StRainDateDao stRainDateDao;
    @Autowired
    private CyWeatherForecastDao cyWeatherForecastDao;
    @Resource
    private WaterBzDao waterBzDao;
    @Resource
    private StSideGateDao sideGateDao;

    public StationOutServiceImpl(StationOutDao stationOutDao, ConverterStationOutEtoT converterStationOutEtoT, ConverterStationOutTtoR converterStationOutTtoR) {
        super(stationOutDao, converterStationOutEtoT, converterStationOutTtoR);
    }

    @SneakyThrows
    @Override
    public List<StationOutVo> getStationData(TimeParam timeParam) {
        List<StationOutVo> resList = new ArrayList<>();
        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null == timeParam.getStartTime() || null == timeParam.getEndTime()) {
            throw new BusinessException("请输入开始结束时间! 开始时间字段: startTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss; 结束时间字段: endTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        }
        if (timeParam.getEndTime().getTime() - timeParam.getStartTime().getTime() > 60 * 60 * 1000) {
            throw new BusinessException("注意: 时间段最大间隔不能大于一小时!");
        }
        List<StationOutDto> stationOutDtos = stationOutDao.selectList(new QueryWrapper<>());
        for (StationOutDto stationOutDto : stationOutDtos) {
            HashMap<String, String> flow = new LinkedHashMap<>();
            HashMap<String, String> waterLevel = new LinkedHashMap<>();
            String sttp = "";
            QueryWrapper<StStbprpBEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stcd", stationOutDto.getStcd());
            StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectOne(queryWrapper);
            switch (stStbprpBEntity.getSttp()) {
                case "ZZ":
                    sttp = "水位站";
                    QueryWrapper convertQuery = new QueryWrapper();
                    convertQuery.eq("stcd", stStbprpBEntity.getStcd());
                    StSnConvertEntity stSnConvertEntities = stSnConvertDao.selectOne(convertQuery);
                    if (null != stSnConvertEntities && StrUtil.isNotEmpty(stSnConvertEntities.getSn())) {
                        QueryWrapper queryWrapper1 = new QueryWrapper();
                        queryWrapper1.in("did", stSnConvertEntities.getSn());
                        queryWrapper1.le("ctime", zzFormat.format(timeParam.getEndTime()));
                        queryWrapper1.ge("ctime", zzFormat.format(timeParam.getStartTime()));
                        queryWrapper1.orderByAsc("ctime");
                        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(queryWrapper1);
                        // 水位站的水位 需要转换下单位
                        for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                            if (StrUtil.isNotEmpty(stWaterRateEntity.getAddrv())) {
                                stWaterRateEntity.setAddrv(String.valueOf(new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP)));
                            } else {
                                stWaterRateEntity.setAddrv(String.valueOf(BigDecimal.ZERO));
                            }
                            try {
                                waterLevel.put(zqFormat.format(zzFormat.parse(stWaterRateEntity.getCtime())), stWaterRateEntity.getAddrv());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case "ZQ":
                    sttp = "流量站";
                    QueryWrapper<StWaterRateEntity> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.in("did", stStbprpBEntity.getStcd().substring(2));
                    queryWrapper2.le("ctime", timeParam.getEndTime());
                    queryWrapper2.ge("ctime", timeParam.getStartTime());
                    queryWrapper2.orderByAsc("ctime");
                    List<StWaterRateEntity> stWaterRateEntities1 = stWaterRateDao.selectList(queryWrapper2);
                    // 流量站的水位
                    for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities1) {
                        if(stWaterRateEntity.getCtime().contains(".")) {
                            flow.put(stWaterRateEntity.getCtime().substring(0, stWaterRateEntity.getCtime().lastIndexOf(".")), stWaterRateEntity.getMomentRate());
                            waterLevel.put(stWaterRateEntity.getCtime().substring(0, stWaterRateEntity.getCtime().lastIndexOf(".")), stWaterRateEntity.getMomentRiverPosition());
                        } else {
                            flow.put(stWaterRateEntity.getCtime(), stWaterRateEntity.getMomentRate());
                            waterLevel.put(stWaterRateEntity.getCtime(), stWaterRateEntity.getMomentRiverPosition());
                        }

                    }
                    break;
                default:
                    break;
            }
            StationOutVo res = new StationOutVo();
            res.setStcd(stationOutDto.getStcd());
            res.setStnm(stationOutDto.getStnm());
            res.setMonitor(stationOutDto.getMonitor());
            res.setFlow(flow);
            res.setSttp(sttp);
            res.setWaterLevel(waterLevel);
            resList.add(res);
        }
        return resList;
    }

    @Override
    public List<StRainDateOutSelect> getRainData(TimeParam timeParam) {
        List<StRainDateOutSelect> stCaseResRainListList = new ArrayList<>();
        //雨量站台账数据
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").isNotNull(StStbprpBEntity::getStcd).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
           List<String> stStbprpBList = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStcd()).collect(Collectors.toList())).orElse(Lists.newArrayList());
            List<StRainDateDto> stRainDateDtos = Optional.ofNullable(stRainDateDao.selectList(new QueryWrapper<StRainDateDto>().lambda().ge(StRainDateDto::getDate, timeParam.getStartTime()).le(StRainDateDto::getDate, timeParam.getEndTime()).in(StRainDateDto::getStationId, stStbprpBList))).orElse(Lists.newArrayList());
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                StRainDateOutSelect stRainDateOutSelect = new StRainDateOutSelect();
                stRainDateOutSelect.setStcd(stStbprpBEntity.getStcd());
                if (!CollectionUtils.isEmpty(stRainDateDtos)) {
                    List<StRainDateDto> stRainDateDtoList = Optional.ofNullable(stRainDateDtos.stream().filter(x -> x.getStationId().equals(stStbprpBEntity.getStcd())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(stRainDateDtoList)) {
                        List<StRainDateOut> stRainDateOutList = stRainDateDtoList.stream().map(x -> {
                            StRainDateOut stRainDateOut = new StRainDateOut();
                            stRainDateOut.setRain(x.getHhRain());
                            stRainDateOut.setStcd(x.getStationId());
                            stRainDateOut.setTm(x.getDate());
                            return stRainDateOut;
                        }).collect(Collectors.toList());
                        stRainDateOutSelect.setRainDate(stRainDateOutList);
                    }
                }
                stCaseResRainListList.add(stRainDateOutSelect);
            }
        }
        return stCaseResRainListList;
    }

    @Override
    public List<CyWeatherForecastOut> getCyWeatherForecast(TimeParam timeParam) {
        List<CyWeatherForecastOut> cyWeatherForecastOutList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<CyWeatherForecastDto> cyWeatherForecastDtos = Optional.ofNullable(cyWeatherForecastDao.selectList(new QueryWrapper<CyWeatherForecastDto>().lambda().ge(CyWeatherForecastDto::getPublishTime, simpleDateFormat.format(timeParam.getStartTime())).le(CyWeatherForecastDto::getPublishTime, simpleDateFormat.format(timeParam.getEndTime())).orderByAsc(CyWeatherForecastDto::getPublishTime))).orElse(Lists.newArrayList());
        cyWeatherForecastOutList = cyWeatherForecastDtos.stream().map(x -> {
            CyWeatherForecastOut cyWeatherForecastOut = new CyWeatherForecastOut();
            cyWeatherForecastOut.setPublishDepartment(x.getPublishDepartment());
            cyWeatherForecastOut.setPublishTime(x.getPublishTime());
            cyWeatherForecastOut.setWeatherType(x.getWeatherType());
            cyWeatherForecastOut.setWeatherLevel(x.getWeatherLevel());
            cyWeatherForecastOut.setStatus(x.getStatus());
            cyWeatherForecastOut.setContext(x.getContext());
            cyWeatherForecastOut.setDefence(x.getDefence());
            cyWeatherForecastOut.setMsg(x.getMsg());
            cyWeatherForecastOut.setIcon(x.getIcon());
            cyWeatherForecastOut.setNowWaring(x.getNowWaring());
            return cyWeatherForecastOut;
        }).collect(Collectors.toList());
        return cyWeatherForecastOutList;
    }

    @SneakyThrows
    @Override
    public List<GateStationOutVo> getGateStationData(TimeParam timeParam) {
        List<GateStationOutVo> resList1 = new ArrayList<>();
        List<GateStationOutVo> resList2 = new ArrayList<>();
        List<GateStationOutVo> resList3 = new ArrayList<>();
        SimpleDateFormat zzFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat zqFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null == timeParam.getStartTime() || null == timeParam.getEndTime()) {
            throw new BusinessException("请输入开始结束时间! 开始时间字段: startTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss; 结束时间字段: endTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        }
        if (timeParam.getEndTime().getTime() - timeParam.getStartTime().getTime() > 60 * 60 * 1000) {
            throw new BusinessException("注意: 时间段最大间隔不能大于一小时!");
        }
        QueryWrapper<GateStationOutDto> queryWrapper = new QueryWrapper<>();
        List<GateStationOutDto> gateStationOutDtos = gateStationOutDao.selectList(queryWrapper);
        // 优先取水位站的水位数据
        List<String> zzWaterList = new ArrayList<>();
        for (GateStationOutDto gateStationOutDto : gateStationOutDtos) {
            if (StrUtil.isNotEmpty(gateStationOutDto.getSttp())) {
                switch (gateStationOutDto.getSttp()) {
                    case "ZZ":
                        QueryWrapper convertQuery = new QueryWrapper();
                        convertQuery.eq("stcd", gateStationOutDto.getStcd());
                        StSnConvertEntity stSnConvertEntities = stSnConvertDao.selectOne(convertQuery);
                        if (null == stSnConvertEntities) {
                            break;
                        }
                        QueryWrapper queryWrapper1 = new QueryWrapper();
                        queryWrapper1.in("did", stSnConvertEntities.getSn());
                        queryWrapper1.le("ctime", zzFormat.format(timeParam.getEndTime()));
                        queryWrapper1.ge("ctime", zzFormat.format(timeParam.getStartTime()));
                        queryWrapper1.orderByAsc("ctime");
                        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(queryWrapper1);
                        // 水位站的水位 需要转换下单位
                        for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                            GateStationOutVo res = new GateStationOutVo();
                            res.setGateDam(gateStationOutDto.getGateName());
                            res.setStreamLoc(gateStationOutDto.getStreamLoc());
                            if (StrUtil.isNotEmpty(stWaterRateEntity.getAddrv())) {
                                stWaterRateEntity.setAddrv(String.valueOf(new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP)));
                            } else {
                                stWaterRateEntity.setAddrv(String.valueOf(BigDecimal.ZERO));
                            }
                            try {
                                res.setLevel(stWaterRateEntity.getAddrv());
                                res.setTm(zzFormat.parse(stWaterRateEntity.getCtime()));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            resList1.add(res);
                            zzWaterList.add(res.getGateDam());
                        }
                        break;
                    case "ZQ":
                        QueryWrapper<StWaterRateEntity> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.in("did", gateStationOutDto.getStcd().substring(2));
                        queryWrapper2.le("ctime", timeParam.getEndTime());
                        queryWrapper2.ge("ctime", timeParam.getStartTime());
                        queryWrapper2.orderByAsc("ctime");
                        List<StWaterRateEntity> stWaterRateEntities1 = stWaterRateDao.selectList(queryWrapper2);
                        // 流量站的水位
                        for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities1) {
                            GateStationOutVo res = new GateStationOutVo();
                            res.setGateDam(gateStationOutDto.getGateName());
                            res.setStreamLoc(gateStationOutDto.getStreamLoc());
                            if (!zzWaterList.contains(res.getGateDam())) {
                                res.setLevel(stWaterRateEntity.getMomentRiverPosition());
                            }
                            res.setFlow(stWaterRateEntity.getMomentRate());
                            try {
                                if(stWaterRateEntity.getCtime().contains(".")) {
                                    res.setTm(zqFormat.parse(stWaterRateEntity.getCtime().substring(0, stWaterRateEntity.getCtime().lastIndexOf("."))));
                                } else {
                                    res.setTm(zqFormat.parse(stWaterRateEntity.getCtime()));
                                }

                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            resList1.add(res);
                        }
                        break;
                    default:
                        GateStationOutVo res = new GateStationOutVo();
                        res.setGateDam(gateStationOutDto.getGateName());
                        res.setStreamLoc(gateStationOutDto.getStreamLoc());
                        resList1.add(res);
                }
            }
            if (StrUtil.isNotEmpty(gateStationOutDto.getStcd())) {
                QueryWrapper<StGaConvertDto> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("stcd", gateStationOutDto.getStcd());
                StGaConvertDto stGaConvertDto = stGaConvertDao.selectOne(queryWrapper1);
                if (null != stGaConvertDto) {
                    List normList = new ArrayList(Arrays.asList("M0.1", "M0.6"));
                    QueryWrapper<StWaterGateDto> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("did", stGaConvertDto.getSn());
                    queryWrapper2.in("addr", normList);
                    queryWrapper2.le("ctime", timeParam.getEndTime());
                    queryWrapper2.ge("ctime", timeParam.getStartTime());
                    queryWrapper2.orderByAsc("ctime");
                    List<StWaterGateDto> stWaterGateDtos = stWaterGateDao.selectList(queryWrapper2);

                    Map<String, List<StWaterGateDto>> collectByDid = stWaterGateDtos.stream().collect(Collectors.groupingBy(StWaterGateDto::getDid, LinkedHashMap::new, Collectors.toCollection(ArrayList::new)));
                    for (Map.Entry<String, List<StWaterGateDto>> stringListEntry : collectByDid.entrySet()) {
                        Map<String, List<StWaterGateDto>> collectByTime = stringListEntry.getValue().stream().collect(Collectors.groupingBy(StWaterGateDto::getCtime, LinkedHashMap::new, Collectors.toCollection(ArrayList::new)));
                        for (Map.Entry<String, List<StWaterGateDto>> listEntry : collectByTime.entrySet()) {
                            GateStationOutVo e = new GateStationOutVo();
                            e.setGateDam(gateStationOutDto.getGateName());
                            e.setStreamLoc(gateStationOutDto.getStreamLoc());
                            String state = "";
                            for (StWaterGateDto stWaterGateDto : listEntry.getValue()) {
                                if (normList.contains(stWaterGateDto.getAddr()) && "1".equals(stWaterGateDto.getAddrv())) {
                                    if(StrUtil.isNotEmpty(state)) {
                                        state = state + ";" + (normList.indexOf(stWaterGateDto.getAddr()) + 1) + "号闸门开启";
                                    } else {
                                        state = state + (normList.indexOf(stWaterGateDto.getAddr()) + 1) + "号闸门开启";
                                    }
                                } else if (normList.contains(stWaterGateDto.getAddr()) && "0".equals(stWaterGateDto.getAddrv())) {
                                    if(StrUtil.isNotEmpty(state)) {
                                        state = state + ";" + (normList.indexOf(stWaterGateDto.getAddr()) + 1) + "号闸门关闭";
                                    } else {
                                        state = state + (normList.indexOf(stWaterGateDto.getAddr()) + 1) + "号闸门关闭";
                                    }
                                }
                            }
                            e.setState(state);
                            e.setTm(zqFormat.parse(listEntry.getKey()));
                            resList2.add(e);
                        }
                    }
                }
            }
        }
        resList3 = mergeListBySameDeclared(resList1, resList2, GateStationOutVo.class, "gateDam", "streamLoc", "tm");
        resList3 = resList3.stream()
                .sorted(Comparator.comparing(o -> o.getTm()))
                .collect(Collectors.toList());
        Set<GateStationOutVo> set = new LinkedHashSet<>(resList3);
        resList3 = new ArrayList<>(set);
        return resList3;
    }

    public <T> List<T> mergeListBySameDeclared(List<T> list1, List<T> list2, Class clazz, String... sameValueDeclared) {
        if(CollectionUtils.isEmpty(list1)) {
            return list2;
        }
        if(CollectionUtils.isEmpty(list2)) {
            return list1;
        }
        if (!clazz.equals(list1.get(0).getClass()) || !clazz.equals(list2.get(0).getClass())) {
            throw new IllegalArgumentException("The declared element type of the lists are different");
        }

        // Create a map of the declared fields' values to objects from list1.
        Map<List<Object>, T> map = new HashMap<>();
        for (T t : list1) {
            List<Object> values = null;
            try {
                values = getValues(t, sameValueDeclared);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            map.put(values, t);
        }

        // Merge objects from list2 into the map, overwriting any previous values with the same field values.
        for (T t : list2) {
            List<Object> values = null;
            try {
                values = getValues(t, sameValueDeclared);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (map.containsKey(values)) {
                T original = map.get(values);
                try {
                    mergeObjects(original, t);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                map.put(values, t);
            }
        }

        // Return the merged objects as a list.
        return new ArrayList<>(map.values());
    }

    private static <T> List<Object> getValues(T object, String... fieldNames) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (String fieldName : fieldNames) {
            Field field;
            try {
                field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(object);
                values.add(value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        return values;
    }

    private static <T> void mergeObjects(T target, T source) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                field.set(target, value);
            }
        }
    }

    public <T> List<T> listDistinct(List<T> list, Function<? super T, ? extends String> classifier) {
        return null == list ? null : list.isEmpty() ? new ArrayList<>() : list.stream().collect(Collectors.toMap(classifier, v->v, (v1,v2)->v1)).values().stream().collect(Collectors.toList());
    }

    @Override
    public String getNumber() {
        System.out.println("开始时间");
        Long beginTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newCachedThreadPool();
        Random random = new Random();

        DecimalFormat df = new DecimalFormat("0.00");
        CompletableFuture<StringBuilder> infoFuture1 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture2 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture3 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture4 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture5 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture6 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture7 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture8 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture9 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture10 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture11 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture12 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture13 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture14 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        CompletableFuture<StringBuilder> infoFuture15 = CompletableFuture.supplyAsync(() -> {
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < 40000; i++) {
                double d = random.nextDouble() * 2.00;
                ss.append(df.format(d) + ",");
            }
            return ss;
        }, executor);
        //等待所有任务完成
        try {
            CompletableFuture.allOf(infoFuture1, infoFuture2, infoFuture3).get();
            StringBuilder stringBuilder1 = infoFuture1.get();
            StringBuilder stringBuilder2 = infoFuture2.get();
            StringBuilder stringBuilder3 = infoFuture3.get();
            StringBuilder stringBuilder4 = infoFuture4.get();
            StringBuilder stringBuilder5 = infoFuture5.get();
            StringBuilder stringBuilder6 = infoFuture6.get();
            StringBuilder stringBuilder7 = infoFuture7.get();
            StringBuilder stringBuilder8 = infoFuture8.get();
            StringBuilder stringBuilder9 = infoFuture9.get();
            StringBuilder stringBuilder10 = infoFuture10.get();
            StringBuilder stringBuilder11 = infoFuture11.get();
            StringBuilder stringBuilder12 = infoFuture12.get();
            StringBuilder stringBuilder13 = infoFuture13.get();
            StringBuilder stringBuilder14 = infoFuture14.get();
            StringBuilder stringBuilder15 = infoFuture15.get();
            stringBuilder1.append(stringBuilder2);
            stringBuilder1.append(stringBuilder3);
            stringBuilder1.append(stringBuilder4);
            stringBuilder1.append(stringBuilder5);
            stringBuilder1.append(stringBuilder6);
            stringBuilder1.append(stringBuilder7);
            stringBuilder1.append(stringBuilder8);
            stringBuilder1.append(stringBuilder9);
            stringBuilder1.append(stringBuilder10);
            stringBuilder1.append(stringBuilder11);
            stringBuilder1.append(stringBuilder12);
            stringBuilder1.append(stringBuilder13);
            stringBuilder1.append(stringBuilder14);
            stringBuilder1.append(stringBuilder15);
            Long opetime = System.currentTimeMillis() - beginTime;
            System.out.println("运行时间" + opetime);
            return stringBuilder1.substring(0, stringBuilder1.length() - 1);
        } catch (Exception e) {
            System.out.println("cuowu");
            return "失败！";
        }
    }

    /**
     * 目前只提供 清河向奥林匹克森林公园调水（泵站1）
     * @param start
     * @param end
     */
    @Override
    public void getPumpOut(String start, String end) {
        QueryWrapper queryGate = new QueryWrapper();
        queryGate.eq("stcd","0030510080");
        StSideGateDto stSideGateDto = sideGateDao.selectOne(queryGate);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sttp","DP");
        List<StPumpConvertEntity> stPumpConvertEntities = pumpConvertDao.selectList(queryWrapper);
//
        if (CollUtil.isNotEmpty(stPumpConvertEntities)){
            Map<String, String> stcdMap = stPumpConvertEntities.parallelStream().collect(Collectors.toMap(StPumpConvertEntity::getStcd, StPumpConvertEntity::getSn, (o1, o2) -> o2));
            String stnm = stSideGateDto.getStnm();
            String holesNumber = stSideGateDto.getHolesNumber();
            String s = stcdMap.get("0030510080");
            QueryWrapper query = new QueryWrapper();
            query.eq("did",s);
            query.between("ctime",start,end );
            List<StWaterBzData> stWaterBzData = waterBzDao.selectList(query);
            for (StWaterBzData stWaterBzDatum : stWaterBzData) {
//                String addr = stWaterBzDatum.getAddr();
//                if (StrUtil.isNotEmpty(addr) && addr.equals())
//
//                String ctime = stWaterBzDatum.getCtime();
//
//                String addrv = stWaterBzDatum.getAddrv();
            }
        }
    }
}
