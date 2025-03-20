package com.essence.service.impl.alg;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.cache.service.RedisService;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.dao.entity.alg.StCaseProcessDto;
import com.essence.dao.entity.alg.StCaseResDto;
import com.essence.dao.entity.alg.StCaseResParamDto;
import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StCaseBaseInfoService;
import com.essence.interfaces.api.StCaseResService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingService;
import com.essence.interfaces.model.StCaseBaseInfoEsr;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.model.StCaseResRainList;
import com.essence.interfaces.param.StCaseBaseInfoEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaseBaseInfoEtoT;
import com.essence.service.converter.ConverterStCaseBaseInfoTtoR;
import com.essence.service.impl.alg.util.ConnToPihe1dService_c;
import com.essence.service.impl.alg.util.ConnToPihe1dService_cImpl;
import com.essence.service.utils.DataUtils;
import com.google.common.collect.Lists;
import com.sun.jna.ptr.DoubleByReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 防汛调度-方案基础表(StCaseBaseInfo)业务层
 *
 * @author BINX
 * @since 2023-04-17 16:29:53
 */
@Service
@Slf4j
public class StCaseBaseInfoServiceImpl extends BaseApiImpl<StCaseBaseInfoEsu, StCaseBaseInfoEsp, StCaseBaseInfoEsr, StCaseBaseInfoDto> implements StCaseBaseInfoService {
    /**
     * 编码补充
     */
    public static final String CODE_FIX ="\0";

    @Autowired
    StCaseResService stCaseResService;
    @Resource
    RedisService redisService;
    @Autowired
    private StCaseBaseInfoDao stCaseBaseInfoDao;
    @Autowired
    private StCaseProcessDao stCaseProcessDao;

    @Autowired
    private ConverterStCaseBaseInfoEtoT converterStCaseBaseInfoEtoT;
    @Autowired
    private ConverterStCaseBaseInfoTtoR converterStCaseBaseInfoTtoR;
    @Resource
    private StStbprpBDao stStbprpBDao;
    @Resource
    private StCaseProgressDao stCaseProgressDao;
    @Autowired
    private StCaseResDao stCaseResDao;
    @Resource
    private StSectionModelDao stSectionModelDao;
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private StForecastSectionDao forecastSectionDao;
    @Autowired
    private StDesigRainPatternDao stDesigRainPatternDao;
    @Resource
    private StCaiyunPrecipitationHistoryDao stCaiyunPrecipitationHistoryDao;
    @Resource
    private StSideGateDao stSideGateDao;
    @Resource
    private StCaiyunMeshDao stCaiyunMeshDao;
    /**
     * 雨量站
     */
    @Resource
    private RainDateDtoDao rainDateDtoDao;
    /**
     * 流量站 水位站
     */
    @Resource
    private StWaterRateDao stWaterRateDao;
    @Resource
    private StSnConvertDao stSnConvertDao;
    @Resource
    private StCaseResParamDao stCaseResParamDao;
    @Resource
    private StForeseeProjectDao stForeseeProjectDao;

    /**
     * 要提速的方法
     */
    @Autowired
    private StWaterEngineeringSchedulingService stWaterEngineeringSchedulingService;

    public StCaseBaseInfoServiceImpl(StCaseBaseInfoDao stCaseBaseInfoDao, ConverterStCaseBaseInfoEtoT converterStCaseBaseInfoEtoT, ConverterStCaseBaseInfoTtoR converterStCaseBaseInfoTtoR) {
        super(stCaseBaseInfoDao, converterStCaseBaseInfoEtoT, converterStCaseBaseInfoTtoR);
    }

    @Override
    @Transactional
    public String insertCase(StCaseBaseInfoEsu e) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("case_name",e.getCaseName());
        StCaseBaseInfoDto stCaseBaseInfoDtos = stCaseBaseInfoDao.selectOne(wrapper);
        if (stCaseBaseInfoDtos != null && StrUtil.isNotEmpty(stCaseBaseInfoDtos.getId()) ){
            StCaseBaseInfoDto baseInfoDto = new StCaseBaseInfoDto();
            BeanUtil.copyProperties(e,baseInfoDto);
            int i = stCaseBaseInfoDao.updateById(baseInfoDto);
            QueryWrapper delWrapper = new QueryWrapper();
            delWrapper.eq("case_id",stCaseBaseInfoDtos.getId());
            int delete = forecastSectionDao.delete(delWrapper);
            //同时删除 水闸 水坝的控制参数
        }

        String caseId = UUID.randomUUID().toString().replace("-", "");
        updateCaseStatus(caseId, new BigDecimal(1), "1", e.getCaseName(),e.getModelType());
        StCaseBaseInfoDto stCaseBaseInfoDto = new StCaseBaseInfoDto();
        //如果预见期位null 则不需要 查询或者补充 ，否则就需要给站点 补充数据
        BeanUtil.copyProperties(e, stCaseBaseInfoDto);
        stCaseBaseInfoDto.setId(caseId);

