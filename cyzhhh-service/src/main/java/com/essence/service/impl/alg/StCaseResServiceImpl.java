package com.essence.service.impl.alg;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.common.exception.BusinessException;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.alg.StCaseResDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.RiverGateMaxFlowViewService;
import com.essence.interfaces.api.StCaseResService;
import com.essence.interfaces.api.StationService;
import com.essence.interfaces.dot.*;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StCaseResEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaseResEtoT;
import com.essence.service.converter.ConverterStCaseResTtoR;
import com.essence.service.utils.DataUtils;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 方案执行结果表(StCaseRes)业务层
 *
 * @author BINX
 * @since 2023-04-18 14:39:12
 */
@Service
@Slf4j
public class StCaseResServiceImpl extends BaseApiImpl<StCaseResEsu, StCaseResEsp, StCaseResEsr, StCaseResDto> implements StCaseResService {

    @Autowired
    private StCaseResDao stCaseResDao;
    @Autowired
    private StCaseBaseInfoDao stCaseBaseInfoDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private StWaterRateDao stWaterRateDao;
    @Autowired
    private StSnConvertDao stSnConvertDao;
    @Autowired
    private StSectionModelDao stSectionModelDao;
    @Autowired
    ReaBaseDao reaBaseDao;
    @Autowired
    StSideGateDao stSideGateDao;
    @Autowired
    RiverGateMaxFlowViewService riverGateMaxFlowViewService;

    @Autowired
    private RiverSectionWaterViewDao riverSectionWaterViewDao;

    @Autowired
    private ConverterStCaseResEtoT converterStCaseResEtoT;
    @Autowired
    private ConverterStCaseResTtoR converterStCaseResTtoR;
    @Resource
    private StRainDateDao stRainDateDao;
    @Autowired
    private StDesigRainPatternDao stDesigRainPatternDao;

    @Resource
    private GateStationRelatedDao gateStationRelatedDao;

    @Autowired
    private StationService stationService;
    @Resource
    private StCaseBaseInfoDao caseBaseInfoDao;

    private final Executor executor = Executors.newFixedThreadPool(10);


    public StCaseResServiceImpl(StCaseResDao stCaseResDao, ConverterStCaseResEtoT converterStCaseResEtoT, ConverterStCaseResTtoR converterStCaseResTtoR) {
        super(stCaseResDao, converterStCaseResEtoT, converterStCaseResTtoR);
    }

