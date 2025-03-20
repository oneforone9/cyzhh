package com.essence.job.forecast;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForecastService;
import com.essence.interfaces.model.StForecastEsr;
import com.essence.interfaces.model.StForecastEsu;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 闸坝泵监测定时任务
 *
 * @author majunjie
 * @since 2023/04/19 16:07
 */
@Component
@Log4j2
public class SideTask {

    @Autowired
    private StForecastService stForecastService;
    @Autowired
    private StSideGateDao stSideGateDao;
    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private StSideGateRelationDao stSideGateRelationDao;
    @Autowired
    private StPumpDataDao stPumpDataDao;
    @Autowired
    private VGateDataDao vGateDataDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Transactional
    @Scheduled(cron = "0 0 0/5 * * ?")
    public void syncCreateOrderTask() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行闸坝泵监测任务,结束.", BACK_JOB);
            return;
        }
//        try {
        log.info("闸坝泵监测开始....");
        //开始时间
        //结束时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = new Date();
        Date startTime = DateUtil.offsetHour(endTime, -2);
        String startTm = simpleDateFormat.format(startTime);
        String endTm = simpleDateFormat.format(endTime);

        //1.获取水闸数据
        List<VGateDataDto> stWaterGateDtos = Optional.ofNullable(vGateDataDao.selectList(new QueryWrapper<VGateDataDto>().lambda().ge(VGateDataDto::getCtime, startTm).le(VGateDataDto::getCtime, endTm))).orElse(Lists.newArrayList());
        //2.获取泵站数据
        List<StPumpDataDto> stPumpDataDtos = Optional.ofNullable(stPumpDataDao.selectList(new QueryWrapper<StPumpDataDto>().lambda().ge(StPumpDataDto::getDate, startTime).le(StPumpDataDto::getDate, endTime))).orElse(Lists.newArrayList());
        //todo  3坝数据暂无
        //4.查询数据台账
        //闸坝泵台账数据  DD闸SB坝DP泵站  DP-泵站 DD-闸  SB-水坝
        List<StSideGateDto> stSideGateDtos = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "DD").isNotNull(StSideGateDto::getStcd))).orElse(Lists.newArrayList());
        List<StSideGateDto> stSideGateDtos2 = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "DP").isNotNull(StSideGateDto::getStcd))).orElse(Lists.newArrayList());
        List<StSideGateDto> stSideGateDtos3 = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().eq(StSideGateDto::getSttp, "SB").isNotNull(StSideGateDto::getStcd))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stSideGateDtos2)) {
            stSideGateDtos.addAll(stSideGateDtos2);
        }
        if (!CollectionUtils.isEmpty(stSideGateDtos3)) {
            stSideGateDtos.addAll(stSideGateDtos3);
        }
        if (!CollectionUtils.isEmpty(stSideGateDtos)) {
            stSideGateDtos = stSideGateDtos.stream().filter(x -> StringUtil.isNotBlank(x.getStcd())).collect(Collectors.toList());
        }
        //5.查询河道数据
        List<ReaBase> reaBaseList = Optional.ofNullable(reaBaseDao.selectList(new QueryWrapper<ReaBase>().lambda().le(ReaBase::getId, 31))).orElse(Lists.newArrayList());
        //6.闸坝负责人关联表
        List<StSideGateRelationDto> stSideGateRelationDtos = stSideGateRelationDao.selectList(new QueryWrapper<StSideGateRelationDto>());
        List<StForecastEsu> stForecastEsu = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stSideGateDtos)) {
            Map<String, List<StSideGateDto>> map = stSideGateDtos.stream().collect(Collectors.groupingBy(StSideGateDto::getSttp));
            for (Map.Entry<String, List<StSideGateDto>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<StSideGateDto> stbprpBEntityList = entry.getValue();
                if (null != stbprpBEntityList) {
                    if (key.equals("DD")) {//DD闸
                        //预警查询 设备类型1闸坝2泵站3水位站4流量站
                        List<StForecastEsr> stForecastEsrList = stForecastService.selectStForecastList("1");
                        List<StForecastEsu> stForecastEsuList = selectSideGateDD(stSideGateRelationDtos, stWaterGateDtos, stbprpBEntityList, stForecastEsrList, "1", reaBaseList);
                        if (!CollectionUtils.isEmpty(stForecastEsuList)) {
                            stForecastEsu.addAll(stForecastEsuList);
                        }
                    } else {   //预警查询 设备类型1闸坝2泵站3水位站4流量站
                        if (key.equals("SB")) {//SB坝
                           /* //
                            List<StForecastEsr> stForecastEsrList1 = stForecastService.selectStForecastList("1");
                            List<StForecastEsu> stForecastEsuList = selectSideGate(stbprpBEntityList, stForecastEsrList1, "1", reaBaseList);
                            if (!CollectionUtils.isEmpty(stForecastEsuList)) {
                                stForecastEsu.addAll(stForecastEsuList);
                            }*/
                        } else {
                            // DP泵站
                            List<StForecastEsr> stForecastEsrList1 = stForecastService.selectStForecastList("2");
                            List<StForecastEsu> stForecastEsuList = selectSideGateDP(stPumpDataDtos, stbprpBEntityList, stForecastEsrList1, "2", reaBaseList);
                            if (!CollectionUtils.isEmpty(stForecastEsuList)) {
                                stForecastEsu.addAll(stForecastEsuList);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(stForecastEsu)) {
                saveStForecast(stForecastEsu);
            }
        }
        log.info("闸坝泵监测监测结束");
//        } catch (Exception e) {
//            log.info("闸坝泵监测监测异常" + e);
//        }
    }

    //泵站数据监测
    public List<StForecastEsu> selectSideGateDP(List<StPumpDataDto> stPumpDataDtos, List<StSideGateDto> stbprpBEntityList, List<StForecastEsr> stForecastEsrList, String type, List<ReaBase> reaBaseList) {
        List<StForecastEsu> stForecastEsu = new ArrayList<>();
        for (StSideGateDto stSideGateDto : stbprpBEntityList) {
            if (CollectionUtils.isEmpty(stPumpDataDtos)) {
                if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                    List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stSideGateDto.getStcd()) && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (CollectionUtils.isEmpty(stForecastEsrs)) {
                        stForecastEsu.add(onLineSide(type, "17", stSideGateDto, reaBaseList));
                    }
                } else {
                    stForecastEsu.add(onLineSide(type, "17", stSideGateDto, reaBaseList));
                }
            } else {
                List<StPumpDataDto> stPumpDataDtoList = Optional.ofNullable(stPumpDataDtos.stream().filter(x -> x.getDeviceAddr().equals(stSideGateDto.getStcd())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(stPumpDataDtoList)) {
                    stPumpDataDtoList = stPumpDataDtoList.stream().sorted(Comparator.comparing(StPumpDataDto::getDate, Comparator.nullsFirst(Date::compareTo)).reversed()).collect(Collectors.toList());
                    StPumpDataDto stPumpDataDto = stPumpDataDtoList.get(0);
                    //去判断数据内容
                    if (StringUtil.isNotBlank(stPumpDataDto.getP1Hitch()) && stPumpDataDto.getP1Hitch().equals("1")) {
                        if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                            List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stSideGateDto.getStcd()) && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            if (CollectionUtils.isEmpty(stForecastEsrs)) {
                                stForecastEsu.add(onLineSide(type, "11", stSideGateDto, reaBaseList));
                            }
                        } else {
                            stForecastEsu.add(onLineSide(type, "11", stSideGateDto, reaBaseList));
                        }
                    } else {
                        if (StringUtil.isNotBlank(stPumpDataDto.getP2Hitch()) && stPumpDataDto.getP2Hitch().equals("1")) {
                            if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                                List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stSideGateDto.getStcd()) && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                                if (CollectionUtils.isEmpty(stForecastEsrs)) {
                                    stForecastEsu.add(onLineSide(type, "11", stSideGateDto, reaBaseList));
                                }
                            } else {
                                stForecastEsu.add(onLineSide(type, "11", stSideGateDto, reaBaseList));
                            }

                        }
                    }
                } else {
                    if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                        List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stSideGateDto.getStcd()) && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        if (CollectionUtils.isEmpty(stForecastEsrs)) {
                            stForecastEsu.add(onLineSide(type, "17", stSideGateDto, reaBaseList));
                        }
                    } else {
                        stForecastEsu.add(onLineSide(type, "17", stSideGateDto, reaBaseList));
                    }
                }
            }
        }
        return stForecastEsu;
    }

    //闸坝数据监测
    public List<StForecastEsu> selectSideGateDD(List<StSideGateRelationDto> stSideGateRelationDtos, List<VGateDataDto> stWaterGateDtos, List<StSideGateDto> stbprpBEntityList, List<StForecastEsr> stForecastEsrList, String type, List<ReaBase> reaBaseList) {
        List<StForecastEsu> stForecastEsu = new ArrayList<>();
        for (StSideGateDto stSideGateDto : stbprpBEntityList) {
            if (CollectionUtils.isEmpty(stWaterGateDtos)) {
                if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                    List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> null != x.getStcd() && x.getStcd().equals(stSideGateDto.getStcd()) && null != x.getForecastType() && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (CollectionUtils.isEmpty(stForecastEsrs)) {
                        stForecastEsu.add(onLineSides(type, "17", stSideGateDto, reaBaseList, stSideGateRelationDtos));
                    }
                } else {
                    stForecastEsu.add(onLineSides(type, "17", stSideGateDto, reaBaseList, stSideGateRelationDtos));
                }
            } else {
                List<VGateDataDto> stPumpDataDtoList = Optional.ofNullable(stWaterGateDtos.stream().filter(x -> null != x.getStcd() && x.getStcd().equals(stSideGateDto.getStcd())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(stPumpDataDtoList)) {
                    for (VGateDataDto vGateDataDto : stPumpDataDtoList) {
                        if (vGateDataDto.getAddr().equals("I0.4") && vGateDataDto.getAddrv().equals("1")) {
                            stForecastEsu.add(onLineSides(type, "3", stSideGateDto, reaBaseList, stSideGateRelationDtos));
                        }
                    }
                } else {
                    if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                        List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> null != x.getStcd() && x.getStcd().equals(stSideGateDto.getStcd()) && null != x.getForecastType() && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                        if (CollectionUtils.isEmpty(stForecastEsrs)) {
                            stForecastEsu.add(onLineSides(type, "17", stSideGateDto, reaBaseList, stSideGateRelationDtos));
                        }
                    } else {
                        stForecastEsu.add(onLineSides(type, "17", stSideGateDto, reaBaseList, stSideGateRelationDtos));
                    }
                }
            }
        }
        return stForecastEsu;
    }

    //异常数据填充
    public StForecastEsu onLineSides(String type, String forecastType, StSideGateDto stSideGateDto, List<ReaBase> reaBaseList, List<StSideGateRelationDto> stSideGateRelationDtos) {
        StForecastEsu stForecastEsu1 = new StForecastEsu();
        stForecastEsu1.setForecastType(forecastType);
        stForecastEsu1.setStcd(stSideGateDto.getStcd());
        stForecastEsu1.setStnm(stSideGateDto.getStnm());
        if (!CollectionUtils.isEmpty(reaBaseList)) {
            if (null != stSideGateDto.getRiverId()) {
                List<ReaBase> reaBases = Optional.ofNullable(reaBaseList.stream().filter(x -> x.getId().equals(stSideGateDto.getRiverId().toString())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(reaBases)) {
                    ReaBase reaBase = reaBases.get(0);
                    stForecastEsu1.setRvnm(reaBase.getReaName());
                    stForecastEsu1.setRiver(reaBase.getId());
                }
            }
        }
        if (!CollectionUtils.isEmpty(stSideGateRelationDtos)) {
            List<StSideGateRelationDto> sideGateRelationDtoList = Optional.ofNullable(stSideGateRelationDtos.stream().filter(x -> x.getSideGateId().toString().equals(stSideGateDto.getId().toString())).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(sideGateRelationDtoList)) {
                StSideGateRelationDto stSideGateRelationDto = sideGateRelationDtoList.get(0);
                stForecastEsu1.setXcfzr(stSideGateRelationDto.getXcfzr());
                stForecastEsu1.setXcfzrPhone(stSideGateRelationDto.getXcfzrPhone());
                stForecastEsu1.setXcfzrUserId(stSideGateRelationDto.getXcfzrUserId());
            }
        }
        stForecastEsu1.setSourceType("系统");
        stForecastEsu1.setPoliceTime(new Date());
        stForecastEsu1.setForecastState(0);
        stForecastEsu1.setDeviceType(type);
        stForecastEsu1.setLgtd(stSideGateDto.getLgtd());
        stForecastEsu1.setLttd(stSideGateDto.getLttd());
        stForecastEsu1.setState(0);
        stForecastEsu1.setReception(0);
        return stForecastEsu1;
    }

    //异常数据填充
    public StForecastEsu onLineSide(String type, String forecastType, StSideGateDto stSideGateDto, List<ReaBase> reaBaseList) {
        StForecastEsu stForecastEsu1 = new StForecastEsu();
        stForecastEsu1.setForecastType(forecastType);
        stForecastEsu1.setStcd(stSideGateDto.getStcd());
        stForecastEsu1.setStnm(stSideGateDto.getStnm());
        if (!CollectionUtils.isEmpty(reaBaseList)) {
            if (null != stSideGateDto.getRiverId()) {
                List<ReaBase> reaBases = Optional.ofNullable(reaBaseList.stream().filter(x -> x.getId().equals(stSideGateDto.getRiverId().toString())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(reaBases)) {
                    ReaBase reaBase = reaBases.get(0);
                    stForecastEsu1.setRvnm(reaBase.getReaName());
                    stForecastEsu1.setRiver(reaBase.getId());
                }
            }
        }
        stForecastEsu1.setSourceType("系统");
        stForecastEsu1.setPoliceTime(new Date());
        stForecastEsu1.setForecastState(0);
        stForecastEsu1.setDeviceType(type);
        stForecastEsu1.setLgtd(stSideGateDto.getLgtd());
        stForecastEsu1.setLttd(stSideGateDto.getLttd());
        stForecastEsu1.setState(0);
        stForecastEsu1.setReception(0);
        return stForecastEsu1;
    }

    public void saveStForecast(List<StForecastEsu> stForecastEsu) {
        stForecastService.addStForecasts(stForecastEsu);
    }
}
