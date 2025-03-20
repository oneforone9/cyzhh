package com.essence.job.quick;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.dao.*;
import com.essence.dao.entity.StSectionModelDto;
import com.essence.dao.entity.StSnConvertEntity;
import com.essence.dao.entity.StStbprpBEntity;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.alg.StCaseResDto;
import com.essence.dao.entity.water.StQpModelDto;
import com.essence.dao.entity.water.StWaterRiskForecastDto;
import com.essence.interfaces.api.StCaseResService;
import com.essence.interfaces.api.StWaterGateService;
import com.essence.interfaces.api.SzyManageService;
import com.essence.interfaces.api.TransitWaterService;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.job.validate.CronJobIdentifierProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 针对慢查询, 采取存储redis缓存方案
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/7/17 16:24
 */
@Component
@Slf4j
@Order(2)
public class QuickViewHandler implements CommandLineRunner {

    /**
     * 注入redis
     */
    @Autowired
    RedisService redisService;

    /**
     * 要提速的方法
     */
    @Autowired
    TransitWaterService transitWaterService;
    @Autowired
    SzyManageService szyManageService;
    @Autowired
    StWaterGateService stWaterGateService;
    @Autowired
    StCaseResService stCaseResService;
    @Autowired
    StQpModelDao stQpModelDao;
    @Autowired
    StSnConvertDao stSnConvertDao;
    @Autowired
    StWaterRiskForecastDao stWaterRiskForecastDao;
    @Autowired
    StStbprpBDao stStbprpBDao;
    @Autowired
    StSectionModelDao stSectionModelDao;
    @Autowired
    GateStationRelatedDao gateStationRelatedDao;
    @Autowired
    StSideGateDao stSideGateDao;
    @Autowired
    StCaseResDao stCaseResDao;
    @Autowired
    StCaseBaseInfoDao stCaseBaseInfoDao;
    @Autowired
    StWaterRateDao stWaterRateDao;
    @Autowired
    StWaterEngineeringSchedulingDao stWaterEngineeringSchedulingDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 初始化
     *
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (DEV.equals(env)) {
            return;
        }
        // 启动时加载的查询
        log.info("启动时异步加载预热数据,开始...");
        // 使用异步任务分批加载数据
        CompletableFuture.runAsync(this::initWaterTransit);
        CompletableFuture.runAsync(this::initRiverWaterLevel);
        CompletableFuture.runAsync(this::initAllStQpModelList);
        CompletableFuture.runAsync(this::initAllStSectionModelList);
        CompletableFuture.runAsync(this::initAllStSnConvertList);
        CompletableFuture.runAsync(this::initAllStWaterRiskForecastList);
        CompletableFuture.runAsync(this::initStvprp);
        CompletableFuture.runAsync(this::initSelectRiverRiskPoint);
        CompletableFuture.runAsync(this::initAllGateStationRelated);
        CompletableFuture.runAsync(this::initAllStSideGateList);
        CompletableFuture.runAsync(this::initAllStCaseResList);
        CompletableFuture.runAsync(this::initRefSnId);
        CompletableFuture.runAsync(this::initAllEngineeringScheduling);
        CompletableFuture.runAsync(this::initSection);
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 1 * * ?")
    public void sync01() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initWaterTransit任务,结束.", BACK_JOB);
            return;
        }
        // 过境水量
        initWaterTransit();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 2 * * ?")
    public void sync02() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initRiverWaterLevel任务,结束.", BACK_JOB);
            return;
        }
        // 全区河道水位态势预热 所有状态
        initRiverWaterLevel();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 3 * * ?")
    public void sync03() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStQpModelList任务,结束.", BACK_JOB);
            return;
        }
        // 水系联通-预报水位-河段断面关联查询
        initAllStQpModelList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 4 * * ?")
    public void sync04() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStSectionModelList任务,结束.", BACK_JOB);
            return;
        }
        // 模型断面基础表(StSectionModel)表
        initAllStSectionModelList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 19 * * ?")
    public void sync05() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStSnConvertList任务,结束.", BACK_JOB);
            return;
        }
        // 所有水位站编码转换Sn
        initAllStSnConvertList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 18 * * ?")
    public void sync06() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStWaterRiskForecastList任务,结束.", BACK_JOB);
            return;
        }
        // 水系联调-模型风险预报 (st_water_risk_forecast)表查询
        initAllStWaterRiskForecastList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 9 * * ?")
    public void sync07() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initStvprp任务,结束.", BACK_JOB);
            return;
        }
        // 所有水位流量站
        initStvprp();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 10 * * ?")
    public void sync08() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initSelectRiverRiskPoint任务,结束.", BACK_JOB);
            return;
        }
        // 河道风险点
        initSelectRiverRiskPoint();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 40 10 * * ?")
    public void sync09() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllGateStationRelated任务,结束.", BACK_JOB);
            return;
        }
        // 闸坝对应流量水位站关联表(GateStationRelated)
        initAllGateStationRelated();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void sync10() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStSideGateList任务,结束.", BACK_JOB);
            return;
        }
        // 所有闸坝
        initAllStSideGateList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 10 12 * * ?")
    public void sync11() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllStCaseResList任务,结束.", BACK_JOB);
            return;
        }
        // 所有方案执行结果
        initAllStCaseResList();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0 13 * * ?")
    public void sync12() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initRefSnId任务,结束.", BACK_JOB);
            return;
        }
        // 所有水位流量站点当前时刻数据
        initRefSnId();
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0 14 * * ?")
    public void sync13() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initAllEngineeringScheduling任务,结束.", BACK_JOB);
            return;
        }
        // 水系联调-工程调度
        initAllEngineeringScheduling();
    }

    @Scheduled(cron = "0 0 15 * * ?")
    public void sync14() throws ParseException {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initSection任务,结束.", BACK_JOB);
            return;
        }
        // 所有河道断面数据
        log.info("河道断面数据加载开始");
        initSection();
        log.info("河道断面数据加载结束");
    }

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0 16 * * ?")
    public void sync15() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行initWaterTransfer任务,结束.", BACK_JOB);
            return;
        }
        // 全区调水态势
        initWaterTransfer();
    }

    /**
     * 过境水量
     */
    private void initWaterTransit() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date todayZero = DateUtil.beginOfDay(now);
        StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
        stStbprpBEntityDTO.setStartTime(timeFormat.format(todayZero));
        stStbprpBEntityDTO.setEndTime(timeFormat.format(now));
        redisService.setCacheObject("WaterTransit", transitWaterService.getWaterTransit(stStbprpBEntityDTO));
        log.info("过境水量预热成功!");
    }

    /**
     * 全区河道水位态势
     */
    private void initRiverWaterLevel() {
        int i = 0;
        for (; i < 5; i++) {
            redisService.setCacheObject("szyManageService.getWaterLevel@" + i, szyManageService.getWaterLevel(i));
        }
        log.info("全区河道水位态势热成功!");
    }

    /**
     * 水系联通-预报水位-河段断面关联查询
     */
    private void initAllStQpModelList() {
        redisService.setCacheObject("AllStQpModelList", stQpModelDao.selectList(new QueryWrapper<StQpModelDto>().orderByAsc("qp_id")));
        int riverId = 0;
        for (; riverId < 32; riverId++) {
            redisService.setCacheObject("AllStQpModelList" + riverId, stQpModelDao.selectList(new QueryWrapper<StQpModelDto>().eq("river_id", riverId).orderByAsc("qp_id")));
        }
        log.info("水系联通-预报水位-河段断面关联查询预热成功!");
    }

    /**
     * 模型断面基础表(StSectionModel)表
     */
    private void initAllStSectionModelList() {
        redisService.setCacheObject("AllStSectionModelList", stSectionModelDao.selectList(new QueryWrapper<>()));
        log.info("模型断面基础表(StSectionModel)表预热成功!");
    }

    /**
     * 所有水位站编码转换Sn
     */
    private void initAllStSnConvertList() {
        redisService.setCacheObject("AllStSnConvertList", stSnConvertDao.selectList(new QueryWrapper<>()));
        log.info("所有水位站编码转换Sn预热成功!");
    }

    /**
     * 水系联调-模型风险预报 (st_water_risk_forecast)表查询
     */
    private void initAllStWaterRiskForecastList() {
        redisService.setCacheObject("AllStWaterRiskForecastList", stWaterRiskForecastDao.selectList(new QueryWrapper<>()));
        log.info("水系联调-模型风险预报 (st_water_risk_forecast)表查询预热成功!");
    }

    /**
     * 所有水位流量站
     */
    private void initStvprp() {
        redisService.setCacheObject("stvprp", stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().in("sttp", "ZQ", "ZZ")));
        log.info("所有水位流量站预热成功!");
    }

    /**
     * 河道风险点
     */
    private void initSelectRiverRiskPoint() {
        List<StSectionModelDto> allStSectionModelList = redisService.getCacheObject("AllStSectionModelList");
        if (allStSectionModelList == null){
            allStSectionModelList  = stSectionModelDao.selectList(new QueryWrapper<>());
        }
        List<StSnConvertEntity> allStSnConvertList = redisService.getCacheObject("AllStSnConvertList");
        if (allStSnConvertList == null){
            allStSnConvertList =  stSnConvertDao.selectList(new QueryWrapper<>());
        }
        List<StWaterRiskForecastDto> allStWaterRiskForecastList = redisService.getCacheObject("AllStWaterRiskForecastList");
        if (allStWaterRiskForecastList == null){
            allStWaterRiskForecastList = stWaterRiskForecastDao.selectList(new QueryWrapper<>());
        }

        Map<String, List<StSnConvertEntity>> stSnMap = allStSnConvertList.stream().collect(Collectors.groupingBy(StSnConvertEntity::getStcd));
        Map<String, List<StSectionModelDto>> stSectionModelDtoMap = allStSectionModelList.stream().collect(Collectors.groupingBy(StSectionModelDto::getSectionName));
        Map<String, List<StWaterRiskForecastDto>> stWaterRiskForecastMap = allStWaterRiskForecastList.stream().collect(Collectors.groupingBy(StWaterRiskForecastDto::getStcd));

        List<StStbprpBEntity> stStbprpBEntities =  redisService.getCacheObject("stvprp") ;
        if (stStbprpBEntities == null){
            stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().in("sttp", "ZQ", "ZZ"));
        }

        redisService.setCacheObject("selectRiverRiskPoint", stWaterGateService.getRiverRiskPoints(stSnMap, stSectionModelDtoMap, stStbprpBEntities, stWaterRiskForecastMap));
        log.info("河道风险点预热成功!");
    }

    /**
     * 闸坝对应流量水位站关联表(GateStationRelated)
     */
    private void initAllGateStationRelated() {
        redisService.setCacheObject("AllGateStationRelated", gateStationRelatedDao.selectList(new QueryWrapper<>()));
        log.info("闸坝对应流量水位站关联表(GateStationRelated)预热成功!");
    }

    /**
     * 所有闸坝
     */
    private void initAllStSideGateList() {
        redisService.setCacheObject("AllStSideGateList", stSideGateDao.selectList(new QueryWrapper<>()));
        log.info("所有闸坝预热成功!");
    }

    /**
     * 所有方案执行结果
     */
    private void initAllStCaseResList() {
        List<StCaseBaseInfoDto> stCaseBaseInfoDtos = stCaseBaseInfoDao.selectList(new QueryWrapper<>());
        for (StCaseBaseInfoDto stCaseBaseInfoDto : stCaseBaseInfoDtos) {
            String caseId = stCaseBaseInfoDto.getId();
            List<StCaseResDto> list = stCaseResDao.selectList(new QueryWrapper<StCaseResDto>().select("rv_id", "section_name", "river_z", "step").eq("case_id", caseId));
            redisService.setCacheObject("AllStCaseResList" + caseId, list);
            int riverId = 0;
            for (; riverId < 32; riverId++) {
                List<StCaseResDto> stCaseResDtos = stCaseResDao.selectList(new QueryWrapper<StCaseResDto>()
                        .select("rv_id", "section_name", "river_z", "step")
                        .eq("case_id", caseId)
                        .eq("rv_id", riverId));
                redisService.setCacheObject("AllStCaseResList" + caseId + riverId, stCaseResDtos);
            }
        }
        log.info("所有方案执行结果预热成功!");
    }

    /**
     * 所有水位流量站点当前时刻数据
     */
    private void initRefSnId() {
        Date now = new Date();
        String refSnId;
        List<StSnConvertEntity> allStSnConvertList = redisService.getCacheObject("AllStSnConvertList");
        if (allStSnConvertList == null){
            allStSnConvertList = stSnConvertDao.selectList(new QueryWrapper<>());
        }

        Map<String, List<StSnConvertEntity>> stSnMap = allStSnConvertList.stream().collect(Collectors.groupingBy(StSnConvertEntity::getStcd));
        List<StStbprpBEntity> stStbprpBEntities =  redisService.getCacheObject("stvprp") ;
        if (stStbprpBEntities == null){
            stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().in("sttp", "ZQ", "ZZ"));
        }
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            String sttp = stStbprpBEntity.getSttp();
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
            redisService.setCacheObject("refSnId" + refSnId, stWaterRateDao.selectByOneByDid(refSnId, now));
        }
        log.info("所有水位流量站点当前时刻数据预热成功!");
    }

    /**
     * 水系联调-工程调度
     */
    private void initAllEngineeringScheduling() {
        for (int type = 1; type <= 3; type++) {
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            redisService.setCacheObject("allEngineeringScheduling" + type, stWaterEngineeringSchedulingDao.selectList(queryWrapper));
        }
        log.info("水系联调-工程调度预热成功!");
    }

    /**
     * 所有河道断面数据
     */
    private void initSection() {
        List<StCaseBaseInfoDto> stCaseBaseInfoDtos = stCaseBaseInfoDao.selectList(new QueryWrapper<>());
        for (StCaseBaseInfoDto stCaseBaseInfoDto : stCaseBaseInfoDtos) {
            String caseId = stCaseBaseInfoDto.getId();
            log.info(caseId + "caseId");
            List<StCaseResDto> cacheObject = redisService.getCacheObject("AllStCaseResList" + caseId);
            List<StCaseResDto> stCaseResDtos = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(cacheObject)) {
                stCaseResDtos = cacheObject;
            } else {
                QueryWrapper<StCaseResDto> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("case_id", caseId);
                stCaseResDtos = stCaseResDao.selectList(queryWrapper);
            }
            List<String> rvIds = stCaseResDtos.stream().map(StCaseResDto::getRvId).collect(Collectors.toList());
            List<String> rvIdsUnique = new ArrayList<>(new HashSet<>(rvIds));
            // 加载全部断面
            for (String riverId : rvIdsUnique) {
                if (null != riverId) {
                    Object cacheObject1 = redisService.getCacheObject("RiverSection@" + caseId + "-" + riverId);
                    if (null == cacheObject1) {
                        log.info(riverId + "riverId");
                        StCaseBaseInfoEsu stCaseBaseInfoEsu = new StCaseBaseInfoEsu();
                        stCaseBaseInfoEsu.setId(caseId);
                        stCaseBaseInfoEsu.setRiverId(riverId);
                        redisService.setCacheObject("RiverSection@" + caseId + "-" + riverId, stCaseResService.getRiverSection(stCaseBaseInfoEsu));
                    }
                }
            }
        }
        log.info("所有河道断面数据预热成功!");
    }

    /**
     * 全区调水态势
     */
    private void initWaterTransfer() {
        redisService.setCacheObject("WaterTransfer", szyManageService.getWaterTransfer());
        log.info("全区调水态势预热成功!");
    }
}