        String oldPath = "E:\\model\\alg\\model\\0526.inp";
//        String fileName = "032502.inp";
//        String fileName = "042602.inp";
//        //复制生成新的 inp 文件
//        String newPath = folderCopy(oldPath, caseId, e) + File.separator + fileName;
        stCaseBaseInfoDto.setModelType(3);
        stCaseBaseInfoDto.setModelPath(oldPath);
        stCaseBaseInfoDto.setUpdateTime(new Date());
        stCaseBaseInfoDto.setStep(5);
        stCaseBaseInfoDao.insert(stCaseBaseInfoDto);
        return caseId;
    }

    @Override
    public void execute(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        //执行计算模型
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletableFuture.runAsync(() -> {
            try {
                executeModel(stCaseBaseInfoEsu);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateCaseStatus(String caseId, BigDecimal percent, String status, String caseName, Integer modelType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("case_id", caseId);
        StCaseProgressDto stCaseProgressDto1 = stCaseProgressDao.selectOne(queryWrapper);
        StCaseProgressDto stCaseProgressDto = new StCaseProgressDto();
        stCaseProgressDto.setProgress(percent);
        stCaseProgressDto.setStatus(status);
        stCaseProgressDto.setCaseName(caseName);
        stCaseProgressDto.setModelType(modelType);
        stCaseProgressDto.setCaseId(caseId);
        if (stCaseProgressDto1 != null) {

            stCaseProgressDao.update(stCaseProgressDto, queryWrapper);
        } else {
            stCaseProgressDao.insert(stCaseProgressDto);
        }

        //----------------------------更新案件状态
        if (!status.equals("1")) {
            QueryWrapper caseInfo = new QueryWrapper();
            caseInfo.eq("id", caseId);
            StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectOne(caseInfo);
            if (stCaseBaseInfoDto != null) {
                stCaseBaseInfoDto.setStatus(status);
                stCaseBaseInfoDao.update(stCaseBaseInfoDto, caseInfo);
            }
        }

    }


    /**
     * 执行算法文件 进行 拼接入参  获取结果出参
     *
     * @param stCaseBaseInfoEsu
     * @throws ParseException
     */
    public void executeModel(StCaseBaseInfoEsu stCaseBaseInfoEsu) throws ParseException {
        QueryWrapper caseQuery = new QueryWrapper<>();
        caseQuery.eq("id", stCaseBaseInfoEsu.getId());
        StCaseBaseInfoDto stCaseBaseInfoDto = stCaseBaseInfoDao.selectOne(caseQuery);
        if (stCaseBaseInfoDto == null) {
            updateCaseStatus(stCaseBaseInfoEsu.getId(), new BigDecimal(30), "3", "",stCaseBaseInfoDto.getModelType());
            return;
        }

        BeanUtil.copyProperties(stCaseBaseInfoDto, stCaseBaseInfoEsu);
        //系统内部默认给预热期往前添加两小时
        DateTime dateTime = DateUtil.offsetHour(stCaseBaseInfoEsu.getPreHotTime(), -2);
        stCaseBaseInfoEsu.setPreFixTime(dateTime);
        //创建模型
        ConnToPihe1dService_c  connToPihe1dService_c = new ConnToPihe1dService_cImpl();
        String modelPath = stCaseBaseInfoEsu.getModelPath();
        System.out.println(modelPath);
        updateCaseStatus(stCaseBaseInfoDto.getId(), new BigDecimal(30), "2", stCaseBaseInfoDto.getCaseName(),stCaseBaseInfoDto.getModelType());
        Integer integer = connToPihe1dService_c.apiSprs1DOpen(stCaseBaseInfoEsu.getModelPath());
        System.out.println("模型打开======" + integer);
        updateCaseStatus(stCaseBaseInfoEsu.getId(), new BigDecimal(55), "2", stCaseBaseInfoEsu.getCaseName(),stCaseBaseInfoDto.getModelType());
        //获取模型中的边界 （流量站 名称 水位站 名称 雨量站名称 .补水口）
        List<String> nameList = getInputParamTimeSer(connToPihe1dService_c);
        List<String> fixWaterSerial = nameList.stream().filter(s -> {return s.contains("补水口");}).collect(Collectors.toList());

        List<String> rainStcd = nameList.stream().filter(s -> {
            return s.contains("A");
        }).collect(Collectors.toList());
        //站点名称与系统站点名称关联
        List<StStbprpBEntity> stStbprpBEntity = stStbprpBDao.selectList(new QueryWrapper<>());
//        //流量站入参
        Map<String, List<StWaterRateEntity>> waterRateInput = getWaterRateInput(stStbprpBEntity, stCaseBaseInfoEsu);
//        Map<String, List<StWaterRateEntity>> waterRateInput = new HashMap<>();
//        //水位站入参
//        Map<String, List<StWaterRateEntity>> waterPositionInput = new HashMap<>();
        Map<String, List<StWaterRateEntity>> waterPositionInput = getWaterPositionInput(stCaseBaseInfoEsu);
//        //雨量站入参
//
        Map<String, List<RainDateDto>> rainDataInput = getRainDataInput(rainStcd, stCaseBaseInfoEsu);
//        Map<String, List<HourTimeAxisRainVo>> rainDataInput =new HashMap<>();
//        //修改模拟时间
        int setStimulateTimeRes = setStimulateTime(stCaseBaseInfoEsu, connToPihe1dService_c);
        if (setStimulateTimeRes != 0){
            System.out.println("修改时间出错");
        }
//         给模型文件添加 时间序列
        try {
            doInputParam(waterRateInput, waterPositionInput, rainDataInput, connToPihe1dService_c, stCaseBaseInfoEsu.getId(), stCaseBaseInfoEsu.getModelType(),stCaseBaseInfoEsu.getModelPath());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //添加补水口
        dealFixWater(  connToPihe1dService_c,  fixWaterSerial,stCaseBaseInfoEsu);
        updateCaseStatus(stCaseBaseInfoEsu.getId(), new BigDecimal(80), "2", stCaseBaseInfoEsu.getCaseName(),stCaseBaseInfoDto.getModelType());
//        获取所有控制规则相关信息  返回的是 对应入参的名称 和 下标id
        Map<String, Integer> inputParamCurve = getInputParamCurve(connToPihe1dService_c);
//        获取所有控制规则相关信息  返回的是 对应入参的名称 和 下标id
        Map<String, Integer> inputControl = getInputControl(connToPihe1dService_c);
//        控制规则输入
        try {
            //获取入参水坝的 相关信息
            List<StSideGateDto> stSideGateDtos = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "SB").isNotNull(StSideGateDto::getSeriaName))).orElse(Lists.newArrayList());
            stSideGateDtos = stSideGateDtos.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).collect(Collectors.toList());
            curveParamIn(connToPihe1dService_c,stCaseBaseInfoEsu,inputParamCurve,inputControl,stSideGateDtos);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        int initFlag = connToPihe1dService_c.apiSprs1DInitial();
        System.out.println("模型初始化======" + initFlag);
        //开始进行单独计算
        doCalculate(stCaseBaseInfoEsu.getPreFixTime(),stCaseBaseInfoEsu.getPreHotTime(), connToPihe1dService_c, stCaseBaseInfoEsu.getStep(), stCaseBaseInfoEsu.getId(), stCaseBaseInfoEsu.getModelPath(), stStbprpBEntity, stCaseBaseInfoEsu.getCaseName(),stCaseBaseInfoDto.getModelType());
        //添加一条缓存数据
        List<StWaterEngineeringSchedulingDto> stWaterEngineeringSchedulingDtos = stWaterEngineeringSchedulingService.selectEngineeringScheduling(stCaseBaseInfoEsu.getId());
        redisService.setCacheObject("quick:"+stCaseBaseInfoEsu.getId(),stWaterEngineeringSchedulingDtos);
        updateCaseStatus(stCaseBaseInfoEsu.getId(), new BigDecimal(100), "4", stCaseBaseInfoEsu.getCaseName(),stCaseBaseInfoEsu.getModelType());
        System.out.println("结束");
    }

    /**
     * 给补水口进行入参
     */
    public void dealFixWater(ConnToPihe1dService_c  connToPihe1dService_c, List<String> fixWaterSerial,StCaseBaseInfoEsu stCaseBaseInfoEsu){
        Date preFixTime = stCaseBaseInfoEsu.getPreFixTime();
        Date forecastStartTime = stCaseBaseInfoEsu.getForecastStartTime();
        List<Date> timeSplit = DataUtils.getTimeSplit(preFixTime, forecastStartTime, 30, DateField.MINUTE);
        if (CollUtil.isNotEmpty(fixWaterSerial)){
            for (String s : fixWaterSerial) {
                int model = 2 ;
                for (Date date : timeSplit) {
                    Map<String, String> timeFormatStart = getTimeFormat(date);
                    String cDate = timeFormatStart.get("cDate");
                    String cTime = timeFormatStart.get("cTime");
                    int res = connToPihe1dService_c.apiSprs1DTimeseriesAdd(s+CODE_FIX, model, cDate+CODE_FIX, cTime+CODE_FIX, "0"+CODE_FIX);
                    model = 1 ;
                }
            }
        }


    }

    /**
     * 获取入参时间序列名称
     */
    public List<String> getInputParamTimeSer( ConnToPihe1dService_c  connToPihe1dService_c){
        //获取时间序列的数量
        int timeseries_number = connToPihe1dService_c.apiSprs1DTimeseriesNumber();
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < timeseries_number; i++) {
            byte[] name = new byte[50];
            int name_code = connToPihe1dService_c.apiSprs1DTimeseriesName(i, name);
            String gbkName = new String(name, Charset.forName("GB2312")).trim();
            String utf8StringFromGBKString = getUTF8StringFromGBKString(gbkName);
            System.out.println(utf8StringFromGBKString);
            nameList.add(utf8StringFromGBKString);
            //对名称进行其他存储操作
        }
        return nameList;
        //给 控制规则添加 控制信息

    }

    /**
     * 获取入参控制参数 名称
     */
    public Map<String,Integer> getInputParamCurve( ConnToPihe1dService_c  connToPihe1dService_c){
        Map<String,Integer> res = new HashMap<>();
        //获取时间序列的数量
        int control_number = connToPihe1dService_c.API_SPRS_1D_CURVE_NUMBER();

        for (int i = 0; i < control_number; i++) {
            byte[] name = new byte[50];
            int name_code = connToPihe1dService_c.API_SPRS_1D_CURVE_NAME(i, name);
            String gbkName = new String(name, Charset.forName("GB2312")).trim();
            String utf8StringFromGBKString = getUTF8StringFromGBKString(gbkName);
            System.out.println(utf8StringFromGBKString);
            res.put(utf8StringFromGBKString,i);
        }
        return res;

    }

    /**
     * 获取入参控制参数id 序列名称
     */
    public Map<String,Integer> getInputControl( ConnToPihe1dService_c  connToPihe1dService_c){
        Map<String,Integer> res = new HashMap<>();
        //获取时间序列的数量
        int control_number = connToPihe1dService_c.API_SPRS_1D_CONTROL_NUMBER();

        for (int i = 0; i < control_number; i++) {
            byte[] name = new byte[50];
            int name_code = connToPihe1dService_c.API_SPRS_1D_CONTROL_NAME(i, name);
            String gbkName = new String(name, Charset.forName("GB2312")).trim();
            String utf8StringFromGBKString = getUTF8StringFromGBKString(gbkName);
            System.out.println(utf8StringFromGBKString);
            res.put(utf8StringFromGBKString,i);
        }
        return res;

    }

    public void curveParamIn(ConnToPihe1dService_c  connToPihe1dService_c,StCaseBaseInfoEsu stCaseBaseInfoEsu, Map<String, Integer> inputParamCurve, Map<String, Integer> inputControl, List<StSideGateDto> stSideGateDtos ) throws UnsupportedEncodingException {
        //泵站控制信息
        QueryWrapper pumpQuery = new QueryWrapper<>();
        pumpQuery.eq("case_id",stCaseBaseInfoEsu.getId());
        List<StCaseResParamDto> stCaseResParamDtos = stCaseResParamDao.selectList(pumpQuery);
        if (CollUtil.isNotEmpty(stCaseResParamDtos)){
            for (StCaseResParamDto stCaseResParamDto : stCaseResParamDtos) {
                if (StrUtil.isNotEmpty(stCaseResParamDto.getSeriaName())){
                    String seriaName = stCaseResParamDto.getSeriaName();
                    //启动液位
                    String liquidLevel = stCaseResParamDto.getLiquidLevel();
                    //停止液位
                    String stopLevel = stCaseResParamDto.getStopLevel();
                    int controlType = stCaseResParamDto.getControlType();
                    //1 固定值 2 策略控制
                    if (controlType == 1){
                        int model = 2;
                        //1 开 2 关
                        Integer controlValue = Integer.valueOf(stCaseResParamDto.getControlValue())  == null ? 2 : 1;
                        if (controlValue == 1){
                            if (seriaName.equals("马家湾泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        马家湾泵站            Pump2      0          2
                                //                        马家湾泵站                       100        2
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"2"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"2"+CODE_FIX );
                            }
                            if (seriaName.equals("垡头泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        垡头泵站             Pump2      0          1.15
                                //                        垡头泵站                        100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"2"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"2"+CODE_FIX );
                            }
                            if (seriaName.equals("曹各庄排水沟补水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        曹各庄排水沟补水泵站       Pump2      0          0
                                //                        曹各庄排水沟补水泵站                  0.5        0
                                //                        曹各庄排水沟补水泵站                  1.6        0.1736
                                //                        曹各庄排水沟补水泵站                  100        0.1736
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX," 0.1736"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX," 0.1736"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.6"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0"+CODE_FIX );
                            }
                            if (seriaName.equals("大柳树沟循环调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        大柳树沟循环调水泵站       Pump2      0          0
                                //                        大柳树沟循环调水泵站                  0.5        0
                                //                        大柳树沟循环调水泵站                  1.8        1.15
                                //                        大柳树沟循环调水泵站                  100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"1.5"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"1.5"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.8"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0"+CODE_FIX );
                            }
                            if (seriaName.equals("北小河向望京沟调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        北小河向望京沟调水泵站      Pump2      0          0
                                //                        北小河向望京沟调水泵站                 0.5        0
                                //                        北小河向望京沟调水泵站                 1.94       1.15
                                //                        北小河向望京沟调水泵站                 100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"1.5"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"1.5"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.94"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0"+CODE_FIX );
                            }
                            if (seriaName.equals("北小河向黑桥公园调水泵站")){
                                int integer = inputParamCurve.get(seriaName);

                                //                        ;
                                //                        北小河向黑桥公园调水泵站     Pump2      0          0
                                //                        北小河向黑桥公园调水泵站                0.5        0
                                //                        北小河向黑桥公园调水泵站                2.8        0.7
                                //                        北小河向黑桥公园调水泵站                100        0.7
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0.7"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"0.7"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"2.8"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0"+CODE_FIX );

                            }
                            if (seriaName.equals("沈家坟干渠向崔各庄调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        沈家坟干渠向崔各庄调水泵站    Pump2      0          0
                                //                        沈家坟干渠向崔各庄调水泵站               0.5        0
                                //                        沈家坟干渠向崔各庄调水泵站               0.7        0.08
                                //                        沈家坟干渠向崔各庄调水泵站               1.7        0.37
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0.08"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5 "+CODE_FIX,"0.37"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.7"+CODE_FIX,"0.37"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.7"+CODE_FIX,"0"+CODE_FIX );
                                int rs5 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0"+CODE_FIX );
                            }
                        }
                        //=========================   对泵站进行关闭==================================
                        else {
                            if (seriaName.equals("马家湾泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        马家湾泵站            Pump2      0          2
                                //                        马家湾泵站                       100        2
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"2"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                            if (seriaName.equals("垡头泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        垡头泵站             Pump2      0          1.15
                                //                        垡头泵站                        100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX," 1.15"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                            if (seriaName.equals("曹各庄排水沟补水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        曹各庄排水沟补水泵站       Pump2      0          0
                                //                        曹各庄排水沟补水泵站                  0.5        0
                                //                        曹各庄排水沟补水泵站                  1.6        0.1736
                                //                        曹各庄排水沟补水泵站                  100        0.1736
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX," 0"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"0"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.6"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                            if (seriaName.equals("大柳树沟循环调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        大柳树沟循环调水泵站       Pump2      0          0
                                //                        大柳树沟循环调水泵站                  0.5        0
                                //                        大柳树沟循环调水泵站                  1.8        1.15
                                //                        大柳树沟循环调水泵站                  100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"0"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.8"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                            if (seriaName.equals("北小河向望京沟调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        北小河向望京沟调水泵站      Pump2      0          0
                                //                        北小河向望京沟调水泵站                 0.5        0
                                //                        北小河向望京沟调水泵站                 1.94       1.15
                                //                        北小河向望京沟调水泵站                 100        1.15
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"0"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.94"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                            if (seriaName.equals("北小河向黑桥公园调水泵站")){
                                int integer = inputParamCurve.get(seriaName);

                                //                        ;
                                //                        北小河向黑桥公园调水泵站     Pump2      0          0
                                //                        北小河向黑桥公园调水泵站                0.5        0
                                //                        北小河向黑桥公园调水泵站                2.8        0.7
                                //                        北小河向黑桥公园调水泵站                100        0.7
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5"+CODE_FIX,"0"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"2.8"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );

                            }
                            if (seriaName.equals("沈家坟干渠向崔各庄调水泵站")){
                                int integer = inputParamCurve.get(seriaName);
                                //                        ;
                                //                        沈家坟干渠向崔各庄调水泵站    Pump2      0          0
                                //                        沈家坟干渠向崔各庄调水泵站               0.5        0
                                //                        沈家坟干渠向崔各庄调水泵站               0.7        0.08
                                //                        沈家坟干渠向崔各庄调水泵站               1.7        0.37
                                //                        沈家坟干渠向崔各庄调水泵站               1.7        0.37
                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,2,"0"+CODE_FIX,"0"+CODE_FIX);
                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.5 "+CODE_FIX,"0"+CODE_FIX );
                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"0.7"+CODE_FIX,"0"+CODE_FIX);
                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"1.7"+CODE_FIX,"0"+CODE_FIX );
                                int rs5 = connToPihe1dService_c.API_SPRS_1D_CURVE_ADDBYINDEX(integer,1,"100"+CODE_FIX,"0.000001"+CODE_FIX );
                            }
                        }

                    }else {
                        //泵站的启停水位
                    }
//
                }
            }
        }

        //水闸和水坝的控制信息
        QueryWrapper szWrapper = new QueryWrapper();
        szWrapper.eq("case_id",stCaseBaseInfoEsu.getId());
        List<StForeseeProjectDto> stForeseeProjectDtos = stForeseeProjectDao.selectList(szWrapper);
        if (CollUtil.isNotEmpty(stForeseeProjectDtos)){
            Map<String, List<StForeseeProjectDto>> szsbMap = stForeseeProjectDtos.parallelStream().collect(Collectors.groupingBy(StForeseeProjectDto::getSttp));
            //水闸
            List<StForeseeProjectDto> ddData = szsbMap.get("DD");
            if (CollUtil.isNotEmpty(ddData)){
                for (StForeseeProjectDto dd : ddData) {
                    String seriaName1 = dd.getSeriaName()+"1";
                    Integer integer = inputControl.get(seriaName1);
                    String seriaName2 = dd.getSeriaName()+"2";
                    Integer integer2 = inputControl.get(seriaName2);
                    if (integer == null){
                        continue;
                    }
                    Integer controlType = dd.getControlType();
                    if (controlType == null){
                        controlType = 1;
                    }
                    if (controlType ==1 ){
                        //如果控制 value 没有传递数据 则使用 默认的
                        String controlValue = dd.getControlValue();
                        int model1 = 1;
                        int model3 = 3;
//                        if (controlValue.equals("0")){
//                            int rs2 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,Double.valueOf(999));
//                            int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3, 0 );
//                            int rs5 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,Double.valueOf(999));
//                            int rs6 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3, 1 );
//                        }else {
//                            String holesG = dd.getHolesG();
//                            double s = new BigDecimal(controlValue).divide(new BigDecimal(holesG),2,RoundingMode.HALF_UP).doubleValue();
                            int rs1 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,Double.valueOf(-999));
                            int rs3 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3,Double.valueOf(1));
                            int rs = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,Double.valueOf(-999));
                            int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3, Double.valueOf(0) );
//                            int rs1 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,Double.valueOf(-999));
//                            int rs3 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3,s);
//                            int rs = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,Double.valueOf(-999));
//                            int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3,s );
//                        }

                    }
                    else {
                        // 开闸水位
                        double preWaterLevel = new Double(dd.getPreWaterLevel()) ;
                        //关闭闸水位
                        double openHigh = new Double(dd.getOpenHigh()) ;
                        int model1 = 1;
                        int model2 = 2;

                        int rs1 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,preWaterLevel);
                        int rs2 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model2,openHigh);
                        int rs3 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,1);
                        int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model2,0 );

                    System.out.println("水闸入参"+rs1+rs2+rs3+rs4);
                    }
                }
            }
            //拦河坝
            Map<String, StSideGateDto> serMap = stSideGateDtos.parallelStream().collect(Collectors.toMap(StSideGateDto::getSeriaName, Function.identity(), (o1, o2) -> o2));
            List<StForeseeProjectDto> sbData = szsbMap.get("SB");
            if (CollUtil.isNotEmpty(ddData)){
                for (StForeseeProjectDto sb : sbData) {
                    if (StrUtil.isNotEmpty(sb.getSeriaName())){
                        //开坝水位
                        String seriaName1 = sb.getSeriaName()+"1";
                        Integer integer = inputControl.get(seriaName1);
                        //闭坝水位
                        String seriaName2 = sb.getSeriaName()+"2";
                        Integer integer2 = inputControl.get(seriaName2);
                        Integer controlType = sb.getControlType();
                        //1 固定值 2 策略控制
                        if (controlType == null){
                            controlType = 1;
                        }
                        if (controlType ==2){
                            if (StrUtil.isEmpty(sb.getPreWaterLevel()) || StrUtil.isEmpty(sb.getOpenHigh())){
                                continue;
                            }
                            //升坝水位
                            double preWaterLevel = new Double(sb.getPreWaterLevel()) ;
                            String high1 = sb.getHigh1();
                            //开闸百分比
                            double value1 = new BigDecimal(preWaterLevel).divide(new BigDecimal(high1),2,RoundingMode.HALF_UP).doubleValue();
                            //降坝水位
                            double openHigh = new Double(sb.getOpenHigh()) ;
                            String high2 = sb.getHigh2();
                            //闭闸百分比
                            double value2 = new BigDecimal(openHigh).divide(new BigDecimal(high2),2,RoundingMode.HALF_UP).doubleValue();
                            int model1 = 1;
                            int model2 = 2;

                            int rs1 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,preWaterLevel);
                            int rs2 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model2,openHigh );
                            int rs3 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,value1);
                            int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model2,value2 );
                            System.out.println("拦河坝入参"+rs1+rs2);
                        }else {
                            //fix 修改坝底高程 + 输入的一个固定数值  > 数值 给 1  < 数值给 0
                            String bDefault = StrUtil.isEmpty(sb.getBDefault()) ? "0" : sb.getBDefault();
                            StSideGateDto stSideGateDto = serMap.get(sb.getSeriaName());
                            Double bottom = Double.valueOf(bDefault) + Double.valueOf(stSideGateDto.getGateBottom());
                            int model1 = 1;
                            int model3 = 3;
                            int rs2 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,bottom);
                            int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3, 0 );
                            int rs5 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,bottom);
                            int rs6 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3, 1 );
