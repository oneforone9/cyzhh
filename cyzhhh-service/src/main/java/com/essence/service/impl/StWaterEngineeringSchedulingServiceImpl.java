package com.essence.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.common.constant.OnlineStatusConstant;

import com.essence.common.utils.UuidUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.api.StWaterEngineeringSchedulingDataService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StWaterEngineeringSchedulingEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterFileBaseTtoR;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingCodeEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 水系联调-工程调度 (st_water_engineering_scheduling)表数据库业务层
 *
 * @author BINX
 * @since 2023年5月13日 下午3:50:06
 */
@Service
public class StWaterEngineeringSchedulingServiceImpl extends BaseApiImpl<StWaterEngineeringSchedulingEsu, StWaterEngineeringSchedulingEsp, StWaterEngineeringSchedulingEsr, StWaterEngineeringSchedulingDto> implements StWaterEngineeringSchedulingService {

    @Autowired
    private StWaterEngineeringSchedulingDao stWaterEngineeringSchedulingDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private StSnConvertDao stSnConvertDao;
    @Autowired
    private StWaterRateDao stWaterRateDao;
    @Autowired
    private ConverterStWaterEngineeringSchedulingEtoT converterStWaterEngineeringSchedulingEtoT;
    @Autowired
    private ConverterStWaterEngineeringSchedulingTtoR converterStWaterEngineeringSchedulingTtoR;
    @Autowired
    private StWaterEngineeringSchedulingDataService stWaterEngineeringSchedulingDataService;
    @Autowired
    RedisService redisService;
    @Autowired
    private ConverterFileBaseTtoR converterFileBaseTtoR;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private FileBaseDao fileBaseDao;
    @Resource
    private GateStationRelatedDao gateStationRelatedDao;
    @Resource
    private StSideGateDao stSideGateDao;
    @Resource
    private StCaseBaseInfoDao caseBaseInfoDao;
    @Autowired
    private ConverterStWaterEngineeringSchedulingCodeEtoT converterStWaterEngineeringSchedulingCodeEtoT;
    @Autowired
    private StWaterEngineeringSchedulingCodeDao stWaterEngineeringSchedulingCodeDao;

    public StWaterEngineeringSchedulingServiceImpl(StWaterEngineeringSchedulingDao stWaterEngineeringSchedulingDao, ConverterStWaterEngineeringSchedulingEtoT converterStWaterEngineeringSchedulingEtoT, ConverterStWaterEngineeringSchedulingTtoR converterStWaterEngineeringSchedulingTtoR) {
        super(stWaterEngineeringSchedulingDao, converterStWaterEngineeringSchedulingEtoT, converterStWaterEngineeringSchedulingTtoR);
    }

