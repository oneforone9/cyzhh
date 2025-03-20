package com.essence.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.*;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.vo.CaiyunPrecipitationRealVo;
import com.essence.interfaces.api.StRainDateService;
import com.essence.interfaces.dot.HdyqDzmGisResp;
import com.essence.interfaces.entity.HourTimeAxisRainVo;
import com.essence.interfaces.model.StRainDateEsr;
import com.essence.interfaces.model.StRainDateEsu;
import com.essence.interfaces.param.StRainDateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStRainDateEtoT;
import com.essence.service.converter.ConverterStRainDateTtoR;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import wcontour.Contour;
import wcontour.Interpolate;
import wcontour.global.Border;
import wcontour.global.PointD;
import wcontour.global.PolyLine;
import wcontour.global.Polygon;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (StRainDate)业务层
 *
 * @author BINX
 * @since 2023-02-20 14:33:07
 */
@Service
@Slf4j
public class StRainDateServiceImpl extends BaseApiImpl<StRainDateEsu, StRainDateEsp, StRainDateEsr, StRainDateDto> implements StRainDateService {
    /**
     * 降雨等值面url
     */
//    172.16.52.5:10088
    @Value("${gis.proxy.loacte}")
    private String API_RAIN_FALL_EQUALS_CASE;
    @Resource
    private RainHourDataDao rainHourDataDao;
    @Resource
    private RainTokenDao rainTokenDao;
    @Resource
    private StStbprpBDao stStbprpBDao;

    @Autowired
    private StRainDateDao stRainDateDao;
    @Resource
    private RainDayCountDao rainDayCountDao;
    @Resource
    private StRainDailyDao rainDailyDao;

    @Autowired
    private StCaiyunPrecipitationRealDao caiYunPrecipitationRealDao;

    public StRainDateServiceImpl(StRainDateDao stRainDateDao, ConverterStRainDateEtoT converterStRainDateEtoT, ConverterStRainDateTtoR converterStRainDateTtoR) {
        super(stRainDateDao, converterStRainDateEtoT, converterStRainDateTtoR);
    }

