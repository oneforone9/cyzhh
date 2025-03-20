package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.GisUtils;
import com.essence.common.utils.UuidUtil;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StForecastDataService;
import com.essence.interfaces.api.StForecastService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StForecastEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterFileBaseTtoR;
import com.essence.service.converter.ConverterStForecastEtoT;
import com.essence.service.converter.ConverterStForecastTtoR;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 预警报表(StForecast)业务层
 *
 * @author majunjie
 * @since 2023-04-17 19:38:16
 */
@Service
public class StForecastServiceImpl extends BaseApiImpl<StForecastEsu, StForecastEsp, StForecastEsr, StForecastDto> implements StForecastService {


    @Autowired
    private StForecastDao stForecastDao;
    @Autowired
    private StForecastDataService stForecastDataService;
    @Autowired
    private ConverterStForecastEtoT converterStForecastEtoT;
    @Autowired
    private ConverterStForecastTtoR converterStForecastTtoR;
    @Autowired
    private StCompanyBaseRelationDao stCompanyBaseRelationDao;
    @Autowired
    private StSideGateRelationDao stSideGateRelationDao;
    @Autowired
    private StCompanyBaseDao stCompanyBaseDao;
    @Autowired
    private StSideGateDao stSideGateDao;
    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Autowired
    private FileBaseDao fileBaseDao;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private ConverterFileBaseTtoR converterFileBaseTtoR;

    public StForecastServiceImpl(StForecastDao stForecastDao, ConverterStForecastEtoT converterStForecastEtoT, ConverterStForecastTtoR converterStForecastTtoR) {
        super(stForecastDao, converterStForecastEtoT, converterStForecastTtoR);
    }

    @Override
    public StForecastEsr addStForecast(StForecastEsu stForecastEsu) {
        //插入预警信息
        StForecastDto stForecastDto = converterStForecastEtoT.toBean(stForecastEsu);
        stForecastDto.setForecastId(UuidUtil.get32UUIDStr());
        stForecastDto.setForecastState(0);
        stForecastDao.insert(stForecastDto);
        //插入预警报表记录
        StForecastEsr stForecastEsr = converterStForecastTtoR.toBean(stForecastDto);
        stForecastDataService.addStForecastData(stForecastEsr);
        return stForecastEsr;
    }

    @Override
    public void addStForecasts(List<StForecastEsu> stForecastEsu) {
        List<StForecastDto> stForecastDtoList=new ArrayList<>();
        if (!CollectionUtils.isEmpty(stForecastEsu)){
            for (StForecastEsu forecastEsu : stForecastEsu) {
                StForecastDto stForecastDto = converterStForecastEtoT.toBean(forecastEsu);
                stForecastDto.setForecastId(UuidUtil.get32UUIDStr());
                stForecastDto.setForecastState(0);
                stForecastDtoList.add(stForecastDto);
            }
        }
        if (!CollectionUtils.isEmpty(stForecastDtoList)){
            stForecastDao.insertStForecast(stForecastDtoList);
            //插入预警报表记录
            List<StForecastEsr> stForecastEsrList = Optional.ofNullable(converterStForecastTtoR.toList(stForecastDtoList)).orElse(Lists.newArrayList());
            stForecastDataService.addStForecastDatas(stForecastEsrList);
        }
    }

    @Override
    public StForecastEsr updateStForecast(StForecastEsu stForecastEsu) {
        StForecastDto stForecastDto = converterStForecastEtoT.toBean(stForecastEsu);
        stForecastDao.updateById(stForecastDto);
        //插入预警报表记录
        StForecastEsr stForecastEsr = converterStForecastTtoR.toBean(stForecastDto);
        stForecastDataService.addStForecastData(stForecastEsr);
        if (!CollectionUtils.isEmpty(stForecastEsu.getFile())) {
            taskExecutor.execute(() -> {
                saveFile(stForecastEsu);
            });
        }
        return stForecastEsr;
    }

