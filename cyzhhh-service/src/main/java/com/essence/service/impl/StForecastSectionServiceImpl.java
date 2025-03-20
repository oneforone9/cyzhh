package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.UuidUtil;
import com.essence.dao.*;
import com.essence.dao.entity.StForecastSectionDto;
import com.essence.dao.entity.StSnConvertEntity;
import com.essence.dao.entity.StStbprpBEntity;
import com.essence.dao.entity.StWaterRateEntity;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForecastSectionService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StForecastSectionEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStForecastSectionEtoT;
import com.essence.service.converter.ConverterStForecastSectionTtoR;
import com.essence.service.impl.listener.StForecastSectionListenerZQ;
import com.essence.service.utils.DataUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (StForecastSection)业务层
 *
 * @author BINX
 * @since 2023-04-22 10:55:00
 */
@Service
@Slf4j
public class StForecastSectionServiceImpl extends BaseApiImpl<StForecastSectionEsu, StForecastSectionEsp, StForecastSectionEsr, StForecastSectionDto> implements StForecastSectionService {

    @Autowired
    private StForecastSectionDao stForecastSectionDao;
    @Resource
    private StWaterRateDao stWaterRateDao;
    @Resource
    private StCaseBaseInfoDao stCaseBaseInfoDao;
    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private ConverterStForecastSectionEtoT converterStForecastSectionEtoT;
    @Autowired
    private ConverterStForecastSectionTtoR converterStForecastSectionTtoR;
    @Autowired
    private StStbprpBDao stStbprpBDao;

    public StForecastSectionServiceImpl(StForecastSectionDao stForecastSectionDao, ConverterStForecastSectionEtoT converterStForecastSectionEtoT, ConverterStForecastSectionTtoR converterStForecastSectionTtoR) {
        super(stForecastSectionDao, converterStForecastSectionEtoT, converterStForecastSectionTtoR);
    }

    //导入方案预报流量边界数据
    @Override
    public void importStForecastSectionZQ(String caseId, MultipartFile file,String type) {
        try {
            EasyExcel.read(file.getInputStream(), StForecastSectionImport.class, new StForecastSectionListenerZQ(this, caseId,type)).sheet().doRead();
        } catch (IOException e) {
            log.error("预报流量边界数据Excel导入失败", e);
        }
    }

    //导入方案预报水位边界数据
    @Override
    public void importStForecastSectionZZ(String caseId, MultipartFile file,String type) {
        try {
            EasyExcel.read(file.getInputStream(), StForecastSectionImport.class, new StForecastSectionListenerZQ(this, caseId,type)).sheet().doRead();
        } catch (IOException e) {
            log.error("预报水位边界数据Excel导入失败", e);
        }
    }