//                            if (bDefault.equals("0")){
//                                int rs2 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,Double.valueOf(999));
//                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3, 0 );
//                                int rs5 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,Double.valueOf(999));
//                                int rs6 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3, 1 );
//                            }else {
//                                double s = new BigDecimal(bDefault).divide(new BigDecimal(sb.getBHigh()),2,RoundingMode.HALF_UP).doubleValue();
//                                int rs1 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model1,Double.valueOf(-999));
//                                int rs3 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer,model3,s);
//                                int rs = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model1,Double.valueOf(-999));
//                                int rs4 = connToPihe1dService_c.API_SPRS_1D_CONTROL_REVISEBYINDEX(integer2,model3, s );
//                            }
                        }
                    }
                }
            }
        }
        System.out.println("控制参数入参完成");
    }

    /**
     * 修改模型执行时间
     * @param stCaseBaseInfoEsu
     * @param connToPihe1dService_c
     * @return
     */
    public Integer setStimulateTime(StCaseBaseInfoEsu stCaseBaseInfoEsu, ConnToPihe1dService_c connToPihe1dService_c){
        Map<String, String> timeFormatStart = getTimeFormat(stCaseBaseInfoEsu.getPreHotTime());
        String cDate = timeFormatStart.get("cDate");
        String cTime = timeFormatStart.get("cTime");
        Map<String, String> timeFormatEnd = getTimeFormat(stCaseBaseInfoEsu.getPreSeeTime());
        String cDateEnd = timeFormatEnd.get("cDate");
        String cTimeEnd = timeFormatEnd.get("cTime");
        Integer integer1 = connToPihe1dService_c.API_SPRS_1D_SIMULATION_TIME(cDate+CODE_FIX,cTime+CODE_FIX ,cDateEnd+CODE_FIX,cTimeEnd+CODE_FIX);
        return integer1;
    }

    /**
     * 方案的计算与保存
     * @param preFixTime 预热期 前置两小时
     * @param connToPihe1dService_c
     * @param step
     * @param caseId
     * @param modelPath
     * @param stStbprpBEntity
     * @param caseName
     */
    public void doCalculate(Date preFixTime,Date preHot, ConnToPihe1dService_c connToPihe1dService_c, Integer step, String caseId, String modelPath, List<StStbprpBEntity> stStbprpBEntity, String caseName,Integer modelType) throws ParseException {
        double[] riverZ = new double[1600]; //河道断面水位数组
        double[] riverH = new double[1600]; //河道断面水深数组
        double[] riverQ = new double[1600]; //河道断面流量数组
        double[] riverV = new double[1600]; //河道断面过水流速数组
        double[] riverA = new double[1600]; //河道断面过水面积数组
        double[] riverW = new double[1600]; //河道断面过水宽度数组
        List<String> rvIds = new ArrayList(); // 统计关联的所有河流Id
        Map<String, StStbprpBEntity> sectionStationMap = stStbprpBEntity.parallelStream()
                .filter(stStbprpBEntity1 -> {
                    return !StrUtil.isNotEmpty(stStbprpBEntity1.getSectionName());
                })
                .collect(Collectors.toMap(StStbprpBEntity::getSectionName, Function.identity(), (o1, o2) -> o2));

        List<StSectionModelDto> stSectionModelDtos = stSectionModelDao.selectList(new QueryWrapper<>());
        Map<String, StSectionModelDto> sectionRiverIdMap = new HashMap<>();
        if (CollUtil.isNotEmpty(stSectionModelDtos)) {
            sectionRiverIdMap = stSectionModelDtos.parallelStream().collect(Collectors.toMap(StSectionModelDto::getSectionName, Function.identity(), (o1, o2) -> o2));
        }

        //获取断面数量

        int num = connToPihe1dService_c.API_SPRS_1D_TRANSECT_NUMBER();
        System.out.println("断面数量是" + num);

        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            byte[] name = new byte[50];
            int name_code = connToPihe1dService_c.API_SPRS_1D_TRANSECT_NAME(i, name);
            String gbkName = new String(name, Charset.forName("GB2312")).trim();
            String utf8StringFromGBKString = getUTF8StringFromGBKString(gbkName);
            System.out.println(utf8StringFromGBKString);
            nameList.add(utf8StringFromGBKString);
            //对名称进行其他存储操作
        }
        DoubleByReference elapsedTime = new DoubleByReference();
        int err = 0;
        Integer stepAdd = 0;

        List<StCaseResDto> res = new ArrayList<>();
        do {
            //第一次 需要重置之后则不需要
            connToPihe1dService_c.API_SPRS_1D_FIXEDSTEP(elapsedTime,step*60);
//            BigDecimal second = new BigDecimal(elapsedTime.getValue()).divide(new BigDecimal(1), 0, RoundingMode.HALF_UP);
//            BigDecimal sub = new BigDecimal(elapsedTime.getValue() - 0.1).divide(new BigDecimal(1), 0, RoundingMode.HALF_UP);
//            if (process.compareTo(sub) >= 0 && process.compareTo(second) <= 0) {
            connToPihe1dService_c.apiSprs1DResult(riverZ, riverH, riverQ, riverV, riverA, riverW);
            for (int i = 0; i < nameList.size(); i++) {
                String sectionName = nameList.get(i);
                StStbprpBEntity stStbprpBEntity1 = sectionStationMap.get(sectionName);
                Double z = riverZ[i];
                Double h = riverH[i];
                Double q = riverQ[i];
                Double v = riverV[i];
                Double a = riverA[i];
                Double w = riverW[i];
                StCaseResDto stCaseResDto = new StCaseResDto();
                stCaseResDto.setCaseId(caseId);
                stCaseResDto.setStep(step);
                stCaseResDto.setId(UUID.randomUUID().toString().replace("-", ""));
                stCaseResDto.setRiverZ(z.toString());
                stCaseResDto.setRiverH(h.toString());
                stCaseResDto.setRiverQ(q.toString());
                stCaseResDto.setRiverV(v.toString());
                stCaseResDto.setRiverA(a.toString());
                stCaseResDto.setRiverW(w.toString());
                stCaseResDto.setStep(stepAdd);
//                stCaseResDto.setStepTime(DateUtil.offsetMinute(preFixTime, stepAdd));
//                if (stCaseResDto.getStepTime().getTime() < preHot.getTime()){
//                    stCaseResDto.setDataType("1");
//                }else {
//                    stCaseResDto.setDataType("2");
//                }
                stCaseResDto.setStepTime(DateUtil.offsetMinute(preHot, stepAdd));
                stCaseResDto.setDataType("2");
                stCaseResDto.setSectionName(sectionName);
                stCaseResDto.setCreateTime(new Date());
                if (stStbprpBEntity1 != null) {
                    stCaseResDto.setStcd(stStbprpBEntity1.getStcd());
                    stCaseResDto.setStnm(stStbprpBEntity1.getStnm());
                }
                try {
                    stCaseResDto.setRvId(sectionRiverIdMap.get(sectionName) == null ? null : sectionRiverIdMap.get(sectionName).getRiverId().toString());
                    // 统计断面关联的河流id
                    if(null != stCaseResDto.getRvId() && !rvIds.contains(stCaseResDto.getRvId())) {
                        rvIds.add(stCaseResDto.getRvId());
                    }
                } catch (Exception e) {
                    stCaseResDto.setRvId(null);
                    System.out.println("断面" + sectionName + "没有关联相关河道");
                }
                res.add(stCaseResDto);
            }
            stepAdd += step;
        } while (elapsedTime.getValue() > 0.0 && err == 0);
        updateCaseStatus(caseId, new BigDecimal(80), "2", caseName,modelType);

        if (CollUtil.isNotEmpty(res)) {
            save(res);
            connToPihe1dService_c.apiSprs1DClose();
            updateCaseStatus(caseId, new BigDecimal(90), "2", caseName,modelType);
        }
        // 加载断面
        for(String riverId : rvIds) {
            StCaseBaseInfoEsu stCaseBaseInfoEsu = new StCaseBaseInfoEsu();
            stCaseBaseInfoEsu.setId(caseId);
            stCaseBaseInfoEsu.setRiverId(riverId);
            redisService.setCacheObject("RiverSection@" + caseId + "-" + riverId, stCaseResService.getRiverSection(stCaseBaseInfoEsu));
            System.out.println("duanmiancaseId");
        }
    }

    public void save(List<StCaseResDto> res) {
        List<List<StCaseResDto>> f = Lists.partition(res, 3000);
        List<List<StCaseResDto>> list =
                f.parallelStream().peek(x -> {
                    stCaseResDao.saveFloodModelOutCellStatistic(x);
                }).collect(Collectors.toList());


    }


    public <T, S> List<Boolean> insertBatch(List<T> list, int unitSize, Class<S> cls, BiConsumer<T, S> function) {
        return mapIterator(list, unitSize, unit -> unitInsertBatch(unit, cls, function));

    }

    private <T, R> List<R> mapIterator(List<T> list, int unitSize, Function<List<T>, R> handleRow) {
        Iterator<T> iterator = list.iterator();
        List<R> lists = new ArrayList<>();
        while (iterator.hasNext()) {
//            mapIterator(iterator, lists, unitSize, handleRow);
            List<T> rowList = new ArrayList<>();
            Integer i = 0;
            while (true) {
                T next = iterator.next();
                rowList.add(next);
                if (++i == unitSize) {
                    lists.add(handleRow.apply(rowList));
                    break;
                }
                //只有最后一轮才会触发
                if (!iterator.hasNext()) {
                    lists.add(handleRow.apply(rowList));
                    break;
                }
            }
        }
        return lists;
    }

    private <T, R> boolean unitInsertBatch(List<T> list, Class<R> rClass, BiConsumer<T, R> function) {
        // 对象转换
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

        R mapper = sqlSession.getMapper(rClass);
        try {
            for (T t : list) {
                function.accept(t, mapper);
            }
            // 调用新增的方法
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            return false;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return true;
    }

    /**
     * 将数据入参加入时间序列
     *
     * @param waterRateInput     流量站序列
     * @param waterPositionInput 水位站序列
     * @param rainDataInput      雨量站序列
     * @param modelType          方案模型类型 如果是水资源则不需要雨量站的入参
     */
    public void doInputParam(Map<String, List<StWaterRateEntity>> waterRateInput, Map<String, List<StWaterRateEntity>> waterPositionInput, Map<String, List<RainDateDto>> rainDataInput, ConnToPihe1dService_c connToPihe1dService_c, String caseId, Integer modelType,String modelPath) throws IOException {


        List<StCaseProcessDto> inputParam = new ArrayList<>();
        //流量站 添加时间序列

        for (String stcd : waterRateInput.keySet()) {
            List<StWaterRateEntity> stWaterRateEntities = waterRateInput.get(stcd);
            Integer mode = 2;
            if (CollUtil.isNotEmpty(stWaterRateEntities)) {
                stWaterRateEntities = stWaterRateEntities.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime)).collect(Collectors.toList());
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    String momentRate = stWaterRateEntity.getMomentRate();
                    String ctime = stWaterRateEntity.getCtime();
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(ctime);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
//                    byte[] gbks = stcd.getBytes("gbk");
//                    if (cDate.contains("-")){
//                        cDate = cDate.replace("-", "/");
//                    }
                    int res = connToPihe1dService_c.apiSprs1DTimeseriesAdd(stcd+CODE_FIX, mode, cDate+CODE_FIX, cTime+CODE_FIX, momentRate+CODE_FIX);
                    mode = 1;
                    StCaseProcessDto stCaseProcessDto = new StCaseProcessDto();
                    stCaseProcessDto.setCaseId(caseId);
                    stCaseProcessDto.setStcd("00" + stWaterRateEntity.getDid());
                    stCaseProcessDto.setSerializeName(stcd);
                    stCaseProcessDto.setInputData(momentRate);
                    stCaseProcessDto.setType("ZQ");
                    inputParam.add(stCaseProcessDto);
                }
            }
        }


        //水位站 添加时间序列
        for (String name : waterPositionInput.keySet()) {
            List<StWaterRateEntity> stWaterRateEntities = waterPositionInput.get(name);
            if (CollUtil.isNotEmpty(stWaterRateEntities)) {
                Integer mode = 2;
                stWaterRateEntities = stWaterRateEntities.stream().sorted(Comparator.comparing(StWaterRateEntity::getCtime)).collect(Collectors.toList());
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    String[] split = name.split("-");
                    String serialize = split[0];
                    if (StrUtil.isEmpty(stWaterRateEntity.getAddrv())){
                        continue;
                    }
                    String addrv =  new BigDecimal(stWaterRateEntity.getAddrv()).toString()  ;
                    String ctime = stWaterRateEntity.getCtime();
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(ctime);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
                    if (cDate.contains("-")){
                        cDate = cDate.replace("-", "/");
                    }
                    byte[] gbks = serialize.getBytes("gbk");
                    int res = connToPihe1dService_c.apiSprs1DTimeseriesAdd(serialize+CODE_FIX, mode, cDate+CODE_FIX, cTime+CODE_FIX, addrv+CODE_FIX);
                    mode = 1;
                    StCaseProcessDto stCaseProcessDto = new StCaseProcessDto();
                    stCaseProcessDto.setCaseId(caseId);
                    stCaseProcessDto.setSerializeName(serialize);
                    stCaseProcessDto.setInputData(addrv);
                    stCaseProcessDto.setType("ZZ");
                    inputParam.add(stCaseProcessDto);
                }

            }
        }


        //雨量站 添加时间序列
        for (String stationId : rainDataInput.keySet()) {
            List<RainDateDto> rainDateDtoList = rainDataInput.get(stationId);
            rainDateDtoList = rainDateDtoList.stream().sorted(Comparator.comparing(RainDateDto::getDate)).collect(Collectors.toList());
            Integer mode = 2;
            for (RainDateDto rainDateDto : rainDateDtoList) {
                String hhRain = rainDateDto.getHhRain();
                Date date = rainDateDto.getDate();
                Map<String, String> timeFormat = getTimeFormat(date);
                String cDate = timeFormat.get("cDate");
                String cTime = timeFormat.get("cTime");
                if (cDate.contains("-")){
                    cDate = cDate.replace("-", "/");
                }
                int res = connToPihe1dService_c.apiSprs1DTimeseriesAdd(stationId+CODE_FIX, mode, cDate+CODE_FIX, cTime+CODE_FIX, hhRain+CODE_FIX);
                mode = 1;
                StCaseProcessDto stCaseProcessDto = new StCaseProcessDto();
                stCaseProcessDto.setCaseId(caseId);
                stCaseProcessDto.setStcd(stationId);
                stCaseProcessDto.setSerializeName(stationId);
                stCaseProcessDto.setInputData(hhRain);
                stCaseProcessDto.setType("PP");
                inputParam.add(stCaseProcessDto);
            }
        }

        if (CollUtil.isNotEmpty(inputParam)) {
            for (StCaseProcessDto stCaseProcessDto : inputParam) {
                stCaseProcessDao.insert(stCaseProcessDto);
            }
        }
    }

    /**
     * 获取雨量站的入参 取数规则 分钟雨量获取成功之后 进行按照站点为单位分组 进行遍历每一个分钟数据
     * 获取数组长度为0 的点是第一个点 第二个点为 0点累加 步长-1 累加的数据 ...以此类推直到结束
     * 其获取除第一个点外其余的点需将步长累加一次 ，同时当前的点需要减去上一个点
     *
     *
     * @param rainStcd
     * @param algParamRequestDto
     * @return
     */
    public Map<String, List<RainDateDto>> getRainDataInput(List<String> rainStcd, StCaseBaseInfoEsu algParamRequestDto) {
        Map<String, List<RainDateDto>> res = new HashMap<>();
        //雨量站  时间格式 Date
        QueryWrapper rainQuery = new QueryWrapper();
        rainQuery.in("station_id", rainStcd);
        rainQuery.le("date", algParamRequestDto.getForecastStartTime());
        rainQuery.ge("date", algParamRequestDto.getPreHotTime());
        System.out.println(rainStcd);
        List<RainDateDto> rainDateDtos = rainDateDtoDao.selectList(rainQuery);
        if (CollUtil.isEmpty(rainDateDtos)){
            //补充0点
            List<Date> timeSplit = DataUtils.getTimeSplit( algParamRequestDto.getPreHotTime(), algParamRequestDto.getForecastStartTime(), algParamRequestDto.getStep(), DateField.MINUTE);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "PP").isNotNull(StStbprpBEntity::getSectionName).orderByAsc(StStbprpBEntity::getStcd))).orElse(com.google.common.collect.Lists.newArrayList());
            List<String> collect = Optional.ofNullable(stStbprpBEntityList.stream().map(x -> x.getStnm()).collect(Collectors.toList())).orElse(com.google.common.collect.Lists.newArrayList());

            //如果实测降雨 查询不到数据就补充0
            for (int i = 0; i < timeSplit.size() - 1; i++) {
                StCaseResRainList stCaseResRainList = new StCaseResRainList();
                stCaseResRainList.setTime(df.format(timeSplit.get(i)));
                Date begin = timeSplit.get(i);
                for (StStbprpBEntity stStbprpBEntity : stStbprpBEntityList) {
                    RainDateDto rainDateDto = new RainDateDto();
                    Double rain = 0.0;
                    rainDateDto.setHhRain(rain.toString());
                    rainDateDto.setDate(begin);
                    rainDateDto.setStationID(stStbprpBEntity.getStcd());
                    rainDateDtos.add(rainDateDto);
                }

            }
        }
        Map<String, List<RainDateDto>> rainStationMap = rainDateDtos.parallelStream().collect(Collectors.groupingBy(RainDateDto::getStationID));
        //每个站点存放的雨量数据

        for (String stationId : rainStationMap.keySet()) {
            List<RainDateDto> rainDateDtos1 = rainStationMap.get(stationId);
            rainDateDtos1 = rainDateDtos1.stream().sorted(Comparator.comparing(RainDateDto::getDate)).collect(Collectors.toList());
            List<RainDateDto> rainResList = new ArrayList<>();
            //假设 10 分钟
            int step = 10;
            //控制步长 累加
            int constep = 10;
            int n = 1;
            //累计雨量
            BigDecimal addtionRain = new BigDecimal(0);
            for (int i = 0; i < rainDateDtos1.size();i++ ) {
                String hhRain = rainDateDtos1.get(i).getHhRain().equals("9999") ? "0" : rainDateDtos1.get(i).getHhRain();
                addtionRain = addtionRain.add(new BigDecimal(hhRain));
                if (i == 0) {
                    rainDateDtos1.get(i).setHhRain(addtionRain.toString());
                    Date preHotTime = algParamRequestDto.getPreHotTime();
                    rainDateDtos1.get(i).setDate(preHotTime);
                    rainResList.add(rainDateDtos1.get(i));
                }
                if (i == constep-1){
                    rainDateDtos1.get(i).setHhRain(addtionRain.toString());
                    Date preHotTime = algParamRequestDto.getPreHotTime();
                    DateTime dateTime = DateUtil.offsetMinute(preHotTime, step*n);
                    rainDateDtos1.get(i).setDate(dateTime);
                    RainDateDto rainDateDto = rainResList.get(rainResList.size() - 1);
                    BigDecimal value = new BigDecimal(rainDateDtos1.get(i).getHhRain()).subtract(new BigDecimal(rainDateDto.getHhRain()) ) ;
                    rainDateDtos1.get(i).setHhRain(value.toString());
                    rainResList.add(rainDateDtos1.get(i));
                    constep += step;
                    //再次重置0
                    addtionRain = new BigDecimal(0);
                    n = n+1;
                }
            }
            res.put(stationId, rainResList);
        }
        //进行 对雨量站数据进行补点数据