    public void saveFile(StForecastEsu stForecastEsu) {
        List<String> fileId = stForecastEsu.getFile();
        List<FileBase> fileBases = fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().in(FileBase::getId, fileId));
        if (!CollectionUtils.isEmpty(fileBases)) {
            for (FileBase fileBase : fileBases) {
                fileBase.setTypeId("YJ" + stForecastEsu.getForecastId());
                fileBaseDao.updateById(fileBase);
            }
        }
    }

    @Override
    public List<StForecastPerson> selectStForecastPerson(StForecast stForecast) {
        List<StForecastPerson> stForecastPersonList = new ArrayList<>();
        if (StringUtils.isNotBlank(stForecast.getDeviceType()) && stForecast.getDeviceType().equals("1")) {
            List<StSideGateRelationDto> stSideGateRelationDtos = stSideGateRelationDao.selectList(new QueryWrapper<StSideGateRelationDto>().lambda().eq(StSideGateRelationDto::getSideGateId, stForecast.getStcdId()));
            if (!CollectionUtils.isEmpty(stSideGateRelationDtos)) {
                StSideGateRelationDto stSideGateRelationDto = stSideGateRelationDtos.get(0);
                StForecastPerson stForecastPerson = new StForecastPerson();
                stForecastPerson.setDeviceType("行政负责人");
                stForecastPerson.setFzr(stSideGateRelationDto.getXzfzr());
                stForecastPerson.setPhone(stSideGateRelationDto.getXzfzrPhone());
                stForecastPersonList.add(stForecastPerson);
                StForecastPerson stForecastPerson1 = new StForecastPerson();
                stForecastPerson1.setDeviceType("现场负责人");
                stForecastPerson1.setFzr(stSideGateRelationDto.getXcfzr());
                stForecastPerson1.setPhone(stSideGateRelationDto.getXcfzrPhone());
                stForecastPersonList.add(stForecastPerson1);
                StForecastPerson stForecastPerson2 = new StForecastPerson();
                stForecastPerson2.setDeviceType("技术负责人");
                stForecastPerson2.setFzr(stSideGateRelationDto.getJsfzr());
                stForecastPerson2.setPhone(stSideGateRelationDto.getJsfzrPhone());
                stForecastPersonList.add(stForecastPerson2);
            }
        }
        List<StCompanyBaseRelationDto> stCompanyBaseRelationDtos = Optional.ofNullable(stCompanyBaseRelationDao.selectList(new QueryWrapper<StCompanyBaseRelationDto>().lambda().eq(StCompanyBaseRelationDto::getType, "2").eq(StCompanyBaseRelationDto::getDataId, stForecast.getRiver()))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stCompanyBaseRelationDtos)) {
            List<String> collect = stCompanyBaseRelationDtos.stream().map(x -> x.getStCompanyBaseId()).collect(Collectors.toList());
            List<StCompanyBaseDto> stCompanyBaseDtos = Optional.ofNullable(stCompanyBaseDao.selectList(new QueryWrapper<StCompanyBaseDto>().lambda().in(StCompanyBaseDto::getId, collect))).orElse(Lists.newArrayList());
            for (StCompanyBaseDto stCompanyBaseDto : stCompanyBaseDtos) {
                StForecastPerson stForecastPerson = new StForecastPerson();
                stForecastPerson.setDeviceType("三方负责人");
                stForecastPerson.setFzr(stCompanyBaseDto.getManageName());
                stForecastPerson.setPhone(stCompanyBaseDto.getManagePhone());
                stForecastPersonList.add(stForecastPerson);
            }
        }
        return stForecastPersonList;
    }


    @Override//
    public List<StForecastRelevanceList> selectStForecastRelevanceList(StForecastRelevance stForecastRelevance) {
        List<StForecastRelevanceList> stForecastRelevanceList = new ArrayList<>();
        //查询设备报警
        List<StForecastDto> stForecastDtos = Optional.ofNullable(stForecastDao.selectList(new QueryWrapper<StForecastDto>().lambda().ne(StForecastDto::getForecastState, 2))).orElse(Lists.newArrayList());
        //站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝',
        List<StSideGateDto> stSideGateDtos = Optional.ofNullable(stSideGateDao.selectList(new QueryWrapper<StSideGateDto>().lambda().ne(StSideGateDto::getPresentStatus, 3))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stSideGateDtos)) {
            //1.查询边闸
            List<StSideGateDto> bz = Optional.ofNullable(stSideGateDtos.stream().filter(x -> x.getSttp().equals("BZ")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(bz)) {
                for (StSideGateDto stSideGateDto : bz) {
                    Boolean save = save(stForecastRelevance.getLgtd(), stForecastRelevance.getLttd(), stSideGateDto.getLgtd(), stSideGateDto.getLttd());
                    if (save) {
                        if (StringUtil.isNotBlank(stSideGateDto.getStcd())) {
                            StForecastRelevanceList stForecastRelevanceList1 = new StForecastRelevanceList();
                            stForecastRelevanceList1.setLgtd(stSideGateDto.getLgtd());
                            stForecastRelevanceList1.setLttd(stSideGateDto.getLttd());
                            stForecastRelevanceList1.setStcd(stSideGateDto.getStcd());
                            stForecastRelevanceList1.setStnm(stSideGateDto.getStnm());
                            stForecastRelevanceList1.setDeviceType("边闸");
                            stForecastRelevanceList.add(stForecastRelevanceList1);
                        }
                    }
                }
            }
            //2.查询水闸
            List<StSideGateDto> dd = Optional.ofNullable(stSideGateDtos.stream().filter(x -> x.getSttp().equals("DD")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(dd)) {
                List<StForecastRelevanceList> stForecastRelevanceListList = saveStForecastRelevanceList(dd, stForecastRelevance, stForecastDtos, "水闸");
                if (!CollectionUtils.isEmpty(stForecastRelevanceListList)) {
                    stForecastRelevanceList.addAll(stForecastRelevanceListList);
                }
            }
            //3.查询泵站
            List<StSideGateDto> dp = Optional.ofNullable(stSideGateDtos.stream().filter(x -> x.getSttp().equals("DP")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(dp)) {
                List<StForecastRelevanceList> stForecastRelevanceListList = saveStForecastRelevanceList(dp, stForecastRelevance, stForecastDtos, "泵站");
                if (!CollectionUtils.isEmpty(stForecastRelevanceListList)) {
                    stForecastRelevanceList.addAll(stForecastRelevanceListList);
                }
            }
            //4.查询水坝
            List<StSideGateDto> sb = Optional.ofNullable(stSideGateDtos.stream().filter(x -> x.getSttp().equals("SB")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(sb)) {
                for (StSideGateDto stSideGateDto : sb) {
                    Boolean save = save(stForecastRelevance.getLgtd(), stForecastRelevance.getLttd(), stSideGateDto.getLgtd(), stSideGateDto.getLttd());
                    if (save) {
                        if (StringUtil.isNotBlank(stSideGateDto.getStcd())) {
                            StForecastRelevanceList stForecastRelevanceList1 = new StForecastRelevanceList();
                            stForecastRelevanceList1.setLgtd(stSideGateDto.getLgtd());
                            stForecastRelevanceList1.setLttd(stSideGateDto.getLttd());
                            stForecastRelevanceList1.setStcd(stSideGateDto.getStcd());
                            stForecastRelevanceList1.setStnm(stSideGateDto.getStnm());
                            stForecastRelevanceList1.setDeviceType("水坝");
                            stForecastRelevanceList.add(stForecastRelevanceList1);
                        }
                    }
                }
            }
        }
        List<String>sttpS=new ArrayList<>();
        sttpS.add("ZQ");
        sttpS.add("ZZ");
        sttpS.add("PP");
        List<StStbprpBEntity> stStbprpBEntityList = Optional.ofNullable(stStbprpBDao.selectList(new QueryWrapper<StStbprpBEntity>().lambda().in(StStbprpBEntity::getSttp, sttpS))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stStbprpBEntityList)) {
            //5.查询水位站
            List<StStbprpBEntity> zz = Optional.ofNullable(stStbprpBEntityList.stream().filter(x -> x.getSttp().equals("ZZ")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(zz)) {
                List<StForecastRelevanceList> stForecastRelevanceListList = saveStForecastRelevanceLists(zz, stForecastRelevance, stForecastDtos, "水位站");
                if (!CollectionUtils.isEmpty(stForecastRelevanceListList)) {
                    stForecastRelevanceList.addAll(stForecastRelevanceListList);
                }
            }
            //6.查询流量站
            List<StStbprpBEntity> zq = Optional.ofNullable(stStbprpBEntityList.stream().filter(x -> x.getSttp().equals("ZQ")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(zq)) {
                List<StForecastRelevanceList> stForecastRelevanceListList = saveStForecastRelevanceLists(zq, stForecastRelevance, stForecastDtos, "流量站");
                if (!CollectionUtils.isEmpty(stForecastRelevanceListList)) {
                    stForecastRelevanceList.addAll(stForecastRelevanceListList);
                }
            }
            //7.查询雨量站
            List<StStbprpBEntity> pp = Optional.ofNullable(stStbprpBEntityList.stream().filter(x -> x.getSttp().equals("PP")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(pp)) {
                for (StStbprpBEntity stStbprpBEntity : pp) {
                    Boolean save = save(stForecastRelevance.getLgtd(), stForecastRelevance.getLttd(), stStbprpBEntity.getLgtd().doubleValue(), stStbprpBEntity.getLttd().doubleValue());
                    if (save) {
                        StForecastRelevanceList stForecastRelevanceList1 = new StForecastRelevanceList();
                        stForecastRelevanceList1.setLgtd(stStbprpBEntity.getLgtd().doubleValue());
                        stForecastRelevanceList1.setLttd(stStbprpBEntity.getLttd().doubleValue());
                        stForecastRelevanceList1.setStcd(stStbprpBEntity.getStcd());
                        stForecastRelevanceList1.setStnm(stStbprpBEntity.getStnm());
                        stForecastRelevanceList1.setDeviceType("雨量站");
                        stForecastRelevanceList.add(stForecastRelevanceList1);
                    }
                }
            }
        }
        return stForecastRelevanceList;
    }

    public List<StForecastRelevanceList> saveStForecastRelevanceLists(List<StStbprpBEntity> dd, StForecastRelevance stForecastRelevance, List<StForecastDto> stForecastDtos, String type) {
        List<StForecastRelevanceList> stForecastRelevanceList = new ArrayList<>();
        for (StStbprpBEntity stStbprpBEntity : dd) {
            Boolean save = save(stForecastRelevance.getLgtd(), stForecastRelevance.getLttd(), stStbprpBEntity.getLgtd().doubleValue(), stStbprpBEntity.getLttd().doubleValue());
            if (save) {
                StForecastRelevanceList stForecastRelevanceList1 = new StForecastRelevanceList();
                stForecastRelevanceList1.setLgtd(stStbprpBEntity.getLgtd().doubleValue());
                stForecastRelevanceList1.setLttd(stStbprpBEntity.getLttd().doubleValue());
                stForecastRelevanceList1.setStcd(stStbprpBEntity.getStcd());
                stForecastRelevanceList1.setStnm(stStbprpBEntity.getStnm());
                if (!CollectionUtils.isEmpty(stForecastDtos)) {
                    List<StForecastDto> stForecastDtoList = Optional.ofNullable(stForecastDtos.stream().filter(x -> x.getStcd().equals(stStbprpBEntity.getStcd())).collect(Collectors.toList())).orElse(Lists.newArrayList());
                    if (CollectionUtils.isEmpty(stForecastDtoList)) {
                        stForecastRelevanceList1.setOnlineStatus("1");
                    } else {
                        stForecastRelevanceList1.setOnlineStatus("2");
                    }
                } else {
                    stForecastRelevanceList1.setOnlineStatus("1");
                }

                stForecastRelevanceList1.setDeviceType(type);
                stForecastRelevanceList.add(stForecastRelevanceList1);
            }
        }
        return stForecastRelevanceList;
    }

    public List<StForecastRelevanceList> saveStForecastRelevanceList(List<StSideGateDto> dd, StForecastRelevance stForecastRelevance, List<StForecastDto> stForecastDtos, String type) {
        List<StForecastRelevanceList> stForecastRelevanceList = new ArrayList<>();
        for (StSideGateDto stSideGateDto : dd) {
            Boolean save = save(stForecastRelevance.getLgtd(), stForecastRelevance.getLttd(), stSideGateDto.getLgtd(), stSideGateDto.getLttd());
            if (save) {
                if (StringUtil.isNotBlank(stSideGateDto.getStcd())) {
                    StForecastRelevanceList stForecastRelevanceList1 = new StForecastRelevanceList();
                    stForecastRelevanceList1.setLgtd(stSideGateDto.getLgtd());
                    stForecastRelevanceList1.setLttd(stSideGateDto.getLttd());
                    stForecastRelevanceList1.setStcd(stSideGateDto.getStcd());
                    stForecastRelevanceList1.setStnm(stSideGateDto.getStnm());
                    //远传设备 /查询报警实际是否有该设备
                    if (StringUtils.isNotBlank(stSideGateDto.getRemoteControl()) && stSideGateDto.getRemoteControl().equals("1")) {
                        if (!CollectionUtils.isEmpty(stForecastDtos)) {
                            List<StForecastDto> stForecastDtoList = Optional.ofNullable(stForecastDtos.stream().filter(x -> x.getStcdId() == stSideGateDto.getId()).collect(Collectors.toList())).orElse(Lists.newArrayList());
                            if (CollectionUtils.isEmpty(stForecastDtoList)) {
                                stForecastRelevanceList1.setOnlineStatus("1");
                            } else {
                                stForecastRelevanceList1.setOnlineStatus("2");
                            }
                        } else {
                            stForecastRelevanceList1.setOnlineStatus("1");
                        }
                    }
                    stForecastRelevanceList1.setDeviceType(type);
                    stForecastRelevanceList.add(stForecastRelevanceList1);
                }
            }
        }
        return stForecastRelevanceList;
    }

    public Boolean save(double longitudeFrom, double latitudeFrom, double longitudeTo,
                        double latitudeTo) {
        Double distance = GisUtils.distance(longitudeFrom, latitudeFrom, longitudeTo, latitudeTo);
        if (distance < 2000) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void receptionStForecast(StForecastReception stForecastReception) {
        if (stForecastReception.getState() == 1) {
            //修改发送状态
            stForecastDao.updateStForecastDao(stForecastReception.getForecastId());
        }
        if (stForecastReception.getState() == 2) {
            //修改接收状态
            stForecastDao.updateStForecastDaos(stForecastReception.getForecastId());
        }
    }

    @Override
    public StForecastEsr selectStForecast(String forecastId) {
        List<StForecastDto> stForecastDtoList = Optional.ofNullable(stForecastDao.selectList(new QueryWrapper<StForecastDto>().lambda().eq(StForecastDto::getForecastId, forecastId))).orElse(Lists.newArrayList());
        StForecastEsr stForecastEsr = new StForecastEsr();
        if (!CollectionUtils.isEmpty(stForecastDtoList)) {
            StForecastDto stForecastDto = stForecastDtoList.get(0);
            stForecastEsr = converterStForecastTtoR.toBean(stForecastDto);
            List<FileBase> fileBases = Optional.ofNullable(fileBaseDao.selectList(new QueryWrapper<FileBase>().lambda().eq(FileBase::getTypeId, "YJ" + stForecastDto.getForecastId()).eq(FileBase::getIsDelete, "0"))).orElse(Lists.newArrayList());
            if (!CollectionUtils.isEmpty(fileBases)) {
                List<FileBaseEsr> fileBaseEsrs = Optional.ofNullable(converterFileBaseTtoR.toList(fileBases)).orElse(Lists.newArrayList());
                stForecastEsr.setFile(fileBaseEsrs);
            }
        }
        return stForecastEsr;
    }

    @Override
    public List<StForecastEsr> selectStForecastList(String type) {
        List<StForecastEsr> stForecastEsrList = new ArrayList<>();
        List<StForecastDto> stForecastDtoList = Optional.ofNullable(stForecastDao.selectList(new QueryWrapper<StForecastDto>().lambda().eq(StForecastDto::getDeviceType, type).ne(StForecastDto::getForecastState, 2))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stForecastDtoList)) {
            stForecastEsrList = converterStForecastTtoR.toList(stForecastDtoList);
        }
        return stForecastEsrList;
    }
}