    @Override
    public List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) {
        List<StWaterRateEntityDTO> res = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        DateTime end = DateUtil.parseDate(stStbprpBEntityDTO.getEndTime());
        wrapper.le("date", end);
        DateTime start = DateUtil.parseDate(stStbprpBEntityDTO.getStartTime());
        wrapper.ge("date", start);
        wrapper.eq("station_id", stStbprpBEntityDTO.getStcd());
        List<StRainDateDto> list = stRainDateDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            for (StRainDateDto stRainDateDto : list) {
                StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                stWaterRateEntityDTO.setAddrv(StrUtil.isEmpty(stRainDateDto.getHhRain()) ? "0" : stRainDateDto.getHhRain());
                stWaterRateEntityDTO.setCtime(DateUtil.format(stRainDateDto.getDate(), "yyyy-MM-dd HH:mm:ss"));
                res.add(stWaterRateEntityDTO);
            }
            res = res.stream().sorted(Comparator.comparing(StWaterRateEntityDTO::getCtime)).collect(Collectors.toList());
        }
        return res;
    }


    /**
     * 获取小时雨量
     *
     * @throws UnirestException
     */
    @Override
    public List<RainDateHourDto> getRainHourData(String stationID, String queryDate) {
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp", "PP");
        if (null != stationID && !"".equals(stationID)) {
            wrapper.eq("stcd", stationID);
        }
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        Date date = new Date();
        String format1 = DateUtil.format(date, "yyyy-MM-dd");
        if (StrUtil.isNotEmpty(queryDate)) {
            format1 = queryDate;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("date", format1);
        List<StRainDailyDto> stRainDailyDtos = rainDailyDao.selectList(queryWrapper);
        Map<String, String> dailyMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stRainDailyDtos)) {
            dailyMap = stRainDailyDtos.parallelStream().collect(Collectors.toMap(StRainDailyDto::getStationId, StRainDailyDto::getValue, (o1, o2) -> o2));
        }

        QueryWrapper rainHourWrapper = new QueryWrapper();
        rainHourWrapper.eq("date", format1);
        if (null != stationID && !"".equals(stationID)) {
            rainHourWrapper.eq("station_id", stationID);
        }
        if (StrUtil.isNotEmpty(queryDate)) {
            rainHourWrapper.eq("date", queryDate);
        }
        List<RainHourDataEntity> rainHourDataEntities = rainHourDataDao.selectList(rainHourWrapper);
        List<RainDateHourDto> rainDateDtos = new ArrayList<>();
        Map<String, RainHourDataEntity> hourMap = new HashMap<>();
        if (CollUtil.isNotEmpty(rainHourDataEntities)) {
            hourMap = rainHourDataEntities.parallelStream().collect(Collectors.toMap(RainHourDataEntity::getStationId, Function.identity(), (o1, o2) -> o2));
        }
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {

            RainDateHourDto rainDateDto = new RainDateHourDto();
            RainHourDataEntity rainHourDataEntity = hourMap.get(stStbprpBEntity.getStcd());
            rainDateDto.setStationID(stStbprpBEntity.getStcd());
            rainDateDto.setStnm(stStbprpBEntity.getStnm() == null ? null : stStbprpBEntity.getStnm());
            rainDateDto.setLttd(stStbprpBEntity.getLttd() == null ? null : stStbprpBEntity.getLttd());
            rainDateDto.setLgtd(stStbprpBEntity.getLgtd() == null ? null : stStbprpBEntity.getLgtd());
            if (rainHourDataEntity != null) {
                rainDateDto.setRain1h(rainHourDataEntity.getHourOne());
                rainDateDto.setRain3h(rainHourDataEntity.getHourThree());
                rainDateDto.setRain6h(rainHourDataEntity.getHourSix());
                rainDateDto.setRain12h(rainHourDataEntity.getHourTwelve());
                rainDateDto.setRain24h(rainHourDataEntity.getHourTwenty());
            }
            String rainH = dailyMap.get(stStbprpBEntity.getStcd());
            rainDateDto.setRainh(StrUtil.isNotEmpty(rainH) ? rainH : rainDateDto.getRain6h());
            rainDateDto.setDate(DateUtil.parseDate(format1));
            rainDateDtos.add(rainDateDto);
        }
        return rainDateDtos;

    }

    @Override
    public RainInfoStatisticDto getRainInfoStatistic() {
        RainInfoStatisticDto dto = new RainInfoStatisticDto();
        // 普通时间
        Date date = new Date();
        Date start = DateUtil.beginOfDay(date);
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp", "PP");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        Map<String, StStbprpBEntity> stcdMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity(), (o1, o2) -> o2));
        DateTime yearBegin = DateUtil.beginOfYear(date);
        DateTime yearEnd = DateUtil.endOfYear(date);
        Map<String, Object> stringObjectMap = dealRainNum(yearBegin, yearEnd);
        BigDecimal addNum = (BigDecimal) stringObjectMap.getOrDefault("addNum", new BigDecimal(0));

        DateTime lastData = DateUtil.offset(date, DateField.YEAR, -1);
        DateTime lastYearBegin = DateUtil.beginOfYear(lastData);
        DateTime lastYearEnd = DateUtil.endOfYear(lastData);
        Map<String, Object> stringObjectMap1 = dealRainNum(lastYearBegin, lastYearEnd);
        BigDecimal lastAddNum = (BigDecimal) stringObjectMap1.getOrDefault("addNum", new BigDecimal(0));
        BigDecimal percent = countCurrent(addNum, lastAddNum);
        // 汛期时间 6.1  -  9.15
        String format = DateUtil.format(yearBegin, "yyyy-MM-dd HH:mm:ss");
        String xqStartDate = format.substring(0, 4) + "-06-01 00:00:00";
        String xqStartEnd = format.substring(0, 4) + "-09-15 23:59:59";
        Map<String, Object> stringObjectMap2 = dealRainNum(DateUtil.parse(xqStartDate), DateUtil.parse(xqStartEnd));
        BigDecimal xqAddNum = (BigDecimal) stringObjectMap2.getOrDefault("addNum", new BigDecimal(0));

        String lastFormat = DateUtil.format(lastYearBegin, "yyyy-MM-dd HH:mm:ss");
        String xqLastStartDate = lastFormat.substring(0, 4) + "-06-01 00:00:00";
        String xqLastStartEnd = lastFormat.substring(0, 4) + "-09-15 23:59:59";
        Map<String, Object> stringObjectMap3 = dealRainNum(DateUtil.parse(xqLastStartDate), DateUtil.parse(xqLastStartEnd));
        BigDecimal lastXqAddNum = (BigDecimal) stringObjectMap3.getOrDefault("addNum", new BigDecimal(0));
        BigDecimal lastPercent = countCurrent(xqAddNum, lastXqAddNum);

        dto.setAddCurrentRain(addNum.divide(new BigDecimal(stStbprpBEntities.size()), 2, RoundingMode.HALF_UP));
        dto.setAddLastRain(lastAddNum.divide(new BigDecimal(stStbprpBEntities.size()), 2, RoundingMode.HALF_UP));
        dto.setAddXQCurrentRain(xqAddNum.divide(new BigDecimal(stStbprpBEntities.size()), 2, RoundingMode.HALF_UP));
        dto.setLastXQAddRain(lastXqAddNum.divide(new BigDecimal(stStbprpBEntities.size()), 2, RoundingMode.HALF_UP));
        if (dto.getAddLastRain().doubleValue() > 0) {
            dto.setCurrentPercent(BigDecimal.valueOf((dto.getAddCurrentRain().doubleValue() - dto.getAddLastRain().doubleValue()) / dto.getAddLastRain().doubleValue() * 100));
        } else {
            dto.setCurrentPercent(BigDecimal.ZERO);
        }
        if (dto.getLastXQAddRain().doubleValue() > 0) {
            dto.setXqPercent(BigDecimal.valueOf((dto.getAddXQCurrentRain().doubleValue() - dto.getLastXQAddRain().doubleValue()) / dto.getLastXQAddRain().doubleValue() * 100));
        } else {
            dto.setXqPercent(BigDecimal.ZERO);
        }

        //全区数据获取当日数据
        List<RainDayCountDto> stRainDateDtoList = rainDayCountDao.selectList(new QueryWrapper<RainDayCountDto>().lambda().ge(RainDayCountDto::getDate, DateUtil.format(start, "yyyy-MM-dd")).le(RainDayCountDto::getDate, DateUtil.format(date, "yyyy-MM-dd")));
        if (!CollectionUtils.isEmpty(stRainDateDtoList)) {
            List<RainDayCountDto> stRainDateDtoList1 = Optional.ofNullable(stRainDateDtoList.stream().filter(x -> x.getDayCount() != null).collect(Collectors.toList())).orElse(Lists.newArrayList());
            BigDecimal total = stRainDateDtoList1.stream().map(RainDayCountDto::getDayCount).reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setAvgRainNum(total.divide(new BigDecimal(stStbprpBEntities.size()), 2, RoundingMode.HALF_UP));
            Map<String, List<RainDayCountDto>> map = stRainDateDtoList1.stream().collect(Collectors.groupingBy(RainDayCountDto::getStationID));

            Map<BigDecimal, String> maps = new LinkedHashMap<>();
            for (String getStationId : map.keySet()) {
                List<RainDayCountDto> rainDateDtos = map.get(getStationId);
                if (CollUtil.isNotEmpty(rainDateDtos)) {
                    BigDecimal sum1 = rainDateDtos.stream().map(RainDayCountDto::getDayCount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    maps.put(sum1, getStationId);
                }
            }
            if (CollUtil.isNotEmpty(maps)) {
                List<BigDecimal> list = maps.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                BigDecimal bigDecimal = list.get(0);
                BigDecimal bigDecimalMin = list.get(list.size() - 1);
                String id = maps.get(bigDecimal);
                String idMin = maps.get(bigDecimalMin);
                dto.setRainNum(bigDecimal.setScale(2, RoundingMode.HALF_UP));
                dto.setRainNumMin(bigDecimalMin.setScale(2, RoundingMode.HALF_UP));
                dto.setStnm(stcdMap.get(id) == null ? null : stcdMap.get(id).getStnm());
                dto.setStnmMin(stcdMap.get(idMin) == null ? null : stcdMap.get(idMin).getStnm());
            }
        }
        return dto;
    }

    @Override
    public HdyqDzmGisResp getRainFallEqualsValueCase(Integer hours) throws Exception {
        Date date = new Date();
        //查询雨量站
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp", "PP");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        //查询小时雨量
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("date", DateUtil.format(date, "yyyy-MM-dd"));
        List<RainHourDataEntity> rainHourDataEntities = rainHourDataDao.selectList(queryWrapper);
        Map<String, RainHourDataEntity> hourMap = new HashMap<>();
        if (CollUtil.isNotEmpty(rainHourDataEntities)) {
            hourMap = rainHourDataEntities.parallelStream().collect(Collectors.toMap(RainHourDataEntity::getStationId, Function.identity(), (o1, o2) -> o2));
        }

        QueryWrapper dailyQuery = new QueryWrapper();
        dailyQuery.eq("date", DateUtil.format(date, "yyyy-MM-dd"));
        List<StRainDailyDto> stRainDailyDtos = rainDailyDao.selectList(queryWrapper);
        Map<String, String> dailyMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stRainDailyDtos)) {
            dailyMap = stRainDailyDtos.parallelStream().collect(Collectors.toMap(StRainDailyDto::getStationId, StRainDailyDto::getValue, (o1, o2) -> o2));
        }

        DzmReqGis dzmReqGis = new DzmReqGis();
        dzmReqGis.setDisplayFieldName("");
        dzmReqGis.setGeometryType("esriGeometryPoint");
        DzmReqGis.FieldAliasesBean fieldAliasesBean = new DzmReqGis.FieldAliasesBean();
        fieldAliasesBean.setDrp("drp");
        fieldAliasesBean.setStcd("stcd");
        fieldAliasesBean.setStnm("stnm");
        fieldAliasesBean.setLgtd("lgtd");
        fieldAliasesBean.setLttd("lttd");
        fieldAliasesBean.setFID("FID");
        dzmReqGis.setFieldAliases(fieldAliasesBean);
        dzmReqGis.setSpatialReference(new DzmReqGis.SpatialReferenceBean(4326, 4326));
        List<DzmReqGis.FieldsBean> fields = new ArrayList<>();
        fields.add(new DzmReqGis.FieldsBean("FID", "esriFieldTypeOID", "FID"));
        fields.add(new DzmReqGis.FieldsBean("stcd", "esriFieldTypeString", "stcd"));
        fields.add(new DzmReqGis.FieldsBean("stnm", "esriFieldTypeString", "stnm", 254));
        //fields.add(new DzmReqGis.FieldsBean("lgtd", "esriFieldTypeDouble", "stnm"));
        fields.add(new DzmReqGis.FieldsBean("lgtd", "esriFieldTypeDouble", "lgtd"));
        fields.add(new DzmReqGis.FieldsBean("lttd", "esriFieldTypeDouble", "lttd"));
        fields.add(new DzmReqGis.FieldsBean("drp", "esriFieldTypeDouble", "drp"));
        dzmReqGis.setFields(fields);
        List<DzmReqGis.FeaturesBean> featuresBeanList = new ArrayList<>();
        dzmReqGis.setFeatures(featuresBeanList);

        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            RainHourDataEntity rainHourDataEntity = hourMap.get(stStbprpBEntity.getStcd());
            String s1 = dailyMap.get(stStbprpBEntity.getStcd());
            double p = 0.0;
            if (rainHourDataEntity != null || StrUtil.isNotEmpty(s1)) {
                if (hours.compareTo(1) == 0) {
                    String hourOne = rainHourDataEntity.getHourOne();
                    p = Double.valueOf(hourOne);
                } else if (hours.compareTo(3) == 0) {
                    String hourThree = rainHourDataEntity.getHourThree();
                    p = Double.valueOf(hourThree);
                } else if (hours.compareTo(6) == 0) {
                    String hourSix = rainHourDataEntity.getHourSix();
                    p = Double.valueOf(hourSix);
                } else if (hours.compareTo(12) == 0) {
                    String hourTwelve = rainHourDataEntity.getHourTwelve();

                    p = Double.valueOf(hourTwelve);
                } else if (hours.compareTo(24) == 0) {
                    String hourTwenty = rainHourDataEntity.getHourTwenty();
                    p = Double.valueOf(hourTwenty);
                } else if (hours.compareTo(0) == 0) {
                    //当天
                    String s = dailyMap.get(stStbprpBEntity.getStcd());
                    if (StrUtil.isNotEmpty(s)) {
                        p = Double.valueOf(s);
                    }
                }
            }


            DzmReqGis.FeaturesBean featuresBean = new DzmReqGis.FeaturesBean();
            DzmReqGis.FeaturesBean.AttributesBean attributesBean = new DzmReqGis.FeaturesBean.AttributesBean();
            attributesBean.setDrp(p);
            attributesBean.setLgtd(Double.valueOf(stStbprpBEntity.getLgtd().toString()));
            attributesBean.setLttd(Double.valueOf(stStbprpBEntity.getLttd().toString()));
            attributesBean.setStcd(stStbprpBEntity.getStcd());
            attributesBean.setStnm(stStbprpBEntity.getStnm());
            attributesBean.setFID(atomicInteger.getAndIncrement());
            featuresBean.setAttributes(attributesBean);
            DzmReqGis.FeaturesBean.GeometryBean geometryBean = new DzmReqGis.FeaturesBean.GeometryBean();
            geometryBean.setX(Double.valueOf(stStbprpBEntity.getLgtd().toString()));
            geometryBean.setY(Double.valueOf(stStbprpBEntity.getLttd().toString()));
            featuresBean.setGeometry(geometryBean);
            featuresBeanList.add(featuresBean);
        }
        atomicInteger.set(1);

        //时段数据默认为日数据
        String reclassification = "0.4 9.9 1;9.9 24.9 2;24.9 49.9 3;49.9 99.9 4;99.9 199.9 5;199.9 9999 6";
        if (null != hours) {
            if (hours.compareTo(1) == 0) {
                reclassification = "0.4 2.49 1;2.49 7.9 2;7.9 16 3;16 20 4;20 9999 5";
            } else if (hours.compareTo(2) == 0) {
                reclassification = "0.4 3.9 1;3.9 11.9 2;11.9 24.9 3;24.9 54.9 4;54.9 89.9 5;89.9 9999 6";
            } else if (hours.compareTo(3) == 0) {
                reclassification = "0.4 3.9 1;3.9 11.9 2;11.9 24.9 3;24.9 54.9 4;54.9 89.9 5;89.9 9999 6";
            } else if (hours.compareTo(12) == 0) {
                reclassification = "0.4 4.9 1;4.9 14.9 2;14.9 29.9 3;29.9 69.9 4;69.9 139.9 5;139.9 9999 6";
            } else if (hours.compareTo(24) == 0) {
                reclassification = "0.4 9.9 1;9.9 24.9 2;24.9 49.9 3;49.9 99.9 4;99.9 199.9 5;199.9 9999 6";
            } else if (hours.compareTo(0) == 0) {
                //当天
                reclassification = "0.4 9.9 1;9.9 24.9 2;24.9 49.9 3;49.9 99.9 4;99.9 199.9 5;199.9 9999 6";
            }
        }

        String s = JSONObject.toJSONString(dzmReqGis);
        String hdyqDzm = getHdyqDzm(API_RAIN_FALL_EQUALS_CASE, reclassification, JSONObject.toJSONString(dzmReqGis));
        HdyqDzmGisResp resp = JSONObject.parseObject(hdyqDzm, HdyqDzmGisResp.class);
        return resp;

    }

    @Override
    public String getYldzm(Integer hours) {
        List<RainDateHourDto> list = getRainHourData(null, null);
        list.forEach(v -> {
            if (ObjectUtil.equal(0, hours)) {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRainh()) ? v.getRainh() : "0"));
            } else if (ObjectUtil.equal(1, hours)) {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRain1h()) ? v.getRain1h() : "0"));
            } else if (ObjectUtil.equal(3, hours)) {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRain3h()) ? v.getRain3h() : "0"));
            } else if (ObjectUtil.equal(6, hours)) {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRain6h()) ? v.getRain6h() : "0"));
            } else if (ObjectUtil.equal(12, hours)) {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRain12h()) ? v.getRain12h() : "0"));
            } else {
                v.setRain(Double.valueOf(StringUtils.isNotBlank(v.getRain24h()) ? v.getRain24h() : "0"));
            }
        });
        return getDzm(list, hours);
    }


    @Override
    public List<HourTimeAxisRainVo> getHourTimeAxis(Integer hour, String stationID) {
        List<HourTimeAxisRainVo> voList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        //测站基本属性
        List<StStbprpBEntity> stationList = stStbprpBDao.selectList(
                new LambdaQueryWrapper<StStbprpBEntity>().eq(StStbprpBEntity::getSttp, "PP")
                        .eq(StringUtils.isNotBlank(stationID), StStbprpBEntity::getStcd, stationID));
        //降雨
        List<StRainDateDto> rainList = stRainDateDao.selectList(
                new LambdaQueryWrapper<StRainDateDto>()
                        .ge(StRainDateDto::getDate, formatter.format(now.minusHours(hour)))
                        .le(StRainDateDto::getDate, formatter.format(now.withMinute(59).withSecond(59)))
        );
        //彩云天气
        List<CaiyunPrecipitationRealVo> rainGridList = caiYunPrecipitationRealDao.getStationAndRainGrid(formatter.format(now), formatter.format(now.plusHours(hour)));
        for (int i = hour; i >= 0; i--) {
            HourTimeAxisRainVo vo = new HourTimeAxisRainVo();
            if (i == 0) {
                vo.setName("当前降雨");
                vo.setHour("当前");
            } else {
                vo.setName("前" + i + "小时降雨");
                vo.setHour(i + "h");
            }
            vo.setDate(now.minusHours(i));
            HashMap<String, Double> map = new HashMap<>();
            if (CollectionUtil.isNotEmpty(rainList)) {
                map = rainList.stream()
                        .filter(v -> {
                            LocalDateTime date = DateUtil.toLocalDateTime(v.getDate());
                            return vo.getDate().isBefore(date) && vo.getDate().withMinute(59).withSecond(59).isAfter(date);
                        }).collect(Collectors.toMap(StRainDateDto::getStationId,
                                stRainDateDto -> Optional.ofNullable(stRainDateDto.getHhRain()).map(Double::parseDouble).orElse(0.0),
                                Double::sum, HashMap::new));
            }
            HashMap<String, Double> finalMap = map;
            List<HourTimeAxisRainVo.Detail> detailList = stationList.stream()
                    .map(station -> {
                        HourTimeAxisRainVo.Detail detail = new HourTimeAxisRainVo.Detail();
                        detail.setStnm(station.getStnm());
                        detail.setLttd(station.getLttd());
                        detail.setLgtd(station.getLgtd());
                        detail.setDate(vo.getDate());
                        detail.setRain(finalMap.getOrDefault(station.getStcd(), 0.0));
                        return detail;
                    }).collect(Collectors.toList());
            vo.setDetailList(detailList);
            voList.add(vo);
        }
        for (int i = 1; i <= hour; i++) {
            HourTimeAxisRainVo vo = new HourTimeAxisRainVo();
            vo.setName("后" + i + "小时预报降雨");
            vo.setHour(i + "h");
            vo.setDate(now.plusHours(i));
            HashMap<String, Double> map = new HashMap<>();
            if (CollectionUtil.isNotEmpty(rainGridList)) {
                map = rainGridList.stream()
                        .filter(v -> v.getDrpTime() != null && DateUtil.toLocalDateTime(v.getDrpTime()).equals(vo.getDate()))
                        .collect(Collectors.toMap(CaiyunPrecipitationRealVo::getStcd,
                                caiYun -> Optional.ofNullable(caiYun.getDrp()).orElse(0.0),
                                Double::sum, HashMap::new));
            }
            HashMap<String, Double> finalMap = map;
            List<HourTimeAxisRainVo.Detail> detailList = stationList.stream()
                    .map(station -> {
                        HourTimeAxisRainVo.Detail detail = new HourTimeAxisRainVo.Detail();
                        detail.setStnm(station.getStnm());
                        detail.setLttd(station.getLttd());
                        detail.setLgtd(station.getLgtd());
                        detail.setDate(vo.getDate());
                        detail.setRain(finalMap.getOrDefault(station.getStcd(), 0.0));
                        return detail;
                    }).collect(Collectors.toList());
            vo.setDetailList(detailList);
            voList.add(vo);
        }
        //累加
        HashMap<String, Double> map = new HashMap<>();
        for (HourTimeAxisRainVo vo : voList) {
            for (HourTimeAxisRainVo.Detail detail : vo.getDetailList()) {
                map.put(detail.getStnm(), map.getOrDefault(detail.getStnm(), 0.0) + detail.getRain());
                Double d = map.get(detail.getStnm());
                detail.setRain(new BigDecimal((Double.toString(d))).setScale(1, RoundingMode.HALF_UP).doubleValue());
            }
        }
        //等值面
        for (HourTimeAxisRainVo vo : voList) {
            String dzm = getRainCommonDzm(vo.getDetailList(), 0);
            vo.setDzm(dzm);
        }

        return voList;
    }

    /**
     * 获取雨晴等值面
     * Reclassification         0.01 2.5 1;2.5 8 2;8 16 3;16 20 4;20 9999 5
     */
    public String getHdyqDzm(String gisUrl0, String Reclassification, String inputRainfall_shp) throws Exception {
        // 创建默认的httpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(gisUrl0);
        // 创建参数队列
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Reclassification", Reclassification));
        params.add(new BasicNameValuePair("input", inputRainfall_shp));
        params.add(new BasicNameValuePair("returnZ", "false"));
        params.add(new BasicNameValuePair("returnM", "false"));
        params.add(new BasicNameValuePair("env:processSR", ""));
        params.add(new BasicNameValuePair("env:outSR", ""));
        params.add(new BasicNameValuePair("f", "pjson"));
        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        httppost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httppost);
        HttpEntity responseEntity = response.getEntity();
        String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
        if (!responseContent.contains("features")) {
            throw new RuntimeException("调用GIS服务-获取水闸，错误信息：" + responseContent);
        }
        response.close();
        // 关闭连接,释放资源
        httpclient.close();
        return responseContent;
    }

    public Map<String, Object> dealRainNum(Date start, Date end) {
        Map<String, Object> infoMap = new HashMap<>();
        String format = DateUtil.format(start, "yyyy-MM-dd");
        String format2 = DateUtil.format(end, "yyyy-MM-dd");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("date", format2);
        wrapper.ge("date", format);
        wrapper.orderByAsc("station_id");
        Double count = 0.0;
        List<RainDayCountDto> rainDayCountDtos = rainDayCountDao.selectList(wrapper);
        Map<BigDecimal, String> map = new LinkedHashMap<>();
        List<RainDayCountDto> rainDayCountDtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(rainDayCountDtos)) {
            Map<String, List<RainDayCountDto>> stationMap = rainDayCountDtos.parallelStream().filter(rainDayCountDto -> {
                return rainDayCountDto.getDayCount() != null;
            }).collect(Collectors.groupingBy(RainDayCountDto::getStationID));
            Integer size = stationMap.size();
            count = size.doubleValue();
            rainDayCountDtoList = rainDayCountDtos.stream().filter(x -> x.getDayCount() != null).collect(Collectors.toList());
            for (String stationId : stationMap.keySet()) {
                List<RainDayCountDto> rainDayCountDtos1 = stationMap.get(stationId);
                if (CollUtil.isNotEmpty(rainDayCountDtos1)) {
                    BigDecimal reduce = rainDayCountDtos1.parallelStream().map(RainDayCountDto::getDayCount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    map.put(reduce, stationId);
                }
            }
        }
        String stationId = "";
        BigDecimal maxNm = new BigDecimal(0);
        if (CollUtil.isNotEmpty(map)) {
            List<BigDecimal> collect = map.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            stationId = map.get(collect.get(0));
            maxNm = collect.get(0);
            BigDecimal sum = BigDecimal.valueOf(rainDayCountDtoList.stream().mapToDouble(x -> x.getDayCount().doubleValue()).sum());
            //累计雨量
            BigDecimal reduce = collect.parallelStream().reduce(BigDecimal.ZERO, BigDecimal::add);
            infoMap.put("addNum", sum);
            //平均雨量
            BigDecimal divide = sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
            infoMap.put("avgNum", divide);

        }
        infoMap.put("stationId", stationId);
        infoMap.put("maxNm", maxNm);
        return infoMap;
    }

    /**
     * 同比计算
     *
     * @param current 本期
     * @param last    上期
     * @return
     */
    public BigDecimal countCurrent(BigDecimal current, BigDecimal last) {
        if (last == null || last.equals(BigDecimal.ZERO)) {
            return new BigDecimal(0);
        } else {
            return (current.subtract(last)).divide(last, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }
    }


    private SimpleFeatureCollection fc;

    private static GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * 启动预加载
     */
    @PostConstruct
    public void preloadShapefile() {
        try {
            Charset charset = Charset.forName("GBK");
            URL hdgwResourceUrl = getClass().getClassLoader().getResource("source/cyxjbj.shp");
            if (hdgwResourceUrl == null) {
                throw new RuntimeException("朝阳等值面shp File not found!");
            }
            ShapefileDataStore hdgwShpDataStore = new ShapefileDataStore(hdgwResourceUrl);
            hdgwShpDataStore.setCharset(charset);
            String hdgwTypeName = hdgwShpDataStore.getTypeNames()[0];
            SimpleFeatureSource hdgwFeatureSource = hdgwShpDataStore.getFeatureSource(hdgwTypeName);
            fc = hdgwFeatureSource.getFeatures();
        } catch (Exception e) {
            log.warn("预加载等直面-地图shp文件，{}", e);
        }
    }


    /**
     * 获取雨量公共等值面
     *
     * @param list  只要经度,纬度,降雨量.
     * @param hours
     * @return
     */
    private String getDzm(List<RainDateHourDto> list, Integer hours) {
        // 雨量站点经纬度及雨量值
        double[][] trainData = new double[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            RainDateHourDto dto = list.get(i);
            trainData[i][0] = dto.getLgtd().doubleValue();
            trainData[i][1] = dto.getLttd().doubleValue();
            trainData[i][2] = dto.getRain();
        }
        return getData(hours, trainData);
    }

    /**
     * 获取雨量公共等值面
     *
     * @param list  只要经度,纬度,降雨量.
     * @param hours
     * @return
     */
    private String getRainCommonDzm(List<HourTimeAxisRainVo.Detail> list, Integer hours) {
        if (CollectionUtils.isEmpty(list) || hours == null) {
            return null;
        }
        // 雨量站点经纬度及雨量值
        double[][] trainData = new double[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            HourTimeAxisRainVo.Detail dto = list.get(i);
            trainData[i][0] = dto.getLgtd().doubleValue();
            trainData[i][1] = dto.getLttd().doubleValue();
            trainData[i][2] = dto.getRain();
        }
        return getData(hours, trainData);
    }

    /**
     * 获取等值面
     */
    private String getData(Integer hours, double[][] trainData) {
        String dzm = null;
        double[] dataInterval;
        if (hours <= 3) {
            // 1小时等级
            dataInterval = new double[]{0, 0.01, 0.06, 1, 2.5, 4, 5, 6, 8, 10, 12, 14, 16, 18, 20, 21, 26, 30, 40, 50, 60, 70, 80, 90, 100};
        } else if (hours <= 6) {
            // 6小时等级
            dataInterval = new double[]{0, 0.01, 0.06, 1, 3, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 55, 60, 65, 70, 75, 80, 85, 90, 120, 140, 150, 160, 280, 200, 220, 240, 280, 300, 350, 400, 600};
        } else if (hours <= 12) {
            // 12小时等级
            dataInterval = new double[]{0, 0.01, 0.06, 1, 3, 5, 12, 14, 15, 16, 18, 20, 22, 24, 26, 28, 30, 34, 38, 42, 45, 47, 50, 54, 58, 60, 64, 68, 70, 74, 76, 80, 84, 88, 90, 94, 98, 100, 115, 120, 125, 130, 135, 140, 145, 150, 160, 165, 170, 180, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 600};
        } else {
            // 24小时等级
            dataInterval = new double[]{0, 0.01, 0.06, 1, 4, 8, 10, 14, 16, 18, 20, 22, 24, 25, 26, 28, 30, 34, 38, 42, 45, 47, 50, 54, 58, 60, 64, 68, 70, 74, 78, 80, 84, 88, 90, 94, 98, 100, 115, 120, 125, 130, 135, 140, 145, 150, 160, 165, 170, 180, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400, 420, 440, 460, 480, 500, 520, 540, 560, 580, 600, 650, 700, 750, 800, 850, 900, 950, 1000, 1100, 1200, 1300, 1400, 1500, 99999999};
        }
        int[] size = new int[]{100, 100};
        try {
            // 返回前端数据
            dzm = calEquiSurface(trainData, dataInterval, size, hours, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dzm;
    }

    /**
     * 生成等值面
     *
     * @param trainData    训练数据
     * @param dataInterval 数据间隔
     * @param size         大小，宽，高
     * @param hours        调用前多少小时等值面
     * @param isClip       是否裁剪
     * @return
     */
    public String calEquiSurface(double[][] trainData, double[] dataInterval, int[] size, Integer hours, boolean isClip) {
        String geojsonpogylon = "";
        try {
            double _undefData = -9999.0;
            SimpleFeatureCollection polygonCollection = null;
            List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
            List<Polygon> cPolygonList = new ArrayList<Polygon>();

            int width = size[0], height = size[1];
            double[] _X = new double[width];
            double[] _Y = new double[height];


            double minX = fc.getBounds().getMinX();
            double minY = fc.getBounds().getMinY();
            double maxX = fc.getBounds().getMaxX();
            double maxY = fc.getBounds().getMaxY();

            Interpolate.createGridXY_Num(minX, minY, maxX + 0.01, maxY, _X, _Y);

            double[][] _gridData = new double[width][height];

            int nc = dataInterval.length;

            _gridData = Interpolate.interpolation_IDW_Neighbor(trainData, _X, _Y, 2, _undefData);// IDW插值
            int[][] S1 = new int[_gridData.length][_gridData[0].length];
            List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y, S1, _undefData);

            cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc, dataInterval, _undefData, _borders, S1);// 生成等值线

            //cPolylineList = Contour.smoothLines(cPolylineList);// 平滑

            cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList, _borders, dataInterval);

//            geojsonpogylon = getPolygonGeoJson(cPolygonList);

            if (isClip) {
//                polygonCollection = readGeoJsonByString(geojsonpogylon);
                polygonCollection = (SimpleFeatureCollection) getFeatureCollection(cPolygonList);
                SimpleFeatureCollection sm = clipPolygonFeatureCollection(fc, polygonCollection);
                SimpleFeatureCollection simpleFeatureCollection = unionFeatureCollection(sm, hours);
                FeatureCollection featureCollection = simpleFeatureCollection;

                FeatureJSON featureJSON = new FeatureJSON();
                StringWriter writer = new StringWriter();
                featureJSON.writeFeatureCollection(featureCollection, writer);
                geojsonpogylon = writer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geojsonpogylon;
    }

    /**
     * 将多个面合并成一个面,并根据面积排序
     *
     * @param featureCollection
     * @return
     * @throws SchemaException
     */
    private SimpleFeatureCollection unionFeatureCollection(FeatureCollection featureCollection, Integer hours) throws SchemaException {
        FeatureIterator contourFeatureIterator = featureCollection.features();
        Map<Integer, SimpleFeature> levelFeatureMap = new HashMap<>();
        SimpleFeatureType TYPE = DataUtilities.createType("polygons", "the_geom:MultiPolygon,level:double");
        while (contourFeatureIterator.hasNext()) {
            SimpleFeature contourFeature = (SimpleFeature) contourFeatureIterator.next();
            Geometry contourGeometry = (Geometry) contourFeature.getDefaultGeometry();
            Double hv = (Double) contourFeature.getProperty("level").getValue();
            int level = getLevel(hv, hours);
            if (levelFeatureMap.containsKey(level)) {
                SimpleFeature simpleFeature = levelFeatureMap.get(level);
                Geometry defaultGeometry = (Geometry) simpleFeature.getDefaultGeometry();
                Geometry unionGeometry = defaultGeometry.union(contourGeometry); // 合并
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                featureBuilder.add(unionGeometry);
                featureBuilder.add(level);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                levelFeatureMap.put(level, feature);
            } else {
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                featureBuilder.add(contourGeometry);
                featureBuilder.add(level);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                levelFeatureMap.put(level, feature);
            }
        }
        List<SimpleFeature> list = new ArrayList<>();
        for (int key : levelFeatureMap.keySet()) {
            SimpleFeature simpleFeature = levelFeatureMap.get(key);
            Geometry dataGeometry = (Geometry) simpleFeature.getDefaultGeometry();
            int numGeometries = dataGeometry.getNumGeometries();
            for (int i = 0; i < numGeometries; i++) {
                Geometry geometryN = dataGeometry.getGeometryN(i);
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                Double levelValue = (Double) simpleFeature.getProperty("level").getValue();
                featureBuilder.add(geometryN);
                featureBuilder.add(levelValue);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                list.add(feature);
            }
        }
        list.sort(new Comparator<SimpleFeature>() {
            @Override
            public int compare(SimpleFeature o1, SimpleFeature o2) {
                Geometry geometry1 = (Geometry) o1.getDefaultGeometry();
                Geometry geometry2 = (Geometry) o2.getDefaultGeometry();
                double area1 = geometry1.getArea();
                double area2 = geometry2.getArea();
                return Double.compare(area2, area1);
            }
        });
        SimpleFeatureCollection simpleFeatureCollection = new ListFeatureCollection(TYPE, list);
        //  合并集合 union
        return simpleFeatureCollection;
    }

    /**
     * 根据geo 属性获取当前面的等级
     * 有标准的有：0小时(当前), 1小时, 3小时, 6小时, 12小时, 24小时, 96小时, 100小时
     *
     * @param value
     * @param hours 大于0 是 雨量站，如上面注释；  -10 属于地下水
     * @return
     */
    private int getLevel(Double value, Integer hours) {
        int level = 0;
        //地下水
        if (hours == -10) {
            if (value <= 0) {
                level = 0;
            } else if (value <= 10) {
                level = 1;
            } else if (value <= 20) {
                level = 2;
            } else if (value <= 30) {
                level = 3;
            } else if (value <= 35) {
                level = 4;
            } else if (value <= 40) {
                level = 5;
            } else if (value <= 45) {
                level = 6;
            } else if (value <= 50) {
                level = 7;
            } else if (value <= 55) {
                level = 8;
            } else {
                level = 9;
            }
        }
        //0小时
        else if (hours == 0) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 10) {
                level = 1;
            } else if (value < 25) {
                level = 2;
            } else if (value < 50) {
                level = 3;
            } else if (value < 100) {
                level = 4;
            } else if (value < 200) {
                level = 5;
            } else {
                level = 6;
            }
        }
        //1小时
        else if (hours == 1) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 2.5) {
                level = 1;
            } else if (value < 8) {
                level = 2;
            } else if (value < 16) {
                level = 3;
            } else if (value < 20) {
                level = 4;
            } else {
                level = 5;
            }
        }
        //3小时
        else if (hours == 3) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 4) {
                level = 1;
            } else if (value < 12) {
                level = 2;
            } else if (value < 25) {
                level = 3;
            } else if (value < 55) {
                level = 4;
            } else if (value < 90) {
                level = 5;
            } else {
                level = 6;
            }
        }
        //6小时
        else if (hours == 6) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 4) {
                level = 1;
            } else if (value < 12) {
                level = 2;
            } else if (value < 25) {
                level = 3;
            } else if (value < 55) {
                level = 4;
            } else if (value < 90) {
                level = 5;
            } else {
                level = 6;
            }
        }
        //12小时
        else if (hours == 12) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 5) {
                level = 1;
            } else if (value < 15) {
                level = 2;
            } else if (value < 30) {
                level = 3;
            } else if (value < 70) {
                level = 4;
            } else if (value < 140) {
                level = 5;
            } else {
                level = 6;
            }
        }
        //24小时,96小时
        else if (hours < 100) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 10) {
                level = 1;
            } else if (value < 25) {
                level = 2;
            } else if (value < 50) {
                level = 3;
            } else if (value < 100) {
                level = 4;
            } else if (value < 200) {
                level = 5;
            } else {
                level = 6;
            }
        }
        //100小时
        else if (hours == 100) {
            if (value <= 0.05) {
                level = 0;
            } else if (value < 10) {
                level = 1;
            } else if (value < 25) {
                level = 2;
            } else if (value < 50) {
                level = 3;
            } else if (value < 100) {
                level = 4;
            } else if (value < 250) {
                level = 5;
            } else if (value < 400) {
                level = 6;
            } else if (value < 600) {
                level = 7;
            } else {
                level = 8;
            }
        }
        return level;
    }

    /**
     * 裁剪图形
     *
     * @param fc
     * @return
     */
    private SimpleFeatureCollection clipPolygonFeatureCollection(FeatureCollection fc, SimpleFeatureCollection gs) throws SchemaException, IOException {
        SimpleFeatureCollection simpleFeatureCollection = null;
        SimpleFeatureType TYPE = DataUtilities.createType("polygons", "the_geom:MultiPolygon,level:double");
        List<SimpleFeature> list = new ArrayList<>();
        SimpleFeatureIterator contourFeatureIterator = gs.features();
        FeatureIterator dataFeatureIterator = fc.features();
        while (dataFeatureIterator.hasNext()) {
            SimpleFeature dataFeature = (SimpleFeature) dataFeatureIterator.next();

            Geometry dataGeometry = (Geometry) dataFeature.getDefaultGeometry();
            contourFeatureIterator = gs.features();
            while (contourFeatureIterator.hasNext()) {
                SimpleFeature contourFeature = contourFeatureIterator.next();
                Geometry contourGeometry = (Geometry) contourFeature.getDefaultGeometry();
//                Double lv = (Double) contourFeature.getProperty("lvalue").getValue();
                Double hv = (Double) contourFeature.getProperty("level").getValue();
//                String id = (String) contourFeature.getProperty("id").getValue();

                if (dataGeometry.getGeometryType() == "MultiPolygon") {
                    for (int i = 0; i < dataGeometry.getNumGeometries(); i++) {
                        Geometry geom = dataGeometry.getGeometryN(i);
                        if (geom.intersects(contourGeometry)) {
                            Geometry geo = geom.intersection(contourGeometry);
                            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                            featureBuilder.add(geo);
//                            featureBuilder.add(lv);
                            featureBuilder.add(hv);
//                            featureBuilder.add(id);
                            SimpleFeature feature = featureBuilder.buildFeature(null);
                            list.add(feature);
                        }
                    }
                } else {
                    if (dataGeometry.intersects(contourGeometry)) {
                        Geometry geo = dataGeometry.intersection(contourGeometry);
                        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                        featureBuilder.add(geo);
//                        featureBuilder.add(lv);
                        featureBuilder.add(hv);
//                        featureBuilder.add(id);
                        SimpleFeature feature = featureBuilder.buildFeature(null);
                        list.add(feature);

                    }

                }
            }
        }
        contourFeatureIterator.close();
        dataFeatureIterator.close();
        simpleFeatureCollection = new ListFeatureCollection(TYPE, list);
        return simpleFeatureCollection;
    }


    /**
     * 结果转换
     *
     * @param cPolygonList
     * @return
     */
    public static FeatureCollection getFeatureCollection(List<Polygon> cPolygonList) {
        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        FeatureCollection cs = null;
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        try {
            for (Polygon pPolygon : cPolygonList) {
                //外圈
                LinearRing mainRing;
                Coordinate[] coordinates = new Coordinate[pPolygon.OutLine.PointList.size() + 1];
                for (int i = 0, len = pPolygon.OutLine.PointList.size(); i < len; i++) {
                    PointD ptd = pPolygon.OutLine.PointList.get(i);
                    coordinates[i] = new Coordinate(ptd.X, ptd.Y);
                }
                coordinates[pPolygon.OutLine.PointList.size()] = coordinates[0]; // 保证坐标闭环
                mainRing = geometryFactory.createLinearRing(coordinates);

                //孔洞
                LinearRing[] holeRing = new LinearRing[pPolygon.HoleLines.size()];
                for (int i = 0; i < pPolygon.HoleLines.size(); i++) {
                    PolyLine hole = pPolygon.HoleLines.get(i);
                    Coordinate[] coordinates_h = new Coordinate[hole.PointList.size() + 1];
                    for (int j = 0, len = hole.PointList.size(); j < len; j++) {
                        PointD ptd = hole.PointList.get(j);
                        coordinates_h[j] = new Coordinate(ptd.X, ptd.Y);
                    }
                    coordinates_h[hole.PointList.size()] = coordinates_h[0]; // 保证坐标闭环
                    holeRing[i] = geometryFactory.createLinearRing(coordinates_h);
                }
                org.locationtech.jts.geom.Polygon geo = geometryFactory.createPolygon(mainRing, holeRing);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("the_geom", geo);
                map.put("level", pPolygon.LowValue);
                values.add(map);
            }

            cs = creatFeatureCollection("polygons", "the_geom:Polygon:srid=4326,level:double", values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cs;
    }


    /**
     * geotools创建FeatureCollection
     *
     * @param typeName
     * @param typeSpec
     * @param values
     * @return
     * @throws SchemaException
     */
    public static FeatureCollection creatFeatureCollection(String typeName, String typeSpec, List<Map<String, Object>> values) throws SchemaException {
        SimpleFeatureType type = DataUtilities.createType(typeName, typeSpec);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        for (Map feat : values) {
            featureBuilder.reset();
            featureBuilder.add(feat.get("the_geom"));
            featureBuilder.add(feat.get("level"));
            SimpleFeature feature = featureBuilder.buildFeature(null);
            collection.add(feature);
        }
        return collection;
    }
}