//        for (String stationId : rainStcd) {
//            List<HourTimeAxisRainVo> rainDateDtoList = rainResMap.get("stationId");
//            if (CollUtil.isNotEmpty(rainDateDtoList)) {
//                //补充部分
//                List<HourTimeAxisRainVo> rainFixList = addPartFixRainData(rainDateDtoList, algParamRequestDto.getStep(), algParamRequestDto.getPreHotTime(), algParamRequestDto.getPreSeeTime());
//                res.put(stationId, rainFixList);
//            } else {
//                //补充全部
//                List<HourTimeAxisRainVo> rainAllFix = addAllFixRainData(stationId, algParamRequestDto.getStep(), algParamRequestDto.getPreHotTime(), algParamRequestDto.getPreSeeTime());
//                res.put(stationId, rainAllFix);
//            }
//        }


//       以上为实测
//       ===================================================================================================
//       以下为预报
        //水资源模型调度不需要 预报
        Integer modelType = algParamRequestDto.getModelType();
        if (modelType != 2){
            //查看方案的预报方式 预报方式 1 气象预测 2 设计雨型
            String forecastType = StrUtil.isNotEmpty( algParamRequestDto.getForecastType()) ?  algParamRequestDto.getForecastType() : "1";
            if ("2".equals(forecastType)){
                List<RainDateDto> rain = findRain(algParamRequestDto);
                if (!CollectionUtils.isEmpty(rain)) {
                    for (Map.Entry<String, List<RainDateDto>> entry : res.entrySet()) {
                        String key = entry.getKey();
                        rain.stream().forEach(x -> {
                            x.setStationID(key);
                        });
                        List<RainDateDto> rainDateDtoList = entry.getValue();
                        rainDateDtoList.addAll(rain);
                        res.put(key, rainDateDtoList);
                    }
                }
            }else {
                QueryWrapper baseWrapper = new QueryWrapper();
                baseWrapper.eq("type","1");
                List<StCaiyunMeshDto> stCaiyunMeshDtos = stCaiyunMeshDao.selectList(baseWrapper);
                //网格id
                List<String> meshids = stCaiyunMeshDtos.parallelStream().map(StCaiyunMeshDto::getMeshId).collect(Collectors.toList());
                Map<String, StCaiyunMeshDto> stationMeshInfoMap = stCaiyunMeshDtos.parallelStream().collect(Collectors.toMap(StCaiyunMeshDto::getMeshId, Function.identity(), (o1, o2) -> o2));
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.ge("drp_time",algParamRequestDto.getForecastStartTime());
                wrapper.le("drp_time",algParamRequestDto.getPreSeeTime());
                wrapper.in("mesh_id",meshids);
                List<StCaiyunPrecipitationHistoryDto> stCaiyunPrecipitationHistoryDtos = stCaiyunPrecipitationHistoryDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stCaiyunPrecipitationHistoryDtos)){
                    Map<String, List<StCaiyunPrecipitationHistoryDto>> meshidsMap = new HashMap<>();
                    if (CollUtil.isNotEmpty(stCaiyunPrecipitationHistoryDtos)){
                        //每个站点网格 1 小时一个数据 然后需要查分数据
                        meshidsMap = stCaiyunPrecipitationHistoryDtos.parallelStream().collect(Collectors.groupingBy(StCaiyunPrecipitationHistoryDto::getMeshId));
                    }
                    for (String meshid : stationMeshInfoMap.keySet()) {
                        List<RainDateDto> list = new ArrayList<>();
                        StCaiyunMeshDto stCaiyunMeshDto = stationMeshInfoMap.get(meshid);
                        //站点id
                        String stcd = stCaiyunMeshDto.getStcd();
                        //每个网格数据
                        List<StCaiyunPrecipitationHistoryDto> stCaiyunPrecipitationHistoryDtos1 = meshidsMap.get(meshid);
                        //对这个网格的 预测时间到预见期 时间进行排序 一个时间是6个点
                        List<StCaiyunPrecipitationHistoryDto> collect = stCaiyunPrecipitationHistoryDtos1.stream().sorted(Comparator.comparing(StCaiyunPrecipitationHistoryDto::getDrpTime)).collect(Collectors.toList());

                        for (StCaiyunPrecipitationHistoryDto stCaiyunPrecipitationHistoryDto : collect) {
                            String drp = StrUtil.isNotEmpty(stCaiyunPrecipitationHistoryDto.getDrp()) ? stCaiyunPrecipitationHistoryDto.getDrp() : "0" ;
                            BigDecimal divide = new BigDecimal(drp).divide(new BigDecimal(7),2,RoundingMode.HALF_UP);
                            Date startDate = stCaiyunPrecipitationHistoryDto.getDrpTime();
                            //分钟
                            int moment = 0;
                            for (int i = 0; i < 7; i++) {
                                RainDateDto rainDateDto = new RainDateDto();
                                rainDateDto.setStationID(stcd);
                                rainDateDto.setDate(startDate);
                                rainDateDto.setHhRain(divide.toString());
                                moment+=10;
                                startDate = DateUtil.offsetMinute(startDate,moment);
                                list.add(rainDateDto);
                            }
                        }
                        List<RainDateDto> rainDateDtoList = res.get(stcd);
                        if (CollUtil.isNotEmpty(rainDateDtoList)){
                            rainDateDtoList.addAll(list);
                            res.put(stcd,rainDateDtoList);
                        }else {
                            res.put(stcd,list);
                        }
                    }
                }
            }

        }



        return res;
    }

    public List<RainDateDto> findRain(StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        List<RainDateDto> floodDateList = new ArrayList<>();
        if (StringUtils.isNotBlank(stCaseBaseInfoEsu.getRainId())) {
            StDesigRainPatternDto stDesigRainPatternDto = stDesigRainPatternDao.selectById(stCaseBaseInfoEsu.getRainId());
            if (null != stDesigRainPatternDto) {
                Date start = stCaseBaseInfoEsu.getForecastStartTime();
                Date end = stCaseBaseInfoEsu.getPreSeeTime();
                //雨型时间区间
                List<Date> desigRain = DataUtils.getTimeSplit(start, end, stDesigRainPatternDto.getTimeInterval(), DateField.MINUTE);
                String param = stDesigRainPatternDto.getParam();
                String[] split = param.split(",");
                List<String> stringList = Arrays.asList(split);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < desigRain.size() - 1; i++) {
                    RainDateDto rainDateDto = new RainDateDto();
                    Double sun = Double.valueOf(stringList.get(i)) * Double.valueOf(stCaseBaseInfoEsu.getRainTotal());
                    rainDateDto.setHhRain(sun.toString());
                    rainDateDto.setDate(desigRain.get(i));
                    floodDateList.add(rainDateDto);
                }
            }
        }
        return floodDateList;
    }


    /**
     * 获取水位站的入参
     *
     * @param algParamRequestDto
     * @return
     */
    public Map<String, List<StWaterRateEntity>> getWaterPositionInput(StCaseBaseInfoEsu algParamRequestDto) {
        //水位站
        List<String>sttpS=new ArrayList<>();
        sttpS.add("ZQ");
        sttpS.add("ZZ");
        List<StStbprpBEntity> stationWaterLevel = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().in(StStbprpBEntity::getSttp, sttpS).orderByAsc(StStbprpBEntity::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stationWaterLevel)) {
            stationWaterLevel = stationWaterLevel.stream().filter(x -> StringUtil.isNotBlank(x.getSectionName())).filter(x->StringUtil.isBlank(x.getSectionFlag())).collect(Collectors.toList());
        }
        //1.水位站入参数据
        Map<String, List<StWaterRateEntity>> levelInputMap = new HashMap<>();
        Map<String, BigDecimal> dtmlMap = stationWaterLevel.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStnm, StStbprpBEntity::getDtmel, (o1, o2) -> o2));

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
                //水位站的水位 需要转换下单位
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    if (StrUtil.isEmpty(stWaterRateEntity.getAddrv())) {
                        continue;
                    }
                    BigDecimal bigDecimal = dtmlMap.get(stbprpBEntity.getStnm());
                    stWaterRateEntity.setAddrv(new BigDecimal(stWaterRateEntity.getAddrv()).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).add(bigDecimal) .toString());
                }
                List<StWaterRateEntity> stWaterRateEntities1 = levelInputMap.get(stbprpBEntity.getSeriaName());
                if (CollUtil.isEmpty(stWaterRateEntities1)){
                    stWaterRateEntities1 = new ArrayList<>();
                }
                levelInputMap.put(stbprpBEntity.getSeriaName(), stWaterRateEntities);
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
                    BigDecimal bigDecimal = dtmlMap.get(stbprpBEntity.getStnm());
                    BigDecimal rs = bigDecimal.add(new BigDecimal(stWaterRateEntity.getMomentRiverPosition()));
                    stWaterRateEntity.setAddrv(rs.toString());
                    stWaterRateEntity.setCtime(stWaterRateEntity.getCtime().replace("-", "/"));
                    List<StWaterRateEntity> list = levelInputMap.get(stbprpBEntity.getSeriaName());
                    if (CollUtil.isNotEmpty(list)){
                        stWaterRateEntities1.addAll(list);
                    }
                    stWaterRateEntities1.add(stWaterRateEntity);
                }
                levelInputMap.put(stbprpBEntity.getSeriaName(), stWaterRateEntities1);
            }
        }
        //水资源模型调度不需要 预报
        Integer modelType = algParamRequestDto.getModelType();
        if (modelType != 2){
            //预报
            QueryWrapper forecastQuery = new QueryWrapper();
            forecastQuery.eq("sttp", "ZZ");
            forecastQuery.le("date", algParamRequestDto.getPreSeeTime());
            forecastQuery.ge("date", algParamRequestDto.getForecastStartTime());
            List<StForecastSectionDto> stForecastSectionDtos = forecastSectionDao.selectList(forecastQuery);
            if (CollUtil.isNotEmpty(stForecastSectionDtos)) {
                Map<String, List<StForecastSectionDto>> sectionForecast = stForecastSectionDtos.parallelStream().collect(Collectors.groupingBy(StForecastSectionDto::getSeriaName));
                for (String sectionName : sectionForecast.keySet()) {
                    List<StForecastSectionDto> dataList = sectionForecast.get(sectionName);
                    List<StWaterRateEntity> inList = new ArrayList<>();
                    for (StForecastSectionDto stForecastSectionDto : dataList) {
                        String value = stForecastSectionDto.getValue();
                        StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
                        stWaterRateEntity.setMomentRate(value);
                        stWaterRateEntity.setCtime(DateUtil.format(stForecastSectionDto.getDate(), "yyyy/MM/dd HH:mm:ss"));
                        inList.add(stWaterRateEntity);
                    }
                    List<StWaterRateEntity> stWaterRateEntities = levelInputMap.get(sectionName);
                    stWaterRateEntities.addAll(inList);
                    levelInputMap.put(sectionName, stWaterRateEntities);
                }

            }
//            else {
//                List<Date> timeSplit1 = DataUtils.getTimeSplit(algParamRequestDto.getForecastStartTime(), algParamRequestDto.getPreSeeTime(), 30, DateField.MINUTE);
//                for (String seriaName : levelInputMap.keySet()) {
//                    String addrv = "";
//                    List<StWaterRateEntity> stWaterRateEntities = levelInputMap.get(seriaName);
//                    if (CollUtil.isNotEmpty(stWaterRateEntities)) {
//                        addrv = stWaterRateEntities.get(0).getAddrv();
//                    }
//
//                    List<StWaterRateEntity> inList = new ArrayList<>();
//                    for (Date date : timeSplit1) {
//                        StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
//                        stWaterRateEntity.setMomentRate(StrUtil.isNotEmpty(addrv) ? addrv : "1200");
//                        stWaterRateEntity.setCtime(DateUtil.format(date, "yyyy/MM/dd HH:mm:ss"));
//                        inList.add(stWaterRateEntity);
//                    }
//                    List<StWaterRateEntity> stWaterRateEntities1 = levelInputMap.get(seriaName);
//                    stWaterRateEntities1.addAll(inList);
//                    levelInputMap.put(seriaName, stWaterRateEntities1);
//                }
//            }
        }

        return levelInputMap;
    }

    /**
     * 获取流量站的入参
     *
     * @param stStbprpBEntity

     * @param algParamRequestDto
     * @return
     */
    public Map<String, List<StWaterRateEntity>> getWaterRateInput(List<StStbprpBEntity> stStbprpBEntity, StCaseBaseInfoEsu algParamRequestDto) {
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
        String momentRate = "";
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
            if (CollUtil.isNotEmpty(stWaterRateEntities)) {
                momentRate = stWaterRateEntities.get(0).getMomentRate();
            }
            rateDataMap = stWaterRateEntities.stream().collect(Collectors.groupingBy(StWaterRateEntity::getDid));
        }

        for (String name : rateMap.keySet()) {
            StStbprpBEntity stStbprpBEntity1 = rateMap.get(name);
            List<StWaterRateEntity> stWaterRateEntities = rateDataMap.get(stStbprpBEntity1.getStcd().substring(2));
            rateInputMap.put(name, stWaterRateEntities);

        }
        //有个站点在数据库中没有 在代码中进行补充 亮马河入流        亮马河上游-0
        List<Date> timeSplit = DataUtils.getTimeSplit(algParamRequestDto.getPreHotTime(), algParamRequestDto.getPreSeeTime(), 30, DateField.MINUTE);
        List<StWaterRateEntity> fixWaterRate = new ArrayList<>();
        //如果时间是在 61或者915 时间节点则给亮马河入参0.5
        // 汛期时间 6.1  -  9.15
        String format = DateUtil.format(algParamRequestDto.getPreHotTime(), "yyyy-MM-dd HH:mm:ss");
        Date xqStartDate= DateUtil.parseDate(format.substring(0,4)+"-06-01 00:00:00") ;
        Date xqEndDate =  DateUtil.parseDate(format.substring(0,4)+"-09-15 23:59:59") ;

        if (algParamRequestDto.getPreHotTime().getTime() >= xqStartDate.getTime() && algParamRequestDto.getPreHotTime().getTime() <= xqEndDate.getTime() ){
            momentRate = "5";
        }
        for (Date date : timeSplit) {
            StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
            stWaterRateEntity.setMomentRate("0.5");
            stWaterRateEntity.setCtime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            fixWaterRate.add(stWaterRateEntity);
        }
        rateInputMap.put("亮马河", fixWaterRate);

        //水资源模型调度不需要 预报
        Integer modelType = algParamRequestDto.getModelType();
        if (modelType != 2){
            //预报
            QueryWrapper forecastQuery = new QueryWrapper();
            forecastQuery.eq("sttp", algParamRequestDto.getId());
            forecastQuery.eq("case_id", "ZQ");
            forecastQuery.le("date", algParamRequestDto.getPreSeeTime());
            forecastQuery.ge("date", algParamRequestDto.getForecastStartTime());
            List<StForecastSectionDto> stForecastSectionDtos = forecastSectionDao.selectList(forecastQuery);
            if (CollUtil.isNotEmpty(stForecastSectionDtos)) {
                Map<String, List<StForecastSectionDto>> sectionForecast = stForecastSectionDtos.parallelStream().collect(Collectors.groupingBy(StForecastSectionDto::getSeriaName));
                for (String sectionName : sectionForecast.keySet()) {
                    List<StForecastSectionDto> dataList = sectionForecast.get(sectionName);
                    List<StWaterRateEntity> inList = new ArrayList<>();
                    for (StForecastSectionDto stForecastSectionDto : dataList) {
                        String value = stForecastSectionDto.getValue();
                        StWaterRateEntity stWaterRateEntity = new StWaterRateEntity();
                        stWaterRateEntity.setCtime(DateUtil.format(stForecastSectionDto.getDate(), "yyyy-MM-dd HH:mm:ss"));
                        stWaterRateEntity.setMomentRate(value);
                        inList.add(stWaterRateEntity);
                    }
                    List<StWaterRateEntity> stWaterRateEntities = rateInputMap.get(sectionName);
                    if (CollUtil.isEmpty(stWaterRateEntities)){
                        stWaterRateEntities = new ArrayList<>();
                    }
                    stWaterRateEntities.addAll(inList);
                    rateInputMap.put(sectionName, stWaterRateEntities);
                }

            }
        }

        return rateInputMap;
    }


    public static Map<String, String> getTimeFormat() {
        String format = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss");
        String cDate = format.substring(5, 7) + "/" + format.substring(8, 10) + "/" + format.substring(0, 4);
        String cTime = format.substring(14, 19);
        if (cTime.startsWith("0")) {
            cTime = cTime.substring(1, 5);
        }
        Map<String, String> map = new HashMap<>();
        map.put("cDate", cDate);
        map.put("cTime", cTime);
        return map;
    }



    public static Map<String, String> getTimeFormat(Date date) {
        String format = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
        String cDate = format.substring(5, 7) + "/" + format.substring(8, 10) + "/" + format.substring(0, 4);
        String cTime = format.substring(11, 16);
        if (cTime.startsWith("0")) {
            cTime = cTime.substring(1, 5);
        }
        Map<String, String> map = new HashMap<>();
        map.put("cDate", cDate);
        map.put("cTime", cTime);
        return map;
    }