    @Override
    public void saveForecastSection(List<StForecastSectionEsr> stForecastSectionEsrList, String caseId,String type) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.isNotNull("seria_name");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        Map<String, String> secNameFlg = new HashMap<>();
        if (CollUtil.isNotEmpty(stStbprpBEntities)){
            secNameFlg = stStbprpBEntities.parallelStream().filter(stStbprpBEntity -> {return StrUtil.isNotEmpty(stStbprpBEntity.getSeriaName()) ; }).collect(Collectors.toMap(StStbprpBEntity::getSectionName, StStbprpBEntity::getSeriaName));
        }
        Map<String, String> finalSecNameFlg = secNameFlg;
        List<StForecastSectionDto> stForecastSectionDtoList = Optional.ofNullable(
                stForecastSectionEsrList.stream().map(x -> {
                    StForecastSectionDto stForecastSectionDto = new StForecastSectionDto();
                    stForecastSectionDto.setId(UuidUtil.get32UUIDStr());
                    stForecastSectionDto.setSectionName(x.getSectionName());
                    stForecastSectionDto.setSttp(x.getSttp());
                    stForecastSectionDto.setDate(x.getDate());
                    stForecastSectionDto.setValue(x.getValue());
                    if (StrUtil.isEmpty(finalSecNameFlg.get(x.getSectionName())))
                    {
                        if (x.getSectionName().contains("亮马河")){
                            stForecastSectionDto.setSeriaName("亮马河");
                        }else {
                            throw new RuntimeException("导入数据失败 case: 断面与 时间序列未能关联");
                        }
                    }else {
                        stForecastSectionDto.setSeriaName(finalSecNameFlg.get(x.getSectionName()));
                    }
                    stForecastSectionDto.setUpdateTime(new DateTime());
                    stForecastSectionDto.setCaseId(caseId);
                    stForecastSectionDto.setType(type);
                    return stForecastSectionDto;
                }).collect(Collectors.toList())).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stForecastSectionDtoList)) {
            stForecastSectionDao.saveForecastSection(stForecastSectionDtoList);
        }
    }

    @Override
    public List<StForecastSectionExport> exportStForecastSectionZQ() {
        List<StForecastSectionExport> stForecastSectionExportList = new ArrayList<>();

        //流量站台账数据  ZQ流量
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "ZQ").isNotNull(StStbprpBEntity::getSectionName).eq(StStbprpBEntity::getSectionFlag, "入流").orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());

        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
                stForecastSectionExportList = stStbprpBEntityList.stream().map(x -> {
                    StForecastSectionExport stForecastSectionExport = new StForecastSectionExport();
                    stForecastSectionExport.setSectionName(x.getSectionName());
                    stForecastSectionExport.setSttp(x.getSttp());
                    return stForecastSectionExport;
                }).collect(Collectors.toList());
            }
        }
        StForecastSectionExport stForecastSectionExport = new StForecastSectionExport();
        stForecastSectionExport.setSectionName("亮马河上游-0");
        stForecastSectionExport.setSttp("ZQ");
        stForecastSectionExportList.add(stForecastSectionExport);
        return stForecastSectionExportList;
    }

    @Override
    public List<StForecastSectionExport> exportStForecastSectionZZ() {
        List<StForecastSectionExport> stForecastSectionExportList = new ArrayList<>();
        List<String>sttpS=new ArrayList<>();
        sttpS.add("ZQ");
        sttpS.add("ZZ");
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().in(StStbprpBEntity::getSttp, sttpS).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
           stStbprpBEntityList = stStbprpBEntityList.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).filter(x->StringUtil.isBlank(x.getSectionFlag())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
                stForecastSectionExportList = stStbprpBEntityList.stream().map(x -> {
                    StForecastSectionExport stForecastSectionExport = new StForecastSectionExport();
                    stForecastSectionExport.setSectionName(x.getSectionName());
                    stForecastSectionExport.setSttp(x.getSttp());
                    return stForecastSectionExport;
                }).collect(Collectors.toList());
            }
        }
        return stForecastSectionExportList;
    }

    @Override
    public Paginator<StForecastSectionEsr> findByPaginator(PaginatorParam param) {
        Paginator<StForecastSectionEsr> byPaginator = super.findByPaginator(param);
        if (byPaginator == null || CollUtil.isEmpty(byPaginator.getItems()) ){
            //开始查询 实测的流量或者 水位的最后一个点数据 给到 暂时分别每个站点给 5条

            Criterion criterion = param.getConditions().get(0);
            //流量1水位2
            String type = (String) criterion.getValue();
            Criterion criterion1 = param.getConditions().get(1);
            String caseId = (String) criterion1.getValue();
            //从方案表中获取预热期
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id",caseId);
            StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectOne(wrapper);
            List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
            if (stCaseBaseInfoDto == null){
                return null;
            }else {
                Date start = stCaseBaseInfoDto.getPreHotTime();
                DateTime end = DateUtil.offsetHour(start, 2);
                StCaseBaseInfoEsu algParamRequestDto = new StCaseBaseInfoEsu();
                algParamRequestDto.setPreHotTime(start);
                algParamRequestDto.setForecastStartTime(stCaseBaseInfoDto.getForecastStartTime());
                algParamRequestDto.setPreSeeTime(stCaseBaseInfoDto.getPreSeeTime());
                algParamRequestDto.setId(caseId);
                if (type.equals("1")){
                    List<StForecastSectionEsr> waterRateInput = getWaterRateInput(stStbprpBEntities, algParamRequestDto);
                    if (CollUtil.isNotEmpty(waterRateInput)){
                        waterRateInput = waterRateInput.stream().sorted(Comparator.comparing(StForecastSectionEsr::getSectionName).thenComparing(StForecastSectionEsr::getDate)).collect(Collectors.toList());
                    }
                    byPaginator.setItems(waterRateInput);
                    System.out.println(1);
                }else {
                    List<StForecastSectionEsr> waterPositionInput = getWaterPositionInput(stStbprpBEntities, algParamRequestDto);
                    if (CollUtil.isNotEmpty(waterPositionInput)){
                        waterPositionInput = waterPositionInput.stream().sorted(Comparator.comparing(StForecastSectionEsr::getSectionName).thenComparing(StForecastSectionEsr::getDate)).collect(Collectors.toList());
                    }
                    byPaginator.setItems(waterPositionInput);
                }
            }
            return byPaginator;
        }else {
            return byPaginator;
        }
    }


    /**
     * 获取水位站的入参
     *
     * @param stStbprpBEntity
     * @param algParamRequestDto
     * @return
     */
    public  List<StForecastSectionEsr> getWaterPositionInput(List<StStbprpBEntity> stStbprpBEntity, StCaseBaseInfoEsu algParamRequestDto) {
        //水位站
        List<String> typeList = new ArrayList<>();
        typeList.add("ZQ");
        typeList.add("ZZ");
        List<StStbprpBEntity> stationWaterLevel = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().in(StStbprpBEntity::getSttp, typeList))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stationWaterLevel)) {
            stationWaterLevel = stationWaterLevel.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).filter(x -> StringUtil.isBlank(x.getSectionFlag())).collect(Collectors.toList());
        }
        //1.水位站入参数据
        Map<String, List<StWaterRateEntity>> levelInputMap = new HashMap<>();
        Map<String, BigDecimal> dtmlMap = stationWaterLevel.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getSeriaName, StStbprpBEntity::getDtmel, (o1, o2) -> o2));

        //新一版本的模型中 水位站数据 也有部分是流量站的数据\
        for (StStbprpBEntity stbprpBEntity : stationWaterLevel) {
            if (stbprpBEntity.getSttp().equals("ZZ")) {
                QueryWrapper convertQuery = new QueryWrapper();
                convertQuery.eq("stcd", stbprpBEntity.getStcd());
                StSnConvertEntity stSnConvertEntities = stSnConvertDao.selectOne(convertQuery);
                QueryWrapper waterLevel = new QueryWrapper();
                waterLevel.in("did", stSnConvertEntities.getSn());
                waterLevel.le("ctime", DateUtil.format(algParamRequestDto.getForecastStartTime(), "yyyy/MM/dd HH:mm:ss"));
                waterLevel.ge("ctime", DateUtil.format(algParamRequestDto.getPreHotTime(), "yyyy/MM/dd HH:mm:ss"));
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(waterLevel);
                List<StWaterRateEntity> levelList = new ArrayList<>();
                //水位站的水位 需要转换下单位
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    if (StrUtil.isEmpty(stWaterRateEntity.getAddrv())) {
                        continue;
                    }
                    stWaterRateEntity.setAddrv(new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).toString());
                    levelList.add(stWaterRateEntity);
                }
                if (CollUtil.isEmpty(levelList)){
                    continue;
                }
                for (StWaterRateEntity stWaterRateEntity : levelList) {
                    stWaterRateEntity.setJd(stbprpBEntity.getLgtd());
                    stWaterRateEntity.setWd(stbprpBEntity.getLttd());
                }
                levelInputMap.put(stbprpBEntity.getSeriaName(), levelList);
                System.out.println("s");
            } else {
                //流量站点 和 站点id 对应关系 站点id 去掉前两位才可以进行关联  //流量站 时间格式 yyyy-MM-dd HH:mm:ss

                QueryWrapper waterLevel = new QueryWrapper();
                waterLevel.eq("did", stbprpBEntity.getStcd().substring(2));
                waterLevel.le("ctime", DateUtil.format(algParamRequestDto.getForecastStartTime(), "yyyy-MM-dd HH:mm:ss"));
                waterLevel.ge("ctime", DateUtil.format(algParamRequestDto.getPreHotTime(), "yyyy-MM-dd HH:mm:ss"));
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(waterLevel);
                //将流量站数据 遍历转化为水位站的数据点位 事件也需要替换一下
                List<StWaterRateEntity> stWaterRateEntities1 = new ArrayList<>();
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    stWaterRateEntity.setAddrv(stWaterRateEntity.getMomentRiverPosition());
                    stWaterRateEntity.setCtime(stWaterRateEntity.getCtime().replace("-", "/"));
                    stWaterRateEntity.setJd(stbprpBEntity.getLgtd());
                    stWaterRateEntity.setWd(stbprpBEntity.getLttd());
                    List<StWaterRateEntity> list = levelInputMap.get(stbprpBEntity.getSeriaName());
                    if (CollUtil.isNotEmpty(list)){
                        stWaterRateEntities1.addAll(list);
                    }
                    stWaterRateEntities1.add(stWaterRateEntity);
                }
                if (CollUtil.isEmpty(stWaterRateEntities1)){
                    continue;
                }
                levelInputMap.put(stbprpBEntity.getSeriaName(), stWaterRateEntities1);
            }
        }
        List<StForecastSectionEsr> res = new ArrayList<>();
