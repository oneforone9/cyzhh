package com.essence.job.forecast;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.DeviceStatusDao;
import com.essence.dao.ReaBaseDao;
import com.essence.dao.StStbprpBDao;
import com.essence.dao.entity.DeviceStatusEntity;
import com.essence.dao.entity.ReaBase;
import com.essence.dao.entity.StStbprpBEntity;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_JOB;

/**
 * 水位流量监测定时任务
 *
 * @author majunjie
 * @since 2023/04/19 16:07
 */
@Component
@Log4j2
public class StbprbTask {

    @Autowired
    private StForecastService stForecastService;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private DeviceStatusDao deviceStatusDao;
    @Autowired
    private ReaBaseDao reaBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Transactional
    @Scheduled(cron = "0 30 3 * * ?")
    public void syncCreateOrderTask() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_JOB.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行水位流量监测任务,结束.", BACK_JOB);
            return;
        }
        try {
            log.info("水位流量监测开始....");
            //1.查询雨量站台账
            //水位站-流量站台账数据  ZQ流量ZZ水位
            List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().eq(StStbprpBEntity::getSttp, "ZQ").or().eq(StStbprpBEntity::getSttp, "ZZ"))).orElse(Lists.newArrayList());
            //2.查询水位流量站报警
            List<DeviceStatusEntity> deviceStatusEntityList = Optional.ofNullable(deviceStatusDao.selectList(new QueryWrapper<DeviceStatusEntity>())).orElse(Lists.newArrayList());
            List<ReaBase> reaBaseList = Optional.ofNullable(reaBaseDao.selectList(new QueryWrapper<ReaBase>().lambda().le(ReaBase::getId, 31))).orElse(Lists.newArrayList());
            List<StForecastEsu> stForecastEsu = new ArrayList<>();
            if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
                Map<String, List<StStbprpBEntity>> map = stStbprpBEntityList.stream().collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
                for (Map.Entry<String, List<StStbprpBEntity>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    List<StStbprpBEntity> stbprpBEntityList = entry.getValue();
                    if (key.equals("ZZ")) {
                        //预警查询 设备类型1闸坝2泵站3水位站4流量站
                        List<StForecastEsr> stForecastEsrList = stForecastService.selectStForecastList("3");
                        //水位
                        List<StForecastEsu> stForecastEsuList = selectStbprb(deviceStatusEntityList, stbprpBEntityList, stForecastEsrList, "3", reaBaseList);
                        if (!CollectionUtils.isEmpty(stForecastEsuList)) {
                            stForecastEsu.addAll(stForecastEsuList);
                        }
                    } else {   //预警查询 设备类型1闸坝2泵站3水位站4流量站
                        // -流量
                        List<StForecastEsr> stForecastEsrList1 = stForecastService.selectStForecastList("4");
                        List<StForecastEsu> stForecastEsuList = selectStbprb(deviceStatusEntityList, stbprpBEntityList, stForecastEsrList1, "4", reaBaseList);
                        if (!CollectionUtils.isEmpty(stForecastEsuList)) {
                            stForecastEsu.addAll(stForecastEsuList);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(stForecastEsu)) {
                    saveStForecast(stForecastEsu);
                }
            }
            log.info("水位流量监测结束");
        } catch (Exception e) {
            log.info("水位流量监测异常" + e);
        }
    }


    //超警戒 流速判断 在线离线判断  //预警查询 设备类型1闸坝2泵站3水位站4流量站
    public List<StForecastEsu> selectStbprb(List<DeviceStatusEntity> deviceStatusEntityList, List<StStbprpBEntity> stbprpBEntityList, List<StForecastEsr> stForecastEsrList, String type, List<ReaBase> reaBaseList) {
        List<StForecastEsu> stForecastEsu = new ArrayList<>();
        for (StStbprpBEntity stStbprpBEntity : stbprpBEntityList) {
            if (!CollectionUtils.isEmpty(deviceStatusEntityList)) {
                //判断
                List<DeviceStatusEntity> deviceStatusEntities = deviceStatusEntityList.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(deviceStatusEntities)) {
                    DeviceStatusEntity deviceStatusEntity = deviceStatusEntities.get(0);
                    //离线
                    if (deviceStatusEntity.getOnlineStatus().equals("2")) {
                        if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                            List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd()) && x.getForecastType().equals("17")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            if (CollectionUtils.isEmpty(stForecastEsrs)) {
                                stForecastEsu.add(onLine(type, "17", stStbprpBEntity, reaBaseList));
                            }
                        } else {
                            stForecastEsu.add(onLine(type, "17", stStbprpBEntity, reaBaseList));
                        }
                    } else {
                        if (type.equals("3")) {
                            //超警戒
                            if (deviceStatusEntity.getWarningStatus().toString().equals("2")) {
                                if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                                    List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd()) && x.getForecastType().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                                    if (CollectionUtils.isEmpty(stForecastEsrs)) {
                                        stForecastEsu.add(onLine(type, "1", stStbprpBEntity, reaBaseList));
                                    }
                                } else {
                                    stForecastEsu.add(onLine(type, "1", stStbprpBEntity, reaBaseList));
                                }
                            }
                        }/* else {
                            //流速底
                            if (deviceStatusEntity.getWarningStatus().toString().equals("3")) {
                                if (!CollectionUtils.isEmpty(stForecastEsrList)) {
                                    List<StForecastEsr> stForecastEsrs = Optional.ofNullable(stForecastEsrList.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd()) && x.getForecastType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
                                    if (CollectionUtils.isEmpty(stForecastEsrs)) {
                                        stForecastEsu.add(onLine(type, "2", stStbprpBEntity, reaBaseList));
                                    }
                                } else {
                                    stForecastEsu.add(onLine(type, "2", stStbprpBEntity, reaBaseList));
                                }
                            }
                        }*/
                    }
                }
            }
        }
        return stForecastEsu;
    }

    //异常数据填充
    public StForecastEsu onLine(String type, String forecastType, StStbprpBEntity stStbprpBEntity, List<ReaBase> reaBaseList) {
        StForecastEsu stForecastEsu1 = new StForecastEsu();
        stForecastEsu1.setForecastType(forecastType);
        stForecastEsu1.setStcd(stStbprpBEntity.getStcd());
        stForecastEsu1.setStnm(stStbprpBEntity.getStnm());
        if (!CollectionUtils.isEmpty(reaBaseList)) {
            List<ReaBase> reaBases = Optional.ofNullable(reaBaseList.stream().filter(x -> x.getId().equals(stStbprpBEntity.getRvnm())).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(reaBases)) {
                ReaBase reaBase = reaBases.get(0);
                stForecastEsu1.setRvnm(reaBase.getReaName());
            }
        }
        stForecastEsu1.setSourceType("系统");
        stForecastEsu1.setPoliceTime(new Date());
        stForecastEsu1.setForecastState(0);
        stForecastEsu1.setDeviceType(type);
        stForecastEsu1.setRiver(stStbprpBEntity.getRvnm());

        stForecastEsu1.setLgtd(stStbprpBEntity.getLgtd().doubleValue());
        stForecastEsu1.setLttd(stStbprpBEntity.getLttd().doubleValue());
        stForecastEsu1.setState(0);
        stForecastEsu1.setReception(0);
        return stForecastEsu1;
    }

    public void saveStForecast(List<StForecastEsu> stForecastEsu) {
        stForecastService.addStForecasts(stForecastEsu);
    }


}
