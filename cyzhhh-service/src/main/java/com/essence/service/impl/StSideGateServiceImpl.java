package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.utils.HttpUtils;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.dao.entity.alg.StCaseResParamDto;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StSideGateService;
import com.essence.interfaces.api.VGateDataService;
import com.essence.interfaces.dot.PublishDTO;
import com.essence.interfaces.dot.WaterGatePushDTO;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StSideGateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSideGateEtoT;
import com.essence.service.converter.ConverterStSideGateTtoR;
import lombok.SneakyThrows;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 边闸基础表(StSideGate)业务层
 *
 * @author BINX
 * @since 2023-01-17 11:05:20
 */
@Service
public class StSideGateServiceImpl extends BaseApiImpl<StSideGateEsu, StSideGateEsp, StSideGateEsr, StSideGateDto> implements StSideGateService {
    @Resource
    private StCaseResParamDao stCaseResParamDao;
    @Resource
    private DeviceStatusDao deviceStatusDao;
    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private StSideGateDao stSideGateDao;
    @Autowired
    private ConverterStSideGateEtoT converterStSideGateEtoT;
    @Autowired
    private ConverterStSideGateTtoR converterStSideGateTtoR;
    @Resource
    private UnitBaseDao unitBaseDao;
    @Resource
    private StSideGateRelationDao stSideGateRelationDao;
    @Autowired
    private StPumpDataDao stPumpDataDao;
    @Autowired
    private VGateDataDao vGateDataDao;
    @Autowired
    private VGateDataService vGateDataService;
    @Autowired
    private StWaterGateDao stWaterGateDao;
    @Resource
    private RainDayCountDao rainDayCountDao;

    @Resource
    @Lazy
    private StGaConvertDao stGaConvertDao;

    //获取 mqtt 服务的水闸控制开关 在 iot 服务内提供 http 请求调用
    @Value("${control.bz.url}")
    private String controlBzUrl;
    //获取卫星云图
    @Value("${control.url}")
    private String controlUrl;

    public StSideGateServiceImpl(StSideGateDao stSideGateDao, ConverterStSideGateEtoT converterStSideGateEtoT, ConverterStSideGateTtoR converterStSideGateTtoR) {
        super(stSideGateDao, converterStSideGateEtoT, converterStSideGateTtoR);
    }