//        for (String s : levelInputMap.keySet()) {
//            List<StWaterRateEntity> stWaterRateEntities = levelInputMap.get(s);
//            for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
//                StForecastSectionEsr stForecastSectionEsr = new StForecastSectionEsr();
//                stForecastSectionEsr.setSectionName(s);
//                boolean contains = stWaterRateEntity.getCtime().contains(".0");
//
//                stWaterRateEntity.setCtime(stWaterRateEntity.getCtime().substring(0,19).replace("/","-"));
//
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                try {
//                    Date parse = format.parse(stWaterRateEntity.getCtime());
//                    stForecastSectionEsr.setDate(parse );
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//
//                stForecastSectionEsr.setValue(stWaterRateEntity.getMomentRate());
//                stForecastSectionEsr.setCaseId(algParamRequestDto.getId());
//                stForecastSectionEsr.setSttp("ZZ");
//                stForecastSectionEsr.setType("2");
//                res.add(stForecastSectionEsr);
//            }
//        }

        List<Date> timeSplit1 = DataUtils.getTimeSplit(algParamRequestDto.getForecastStartTime(), algParamRequestDto.getPreSeeTime(), 30, DateField.MINUTE);
        for (String seriaName : levelInputMap.keySet()) {
            String addrv = "";
            BigDecimal jd = new BigDecimal(0);
            BigDecimal wd = new BigDecimal(0);
            List<StWaterRateEntity> stWaterRateEntities = levelInputMap.get(seriaName);
            if (CollUtil.isNotEmpty(stWaterRateEntities)){
                BigDecimal bigDecimal = dtmlMap.get(seriaName);
                addrv = new BigDecimal(stWaterRateEntities.get(stWaterRateEntities.size()-1).getAddrv()).divide(new BigDecimal(1000),2,RoundingMode.HALF_UP).add(bigDecimal).toString();
                StWaterRateEntity stWaterRateEntity = stWaterRateEntities.get(0);
                jd = stWaterRateEntity.getJd();
                wd = stWaterRateEntity.getWd();
            }
            for (Date date : timeSplit1) {

                StForecastSectionEsr stForecastSectionEsr = new StForecastSectionEsr();
                stForecastSectionEsr.setDate(date);
                stForecastSectionEsr.setValue(addrv);
                stForecastSectionEsr.setSectionName(seriaName);
                stForecastSectionEsr.setJd(jd);
                stForecastSectionEsr.setWd(wd);
                stForecastSectionEsr.setCaseId(algParamRequestDto.getId());
                stForecastSectionEsr.setType("2");
                stForecastSectionEsr.setSttp("ZZ");
                res.add(stForecastSectionEsr);
            }
        }
        if (CollUtil.isNotEmpty(res)){
            res = res.stream().sorted(Comparator.comparing(StForecastSectionEsr::getSectionName).thenComparing(StForecastSectionEsr::getDate)).collect(Collectors.toList());
        }
        //同时给预报水位表 进行插入这些数据
        for (StForecastSectionEsr re : res) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("case_id",re.getCaseId());
            wrapper.eq("type",re.getType());
            wrapper.eq("seria_name",re.getSectionName());
            wrapper.eq("date",re.getDate());
            List<StForecastSectionDto> stForecastSectionDtos = stForecastSectionDao.selectList(wrapper);
            if (CollUtil.isNotEmpty(stForecastSectionDtos)){
                StForecastSectionDto stForecastSectionDto = new StForecastSectionDto();
                BeanUtil.copyProperties(re,stForecastSectionDto);
                stForecastSectionDto.setSeriaName(re.getSectionName());
                stForecastSectionDao.update(stForecastSectionDto,wrapper);
            }else {
                StForecastSectionDto stForecastSectionDto = new StForecastSectionDto();
                BeanUtil.copyProperties(re,stForecastSectionDto);
                stForecastSectionDto.setSeriaName(re.getSectionName());
                stForecastSectionDao.insert(stForecastSectionDto);
            }
        }

        return res;
    }

    /**
     * 获取流量站的入参
     *
     * @param stStbprpBEntity

     * @param algParamRequestDto
     * @return
     */
    public List<StForecastSectionEsr> getWaterRateInput(List<StStbprpBEntity> stStbprpBEntity, StCaseBaseInfoEsu algParamRequestDto) {
        //开始通过名称过滤获取场站id 然后取数
        //流量站
        List<StStbprpBEntity> stationWaterRate = stStbprpBEntity.parallelStream().filter(stStbprpBEntity1 -> {
            return StrUtil.isNotEmpty(stStbprpBEntity1.getSectionName());
        }).collect(Collectors.toList());
        stationWaterRate = stationWaterRate.parallelStream().filter(stStbprpBEntity1 -> {
            return      StrUtil.isNotEmpty(stStbprpBEntity1.getSectionFlag()) && stStbprpBEntity1.getSectionFlag().contains("入流");
        }).collect(Collectors.toList());

        Map<String, StStbprpBEntity> rateMap = stationWaterRate.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getSeriaName, Function.identity()));

        Map<String, List<StWaterRateEntity>> rateDataMap = new HashMap<>();
        //流量站入参数据
        Map<String, List<StWaterRateEntity>> rateInputMap = new HashMap<>();
        //实测

        if (CollUtil.isNotEmpty(stationWaterRate)) {
            //流量站点 和 站点id 对应关系 站点id 去掉前两位才可以进行关联  //流量站 时间格式 yyyy-MM-dd HH:mm:ss
            List<String> stcdList = stationWaterRate.parallelStream().map(stStbprpBEntity1 -> {
                return stStbprpBEntity1.getStcd().substring(2);
            }).collect(Collectors.toList());
            QueryWrapper waterLevel = new QueryWrapper();
            waterLevel.in("did", stcdList);
            waterLevel.le("ctime", DateUtil.format(algParamRequestDto.getForecastStartTime(), "yyyy-MM-dd HH:mm:ss"));
            waterLevel.ge("ctime", DateUtil.format(algParamRequestDto.getPreHotTime(), "yyyy-MM-dd HH:mm:ss"));
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(waterLevel);
            rateDataMap = stWaterRateEntities.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
        }

        for (String name : rateMap.keySet()) {
            StStbprpBEntity stStbprpBEntity1 = rateMap.get(name);
            List<StWaterRateEntity> stWaterRateEntities = rateDataMap.get(stStbprpBEntity1.getStcd().substring(2));
            if (CollUtil.isEmpty(stWaterRateEntities)){
               continue;
            }

            BigDecimal lgtd = stStbprpBEntity1.getLgtd();
            BigDecimal lttd = stStbprpBEntity1.getLttd();
            for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                stWaterRateEntity.setJd(lgtd);
                stWaterRateEntity.setWd(lttd);
            }
            rateInputMap.put(name, stWaterRateEntities);

        }
        //有个站点在数据库中没有 在代码中进行补充 亮马河入流        亮马河上游-0
        String momentRate = "0.5";
        List<Date> timeSplit = DataUtils.getTimeSplit(algParamRequestDto.getPreHotTime(), algParamRequestDto.getForecastStartTime(), 30, DateField.MINUTE);
        List<StWaterRateEntity> fixWaterRate = new ArrayList<>();
        for (Date date : timeSplit) {
            StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
            stWaterRateEntity.setMomentRate(momentRate);
            stWaterRateEntity.setCtime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            fixWaterRate.add(stWaterRateEntity);

        }
        rateInputMap.put("亮马河", fixWaterRate);
        List<StForecastSectionEsr> res = new ArrayList<>();