    @Override
    public List<ForecastPerformanceDto> getRainTendency(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        List<ForecastPerformanceDto> forecastPerformanceDtoList = new ArrayList<>();
        Date start = stCaseBaseInfoEsu.getPreHotTime();
        Date end = stCaseBaseInfoEsu.getForecastStartTime();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("date", start);
        wrapper.le("date", end);
        List<StRainDateDto> stRainDateDtos = stRainDateDao.selectList(wrapper);
        List<Date> timeSplit = DataUtils.getTimeSplit(start, end, 60, DateField.MINUTE);
        if (CollUtil.isNotEmpty(stRainDateDtos)) {
            for (int i = 0; i < timeSplit.size() - 1; i++) {
                Date begin = timeSplit.get(i);
                Date over = timeSplit.get(i + 1);
                List<StRainDateDto> betweenList = Optional.ofNullable(stRainDateDtos.parallelStream().filter(stRainDateDto -> {
                    boolean flag = stRainDateDto.getDate().getTime() >= begin.getTime() && stRainDateDto.getDate().getTime() <= over.getTime();
                    return flag;
                }).collect(Collectors.toList())).orElse(Lists.newArrayList());
                ForecastPerformanceDto forecastPerformanceDto = new ForecastPerformanceDto();
                forecastPerformanceDto.setType(1);
                forecastPerformanceDto.setDate(over);
                Double sum = 0.0;
                if (!CollectionUtils.isEmpty(betweenList)) {
                    List<StRainDateDto> stRainDateDtoList = Optional.ofNullable(betweenList.stream().filter(x -> StringUtil.isNotBlank(x.getHhRain())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(stRainDateDtoList)) {
                        sum = stRainDateDtoList.stream().mapToDouble(x -> Double.valueOf(x.getHhRain().equals("9999") ? "0" : x.getHhRain())).sum();
                    }
                }
                forecastPerformanceDto.setAddRainNum(sum.toString());
                forecastPerformanceDtoList.add(forecastPerformanceDto);
            }
        }
        return forecastPerformanceDtoList;
    }

    @Override
    public StCaseResRainLists selectRain(StCaseResRainQuery stCaseResRainQuery) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StCaseResRainLists stCaseResRainLists = new StCaseResRainLists();
        List<StCaseResRainList> stCaseResRainListList = new ArrayList<>();
        //雨量站台账数据
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").isNotNull(StStbprpBEntity::getSectionName).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            List<String> collect = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStnm()).collect(Collectors.toList())).orElse(Lists.newArrayList());
            stCaseResRainLists.setStnmList(collect);
            stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).collect(Collectors.toList());
            List<String> stStbprpBList = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStcd()).collect(Collectors.toList())).orElse(Lists.newArrayList());
            List<StRainDateDto> stRainDateDtos = Optional.ofNullable(stRainDateDao.selectList(new QueryWrapper<StRainDateDto>().lambda().ge(StRainDateDto::getDate, stCaseResRainQuery.getStartTime()).le(StRainDateDto::getDate, stCaseResRainQuery.getEndTime()).in(StRainDateDto::getStationId, stStbprpBList))).orElse(Lists.newArrayList());
            List<Date> timeSplit = DataUtils.getTimeSplit(stCaseResRainQuery.getStartTime(), stCaseResRainQuery.getEndTime(), stCaseResRainQuery.getStep(), DateField.MINUTE);
            if (CollUtil.isNotEmpty(stRainDateDtos)) {
                stRainDateDtos.forEach(x -> {
                    if (StringUtil.isBlank(x.getHhRain()) || x.getHhRain().equals("9999")) {
                        x.setHhRain("0");
                    }
                });
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StCaseResRainList stCaseResRainList = new StCaseResRainList();
                    stCaseResRainList.setTime(df.format(timeSplit.get(i)));
                    Date begin = timeSplit.get(i);
                    Date over = timeSplit.get(i + 1);
                    List<StCaseResRain> dataList = new ArrayList<>();
                    for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                        StCaseResRain stCaseResRain = new StCaseResRain();
                        stCaseResRain.setStnm(stStbprpBEntity.getStnm());
                        List<StRainDateDto> stRainDateDtoList = stRainDateDtos.stream().filter(x -> x.getStationId().equals(stStbprpBEntity.getStcd()) && x.getDate().getTime() >= begin.getTime() && x.getDate().getTime() <= over.getTime()).collect(Collectors.toList());
                        Double rain = 0.0;
                        if (CollUtil.isNotEmpty(stRainDateDtoList)) {
                            rain = stRainDateDtoList.stream().mapToDouble(x -> Double.valueOf(x.getHhRain())).sum();
                        }
                        stCaseResRain.setRain(rain);
                        stCaseResRain.setJd(stStbprpBEntity.getLgtd().doubleValue());
                        stCaseResRain.setWd(stStbprpBEntity.getLttd().doubleValue());
                        dataList.add(stCaseResRain);
                    }
                    stCaseResRainList.setDataList(dataList);
                    stCaseResRainListList.add(stCaseResRainList);
                }
            } else {
                //如果实测降雨 查询不到数据就补充0
                for (int i = 0; i < timeSplit.size() - 1; i++) {
                    StCaseResRainList stCaseResRainList = new StCaseResRainList();
                    stCaseResRainList.setTime(df.format(timeSplit.get(i)));
                    Date begin = timeSplit.get(i);
                    Date over = timeSplit.get(i + 1);
                    List<StCaseResRain> dataList = new ArrayList<>();
                    for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                        StCaseResRain stCaseResRain = new StCaseResRain();
                        stCaseResRain.setStnm(stStbprpBEntity.getStnm());
                        List<StRainDateDto> stRainDateDtoList = stRainDateDtos.stream().filter(x -> x.getStationId().equals(stStbprpBEntity.getStcd()) && x.getDate().getTime() >= begin.getTime() && x.getDate().getTime() <= over.getTime()).collect(Collectors.toList());
                        Double rain = 0.0;
                        if (CollUtil.isNotEmpty(stRainDateDtoList)) {
                            rain = stRainDateDtoList.stream().mapToDouble(x -> Double.valueOf(x.getHhRain())).sum();
                        }
                        stCaseResRain.setJd(stStbprpBEntity.getLgtd().doubleValue());
                        stCaseResRain.setWd(stStbprpBEntity.getLttd().doubleValue());
                        stCaseResRain.setRain(rain);
                        dataList.add(stCaseResRain);
                    }
                    stCaseResRainList.setDataList(dataList);
                    stCaseResRainListList.add(stCaseResRainList);
                }
            }
        }

        stCaseResRainLists.setStCaseResRainList(stCaseResRainListList);
        return stCaseResRainLists;
    }

    @Override
    public List<StCaseResFlowList> selectFlow(StCaseResRainQuery stCaseResRainQuery) {
        List<StCaseResFlowList> stCaseResRainListList = new ArrayList<>();
        //流量站台账数据  ZQ流量
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "ZQ").isNotNull(StStbprpBEntity::getSectionName).eq(StStbprpBEntity::getSectionFlag, "入流").orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).collect(Collectors.toList());
            List<String> stStbprpBList = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStcd().substring(2)).collect(Collectors.toList())).orElse(Lists.newArrayList());
            List<StWaterRateEntity> stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getStartTime(), "yyyy-MM-dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getEndTime(), "yyyy-MM-dd HH:mm:ss")).in(StWaterRateEntity::getDid, stStbprpBList))).orElse(Lists.newArrayList());
            if (CollUtil.isNotEmpty(stRainDateDtos)) {
                Map<String, List<StWaterRateEntity>> map = stRainDateDtos.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                    List<StWaterRateEntity> stWaterRateEntities = Optional.ofNullable(map.get(stStbprpBEntity.getStcd().substring(2))).orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                        List<StWaterRateEntity> stWaterRateEntityList = stWaterRateEntities.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime).reversed()).collect(Collectors.toList());
                        Map<String, StWaterRateEntity> hourMap = stWaterRateEntityList.stream().collect(Collectors.toMap(stWaterRateEntity -> {
                            return stWaterRateEntity.getCtime().substring(0, 13);
                        }, Function.identity(), (o2, o1) -> o2));
                        for (String s : hourMap.keySet()) {
                            StWaterRateEntity stWaterRateEntity = hourMap.get(s);
                            StCaseResFlowList stCaseResFlowList = new StCaseResFlowList();
                            stCaseResFlowList.setStnm(stStbprpBEntity.getStnm());
                            stCaseResFlowList.setTime(stWaterRateEntity.getCtime());
                            stCaseResFlowList.setFlow(stWaterRateEntity.getMomentRate());
                            stCaseResFlowList.setJd(stStbprpBEntity.getLgtd().doubleValue());
                            stCaseResFlowList.setWd(stStbprpBEntity.getLttd().doubleValue());
                            stCaseResRainListList.add(stCaseResFlowList);
                        }

                    }
                }
            }
        }
        //补充亮马河
        String momentRate = "0.5";
        List<Date> timeSplit = DataUtils.getTimeSplit(stCaseResRainQuery.getStartTime(), stCaseResRainQuery.getEndTime(), 1, DateField.HOUR);
        for (Date date : timeSplit) {
            StCaseResFlowList stCaseResFlowList = new StCaseResFlowList();
            stCaseResFlowList.setStnm("亮马河");
            stCaseResFlowList.setTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            stCaseResFlowList.setFlow(momentRate);
            stCaseResRainListList.add(stCaseResFlowList);
        }
        if (CollUtil.isNotEmpty(stCaseResRainListList)) {
            stCaseResRainListList = stCaseResRainListList.stream().sorted(Comparator.comparing(StCaseResFlowList::getStnm).thenComparing(StCaseResFlowList::getTime)).collect(Collectors.toList());
        }
        return stCaseResRainListList;

    }

    public List<RainDateDto> findRain(StCaseBaseInfoDto stCaseBaseInfoDto) {
        List<RainDateDto> floodDateList = new ArrayList<>();
        if (StringUtils.isNotBlank(stCaseBaseInfoDto.getRainId())) {
            StDesigRainPatternDto stDesigRainPatternDto = stDesigRainPatternDao.selectById(stCaseBaseInfoDto.getRainId());
            if (null != stDesigRainPatternDto) {
                Date start = stCaseBaseInfoDto.getForecastStartTime();
                Date end = stCaseBaseInfoDto.getPreSeeTime();
                //雨型时间区间
                List<Date> desigRain = DataUtils.getTimeSplit(start, end, stDesigRainPatternDto.getTimeInterval(), DateField.MINUTE);
                String param = stDesigRainPatternDto.getParam();
                String[] split = param.split(",");
                List<String> stringList = Arrays.asList(split);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < desigRain.size() - 1; i++) {
                    RainDateDto rainDateDto = new RainDateDto();
                    Double sun = Double.valueOf(stringList.get(i)) * Double.valueOf(stCaseBaseInfoDto.getRainTotal());
                    rainDateDto.setHhRain(sun.toString());
                    rainDateDto.setDate(desigRain.get(i));
                    floodDateList.add(rainDateDto);
                }
            }
        }
        return floodDateList;
    }

    @Override
    public StCaseResRainLists selectRainFallPattern(String caseId) {
        StCaseResRainLists stCaseResRainLists = new StCaseResRainLists();
        StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectById(caseId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //流量站台账数据  ZQ流量
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").isNotNull(StStbprpBEntity::getSectionName).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).filter(x -> StringUtil.isEmpty(x.getSectionFlag())).collect(Collectors.toList());
            List<String> stnmLists = stStbprpBEntityList.stream().map(x -> x.getStnm()).collect(Collectors.toList());
            stCaseResRainLists.setStnmList(stnmLists);
            List<RainDateDto> rain = findRain(stCaseBaseInfoDto);
            if (!CollectionUtils.isEmpty(rain)) {
                List<StCaseResRainList> stCaseResRainListList = new ArrayList<>();
                for (int i = 0; i < rain.size() - 1; i++) {
                    StCaseResRainList stCaseResRainList = new StCaseResRainList();
                    RainDateDto rainDateDto = rain.get(i);
                    stCaseResRainList.setTime(df.format(rainDateDto.getDate()));

                    List<StCaseResRain> dataList = new ArrayList<>();
                    for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                        StCaseResRain stCaseResRain = new StCaseResRain();
                        stCaseResRain.setStnm(stStbprpBEntity.getStnm());
                        stCaseResRain.setRain(Double.valueOf(rainDateDto.getHhRain()));
                        dataList.add(stCaseResRain);
                    }
                    stCaseResRainList.setDataList(dataList);
                    stCaseResRainListList.add(stCaseResRainList);
                }
                stCaseResRainLists.setStCaseResRainList(stCaseResRainListList);
            }
        }

        return stCaseResRainLists;
    }

    @Override
    public List<RiverGateWaterDto> getRiverGateWaterLevel(String rvid, String caseId) {
//        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
//        Predicate<StStbprpBEntity> predicate = stStbprpBEntity -> StrUtil.isNotEmpty(stStbprpBEntity.getStcd()) ;
//        Map<String, StStbprpBEntity> stcdMap = stStbprpBEntities.parallelStream().filter(predicate).collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity(), (o1, o2) -> o1));
        // 1.先去查询河流下的水闸相关数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("river_id", rvid);
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(wrapper);
        List<String> collect1 = stSideGateDtos.parallelStream().map(StSideGateDto::getStnm).collect(Collectors.toList());

        QueryWrapper query = new QueryWrapper();
        if (CollUtil.isNotEmpty(collect1)) {
            query.in("gate_name", collect1);
        }
        List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(query);
        //只剩下水位站 流量站
        List<GateStationRelatedDto> stationRelatedDtos = gateStationRelatedDtos.parallelStream().filter(gateStationRelatedDto -> {
            return StrUtil.isNotEmpty(gateStationRelatedDto.getStreamLoc());
        }).filter(gateStationRelatedDto -> {
            return !"CA".equals(gateStationRelatedDto.getSttp());
        }).collect(Collectors.toList());

        //按照水闸名称进行分组
        Map<String, List<GateStationRelatedDto>> collect = stationRelatedDtos.stream().collect(Collectors.groupingBy(GateStationRelatedDto::getGateName));

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", caseId);
        StCaseBaseInfoDto stCaseBaseInfoDto = caseBaseInfoDao.selectOne(queryWrapper);


        Date start = null; //stCaseBaseInfoDto.getForecastStartTime();
        Date day = null;
        String str = DateUtil.format(stCaseBaseInfoDto.getPreHotTime(), "yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.format(stCaseBaseInfoDto.getForecastStartTime(), "yyyy-MM-dd HH:mm:ss");
        //如果同一天则 开始时间 结束时间这样设置  否则开始时间往前推一天
//        if (DateUtil.format(stCaseBaseInfoDto.getForecastStartTime(),"yyyy-MM-dd HH:mm:ss").substring(0,10).equals(DateUtil.format(stCaseBaseInfoDto.getPreSeeTime(),"yyyy-MM-dd HH:mm:ss").substring(0,10)) ){
        start = DateUtil.parse(str); //stCaseBaseInfoDto.getForecastStartTime();
        day = DateUtil.parse(end);//stCaseBaseInfoDto.getPreSeeTime();
//        }else {
//            start = DateUtil.offsetDay(DateUtil.parse(str),-1); //stCaseBaseInfoDto.getForecastStartTime();
//            day = DateUtil.parse(end);//s
//        }

        //开始时间 和 结束时间线上时候可以是当前时间-1天只替换年月日

        String endZZ = "";
        String startZZ = "";
        Map<String, List<StWaterRateEntityDTO>> map = new HashMap<>();
        if (CollUtil.isNotEmpty(collect)) {
            //s: gate 名称
            for (String s : collect.keySet()) {
                List<GateStationRelatedDto> gateStationRelatedDtos1 = collect.get(s);
                //取第一个站
                GateStationRelatedDto gateStationRelatedDto = gateStationRelatedDtos1.get(0);
                String type = gateStationRelatedDto.getSttp();
                String stcd = gateStationRelatedDto.getStcd();
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
                stStbprpBEntityDTO.setStcd(stcd);
                //水位站或者流量站数据
                List<StWaterRateEntityDTO> stationDataListZZ = stationService.getStationDataMomentList(stStbprpBEntityDTO);
                //组装数据
                map.put(s, stationDataListZZ);
            }
        }
        List<RiverGateWaterDto> list = dealGateWaterData(map);
        return list;
    }


    public List<RiverGateWaterDto> getWaterLevel(String caseId, List<StStbprpBEntity> stStbprpBEntities) throws ParseException {

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", caseId);
        StCaseBaseInfoDto stCaseBaseInfoDto = caseBaseInfoDao.selectOne(queryWrapper);


        Date start = null; //stCaseBaseInfoDto.getForecastStartTime();
        Date day = null;
        String str = DateUtil.format(stCaseBaseInfoDto.getPreHotTime(), "yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.format(stCaseBaseInfoDto.getForecastStartTime(), "yyyy-MM-dd HH:mm:ss");
        start = DateUtil.parse(str); //stCaseBaseInfoDto.getForecastStartTime();
        day = DateUtil.parse(end);//stCaseBaseInfoDto.getPreSeeTime();

        //开始时间 和 结束时间线上时候可以是当前时间-1天只替换年月日

        String endZZ = "";
        String startZZ = "";
        Map<String, List<StWaterRateEntityDTO>> map = new HashMap<>();
        if (CollUtil.isNotEmpty(stStbprpBEntities)) {
            //s: gate 名称
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {

                String type = stStbprpBEntity.getSttp();
                String stcd = stStbprpBEntity.getStcd();
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
                stStbprpBEntityDTO.setStcd(stcd);
                //水位站或者流量站数据
                List<StWaterRateEntityDTO> stationDataListZZ = stationService.getStationDataMomentList(stStbprpBEntityDTO);
                //组装数据
                map.put(stStbprpBEntity.getSectionName2(), stationDataListZZ);
            }
        }
        List<RiverGateWaterDto> list = dealGateWaterData(map, stCaseBaseInfoDto);
        return list;
    }

    @Override
    public void strategyFixCurve(String oldCaseId, String newCaseName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("case_id", oldCaseId);
        List<StCaseResDto> stCaseResDtos = stCaseResDao.selectList(queryWrapper);

        List<List<StCaseResDto>> partition = Lists.partition(stCaseResDtos, 3000);
        for (List<StCaseResDto> caseResDtos : partition) {
            List<StCaseResDto> list = caseResDtos.parallelStream().map(stCaseResDto -> {
                stCaseResDto.setCaseId(null);
                stCaseResDto.setRiverZ(Double.valueOf(stCaseResDto.getRiverZ() + 5d).toString());
                stCaseResDto.setStnm(newCaseName);
                return stCaseResDto;
            }).collect(Collectors.toList());

            stCaseResDao.saveFloodModelOutCellStatistic(list);
        }

    }

    @Override
    public List<RiverStepSectionDto> getRiverFactSection(StCaseBaseInfoEsu stCaseBaseInfoEsu) throws ParseException, ExecutionException, InterruptedException {
        List<RiverStepSectionDto> resList = new ArrayList<>();

        CompletableFuture<Map<String, List<RiverSectionWaterViewDto>>> future = CompletableFuture.supplyAsync(() -> {
            if (null == stCaseBaseInfoEsu.getId()) {
                //默认随机选一个
                List<StCaseBaseInfoDto> stCaseBaseInfoDtos = caseBaseInfoDao.selectList(new QueryWrapper<>());
                StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDtos.get(0);
                stCaseBaseInfoEsu.setId(stCaseBaseInfoDto.getId());
            } else {
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", stCaseBaseInfoEsu.getId());
                StCaseBaseInfoDto stCaseBaseInfoDtos = caseBaseInfoDao.selectOne(queryWrapper);
                BeanUtil.copyProperties(stCaseBaseInfoDtos, stCaseBaseInfoEsu);
            }
            QueryWrapper<RiverSectionWaterViewDto> queryWrapper = new QueryWrapper<>();
            if (null != stCaseBaseInfoEsu.getRiverId()) {
                queryWrapper.eq("river_id", stCaseBaseInfoEsu.getRiverId());
            }
            queryWrapper.eq("case_id", stCaseBaseInfoEsu.getId());
            queryWrapper.eq("data_type", "2");
            List<RiverSectionWaterViewDto> rivers = riverSectionWaterViewDao.selectList(queryWrapper);
            Map<String, List<RiverSectionWaterViewDto>> riversMap = rivers.parallelStream().collect(
                    Collectors.groupingBy(
                            river -> river.getStep()
                    ));
            return riversMap;
        }, executor);


        CompletableFuture<List<StStbprpBEntity>> future2 = CompletableFuture.supplyAsync(() -> {
            // 查询水位站
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("rvnm", stCaseBaseInfoEsu.getRiverId());
            List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(queryWrapper1);
            return stStbprpBEntities;
        }, executor);
        List<StStbprpBEntity> stStbprpBEntities = future2.get();

        Map<String, List<RiverSectionWaterViewDto>> riversMap = future.get();

        Map<String, StStbprpBEntity> collect = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getSectionName2, Function.identity(), (o1, o2) -> o1));

        List<RiverGateWaterDto> riverGateWaterLevel = this.getWaterLevel(stCaseBaseInfoEsu.getId(), stStbprpBEntities);
        Map<String, RiverGateWaterDto> factGateMap = riverGateWaterLevel.parallelStream().collect(Collectors.toMap(RiverGateWaterDto::getGateName, Function.identity()));
        int num = 0;
        for (String s : riversMap.keySet().stream().sorted().collect(Collectors.toList())) {
            RiverStepSectionDto stepSectionDto = new RiverStepSectionDto();
            stepSectionDto.setStep(s);
            List<RiverSectionViewDto> riverList = new ArrayList<>();
            Date stepTime = null;
            List<RiverSectionWaterViewDto> riverSectionWaterViewDtos = riversMap.get(s);
            for (RiverSectionWaterViewDto river : riverSectionWaterViewDtos) {

                RiverSectionViewDto res = new RiverSectionViewDto();
                res.setCaseId(river.getCaseId());
                res.setRiverId(river.getRiverId());
                stepTime = river.getStepTime();
                res.setSectionName(river.getSectionName());
                res.setRiverZ(new BigDecimal(Double.valueOf(river.getRiverZ())).setScale(2, RoundingMode.HALF_UP));
                res.setStep(river.getStep());
                res.setAltitudeLeft(river.getAltitudeLeft());
                res.setAltitudeRight(river.getAltitudeRight());
                res.setAltitudeButtom(river.getAltitudeBottom());
                res.setLgtd(river.getLgtd());
                res.setLttd(river.getLttd());
                if (null != collect.get(river.getSectionName())) {
                    StStbprpBEntity stStbprpBEntity = collect.get(river.getSectionName());
//                    res.setGateId(null != stStbprpBEntity.getStcd() ? stStbprpBEntity.getStcd() : null);
                    res.setGateStcd(null != stStbprpBEntity.getStcd() ? stStbprpBEntity.getStcd() : "");
                    res.setGateName(null != stStbprpBEntity.getStnm() ? stStbprpBEntity.getStnm() : "");
                    res.setGateLgtd(null != stStbprpBEntity.getLgtd() ? stStbprpBEntity.getLgtd().doubleValue() : null);
                    res.setGateLttd(null != stStbprpBEntity.getLttd() ? stStbprpBEntity.getLttd().doubleValue() : null);
                    res.setGateWrz(null != stStbprpBEntity.getWrz() ? stStbprpBEntity.getWrz() : null);
                    res.setGateBhtz(null != stStbprpBEntity.getBhtz() ? stStbprpBEntity.getBhtz() : null);
                    res.setFromWarning(null != stStbprpBEntity.getWrz() ? BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).subtract(stStbprpBEntity.getWrz()).setScale(2, RoundingMode.HALF_UP) : null);
                    res.setFromHighest(null != stStbprpBEntity.getBhtz() ? BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).subtract(stStbprpBEntity.getBhtz()).setScale(2, RoundingMode.HALF_UP) : null);
                }
                if (StrUtil.isNotEmpty(res.getGateName())) {
                    RiverGateWaterDto riverGateWaterDto = factGateMap.get(res.getSectionName());
                    if (riverGateWaterDto != null) {
                        List<String> value = riverGateWaterDto.getValue();
                        if (CollUtil.isNotEmpty(value)) {
                            //如果实时水位在这点数量小于 需要的数量则补充bull
                            if (value.size() - 1 < num) {
                                res.setFactDeep(null);
                            } else {
                                res.setFactDeep(new BigDecimal(value.get(num)));
                            }
                        }
                        res.setDtmel(riverGateWaterDto.getDtmel() == null ? "" : riverGateWaterDto.getDtmel());
                    }

                }
                res.setDepth(BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).setScale(2, RoundingMode.HALF_UP).subtract(river.getAltitudeBottom()));
                riverList.add(res);
            }
            stepSectionDto.setStepTime(stepTime);
            stepSectionDto.setSectionList(riverList);
            resList.add(stepSectionDto);
            num = num + 1;
        }
        resList = resList.stream().sorted(Comparator.comparing(re -> Integer.valueOf(re.getStep()))).collect(Collectors.toList());
        return resList;
    }


    public List<RiverGateWaterDto> dealGateWaterData(Map<String, List<StWaterRateEntityDTO>> map) {
        List<RiverGateWaterDto> res = new ArrayList<>();
        for (String gateName : map.keySet()) {
            RiverGateWaterDto riverGateWaterDto = new RiverGateWaterDto();
            //实时水位数据
            List<String> value = new ArrayList<>();
            //时间
            List<String> time = new ArrayList<>();
            //在这些数据中 第一个点作为 开始 ，往后每个点取相间隔的10分钟的数据 yyyy-MM-dd HH:mm:ss
            List<StWaterRateEntityDTO> stWaterRateEntityDTOS = map.get(gateName);
            if (CollUtil.isEmpty(stWaterRateEntityDTOS)) {
                continue;
            }
//            yyyy-MM-dd HH:mm:ss
            Map<String, StWaterRateEntityDTO> dateMap = stWaterRateEntityDTOS.stream().collect(Collectors.toMap(stWaterRateEntityDTO -> {
                String ctime = stWaterRateEntityDTO.getCtime().substring(0, 15) + "0";
                return ctime;
            }, Function.identity(), (o1, o2) -> o2));
            BigDecimal bhtz = null;
            BigDecimal wrz = null;
            for (String s : dateMap.keySet().stream().sorted().collect(Collectors.toList())) {
                StWaterRateEntityDTO stWaterRateEntityDTO = dateMap.get(s);
                time.add(s);
                value.add(stWaterRateEntityDTO.getWaterDeep().toString());
                bhtz = stWaterRateEntityDTO.getBhtz();
                wrz = stWaterRateEntityDTO.getWrz();
                riverGateWaterDto.setDtmel(stWaterRateEntityDTO.getDtmel());
            }
            riverGateWaterDto.setGateName(gateName);
            riverGateWaterDto.setTime(time);
            riverGateWaterDto.setValue(value);
            riverGateWaterDto.setBhtz(bhtz);
            riverGateWaterDto.setWrz(wrz);
            res.add(riverGateWaterDto);
        }
        return res;
    }

    public List<RiverGateWaterDto> dealGateWaterData(Map<String, List<StWaterRateEntityDTO>> map, StCaseBaseInfoDto stCaseBaseInfoDto) {
        List<RiverGateWaterDto> res = new ArrayList<>();
        for (String gateName : map.keySet()) {
            RiverGateWaterDto riverGateWaterDto = new RiverGateWaterDto();
            //实时水位数据
            List<String> value = new ArrayList<>();
            //时间
            List<String> time = new ArrayList<>();
            //在这些数据中 第一个点作为 开始 ，往后每个点取相间隔的10分钟的数据 yyyy-MM-dd HH:mm:ss
            List<StWaterRateEntityDTO> stWaterRateEntityDTOS = map.get(gateName);
            if (CollUtil.isEmpty(stWaterRateEntityDTOS)) {
                continue;
            }
            Map<String, StWaterRateEntityDTO> dateMap = new HashMap<>();
            if (stCaseBaseInfoDto.getStep() == 5) {
                dateMap = dealFiveMinData(stCaseBaseInfoDto, stWaterRateEntityDTOS);
            } else {
                //            yyyy-MM-dd HH:mm:ss
                dateMap = stWaterRateEntityDTOS.stream().collect(Collectors.toMap(stWaterRateEntityDTO -> {
                    String ctime = stWaterRateEntityDTO.getCtime().substring(0, 15) + "0";
                    return ctime;
                }, Function.identity(), (o1, o2) -> o2));
            }

            BigDecimal bhtz = null;
            BigDecimal wrz = null;
            for (String s : dateMap.keySet().stream().sorted().collect(Collectors.toList())) {
                StWaterRateEntityDTO stWaterRateEntityDTO = dateMap.get(s);
                time.add(s);
                value.add(stWaterRateEntityDTO.getWaterDeep().toString());
                bhtz = stWaterRateEntityDTO.getBhtz();
                wrz = stWaterRateEntityDTO.getWrz();
                riverGateWaterDto.setDtmel(stWaterRateEntityDTO.getDtmel());
            }
            riverGateWaterDto.setGateName(gateName);
            riverGateWaterDto.setTime(time);
            riverGateWaterDto.setValue(value);
            riverGateWaterDto.setBhtz(bhtz);
            riverGateWaterDto.setWrz(wrz);
            res.add(riverGateWaterDto);
        }
        return res;
    }

    public Map<String, StWaterRateEntityDTO> dealFiveMinData(StCaseBaseInfoDto stCaseBaseInfoDto, List<StWaterRateEntityDTO> stWaterRateEntityDTOS) {
        Map<String, StWaterRateEntityDTO> res = new HashMap<>();
        Date preHotTime = stCaseBaseInfoDto.getPreHotTime();
        Date preSeeTime = stCaseBaseInfoDto.getPreSeeTime();
        //            yyyy-MM-dd HH:mm:ss
        Map<String, StWaterRateEntityDTO> dateMap = stWaterRateEntityDTOS.stream().collect(Collectors.toMap(stWaterRateEntityDTO -> {
            String ctime = stWaterRateEntityDTO.getCtime().substring(0, 16);
            return ctime;
        }, Function.identity(), (o1, o2) -> o2));
        Set<String> strings = dateMap.keySet();
        if (preHotTime != null || preSeeTime != null) {
            while (preHotTime.getTime() < preSeeTime.getTime()) {
                String start = DateUtil.format(preHotTime, "yyyy-MM-dd HH:mm:ss");
                preHotTime = DateUtil.offsetMinute(preHotTime, 5);
                String end = DateUtil.format(preHotTime, "yyyy-MM-dd HH:mm:ss");

                for (String string : strings) {
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    if (start.compareTo(string) <= 0 && end.compareTo(string) >= 0) {
                        stWaterRateEntityDTO = dateMap.get(string);
                        res.put(string, dateMap.get(string));
                    }
//                    else {
//                        res.put(string, stWaterRateEntityDTO);
//
//                    }
                }
            }
        }
        return res;

    }

    @Override
    public List<StCaseResWaterList> selectWater(StCaseResRainQuery stCaseResRainQuery) {
        List<StCaseResWaterList> stCaseResWaterListList = new ArrayList<>();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //流量站台账数据  ZQ流量
            List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "ZQ").isNotNull(StStbprpBEntity::getSectionName).isNull(StStbprpBEntity::getSectionFlag).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
            List<StStbprpBEntity> stStbprpBEntityLists = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "ZZ").isNotNull(StStbprpBEntity::getSectionName).isNull(StStbprpBEntity::getSectionFlag).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(stStbprpBEntityLists)) {
                stStbprpBEntityList.addAll(stStbprpBEntityLists);
            }
            if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
                Map<String, BigDecimal> dtmlMap = stStbprpBEntityList.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStnm, StStbprpBEntity::getDtmel, (o1, o2) -> o2));
                stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).filter(x -> StringUtil.isEmpty(x.getSectionFlag())).collect(Collectors.toList());
                Map<String, List<StStbprpBEntity>> map = stStbprpBEntityList.stream().collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
                for (Map.Entry<String, List<StStbprpBEntity>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    List<StStbprpBEntity> stStbprpBEntityList1 = Optional.ofNullable(entry.getValue()).orElse(Lists.newArrayList());
                    if (key.equals("ZQ")) {//流量站
                        if (!CollectionUtils.isEmpty(stStbprpBEntityList1)) {
                            List<String> stStbprpBList = Optional.ofNullable(stStbprpBEntityList1.stream().map(x -> x.getStcd().substring(2)).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            List<StWaterRateEntity> stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getStartTime(), "yyyy-MM-dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getEndTime(), "yyyy-MM-dd HH:mm:ss")).in(StWaterRateEntity::getDid, stStbprpBList))).orElse(Lists.newArrayList());
                            if (CollUtil.isNotEmpty(stRainDateDtos)) {
                                Map<String, List<StWaterRateEntity>> maps = stRainDateDtos.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
                                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList1) {
                                    List<StWaterRateEntity> stWaterRateEntities = Optional.ofNullable(maps.get(stStbprpBEntity.getStcd().substring(2))).orElse(Lists.newArrayList());
                                    if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                                        List<StWaterRateEntity> stWaterRateEntityList = stWaterRateEntities.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime).reversed()).collect(Collectors.toList());
                                        Map<String, StWaterRateEntity> hourMap = stWaterRateEntityList.stream().collect(Collectors.toMap(stWaterRateEntity -> {
                                            return stWaterRateEntity.getCtime().substring(0, 13);
                                        }, Function.identity(), (o1, o2) -> o2));
                                        for (String s : hourMap.keySet()) {
                                            StWaterRateEntity stWaterRateEntity = hourMap.get(s);
                                            StCaseResWaterList stCaseResWaterList = new StCaseResWaterList();
                                            stCaseResWaterList.setStnm(stStbprpBEntity.getStnm());
                                            stCaseResWaterList.setTime(df.format(df.parse(stWaterRateEntity.getCtime())));
                                            stCaseResWaterList.setJd(stStbprpBEntity.getLgtd().doubleValue());
                                            stCaseResWaterList.setWd(stStbprpBEntity.getLttd().doubleValue());
                                            BigDecimal bigDecimal = dtmlMap.get(stStbprpBEntity.getStnm());
                                            BigDecimal rs = bigDecimal.add(new BigDecimal(stWaterRateEntity.getMomentRiverPosition()));
                                            stCaseResWaterList.setWater(rs.toString());
                                            stCaseResWaterListList.add(stCaseResWaterList);
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        if (!CollectionUtils.isEmpty(stStbprpBEntityList1)) {
                            List<String> stStbprpBList = Optional.ofNullable(stStbprpBEntityList1.stream().map(x -> x.getStcd()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            //获取水位数据关联表
                            List<StSnConvertEntity> stSnConvertEntities = stSnConvertDao.selectList(new QueryWrapper<StSnConvertEntity>().lambda().in(StSnConvertEntity::getStcd, stStbprpBList));
                            if (!CollectionUtils.isEmpty(stSnConvertEntities)) {
                                //提取水位数据关联表sn集合
                                List<String> getSnList = Optional.ofNullable(stSnConvertEntities.stream().map(x -> x.getSn()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                                //获取水位数据
                                List<StWaterRateEntity> stRainDateDtos = Optional.ofNullable(stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getStartTime(), "yyyy/MM/dd HH:mm:ss")).le(StWaterRateEntity::getCtime, DateUtil.format(stCaseResRainQuery.getEndTime(), "yyyy/MM/dd HH:mm:ss")).in(StWaterRateEntity::getDid, getSnList))).orElse(Lists.newArrayList());
                                if (CollUtil.isNotEmpty(stRainDateDtos)) {
                                    Map<String, List<StWaterRateEntity>> maps = stRainDateDtos.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
                                    for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList1) {
                                        List<StSnConvertEntity> snConvertEntities = Optional.ofNullable(stSnConvertEntities.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                                        if (CollUtil.isNotEmpty(snConvertEntities)) {
                                            StSnConvertEntity stSnConvertEntity = snConvertEntities.get(0);
                                            List<StWaterRateEntity> stWaterRateEntities = Optional.ofNullable(maps.get(stSnConvertEntity.getSn())).orElse(Lists.newArrayList());
                                            if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                                                List<StWaterRateEntity> stWaterRateEntityList = stWaterRateEntities.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime).reversed()).collect(Collectors.toList());
                                                Map<String, StWaterRateEntity> hourMap = stWaterRateEntityList.stream().collect(Collectors.toMap(stWaterRateEntity -> {
                                                    return stWaterRateEntity.getCtime().substring(0, 13);
                                                }, Function.identity(), (o1, o2) -> o2));
                                                for (String s : hourMap.keySet()) {
                                                    StWaterRateEntity stWaterRateEntity = hourMap.get(s);
                                                    StCaseResWaterList stCaseResWaterList = new StCaseResWaterList();
                                                    stCaseResWaterList.setStnm(stStbprpBEntity.getStnm());
                                                    stCaseResWaterList.setTime(stWaterRateEntity.getCtime());
                                                    stCaseResWaterList.setJd(stStbprpBEntity.getLgtd().doubleValue());
                                                    stCaseResWaterList.setWd(stStbprpBEntity.getLttd().doubleValue());
                                                    String water = "0";
                                                    if (StringUtil.isNotBlank(stWaterRateEntity.getAddrv())) {
                                                        water = new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP).toString();
                                                    }
                                                    BigDecimal bigDecimal = dtmlMap.get(stStbprpBEntity.getStnm());
                                                    BigDecimal rs = bigDecimal.add(new BigDecimal(water));
                                                    stCaseResWaterList.setWater(rs.toString());
                                                    stCaseResWaterListList.add(stCaseResWaterList);
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

        } catch (Exception e) {
            log.error("方案实时获取水位数据异常！" + e);
        }
        if (CollUtil.isNotEmpty(stCaseResWaterListList)) {
            stCaseResWaterListList = stCaseResWaterListList.stream().sorted(Comparator.comparing(StCaseResWaterList::getStnm).thenComparing(StCaseResWaterList::getTime)).collect(Collectors.toList());
        }
        return stCaseResWaterListList;
    }

    @SneakyThrows
    @Override
    public List<WaterForcastDto> getWaterForecast(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        if (StrUtil.isEmpty(stCaseBaseInfoEsu.getId())) {
            throw new BusinessException("案件id不能为空! 字段: id 类型: String");
        }
        if (StrUtil.isEmpty(stCaseBaseInfoEsu.getRiverId())) {
            throw new BusinessException("河流id不能为空! 字段: riverId 类型: String");
        }
        if (StrUtil.isEmpty(stCaseBaseInfoEsu.getSectionName())) {
            throw new BusinessException("断面名称不能为空! 字段: sectionName 类型: String");
        }
        StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectById(stCaseBaseInfoEsu.getId());

        Date preHotTime = stCaseBaseInfoDto.getPreHotTime();
        Date preSeeTime = stCaseBaseInfoDto.getPreSeeTime();
        long preHotTimeStamp = preHotTime.getTime();
        long preSeeTimeStamp = preSeeTime.getTime();
        List<WaterForcastDto> resList = new ArrayList<>();
        QueryWrapper<StCaseResDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("case_id", stCaseBaseInfoEsu.getId());
        queryWrapper.eq("section_name", stCaseBaseInfoEsu.getSectionName());
        queryWrapper.eq("rv_id", stCaseBaseInfoEsu.getRiverId());
        queryWrapper.orderByAsc("step");
        List<StCaseResDto> stCaseResDtos = stCaseResDao.selectList(queryWrapper);
        int size = stCaseResDtos.size();
        for (int i = 0; i < size; i++) {
            StCaseResDto stCaseResDto = stCaseResDtos.get(i);
            WaterForcastDto e = new WaterForcastDto();
            BeanUtils.copyProperties(stCaseResDto, e);
            long l = preHotTimeStamp + (preSeeTimeStamp - preHotTimeStamp) * i / (size - 1);
            Date date = new Date(l);
            e.setSchedulingTime((date.getHours() < 10 ? ("0" + date.getHours()) : date.getHours()) + ":" + (date.getMinutes() < 10 ? ("0" + date.getMinutes()) : date.getMinutes()));
            e.setSchedulingType("提闸");
            resList.add(e);
        }

        return resList;
    }

    @Override
    public Object getFloodPeak(PaginatorParam param) {
        Paginator<RiverGateMaxFlowViewEsr> byPaginator = riverGateMaxFlowViewService.findByPaginator(param);
        List<RiverGateMaxFlowViewEsr> resList = new ArrayList<>();
        byPaginator.getItems().forEach(riverGateMaxFlowViewEsr -> {
            if (0 != riverGateMaxFlowViewEsr.getMaxQ().setScale(3, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO)) {
                riverGateMaxFlowViewEsr.setMaxQ(riverGateMaxFlowViewEsr.getMaxQ().setScale(3, RoundingMode.HALF_UP));
            } else {
                riverGateMaxFlowViewEsr.setMaxQ(riverGateMaxFlowViewEsr.getMaxQ().setScale(3, RoundingMode.HALF_UP));
                riverGateMaxFlowViewEsr.setCreateTime(null);
            }
            resList.add(riverGateMaxFlowViewEsr);
        });
        return resList;
    }

    @Override
    public List<RiverGateMaxFlowViewEsr> getFloodFlowPeak(StCaseResEsu cs) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("case_id", cs.getCaseId());
        List<StCaseResDto> stCaseResDtos = stCaseResDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(stCaseResDtos)) {
            //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
            QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.le("id", 31);
            List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
            Map<String, String> reaMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, ReaBase::getReaName, (o1, o2) -> o2));
            Map<String, List<StCaseResDto>> sectionNameMap = stCaseResDtos.parallelStream().collect(Collectors.groupingBy(StCaseResDto::getSectionName));
            for (String sectionName : sectionNameMap.keySet()) {
//                List<StCaseResDto> stCaseResDtos1 = sectionNameMap.get(sectionName);
//                stCaseResDtos1.parallelStream().filter(stCaseResDto -> {return StrUtil.isNotEmpty(stCaseResDto.getRiverQ()) ;})
//                StCaseResDto stCaseResDto = stCaseResDtos1.get(0);
//                String rvId = stCaseResDto.getRvId();
                //河流名称
                //站点名称
                //最大流量
            }
        }
        return null;
    }

    @SneakyThrows
    @Override
    public OverFlowListDto getOverFlowList(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        OverFlowListDto resList = new OverFlowListDto();
        List<OverFlowDto> overFlowDtoList = new ArrayList<>();
        // 溢流数量
        int total = 0;
        if (null == stCaseBaseInfoEsu.getId()) {
            throw new BusinessException("案件id不能为空! 字段: id 类型: String");
        }
        QueryWrapper<RiverSectionWaterViewDto> queryWrapper = new QueryWrapper<>();
        if (null != stCaseBaseInfoEsu.getRiverId()) {
            queryWrapper.eq("river_id", stCaseBaseInfoEsu.getRiverId());
        }
        queryWrapper.eq("case_id", stCaseBaseInfoEsu.getId());
        List<RiverSectionWaterViewDto> rivers = riverSectionWaterViewDao.selectList(queryWrapper);
        Map<String, List<RiverSectionWaterViewDto>> riversMap = rivers.stream().collect(
                Collectors.groupingBy(
                        river -> river.getSectionName()
                ));
        for (Map.Entry<String, List<RiverSectionWaterViewDto>> entry : riversMap.entrySet()) {
            OverFlowDto res = new OverFlowDto();
            // 溢流判断
            boolean isOverFlow = false;
            // 开始溢流判断
            boolean startOverFlow = true;
            // 统计溢流开关
            boolean addSwitch = true;
            // 溢流时间列表
            List<Map<String, Object>> overFlowTimeList = new ArrayList<>();
            // 预报最高水位
            BigDecimal highest = new BigDecimal(0);
            Date startOverFlowTime = null;
            for (RiverSectionWaterViewDto river : entry.getValue()) {
                if (0 < BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).compareTo(highest)) {
                    highest = BigDecimal.valueOf(Double.valueOf(river.getRiverZ()));
                }
                if (0 < BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).compareTo(river.getAltitudeLeft())) {
                    isOverFlow = true;
                    addSwitch = true;
                } else if (0 < BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).compareTo(river.getAltitudeRight())) {
                    isOverFlow = true;
                    addSwitch = true;
                } else {
                    addSwitch = false;
                }
                Map<String, Object> overFlowMap = new HashMap<>();
                overFlowMap.put("time", river.getStepTime());
                overFlowMap.put("isOverFlow", addSwitch);
                overFlowTimeList.add(overFlowMap);
                if (isOverFlow && startOverFlow) {
                    startOverFlowTime = river.getStepTime();
                    startOverFlow = false;
                }
            }
            if (isOverFlow) {
                long overFlowTime = 0L;
                for (int i = 0; i < overFlowTimeList.size() - 1; i++) {
                    if (Boolean.valueOf(String.valueOf(overFlowTimeList.get(i).get("isOverFlow"))) && Boolean.valueOf(String.valueOf(overFlowTimeList.get(i + 1).get("isOverFlow")))) {
                        long time1 = Date.parse(String.valueOf(overFlowTimeList.get(i).get("time")));
                        long time2 = Date.parse(String.valueOf(overFlowTimeList.get(i + 1).get("time")));
                        overFlowTime += time2 - time1;
                    }
                }
                total++;
                res.setSectionName(entry.getKey());
                res.setHighest(highest.setScale(2, RoundingMode.HALF_UP));
                res.setStartTime(startOverFlowTime);
                res.setOverFlowTime(overFlowTime / 60 / 1000);
                overFlowDtoList.add(res);
            }
        }
        resList.setTotal(total);
        resList.setList(overFlowDtoList);
        return resList;
    }

    @SneakyThrows
    @Override
    public WaringStatisticDto getWaterWarning(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WaringStatisticDto res = new WaringStatisticDto();
        int overFlowCount = 0;
        int overWarningCount = 0;
        int normalCount = 0;
        ArrayList<WarningStationDto> stationList = new ArrayList<>();
        // 异常传参
        if (null == stCaseBaseInfoEsu.getPreHotTime()) {
            throw new BusinessException("预热时间（期）时间不能为空! 字段: preHotTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        } else if (null == stCaseBaseInfoEsu.getForecastStartTime()) {
            throw new BusinessException("预见期时间不能为空! 字段: forecastStartTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        }
        // 查询所有水位流量站 (河道水位站 - ZZ; 河道水文站 - ZQ)
        QueryWrapper<StStbprpBEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "ZQ", "ZZ");
        queryWrapper.isNotNull("section_name");
        List<StStbprpBEntity> stEntities = stStbprpBDao.selectList(queryWrapper);
        for (StStbprpBEntity stbprpBEntity : stEntities) {
            boolean isOverFlow = false;
            boolean isOverWarning = false;
            // 河道水位站 取 addrv
            if (stbprpBEntity.getSttp().equals("ZZ")) {
                QueryWrapper convertQuery = new QueryWrapper();
                convertQuery.eq("stcd", stbprpBEntity.getStcd());
                StSnConvertEntity stSnConvertEntities = stSnConvertDao.selectOne(convertQuery);
                QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("section_name", stbprpBEntity.getSectionName());
                StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                QueryWrapper waterLevel = new QueryWrapper();
                waterLevel.in("did", stSnConvertEntities.getSn());
                waterLevel.le("ctime", DateUtil.format(stCaseBaseInfoEsu.getForecastStartTime(), "yyyy/MM/dd HH:mm:ss"));
                waterLevel.ge("ctime", DateUtil.format(stCaseBaseInfoEsu.getPreHotTime(), "yyyy/MM/dd HH:mm:ss"));
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(waterLevel);
                String warningTime = "";
                //水位站的水位 需要转换下单位
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    if (StrUtil.isEmpty(stWaterRateEntity.getAddrv())) {
                        continue;
                    }
                    BigDecimal waterHigh = new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
                    if (null != stSectionModelDto) {
                        if (null != stSectionModelDto.getAltitudeLeft() && waterHigh.compareTo(stSectionModelDto.getAltitudeLeft()) >= 0) {
                            isOverFlow = true;
                            warningTime = stWaterRateEntity.getCtime();
                            break;
                        }
                        if (null != stSectionModelDto.getAltitudeRight() && waterHigh.compareTo(stSectionModelDto.getAltitudeRight()) >= 0) {
                            isOverFlow = true;
                            warningTime = stWaterRateEntity.getCtime();
                            break;
                        }
                    }
                    if (null != stbprpBEntity.getWrz() && waterHigh.compareTo(stbprpBEntity.getWrz()) >= 0) {
                        isOverWarning = true;
                        warningTime = stWaterRateEntity.getCtime();
                        break;
                    }
                    stWaterRateEntity.setAddrv(waterHigh.toString());
                }
                WarningStationDto resStation = new WarningStationDto();
                resStation.setStcd(stbprpBEntity.getRvnm());
                resStation.setStnm(stbprpBEntity.getStnm());
                // 溢流情况下, 不再统计超预警
                if (isOverWarning) {
                    if (isOverFlow) {
                        resStation.setState("溢流");
                        overFlowCount++;
                    } else {
                        resStation.setState("超预警");
                        overWarningCount++;
                    }
                }
                if (!isOverWarning && !isOverFlow) {
                    resStation.setState("正常");
                    normalCount++;
                }
                // 获取河道
                QueryWrapper<ReaBase> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", stbprpBEntity.getRvnm());
                ReaBase reaBases = reaBaseDao.selectOne(queryWrapper2);
                resStation.setRiverSystem(reaBases.getReaName());
                resStation.setTime(StrUtil.isNotEmpty(warningTime) ? dateFormat.parse(warningTime) : null);
                stationList.add(resStation);
            } else if (stbprpBEntity.getSttp().equals("ZQ")) {
                // 河道水文站 取 momentRiverPosition
                // 流量站点 和 站点id 对应关系 站点id 去掉前两位才可以进行关联  //流量站 时间格式 yyyy-MM-dd HH:mm:ss
                QueryWrapper waterLevel = new QueryWrapper();
                waterLevel.eq("did", stbprpBEntity.getStcd().substring(2));
                waterLevel.le("ctime", DateUtil.format(stCaseBaseInfoEsu.getForecastStartTime(), "yyyy-MM-dd HH:mm:ss"));
                waterLevel.ge("ctime", DateUtil.format(stCaseBaseInfoEsu.getPreHotTime(), "yyyy-MM-dd HH:mm:ss"));
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(waterLevel);
                QueryWrapper<StSectionModelDto> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("section_name", stbprpBEntity.getSectionName());
                StSectionModelDto stSectionModelDto = stSectionModelDao.selectOne(queryWrapper1);
                String warningTime = "";
                //将流量站数据 遍历转化为水位站的数据点位 事件也需要替换一下
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    if (StrUtil.isEmpty(stWaterRateEntity.getMomentRiverPosition())) {
                        continue;
                    }
                    BigDecimal waterHigh = new BigDecimal(stWaterRateEntity.getMomentRiverPosition());
                    if (null != stSectionModelDto) {
                        if (null != stSectionModelDto.getAltitudeLeft() && waterHigh.compareTo(stSectionModelDto.getAltitudeLeft()) >= 0) {
                            isOverFlow = true;
                            warningTime = stWaterRateEntity.getCtime();
                            break;
                        }
                        if (null != stSectionModelDto.getAltitudeRight() && waterHigh.compareTo(stSectionModelDto.getAltitudeRight()) >= 0) {
                            isOverFlow = true;
                            warningTime = stWaterRateEntity.getCtime();
                            break;
                        }
                    }
                    if (null != stbprpBEntity.getWrz() && waterHigh.compareTo(stbprpBEntity.getWrz()) >= 0) {
                        isOverWarning = true;
                        warningTime = stWaterRateEntity.getCtime();
                        break;
                    }
                    stWaterRateEntity.setAddrv(stWaterRateEntity.getMomentRiverPosition());
                    stWaterRateEntity.setCtime(stWaterRateEntity.getCtime().replace("-", "/"));
                }
                WarningStationDto resStation = new WarningStationDto();
                resStation.setStcd(stbprpBEntity.getRvnm());
                resStation.setStnm(stbprpBEntity.getStnm());
                // 溢流情况下, 不再统计超预警
                if (isOverWarning) {
                    if (isOverFlow) {
                        resStation.setState("溢流");
                        overFlowCount++;
                    } else {
                        resStation.setState("超预警");
                        overWarningCount++;
                    }
                }
                if (!isOverWarning && !isOverFlow) {
                    resStation.setState("正常");
                    normalCount++;
                }
                // 获取河道
                QueryWrapper<ReaBase> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", stbprpBEntity.getRvnm());
                ReaBase reaBases = reaBaseDao.selectOne(queryWrapper2);
                resStation.setRiverSystem(reaBases.getReaName());
                resStation.setTime(StrUtil.isNotEmpty(warningTime) ? dateFormat.parse(warningTime) : null);
                stationList.add(resStation);
            }
        }

        res.setList(stationList);
        res.setOverWarning(overWarningCount);
        res.setOverFlow(overFlowCount);
        res.setNormal(normalCount);
        return res;
    }

    @Override
    public List<RiverStepSectionDto> getRiverSection(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        List<RiverStepSectionDto> resList = new ArrayList<>();
        if (null == stCaseBaseInfoEsu.getId()) {
            //默认随机选一个
            List<StCaseBaseInfoDto> stCaseBaseInfoDtos = caseBaseInfoDao.selectList(new QueryWrapper<>());
            StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDtos.get(0);
            stCaseBaseInfoEsu.setId(stCaseBaseInfoDto.getId());
        }
        QueryWrapper<RiverSectionWaterViewDto> queryWrapper = new QueryWrapper<>();
        if (null != stCaseBaseInfoEsu.getRiverId()) {
            queryWrapper.eq("river_id", stCaseBaseInfoEsu.getRiverId());
        }
        queryWrapper.eq("case_id", stCaseBaseInfoEsu.getId());
        queryWrapper.eq("data_type", "2");
        List<RiverSectionWaterViewDto> rivers = riverSectionWaterViewDao.selectList(queryWrapper);
        Map<String, List<RiverSectionWaterViewDto>> riversMap = rivers.stream().collect(
                Collectors.groupingBy(
                        river -> river.getStep()
                ));
        // 查询所有坝
        QueryWrapper<StSideGateDto> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sttp", "SB");
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper1);
        Map<String, List<StSideGateDto>> damMap = stSideGateDtos.stream().filter(dam -> StrUtil.isNotEmpty(dam.getSectionName2())).collect(
                Collectors.groupingBy(
                        dam -> dam.getSectionName2()
                ));
        // 查询所有闸
        QueryWrapper<StSideGateDto> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("sttp", "DD");
        List<StSideGateDto> gateDtos = stSideGateDao.selectList(queryWrapper2);
        Map<String, List<StSideGateDto>> gateMap = gateDtos.stream().filter(gate -> StrUtil.isNotEmpty(gate.getSectionName2())).collect(
                Collectors.groupingBy(
                        gate -> gate.getSectionName2()
                ));
        List<RiverGateWaterDto> riverGateWaterLevel = this.getRiverGateWaterLevel(stCaseBaseInfoEsu.getRiverId(), stCaseBaseInfoEsu.getId());
        Map<String, RiverGateWaterDto> factGateMap = riverGateWaterLevel.parallelStream().collect(Collectors.toMap(RiverGateWaterDto::getGateName, Function.identity()));
        int num = 0;
        for (String s : riversMap.keySet().stream().sorted().collect(Collectors.toList())) {
            RiverStepSectionDto stepSectionDto = new RiverStepSectionDto();
            stepSectionDto.setStep(s);
            List<RiverSectionViewDto> riverList = new ArrayList<>();
            Date stepTime = null;
            List<RiverSectionWaterViewDto> riverSectionWaterViewDtos = riversMap.get(s);
            for (RiverSectionWaterViewDto river : riverSectionWaterViewDtos) {
                // 取断面对应坝
                StSideGateDto gateDto = new StSideGateDto();
                if (null != damMap.get(river.getSectionName())) {
                    gateDto = damMap.get(river.getSectionName()).get(0);
                }
                // 取断面对应闸
                if (null != gateMap.get(river.getSectionName())) {
                    gateDto = gateMap.get(river.getSectionName()).get(0);
                }
                RiverSectionViewDto res = new RiverSectionViewDto();
                res.setCaseId(river.getCaseId());
                res.setRiverId(river.getRiverId());
                stepTime = river.getStepTime();
                res.setSectionName(river.getSectionName());
                res.setRiverZ(new BigDecimal(Double.valueOf(river.getRiverZ())).setScale(2, RoundingMode.HALF_UP));
                res.setStep(river.getStep());
                res.setAltitudeLeft(river.getAltitudeLeft());
                res.setAltitudeRight(river.getAltitudeRight());
                res.setAltitudeButtom(river.getAltitudeBottom());
                res.setLgtd(river.getLgtd());
                res.setLttd(river.getLttd());
                if (null != gateDto) {
                    res.setGateId(null != gateDto.getId() ? gateDto.getId() : null);
                    res.setGateStcd(null != gateDto.getStcd() ? gateDto.getStcd() : "");
                    res.setGateName(null != gateDto.getStnm() ? gateDto.getStnm() : "");
                    res.setGateLgtd(null != gateDto.getLgtd() ? gateDto.getLgtd() : null);
                    res.setGateLttd(null != gateDto.getLttd() ? gateDto.getLttd() : null);
                    res.setGateWrz(null != gateDto.getWrz() ? gateDto.getWrz() : null);
                    res.setGateBhtz(null != gateDto.getBhtz() ? gateDto.getBhtz() : null);
                    res.setFromWarning(null != gateDto.getWrz() ? BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).subtract(gateDto.getWrz()).setScale(2, RoundingMode.HALF_UP) : null);
                    res.setFromHighest(null != gateDto.getBhtz() ? BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).subtract(gateDto.getBhtz()).setScale(2, RoundingMode.HALF_UP) : null);
                }
                if (StrUtil.isNotEmpty(res.getGateName())) {
                    RiverGateWaterDto riverGateWaterDto = factGateMap.get(res.getGateName());
                    if (riverGateWaterDto != null) {
                        List<String> value = riverGateWaterDto.getValue();
                        if (CollUtil.isNotEmpty(value)) {
                            //如果实时水位在这点数量小于 需要的数量则补充bull
                            if (value.size() - 1 < num) {
                                res.setFactDeep(null);
                            } else {
                                res.setFactDeep(new BigDecimal(value.get(num)));
                            }
                        }
                    }
                }
                res.setDepth(BigDecimal.valueOf(Double.valueOf(river.getRiverZ())).setScale(2, RoundingMode.HALF_UP).subtract(river.getAltitudeBottom()));
                riverList.add(res);
            }
            stepSectionDto.setStepTime(stepTime);
            stepSectionDto.setSectionList(riverList);
            resList.add(stepSectionDto);
            num = num + 1;
        }
        resList = resList.stream().sorted(Comparator.comparing(re -> Integer.valueOf(re.getStep()))).collect(Collectors.toList());
        return resList;
    }
}