    @SneakyThrows
    @Override
    public ResponseResult getStationInfoList(StStbprpBEntityDTO stStbprpBEntityDTOR) {

        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        QueryWrapper<StSideGateDto> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getSttp())) {
            wrapper.eq("sttp", stStbprpBEntityDTOR.getSttp());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getStnm())) {
            wrapper.like("stnm", stStbprpBEntityDTOR.getStnm());

        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getArea())) {
            wrapper.like("area", stStbprpBEntityDTOR.getArea());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getRvnm())) {
            wrapper.eq("river_id", stStbprpBEntityDTOR.getRvnm());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getStlc())) {
            wrapper.like("stlc", stStbprpBEntityDTOR.getStlc());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getSttp())) {
            wrapper.eq("sttp", stStbprpBEntityDTOR.getSttp());
        }
        if (CollUtil.isNotEmpty(stStbprpBEntityDTOR.getSttps())) {
            wrapper.in("sttp", stStbprpBEntityDTOR.getSttps());
        }

        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getStcd())) {
            wrapper.eq("stcd", stStbprpBEntityDTOR.getStcd());
        }
        if (stStbprpBEntityDTOR.getId() != null) {
            wrapper.eq("id", stStbprpBEntityDTOR.getId());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getUnitId())) {
            List<UnitBase> unitBases = unitBaseDao.selectList(new QueryWrapper<>());
            Map<String, UnitBase> unitBaseMap = unitBases.parallelStream().collect(Collectors.toMap(UnitBase::getId, Function.identity()));
            UnitBase unitBase = unitBaseMap.get(stStbprpBEntityDTOR.getUnitId());
            wrapper.like("management_unit", unitBase.getUnitName());
        }

        List<StSideGateDto> stStbprpBEntities = stSideGateDao.selectList(wrapper);
        List<StStbprpBEntityDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stStbprpBEntities)) {
            List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(new QueryWrapper<>());
            Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
            if (CollUtil.isNotEmpty(deviceStatusEntities)) {
                stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
            }
            for (StSideGateDto stStbprpBEntity : stStbprpBEntities) {
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                BeanUtil.copyProperties(stStbprpBEntity, stStbprpBEntityDTO);
                DeviceStatusEntity deviceStatusEntity = stcdMap.get(stStbprpBEntity.getStcd());
                if (stStbprpBEntity.getRiverId() != null) {
                    ReaBase reaBase = riverMap.get(stStbprpBEntity.getRiverId().toString());
                    stStbprpBEntityDTO.setRvnm(reaBase.getReaName());
                    stStbprpBEntityDTO.setUnitId(reaBase.getUnitId());
                    stStbprpBEntityDTO.setUnitName(reaBase.getUnitName());
                }
                if (deviceStatusEntity != null) {
                    BeanUtil.copyProperties(deviceStatusEntity, stStbprpBEntityDTO);
                } else {
                    stStbprpBEntityDTO.setOnlineStatus("2");
                }
                list.add(stStbprpBEntityDTO);
            }

        }
        //如果 场站警戒状态不为空 则
        if (CollUtil.isNotEmpty(list)) {
            list = list.stream().sorted(Comparator.comparing(StStbprpBEntityDTO::getSttp)).collect(Collectors.toList());
        }
        if (StringUtil.isNotEmpty(stStbprpBEntityDTOR.getUnitName())) {
            list = list.stream().filter(stStbprpBEntityDTO -> {
                return StrUtil.isNotEmpty(stStbprpBEntityDTO.getUnitName());
            }).filter(stStbprpBEntityDTO -> {
                return stStbprpBEntityDTO.getUnitName().equals(stStbprpBEntityDTOR.getUnitName());
            }).collect(Collectors.toList());
        }
        PageUtil pages = new PageUtil(list, stStbprpBEntityDTOR.getCurrent(), stStbprpBEntityDTOR.getSize(), null, null);
        List<StStbprpBEntityDTO> records = pages.getRecords();

        //增加闸泵站的状态
        String sttp = stStbprpBEntityDTOR.getSttp();
        String date = TimeUtil.getPreDayDate(new Date(), 0);
        DateTime dateTime = DateUtil.parse(date, "yyyy-MM-dd");
        Date start = DateUtil.beginOfDay(dateTime);
        Date end = DateUtil.endOfDay(dateTime);
        //获取系统时间前几小时
        String dateStart = TimeUtil.getPreDayDate002(new Date(), -2);

        //闸的
        if ("DD".equals(sttp)) {
            //获取远传数据最新的数据时间
            String maxTime = stWaterGateDao.selectMaxTime(dateStart);
            Map<String, String> stgaConverMap = stGaConvertDao.selectList(new QueryWrapper<>())
                    .stream().collect(Collectors.toMap(StGaConvertDto::getStcd, StGaConvertDto::getSn));
            for (int i = 0; i < records.size(); i++) {
                StStbprpBEntityDTO stStbprpBEntityDTO = records.get(i);
                String remoteControl = stStbprpBEntityDTO.getRemoteControl();
                if ("1".equals(remoteControl)) {
                    //根据 stcd 获取到对应的sn
                    String sn = stgaConverMap.get(stStbprpBEntityDTO.getStcd());
                    //获取最新数据时间
                    QueryWrapper queryWrapperdd = new QueryWrapper();
                    queryWrapperdd.eq("did", sn);
                    queryWrapperdd.eq("addr", "I0.4");
                    queryWrapperdd.eq("ctime", maxTime);

                    List<StWaterGateDto> list2 = stWaterGateDao.selectList(queryWrapperdd);
                    for (int m = 0; m < list2.size(); m++) {
                        StWaterGateDto StWaterGateDto = list2.get(m);
                        String addr = StWaterGateDto.getAddr();
                        if ("I0.4".equals(addr) && StWaterGateDto.getAddrv().equals("1")) {
                            records.get(i).setRemoteControl2("2");
                            break;
                        }
                    }
                }
            }
            pages.setRecords(records);
        }

        //闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
        //泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
        //泵站的
        if ("DP".equals(sttp)) {
            //获取泵站的最新状态
            for (int i = 0; i < records.size(); i++) {
                StStbprpBEntityDTO stStbprpBEntityDTO = records.get(i);
                String remoteControl = stStbprpBEntityDTO.getRemoteControl();
                if ("1".equals(remoteControl)) {
                    QueryWrapper<StPumpDataDto> queryWrapperdp = new QueryWrapper<>();
                    queryWrapperdp.eq("device_addr", stStbprpBEntityDTO.getStcd());
                    queryWrapperdp.ge("date", start);
                    queryWrapperdp.le("date", end);
                    queryWrapperdp.orderByDesc("date");
                    queryWrapperdp.last("limit 1");
                    List<StPumpDataDto> stPumpDataDtos = stPumpDataDao.selectList(queryWrapperdp);
                    if (stPumpDataDtos.size() == 1) {
                        StPumpDataDto stPumpDataDto = stPumpDataDtos.get(0);
                        //p1_hitch  泵1 故障
                        //p2_hitch  泵2 故障
                        String p1Hitch = stPumpDataDto.getP1Hitch();
                        String p2Hitch = stPumpDataDto.getP2Hitch();
                        if ("1".equals(p1Hitch) || "1".equals(p2Hitch)) {
                            records.get(i).setRemoteControl2("2");
                        }
                    }
                }
            }
            pages.setRecords(records);
        }

        //增加闸泵站的状态
        return ResponseResult.success("获取成功", pages);
    }

    @Override
    public void updateData(StStbprpBEntityDTO e) {
        StSideGateDto stSideGateDto = new StSideGateDto();
        BeanUtil.copyProperties(e, stSideGateDto);
        stSideGateDao.updateById(stSideGateDto);
    }

    /**
     * 查询闸坝负责人信息
     *
     * @param stSideGateEsuParam
     * @return
     */
    @Override
    public Paginator<StSideGateEsrRes> getStSideGateRelation(StSideGateEsuParam stSideGateEsuParam) {

        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }

        PaginatorParam paginatorParam = stSideGateEsuParam.getPaginatorParam();
        int currentPage = paginatorParam.getCurrentPage();
        int pageSize = paginatorParam.getPageSize();

        QueryWrapper<StSideGateDto> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(stSideGateEsuParam.getSttp())) {
            wrapper.eq("sttp", stSideGateEsuParam.getSttp());
        } else {
            //站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
            wrapper.in("sttp", "DD", "BZ", "SB");
        }
        if (StringUtil.isNotEmpty(stSideGateEsuParam.getStnm())) {
            wrapper.like("stnm", stSideGateEsuParam.getStnm());
        }

        if (StringUtil.isNotEmpty(stSideGateEsuParam.getUnitId())) {
            wrapper.eq("unit_id", stSideGateEsuParam.getUnitId());
        }

        List<StSideGateDto> list = stSideGateDao.selectList(wrapper);
        //手动进行分页
        PageUtil<StSideGateDto> pageUtil = new PageUtil(list, currentPage, pageSize, null, null);
        List<StSideGateDto> recordsList = pageUtil.getRecords();
        List<StSideGateEsrRes> stSideGateEsrList = BeanUtil.copyToList(recordsList, StSideGateEsrRes.class);

        for (int i = 0; i < stSideGateEsrList.size(); i++) {
            //去关联表中获取相关的负责人信息
            QueryWrapper<StSideGateRelationDto> wrappe2 = new QueryWrapper<>();
            wrappe2.eq("side_gate_id", stSideGateEsrList.get(i).getId());
            List<StSideGateRelationDto> stSideGateRelationDtos = stSideGateRelationDao.selectList(wrappe2);
            List<StSideGateRelationEsr> stSideGateRelationEsrList = BeanUtil.copyToList(stSideGateRelationDtos, StSideGateRelationEsr.class);
            if (stSideGateRelationEsrList.size() > 0) {
                StSideGateRelationEsr stSideGateRelationEsr = stSideGateRelationEsrList.get(0);
                stSideGateEsrList.get(i).setStSideGateRelationEsr(stSideGateRelationEsr);
            }
            //关联上河流名称
            String riverId = stSideGateEsrList.get(i).getRiverId() == null ? null : stSideGateEsrList.get(i).getRiverId().toString();
            if (!"".equals(riverId) && riverId != null) {
                ReaBase reaBase = riverMap.get(riverId);
                stSideGateEsrList.get(i).setRvnm(reaBase.getReaName());
            }

        }

        Paginator<StSideGateEsrRes> paginator = new Paginator<>();
        paginator.setPageSize(pageUtil.getPageSize());
        paginator.setCurrentPage(pageUtil.getCurrent());
        paginator.setTotalCount(pageUtil.getTotal());
        paginator.setPageCount(pageUtil.getPages());
        paginator.setItems(stSideGateEsrList);

        //获取闸或坝的相关责任人
        return paginator;
    }

    /**
     * 统计水闸泵站的总数、远控、正常以及故障的数量
     *
     * @param sttp
     * @return
     */
    @Override
    public Map<String, Object> getStSideGate(String sttp) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<StSideGateDto> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sttp)) {
            wrapper.eq("sttp", sttp);
        }
        //总数
        List<StSideGateDto> listAll = stSideGateDao.selectList(wrapper);
        int total = listAll.size();
        //可远程控制的
        wrapper.eq("remote_control", 1);
        List<StSideGateDto> listControl = stSideGateDao.selectList(wrapper);
        int control = listControl.size();
        //故障的
        int trouble = 0;
        String date = TimeUtil.getPreDayDate(new Date(), 0);
        DateTime dateTime = DateUtil.parse(date, "yyyy-MM-dd");
        Date start = DateUtil.beginOfDay(dateTime);
        Date end = DateUtil.endOfDay(dateTime);
        //获取系统时间前几小时
        String dateStart = TimeUtil.getPreDayDate002(new Date(), -2);
        //闸的
        if ("DD".equals(sttp)) {
            //获取远传数据最新的数据时间
            String maxTime = stWaterGateDao.selectMaxTime(dateStart);
            //获取泵站的最新状态
            for (int i = 0; i < listControl.size(); i++) {
                StSideGateDto stSideGateDto = listControl.get(i);
                //根据 stcd 获取到对应的sn
                String sn = vGateDataDao.selectSn(stSideGateDto.getStcd());

                if (null != maxTime && !"".equals(maxTime) && null != sn && !"".equals(sn)) {
                    //获取最新数据时间
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("did", sn);
                    queryWrapper.eq("addr", "I0.4");
                    queryWrapper.eq("ctime", maxTime);
                    List<StWaterGateDto> list = stWaterGateDao.selectList(queryWrapper);
                    for (int m = 0; m < list.size(); m++) {
                        StWaterGateDto stWaterGateDto = list.get(m);
                        String addr = stWaterGateDto.getAddr();
                        if ("I0.4".equals(addr) && stWaterGateDto.getAddrv().equals("1")) {
                            trouble = trouble + 1;
                        }
                    }
                }
            }
        }

        //闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
        //泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
        //泵站的
        if ("DP".equals(sttp)) {
            //获取泵站的最新状态
            for (int i = 0; i < listControl.size(); i++) {
                StSideGateDto stSideGateDto = listControl.get(i);

                QueryWrapper<StPumpDataDto> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("device_addr", stSideGateDto.getStcd());
                queryWrapper.ge("date", start);
                queryWrapper.le("date", end);
                queryWrapper.orderByDesc("date");
                queryWrapper.last("limit 1");
                List<StPumpDataDto> stPumpDataDtos = stPumpDataDao.selectList(queryWrapper);
                if (stPumpDataDtos.size() == 1) {
                    StPumpDataDto stPumpDataDto = stPumpDataDtos.get(0);
                    //p1_hitch  泵1 故障
                    //p2_hitch  泵2 故障
                    String p1Hitch = stPumpDataDto.getP1Hitch();
                    String p2Hitch = stPumpDataDto.getP2Hitch();
                    if ("1".equals(p1Hitch) || "1".equals(p2Hitch)) {
                        //故障
                        trouble = trouble + 1;
                    }
                }
            }
        }

        //正常的
        int normal = control - trouble;
        //组装返回参数
        map.put("sttp", sttp);
        map.put("total", total);
        map.put("control", control);
        map.put("trouble", trouble);
        map.put("normal", normal);
        return map;
    }

    /**
     * 查询泵站
     *
     * @return
     */
    @Override
    public List<StSideGateEsr> getPump(String caseId) {
        //设置冬季 11 12 01 月份不运行
        List<String> nuRun = new ArrayList<>();
        nuRun.add("11");
        nuRun.add("12");
        nuRun.add("01");
        Date date = new Date();
        //按照当前时间来给泵站入参
        String format = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String month = format.substring(5, 7);
        String controlValue = "1";
        if (nuRun.contains(month)) {
            controlValue = "2";
        }
        Date xqStartDate = DateUtil.parseDate(format.substring(0, 4) + "-06-01 00:00:00");
        Date xqStartEnd = DateUtil.parseDate(format.substring(0, 4) + "-09-15 23:59:59");

        if (date.getTime() >= xqStartDate.getTime() && date.getTime() <= xqStartEnd.getTime()) {
            // 查询汛期期间是否有雨 有雨的话泵站设置为关闭
            QueryWrapper xqQueryWrapper = new QueryWrapper();
            xqQueryWrapper.eq("date", DateUtil.format(new Date(), "yyyy-MM-dd"));
            List<RainDayCountDto> rainDayCountDtos = rainDayCountDao.selectList(xqQueryWrapper);
            BigDecimal rainAdd = new BigDecimal(0);
            if (CollUtil.isNotEmpty(rainDayCountDtos)) {
                for (RainDayCountDto rainDayCountDto : rainDayCountDtos) {
                    if (rainDayCountDto.getDayCount() == null) {
                        continue;
                    }
                    rainAdd = rainDayCountDto.getDayCount().add(rainAdd);
                }
            }
            if (rainAdd.compareTo(BigDecimal.ZERO) > 0) {
                controlValue = "2";
            }
        }

        QueryWrapper<StSideGateDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sttp", "DP");
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(stSideGateDtos)) {
            stSideGateDtos = stSideGateDtos.parallelStream().filter(stSideGateDto -> {
                return StrUtil.isNotEmpty(stSideGateDto.getSeriaName());
            }).filter(stSideGateDto -> {
                return StrUtil.isNotEmpty(stSideGateDto.getSectionNameDown());
            }).filter(stSideGateDto -> {
                return !stSideGateDto.getSectionNameDown().contains("泵站概化成补水口");
            }).collect(Collectors.toList());
        }
        List<StSideGateEsr> stSideGateEsrs = converterStSideGateTtoR.toList(stSideGateDtos);
        //同时需要将泵站 参数保存一份在数据库中 todo
        String finalControlValue = controlValue;
        stSideGateEsrs = stSideGateEsrs.parallelStream().filter(stSideGateEsr -> {
            stSideGateEsr.setControlValue(finalControlValue);
            stSideGateEsr.setJd(stSideGateEsr.getLgtd());
            stSideGateEsr.setWd(stSideGateEsr.getLttd());
            return true;
        }).collect(Collectors.toList());
        for (StSideGateEsr stSideGateEsr : stSideGateEsrs) {
            StCaseResParamDto stCaseResParamDto = new StCaseResParamDto();
            stCaseResParamDto.setCaseId(caseId);
            stCaseResParamDto.setControlType(1);
            stCaseResParamDto.setControlValue(controlValue);
            stCaseResParamDto.setStnm(stSideGateEsr.getStnm());
            stCaseResParamDto.setLiquidLevel(stSideGateEsr.getLiquidLevel());
            stCaseResParamDto.setSectionName(stSideGateEsr.getSectionName());
            stCaseResParamDto.setSeriaName(stSideGateEsr.getSeriaName());
            stCaseResParamDto.setCreateTime(new Date());
            stCaseResParamDto.setJd(stSideGateEsr.getLgtd());
            stCaseResParamDto.setWd(stSideGateEsr.getLttd());
            stCaseResParamDto.setStopLevel(stSideGateEsr.getStopLevel());
            QueryWrapper<StCaseResParamDto> wrapper = new QueryWrapper();
            wrapper.eq("case_id", caseId);
            wrapper.eq("seria_name", stSideGateEsr.getSeriaName());
            List<StCaseResParamDto> list = stCaseResParamDao.selectList(wrapper);
            if (CollUtil.isEmpty(list)) {
                stCaseResParamDao.insert(stCaseResParamDto);
            }
        }
        return stSideGateEsrs;
    }

    /**
     * 闸坝实时工况
     *
     * @param sttp
     */
    @Override
    public List getStSideGateNow(String sttp, String stnm, String rvnm, String p1Hitch, String p2Hitch, String m00, String m01, String m02) {
        List<StSideGateEsrResCon> list = new ArrayList<>();
        QueryWrapper<StSideGateDto> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(sttp)) {
            wrapper.eq("sttp", sttp);
        }

        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapperRea = new QueryWrapper<>();
        queryWrapperRea.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapperRea);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }

        //可远程控制的
        wrapper.eq("remote_control", 1);
        if (null != stnm && !"".equals(stnm)) {
            wrapper.like("stnm", stnm);
        }
        if (null != rvnm && !"".equals(stnm)) {
            wrapper.like("river_id", stnm);
        }

        List<StSideGateDto> listControl = stSideGateDao.selectList(wrapper);

        //闸的
        if ("DD".equals(sttp)) {
            //获取泵站的最新状态
            for (int i = 0; i < listControl.size(); i++) {
                StSideGateEsrResCon stSideGateEsrResCon = new StSideGateEsrResCon();
                StSideGateDto stSideGateDto = listControl.get(i);
                //根据 stcd 获取到对应的sn
                String sn = vGateDataDao.selectSn(stSideGateDto.getStcd());
                //获取最新数据时间
                String ctime = vGateDataDao.selectCtime(sn);
                VGateDataDto vGateDataDtoRes = (VGateDataDto) vGateDataService.selectvGateData(stSideGateDto.getStcd());
                //组装返回数据
                stSideGateEsrResCon.setStnm(stSideGateDto.getStnm());
                stSideGateEsrResCon.setManagementUnit(stSideGateDto.getManagementUnit());
                stSideGateEsrResCon.setCtime(vGateDataDtoRes.getCtime());
                stSideGateEsrResCon.setZmlb("1#闸门");
                stSideGateEsrResCon.setM00(vGateDataDtoRes.getM00());
                stSideGateEsrResCon.setM01(vGateDataDtoRes.getM01());
                stSideGateEsrResCon.setM02(vGateDataDtoRes.getM02());
                stSideGateEsrResCon.setVD200(vGateDataDtoRes.getVD200());
                stSideGateEsrResCon.setVD4(vGateDataDtoRes.getVD4());
                stSideGateEsrResCon.setVD8(vGateDataDtoRes.getVD8());
                stSideGateEsrResCon.setVD12(vGateDataDtoRes.getVD12());
                stSideGateEsrResCon.setVD16(vGateDataDtoRes.getVD16());
                stSideGateEsrResCon.setVD20(vGateDataDtoRes.getVD20());
                stSideGateEsrResCon.setVD24(vGateDataDtoRes.getVD24());
                stSideGateEsrResCon.setVD212(vGateDataDtoRes.getVD212());
                stSideGateEsrResCon.setVD220(vGateDataDtoRes.getVD220());
                if (stSideGateDto.getRiverId() != null) {
                    ReaBase reaBase = riverMap.get(stSideGateDto.getRiverId().toString());
                    stSideGateEsrResCon.setReaName(reaBase.getReaName());
                }
                list.add(stSideGateEsrResCon);
            }

            //增加故障筛选
            if (m00 != null && !"".equals(m00)) {
                list = list.parallelStream().filter(StSideGateEsrResCon -> {
                            return StrUtil.isNotEmpty(StSideGateEsrResCon.getM00());
                        })
                        .filter(StSideGateEsrResCon -> {
                            return StSideGateEsrResCon.getM00().equals(m00);
                        }).collect(Collectors.toList());
            }

            if (m01 != null && !"".equals(m01)) {
                list = list.parallelStream().filter(StSideGateEsrResCon -> {
                            return StrUtil.isNotEmpty(StSideGateEsrResCon.getM01());
                        })
                        .filter(StSideGateEsrResCon -> {
                            return StSideGateEsrResCon.getM01().equals(m01);
                        }).collect(Collectors.toList());
            }

            if (m02 != null && !"".equals(m02)) {
                list = list.parallelStream().filter(StSideGateEsrResCon -> {
                            return StrUtil.isNotEmpty(StSideGateEsrResCon.getM02());
                        })
                        .filter(StSideGateEsrResCon -> {
                            return StSideGateEsrResCon.getM02().equals(m02);
                        }).collect(Collectors.toList());
            }
            //增加故障筛选
            return list;
        }

        //闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
        //泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
        //泵站的
        if ("DP".equals(sttp)) {
            //获取泵站的最新状态
            for (int i = 0; i < listControl.size(); i++) {
                StSideGateEsrResCon stSideGateEsrResCon = new StSideGateEsrResCon();
                Map<String, Object> map = new HashMap<>();
                StSideGateDto stSideGateDto = listControl.get(i);
                QueryWrapper<StPumpDataDto> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("device_addr", stSideGateDto.getStcd());
                queryWrapper.orderByDesc("date");
                queryWrapper.last("limit 1");
                List<StPumpDataDto> stPumpDataDtos = stPumpDataDao.selectList(queryWrapper);
                if (stPumpDataDtos.size() == 1) {
                    StPumpDataDto stPumpDataDto = stPumpDataDtos.get(0);
                    stSideGateEsrResCon.setStnm(stSideGateDto.getStnm());
                    stSideGateEsrResCon.setManagementUnit(stSideGateDto.getManagementUnit());
                    if (null != stPumpDataDto.getDate() && !"".equals(stPumpDataDto.getDate())) {
                        stSideGateEsrResCon.setCtime(TimeUtil.dateToStringNormal(stPumpDataDto.getDate()));
                    }
                    stSideGateEsrResCon.setAVoltage(stPumpDataDto.getAVoltage());
                    stSideGateEsrResCon.setBVoltage(stPumpDataDto.getBVoltage());
                    stSideGateEsrResCon.setCVoltage(stPumpDataDto.getCVoltage());
                    stSideGateEsrResCon.setAElectric(stPumpDataDto.getAElectric());
                    stSideGateEsrResCon.setBElectric(stPumpDataDto.getBElectric());
                    stSideGateEsrResCon.setCElectric(stPumpDataDto.getCElectric());
                    stSideGateEsrResCon.setElectric(stPumpDataDto.getElectric());
                    stSideGateEsrResCon.setFlowRate(stPumpDataDto.getFlowRate());
                    stSideGateEsrResCon.setPressure(stPumpDataDto.getPressure());
                    stSideGateEsrResCon.setYPosition(stPumpDataDto.getYPosition());
                    stSideGateEsrResCon.setP1CountTime(stPumpDataDto.getP1CountTime());
                    stSideGateEsrResCon.setP2CountTime(stPumpDataDto.getP1CountTime());
                    stSideGateEsrResCon.setLiquidHigh(stPumpDataDto.getLiquidHigh());
                    stSideGateEsrResCon.setLiquidLow(stPumpDataDto.getLiquidLow());
                    stSideGateEsrResCon.setP1Run(stPumpDataDto.getP1Run());
                    stSideGateEsrResCon.setP1Hitch(stPumpDataDto.getP1Hitch());
                    stSideGateEsrResCon.setP2Run(stPumpDataDto.getP2Run());
                    stSideGateEsrResCon.setP2Hitch(stPumpDataDto.getP2Hitch());

                    if (stSideGateDto.getRiverId() != null) {
                        ReaBase reaBase = riverMap.get(stSideGateDto.getRiverId().toString());
                        stSideGateEsrResCon.setReaName(reaBase.getReaName());

                    }

                    list.add(stSideGateEsrResCon);
                    //增加故障筛选
                    if (p1Hitch != null && !"".equals(p1Hitch)) {
                        list = list.parallelStream().filter(StSideGateEsrResCon -> {
                                    return StrUtil.isNotEmpty(StSideGateEsrResCon.getP1Hitch());
                                })
                                .filter(StSideGateEsrResCon -> {
                                    return StSideGateEsrResCon.getP1Hitch().equals(p1Hitch);
                                }).collect(Collectors.toList());
                    }
                    if (p2Hitch != null && !"".equals(p2Hitch)) {
                        list = list.parallelStream().filter(StSideGateEsrResCon -> {
                                    return StrUtil.isNotEmpty(StSideGateEsrResCon.getP2Hitch());
                                })
                                .filter(StSideGateEsrResCon -> {
                                    return StSideGateEsrResCon.getP2Hitch().equals(p2Hitch);
                                }).collect(Collectors.toList());
                    }
                    //增加故障筛选
                }
            }
            return list;
        }
        return new ArrayList();
    }

    /**
     * @param deviceAddr
     * @param pNum
     * @param status     1 开 0 关闭
     */
    @Override
    public ResponseResult dealRemoteSoltPunp(String deviceAddr, Integer pNum, Integer status, JSONObject paramJson) {
        //通过deviceAddr 去查询 泵站表中的 stcd 去关联
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stcd", deviceAddr);
        StSideGateDto stSideGateDto = stSideGateDao.selectOne(queryWrapper);
        if (stSideGateDto == null) {
            return ResponseResult.success("数据台账表中没有该泵站的站点编码", null);
        }
        //并且去查看几标段 solt 1-6   1-5 标段使用的是mqtt 协议  6标段采用的是 modbus 协议
        String lot = stSideGateDto.getLot();
        if (StrUtil.isEmpty(lot)) {

            return ResponseResult.success("请在数据台账中配置标段Lot", null);
        }
        switch (lot) {
            case "1":
                //标段 1
//                deal1Lot(num,  status,  s, waterGateControlDevice);
                break;
            case "2":
                //标段 2
                break;
            case "3":
                //标段 3
                break;
            case "4":
//                deal4Lot(num,  status,  s, waterGateControlDevice);
                //标段 4
                break;
            case "5":
                //标段 5
                //控制下发的指令

                break;
            case "6":
                //标段 6
                //控制下发的指令
                String body = HttpRequest.post(controlUrl).form(paramJson).timeout(900000).execute().body();
                break;
        }
        return ResponseResult.success("指令下发成功", null);
    }

    /**
     * fix 2023-12-06 单独处理一个一标段的翻板闸
     * 处理 1 标段
     *
     * @param num
     * @param status                 1 开 0 关闭
     * @param sn
     * @param waterGateControlDevice
     */
    public ResponseResult deal1Lot(int num, Integer status, String sn, WaterGateControlDevice waterGateControlDevice) {

        //控制下发的指令
        WaterGatePushDTO waterGatePushDTO = new WaterGatePushDTO();
        waterGatePushDTO.setDid(sn);
        waterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<PublishDTO> downCommend = getDownCommend1(num, status, waterGateControlDevice.getPid());
        waterGatePushDTO.setContent(downCommend);

//        WaterGatePushDTO reWaterGatePushDTO = new WaterGatePushDTO();
//        reWaterGatePushDTO.setDid(sn);
//        reWaterGatePushDTO.setUtime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        List<PublishDTO> revertDownCommend = getRevertDownCommend1(num, status, waterGateControlDevice.getPid());
//        reWaterGatePushDTO.setContent(revertDownCommend);
        try {
            HttpUtils.doPost(controlBzUrl, JSONObject.toJSONString(waterGatePushDTO));
//            //复位指令
//            HttpUtils.doPost(controlBzUrl, JSONObject.toJSONString(reWaterGatePushDTO));
        } catch (Exception e) {
            return ResponseResult.error("远程请求调用失败  网络异常", null);
        }

        return ResponseResult.success("发送成功", null);
    }

    /**
     * @param num    第几个泵站
     * @param status 1 开 0 关闭
     * @param pid
     * @return
     */
    private List<PublishDTO> getDownCommend1(int num, Integer status, String pid) {

        List<PublishDTO> res = new ArrayList<>();

        if (num == 1) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M7.1");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 0) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M7.1");
                publishDTO.setAddrv("0");

                res.add(publishDTO);

            }

        }
        if (num == 2) {
            if (status == 1) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M7.3");
                publishDTO.setAddrv("1");
                res.add(publishDTO);
            }
            if (status == 0) {
                PublishDTO publishDTO = new PublishDTO();
                publishDTO.setPid(pid);
                publishDTO.setAddr("M7.3");
                publishDTO.setAddrv("0");
                res.add(publishDTO);
            }

        }
//        if (num == 3) {
//            if (status == 1) {
//                PublishDTO publishDTO = new PublishDTO();
//                publishDTO.setPid(pid);
//                publishDTO.setAddr("M1.4");
//                publishDTO.setAddrv("1");
//                res.add(publishDTO);
//            }
//            if (status == 0) {
//                PublishDTO publishDTO = new PublishDTO();
//                publishDTO.setPid(pid);
//                publishDTO.setAddr("M1.5");
//                publishDTO.setAddrv("1");
//                res.add(publishDTO);
//            }
//
//        }

        return res;
    }

    private List<PublishDTO> getRevertDownCommend1(int num, Integer status, String pid) {
        return null;
    }

}