//        for (String s : rateInputMap.keySet()) {
//            List<StWaterRateEntity> stWaterRateEntities = rateInputMap.get(s);
//            for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
//                StForecastSectionEsr stForecastSectionEsr = new StForecastSectionEsr();
//                stForecastSectionEsr.setSectionName(s);
//                stForecastSectionEsr.setDate(DateUtil.parseDate(stWaterRateEntity.getCtime()) );
//                stForecastSectionEsr.setValue(stWaterRateEntity.getMomentRate());
//                stForecastSectionEsr.setCaseId(algParamRequestDto.getId());
//                stForecastSectionEsr.setSttp("ZQ");
//                stForecastSectionEsr.setType("1");
//                res.add(stForecastSectionEsr);
//            }
//        }

        List<Date> timeSplit1 = DataUtils.getTimeSplit(algParamRequestDto.getForecastStartTime(), algParamRequestDto.getPreSeeTime(), 30, DateField.MINUTE);
        for (Date date : timeSplit1) {
            for (String sectionName : rateInputMap.keySet()) {
                List<StWaterRateEntity> stWaterRateEntities = rateInputMap.get(sectionName);
                StForecastSectionEsr stForecastSectionEsr = new StForecastSectionEsr();
                stForecastSectionEsr.setCaseId(algParamRequestDto.getId());
                stForecastSectionEsr.setSectionName(sectionName);
                stForecastSectionEsr.setType("1");
                stForecastSectionEsr.setSttp("ZQ");
                stForecastSectionEsr.setDate(date);

                if (CollUtil.isNotEmpty(stWaterRateEntities) ){
                    StWaterRateEntity stWaterRateEntity = stWaterRateEntities.get(stWaterRateEntities.size() - 1);
                    stForecastSectionEsr.setValue( stWaterRateEntity.getMomentRate());
                    stForecastSectionEsr.setJd(stWaterRateEntity.getJd());
                    stForecastSectionEsr.setWd(stWaterRateEntity.getWd());
                }
                res.add(stForecastSectionEsr);
            }

        }
        if (CollUtil.isNotEmpty(res)){
            res = res.stream().sorted(Comparator.comparing(StForecastSectionEsr::getSectionName).thenComparing(StForecastSectionEsr::getDate)).collect(Collectors.toList());
        }
        //同时给预报水位表 进行插入这些数据
        for (StForecastSectionEsr re : res) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("case_id",re.getCaseId());
            wrapper.eq("type",re.getType());
            wrapper.eq("seria_name",re.getSectionName());
            wrapper.eq("date",re.getDate());
            List<StForecastSectionDto> stForecastSectionDtos = stForecastSectionDao.selectList(wrapper);
            if (CollUtil.isNotEmpty(stForecastSectionDtos)){
                StForecastSectionDto stForecastSectionDto = new StForecastSectionDto();
                BeanUtil.copyProperties(re,stForecastSectionDto);
                stForecastSectionDto.setSeriaName(re.getSectionName());
                stForecastSectionDao.update(stForecastSectionDto,wrapper);
            }else {
                StForecastSectionDto stForecastSectionDto = new StForecastSectionDto();
                BeanUtil.copyProperties(re,stForecastSectionDto);
                stForecastSectionDto.setSeriaName(re.getSectionName());
                stForecastSectionDao.insert(stForecastSectionDto);
            }
        }
        return res;
    }
}