    @Override
    public List<StWaterEngineeringSchedulingDto> selectEngineeringScheduling(String caseId) {
        //查询方案 判断是 蓝色 黄色 橙色 预警
        StCaseBaseInfoDto stCaseBaseInfoDto = caseBaseInfoDao.selectById(caseId);
        Integer type = 1;
        if (stCaseBaseInfoDto != null) {
            String rainTotal = stCaseBaseInfoDto.getRainTotal();
            if (StrUtil.isEmpty(rainTotal)){
                rainTotal = "0";
            }
            if (0 <= Double.valueOf(rainTotal) && Double.valueOf(rainTotal) <= 50) {
                type = 1;
            }
            if (50 <= Double.valueOf(rainTotal) && Double.valueOf(rainTotal) <= 70) {
                type = 2;
            }
            if (70 <= Double.valueOf(rainTotal) && Double.valueOf(rainTotal) <= 100) {
                type = 3;
            }
        }

        List<StSnConvertEntity> allStSnConvertList = null != redisService.getCacheObject("AllStSnConvertList") ? redisService.getCacheObject("AllStSnConvertList") : stSnConvertDao.selectList(new QueryWrapper<>());
        Map<String, List<StSnConvertEntity>> stSnMap = allStSnConvertList.stream().collect(Collectors.groupingBy(StSnConvertEntity::getStcd));
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        List<StWaterEngineeringSchedulingDto> allEngineeringScheduling = null != redisService.getCacheObject("allEngineeringScheduling" + type) ? redisService.getCacheObject("allEngineeringScheduling" + type) : stWaterEngineeringSchedulingDao.selectList(queryWrapper);
        allEngineeringScheduling.forEach(engineeringSchedulingDto -> {
            //通过名称找水闸或者水坝的对应关系
            List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(new QueryWrapper<>());
            Map<String, List<GateStationRelatedDto>> collect = gateStationRelatedDtos.parallelStream().collect(Collectors.groupingBy(GateStationRelatedDto::getGateName));
            List<GateStationRelatedDto> gateStationRelatedDto = collect.get(engineeringSchedulingDto.getZbmc());
            if (gateStationRelatedDto != null) {
                for (GateStationRelatedDto stationRelatedDto : gateStationRelatedDto) {
                    List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().eq("stnm", stationRelatedDto.getStnm()));
                    if (stStbprpBEntities != null && stStbprpBEntities.size() > 0) {
                        StStbprpBEntity stStbprpBEntity = stStbprpBEntities.get(0);
                        BigDecimal dtmel = stStbprpBEntity.getDtmel();
                        engineeringSchedulingDto.setLgtd(stStbprpBEntity.getLgtd());
                        engineeringSchedulingDto.setLttd(stStbprpBEntity.getLttd());
                        engineeringSchedulingDto.setStnm(stStbprpBEntity.getStnm());
                        engineeringSchedulingDto.setStcd(stStbprpBEntity.getStcd());
                        String sttp = stStbprpBEntity.getSttp();
                        System.out.println(sttp);
                        String refSnId;
                        if ("ZZ".equals(sttp) && !CollectionUtils.isEmpty(stSnMap.get(stStbprpBEntity.getStcd()))) {
                            refSnId = stSnMap.get(stStbprpBEntity.getStcd()).get(0).getSn();
                        } else {
                            if (stStbprpBEntity.getStcd().length() > 2)
                                refSnId = stStbprpBEntity.getStcd().substring(2);
                            else {
                                refSnId = "";
                            }
                        }
                        if ("".equals(refSnId)) {
                            return;
                        }
                        Date now = new Date();
//                    now.setDate(0);
                        now.setHours(0);
                        List<StWaterRateEntity> stWaterRateEntities = null != redisService.getCacheObject("refSnId" + refSnId) ? redisService.getCacheObject("refSnId" + refSnId) : stWaterRateDao.selectByOneByDid(refSnId, now);
                        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
                            BigDecimal momentRiverPosition = new BigDecimal(0);
                            StWaterRateEntity stWaterGateDto = stWaterRateEntities.get(0);
                            if ("ZZ".equals(sttp)) {
                                String addrv = stWaterGateDto.getAddrv();
                                if (StringUtils.hasText(addrv)) {
                                    BigDecimal bigDecimal = new BigDecimal(addrv);
                                    momentRiverPosition = bigDecimal.divide(new BigDecimal("1000")).setScale(2, BigDecimal.ROUND_HALF_UP);
                                }
                            } else {
                                if (StringUtils.hasText(stWaterGateDto.getMomentRiverPosition()))
                                    momentRiverPosition = new BigDecimal(stWaterGateDto.getMomentRiverPosition()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            momentRiverPosition = momentRiverPosition.add(dtmel == null ? new BigDecimal(0) : dtmel);
                            engineeringSchedulingDto.setMomentRiverPosition(momentRiverPosition);
                        }


                    } else {
                        continue;
                    }
                }

            }
        });

        return allEngineeringScheduling;
    }

    @Override
    public void selectFloodDispatch(StFloodDispatch stFloodDispatch) {
        StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto = stWaterEngineeringSchedulingCodeDao.selectById(stFloodDispatch.getId());

        if (org.apache.commons.lang3.StringUtils.isNotBlank(stFloodDispatch.getType())) {
            //越级---只有接收
            //修改状态（接收-执行）
            stWaterEngineeringSchedulingCodeDto.setSn(stWaterEngineeringSchedulingCodeDto.getSn() + 1);
            stWaterEngineeringSchedulingCodeDto.setState(OnlineStatusConstant.DDSTATEZX);
            stWaterEngineeringSchedulingCodeDto.setReceptionTime(new Date());
            stWaterEngineeringSchedulingCodeDao.updateById(stWaterEngineeringSchedulingCodeDto);
            //添加记录信息
            stWaterEngineeringSchedulingDataService.insertFloodDispatchReceive(stWaterEngineeringSchedulingCodeDto);
        } else {
            stWaterEngineeringSchedulingCodeDto.setSn(stWaterEngineeringSchedulingCodeDto.getSn() + 1);
            stWaterEngineeringSchedulingCodeDto.setState(OnlineStatusConstant.DDSTATEFZR);
            stWaterEngineeringSchedulingCodeDto.setReceptionTime(new Date());
            stWaterEngineeringSchedulingCodeDto.setOrderTimeR(new Date());
            stWaterEngineeringSchedulingCodeDao.updateById(stWaterEngineeringSchedulingCodeDto);
            //添加接收下发记录
            stWaterEngineeringSchedulingDataService.insertFloodDispatch(stWaterEngineeringSchedulingCodeDto);
        }
    }