//    public static void main(String[] args) {
//        Map<String, String> timeFormat = getTimeFormat(new Date());
//        System.out.println(timeFormat);
//    }




    public List<StWaterRateEntityDTO> getDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")) {
            Map<String, String> stcdMap = new HashMap<>();
            List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
            stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));

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
                    // 以小时为分组 取最后一个小时的数据
                    Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                        return stWaterRateEntity.getCtime().substring(0, 13);
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
                        stStbprpBEntityDTO1.setAddrv(meter.toString());
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                            String ctime = stWaterRateEntity.getCtime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date parse = formatter.parse(ctime);
//                            DateTime dateTime = DateUtil.parseDate(ctime);
                            String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(format);
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
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                    return stWaterRateEntity.getCtime().substring(0, 13);
                }));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5, 16));
                        stWaterRateEntityDTO.setAddrv(stWaterRateEntity.getMomentRate() == null ? "0" : stWaterRateEntity.getMomentRate());
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

    /**
     * 检测点位 不够的进行补充部分数据
     *
     * @param inputList 输入列表
     * @param step      步长
     * @param start     开始时间
     * @param end       结束事件
     * @return
     */
    public List<RainDateDto> addPartFixRainData(List<RainDateDto> inputList, Integer step, Date start, Date end) {
        String stationID = inputList.get(0).getStationID();
        List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.MINUTE, step);
        //需要进行补充数据
        if (inputList.size() > dateTimes.size()) {
            int position = dateTimes.size() - inputList.size();
            for (int i = inputList.size(); i < dateTimes.size(); i++) {
                DateTime dateTime = dateTimes.get(i);
                RainDateDto rainDateDto = new RainDateDto();
                rainDateDto.setStationID(stationID);
                rainDateDto.setHhRain("0");
                rainDateDto.setDate(dateTime);
                inputList.add(rainDateDto);
            }
        }

        return inputList;
    }

    /**
     * 补充雨量站点位数据
     */
    public List<RainDateDto> addAllFixRainData(String stationId, Integer step, Date start, Date end) {
        List<RainDateDto> res = new ArrayList<>();
        List<String> stationType = new ArrayList<>();
        stationType.add("PP");
        //站点名称与系统站点名称关联
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("sttp", stationType);
        //获取所有的雨量站数据

        while (start.getTime() <= (end.getTime())) {
            RainDateDto rainDateDto = new RainDateDto();
            rainDateDto.setDate(start);
            rainDateDto.setHhRain("0");
            rainDateDto.setStationID(stationId);
            res.add(rainDateDto);
            start = DateUtil.offsetMinute(start, step);

        }

        return res;
    }

    //1.先拷贝文件夹

    /**
     * 生成新的inp 文件
     * 预报时间= 预报开始时间
     * 方案开始时间=预报时间-预热时间
     * 方案结束时间=预报时间+预见时间
     *
     * @param oldFilePath
     * @param caseId            方案id
     * @param stCaseBaseInfoEsu
     * @return
     */
    public String folderCopy(String oldFilePath, String caseId, StCaseBaseInfoEsu stCaseBaseInfoEsu) {


        //复制模型操作文件返回新的文件操作路径
        String newOutPath = "";
        String newFilePath = "";
        try {
            newOutPath = oldFilePath.substring(oldFilePath.lastIndexOf("\\"), oldFilePath.length());
            oldFilePath = oldFilePath.substring(0, oldFilePath.lastIndexOf("\\")) + File.separator + newOutPath.substring(1);
            newFilePath = oldFilePath + caseId;
            File file = new File(oldFilePath);
            File[] filesList = file.listFiles();
            if (!(new File(newFilePath)).exists()) {
                (new File(newFilePath)).mkdir();
            }
            for (int i = 0; i < filesList.length; i++) {
                if (new File(filesList[i].toString()).isFile()) {
                    String filePath = filesList[i].toString();
                    filePath = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
                    copyFile(filesList[i].toString(), newFilePath + file.separator + filePath, stCaseBaseInfoEsu);
                }
            }
        } catch (Exception e) {
            log.error("文件拷贝失败folderCopy()" + e);
//            saveFloodPlanRunProgress(planId, Numbers.NUM_30, FloodPlanConstant.FLOOD_PLAN_STATUS_CALCULATION_ERROR);
//            floodPlanMsgDao.updateFloodPlanMsg(planId, "-1");
        }
        return newFilePath;
    }

    public void copyFile(String oldPath, String newFile, StCaseBaseInfoEsu stCaseBaseInfoEsu) {
        BufferedReader br = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            File file = new File(oldPath);

            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GB2312"));
            int count = 0;
            StringBuffer resultFileStr = new StringBuffer();
            String str = "";
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(newFile), "GB2312");
            while ((str = br.readLine()) != null) {

                if (str.contains("START_DATE") || str.contains("REPORT_START_DATE")) {
                    //预热期 时间
                    Date preHotTime = stCaseBaseInfoEsu.getPreHotTime();
                    String format = DateUtil.format(preHotTime, "yyyy/MM/dd HH:mm:ss");
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(format);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
                    //模型时间 MM/dd/yyyy
                    //todo 开始计算日期11-01-2022
                    str = str.substring(0, str.indexOf('/') - 2) + cDate;
                }
                if (str.contains("START_TIME") || str.contains("REPORT_START_TIME")) {
                    Date preHotTime = stCaseBaseInfoEsu.getPreHotTime();
                    String format = DateUtil.format(preHotTime, "yyyy/MM/dd HH:mm:ss");
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(format);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
                    //todo 开始计算时间 00：00：00
                    str = str.substring(0, str.indexOf(':') - 2) + cTime;
                }
                if (str.contains("END_DATE")) {
                    Date preSeeTime = stCaseBaseInfoEsu.getPreSeeTime();
                    String format = DateUtil.format(preSeeTime, "yyyy/MM/dd HH:mm:ss");
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(format);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
                    //todo 开始计算日期11-01-2022
                    str = str.substring(0, str.indexOf('/') - 2) + cDate;
                }
                if (str.contains("END_TIME")) {
                    Date preSeeTime = stCaseBaseInfoEsu.getPreSeeTime();
                    String format = DateUtil.format(preSeeTime, "yyyy/MM/dd HH:mm:ss");
                    Map<String, String> timeFormat = DataUtils.getTimeFormat(format);
                    String cDate = timeFormat.get("cDate");
                    String cTime = timeFormat.get("cTime");
                    //todo 开始计算时间 00：00：00
                    str = str.substring(0, str.indexOf(':') - 2) + cTime;
                }
                outputStreamWriter.write(str + "\r\n");

            }


        } catch (Exception e) {
            log.error("文件拷贝失败folderCopy()" + e);

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    @Override
    public List<String> getForecastSectionName() {
        List<String> typeList = new ArrayList<>();
        typeList.add("ZZ");
        typeList.add("ZQ");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.isNotNull("section_name");
        wrapper.in("sttp", typeList);
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        List<String> collect = stStbprpBEntities.parallelStream().map(StStbprpBEntity::getSectionName).collect(Collectors.toList());
        return collect;
    }


}