    @Override
    public void selectFloodDispatchs(StFloodDispatch stFloodDispatch) {
        StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto = stWaterEngineeringSchedulingDao.selectById(stFloodDispatch.getId());
        stWaterEngineeringSchedulingDto.setSn(stWaterEngineeringSchedulingDto.getSn() + 1);
        if (stFloodDispatch.getRank().equals("0")) {
            stWaterEngineeringSchedulingDto.setState(OnlineStatusConstant.DDSTATEHDS);
        } else {
            stWaterEngineeringSchedulingDto.setState(OnlineStatusConstant.DDSTATEFZR);
        }
        stWaterEngineeringSchedulingDto.setOrderTime(new Date());
        stWaterEngineeringSchedulingDto.setRank(stFloodDispatch.getRank());
        stWaterEngineeringSchedulingDto.setSchedulingCodeNew(stFloodDispatch.getSchedulingCodeNew());
        stWaterEngineeringSchedulingDao.updateById(stWaterEngineeringSchedulingDto);
        //添加记录信息
        stWaterEngineeringSchedulingDataService.insertFloodDispatchX(stWaterEngineeringSchedulingDto);
    }

    @Override
    public StWaterEngineeringSchedulingEsr selectFloodDispatchById(String id) {
        StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto = stWaterEngineeringSchedulingDao.selectById(id);
        StWaterEngineeringSchedulingEsr stWaterEngineeringSchedulingEsr = new StWaterEngineeringSchedulingEsr();
        if (null != stWaterEngineeringSchedulingDto) {
            stWaterEngineeringSchedulingEsr = converterStWaterEngineeringSchedulingTtoR.toBean(stWaterEngineeringSchedulingDto);
            List<FileBase> fileBases = Optional.ofNullable(fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().eq(FileBase::getTypeId, "FXDD" + stWaterEngineeringSchedulingEsr.getId()).eq(FileBase::getIsDelete, "0"))).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(fileBases)) {
                List<FileBaseEsr> fileBaseEsrs = Optional.ofNullable(converterFileBaseTtoR.toList(fileBases)).orElse(Lists.newArrayList());
                stWaterEngineeringSchedulingEsr.setFile(fileBaseEsrs);
            }
        }
        return stWaterEngineeringSchedulingEsr;
    }

    @Override
    public StWaterEngineeringSchedulingCodeEsr updateFloodDispatch(StWaterEngineeringSchedulingCodeEsu stWaterEngineeringSchedulingCodeEsu) {
        StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto = converterStWaterEngineeringSchedulingCodeEtoT.toBean(stWaterEngineeringSchedulingCodeEsu);
        stWaterEngineeringSchedulingDao.updateStWaterEngineeringScheduling(stWaterEngineeringSchedulingCodeEsu.getStId());
        //修改状态（执行-完成）
        stWaterEngineeringSchedulingCodeDto.setSn(stWaterEngineeringSchedulingCodeDto.getSn() + 1);
        stWaterEngineeringSchedulingCodeDto.setState(OnlineStatusConstant.DDSTATEWC);
        stWaterEngineeringSchedulingCodeDto.setFinishTime(new Date());
        stWaterEngineeringSchedulingCodeDto.setSn(0);
        stWaterEngineeringSchedulingCodeDao.updateById(stWaterEngineeringSchedulingCodeDto);
        StWaterEngineeringSchedulingCodeEsr stWaterEngineeringSchedulingCodeEsr = new StWaterEngineeringSchedulingCodeEsr();
        BeanUtils.copyProperties(stWaterEngineeringSchedulingCodeDto, stWaterEngineeringSchedulingCodeEsr);
        //添加记录信息
        stWaterEngineeringSchedulingDataService.insertFloodDispatchs(stWaterEngineeringSchedulingCodeDto);
        if (!CollectionUtils.isEmpty(stWaterEngineeringSchedulingCodeEsu.getFile())) {
            taskExecutor.execute(() -> {
                saveFile(stWaterEngineeringSchedulingCodeEsu);
            });
        }
        return stWaterEngineeringSchedulingCodeEsr;
    }

    public void saveFile(StWaterEngineeringSchedulingCodeEsu stWaterEngineeringSchedulingCodeEsu) {
        List<String> fileId = stWaterEngineeringSchedulingCodeEsu.getFile();
        List<FileBase> fileBases = fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().in(FileBase::getId, fileId));
        if (!CollectionUtils.isEmpty(fileBases)) {
            for (FileBase fileBase : fileBases) {
                fileBase.setTypeId("FXDD" + stWaterEngineeringSchedulingCodeEsu.getId());
                fileBaseDao.updateById(fileBase);
            }
        }
    }

    @Override
    public List<StWaterEngineeringSchedulingQuery> getRiverList() {
        List<StWaterEngineeringSchedulingQuery> stWaterEngineeringSchedulingQueries = new ArrayList<>();
        List<StWaterEngineeringSchedulingDto> riverList = stWaterEngineeringSchedulingDao.getRiverList();
        if (!CollectionUtils.isEmpty(riverList)) {
            for (StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto : riverList) {
                StWaterEngineeringSchedulingQuery stWaterEngineeringSchedulingQuery = new StWaterEngineeringSchedulingQuery();
                stWaterEngineeringSchedulingQuery.setStcd(stWaterEngineeringSchedulingDto.getRiverId());
                stWaterEngineeringSchedulingQuery.setStnm(stWaterEngineeringSchedulingDto.getRvnm());
                stWaterEngineeringSchedulingQueries.add(stWaterEngineeringSchedulingQuery);
            }
        }
        return stWaterEngineeringSchedulingQueries;
    }

    @Override
    public List<StWaterEngineeringSchedulingQuery> getStcdList() {
        List<StWaterEngineeringSchedulingQuery> stWaterEngineeringSchedulingQueries = new ArrayList<>();
        List<StWaterEngineeringSchedulingDto> stcdList = stWaterEngineeringSchedulingDao.getStcdList();
        if (!CollectionUtils.isEmpty(stcdList)) {
            for (StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto : stcdList) {
                StWaterEngineeringSchedulingQuery stWaterEngineeringSchedulingQuery = new StWaterEngineeringSchedulingQuery();
                stWaterEngineeringSchedulingQuery.setStcd(stWaterEngineeringSchedulingDto.getStcd());
                stWaterEngineeringSchedulingQuery.setStnm(stWaterEngineeringSchedulingDto.getStnm());
                stWaterEngineeringSchedulingQueries.add(stWaterEngineeringSchedulingQuery);
            }
        }
        return stWaterEngineeringSchedulingQueries;
    }

    @Autowired
    private RainDateDtoDao rainDateDtoDao;

    @Override
    public void getStcdLists() {
        //查询雨量站
        try {
            QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("sttp", "PP");
            String startTime = "2023-05-16 00:01:00";
            String endTime = "2023-05-17 11:00:00";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<DateTime> dateTimes = DateUtil.rangeToList(formatter.parse(startTime), formatter.parse(endTime), DateField.MINUTE);

            List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stStbprpBEntities)) {
                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                    RainDateDto rainDateDto = new RainDateDto();
                    for (DateTime dateTime : dateTimes) {
                        rainDateDto.setDate(dateTime);
                        rainDateDto.setHhRain("0");
                        rainDateDto.setId(UuidUtil.get32UUIDStr());

                        rainDateDto.setStationID(stStbprpBEntity.getStcd());
                        rainDateDtoDao.insert(rainDateDto);
                    }
                }
            }
            System.out.println(dateTimes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStcdListss(String stcd) {

        String startTime = "2023/05/26 00:00:00";
        String endTime = "2023/05/26 23:59:59";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<StWaterRateEntity> stWaterRateEntityList = stWaterRateDao.selectList(new QueryWrapper<StWaterRateEntity>().lambda().ge(StWaterRateEntity::getCtime, startTime).le(StWaterRateEntity::getCtime, endTime).eq(StWaterRateEntity::getDid, "FG6080221006"));
        if (!CollectionUtils.isEmpty(stWaterRateEntityList)) {
            for (StWaterRateEntity stWaterRateEntity : stWaterRateEntityList) {
                StWaterRateEntity stWaterRateEntitys = new StWaterRateEntity();
                BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntitys);
                stWaterRateEntitys.setCtime(stWaterRateEntitys.getCtime().replace("/26", "/30"));
                stWaterRateEntitys.setDid(stcd);
                stWaterRateEntitys.setId(UuidUtil.get32UUIDStr());
                stWaterRateDao.insertStWaterRate(stWaterRateEntitys);
            }
        }
        System.out.println(stWaterRateEntityList.size());
    }

  /*  @Override
    public Paginator<StWaterEngineeringSchedulingEsr> findByPaginator(PaginatorParam param) {
        Paginator<StWaterEngineeringSchedulingEsr> byPaginator = super.findByPaginator(param);
        //给调令中的站点补充单位 有水坝 水闸
        if (byPaginator !=null ){
            List<StWaterEngineeringSchedulingEsr> items = byPaginator.getItems();
            if (CollUtil.isNotEmpty(items)){
                List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(new QueryWrapper<>());
                Map<Integer, StSideGateDto> gateMap = stSideGateDtos.parallelStream().collect(Collectors.toMap(StSideGateDto::getId, Function.identity(), (o1, o2) -> o2));
                for (StWaterEngineeringSchedulingEsr item : items) {
                    StSideGateDto stSideGateDto = gateMap.get(Integer.valueOf(item.getGateId()) );
                    item.setUnitId(stSideGateDto.getUnitId());
                }
            }
        }
        return byPaginator;
    }*/

}
