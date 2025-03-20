package com.essence.service.impl;

import com.essence.common.utils.UuidUtil;
import com.essence.dao.StForecastDataDao;
import com.essence.dao.entity.StForecastDataDto;
import com.essence.interfaces.api.StForecastDataService;
import com.essence.interfaces.model.StForecastDataEsr;
import com.essence.interfaces.model.StForecastDataEsu;
import com.essence.interfaces.model.StForecastEsr;
import com.essence.interfaces.param.StForecastDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStForecastDataEtoT;
import com.essence.service.converter.ConverterStForecastDataTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预警报表记录(StForecastData)业务层
 * @author majunjie
 * @since 2023-04-17 19:38:57
 */
@Service
public class StForecastDataServiceImpl extends BaseApiImpl<StForecastDataEsu, StForecastDataEsp, StForecastDataEsr, StForecastDataDto> implements StForecastDataService {

    @Autowired
    private StForecastDataDao stForecastDataDao;
    @Autowired
    private ConverterStForecastDataEtoT converterStForecastDataEtoT;
    @Autowired
    private ConverterStForecastDataTtoR converterStForecastDataTtoR;

    public StForecastDataServiceImpl(StForecastDataDao stForecastDataDao, ConverterStForecastDataEtoT converterStForecastDataEtoT, ConverterStForecastDataTtoR converterStForecastDataTtoR) {
        super(stForecastDataDao, converterStForecastDataEtoT, converterStForecastDataTtoR);
    }

    @Override
    public StForecastDataEsr addStForecastData(StForecastEsr stForecastEsr) {
        //插入预警信息
        StForecastDataDto stForecastDataDto=new StForecastDataDto();
        stForecastDataDto.setId(UuidUtil.get32UUIDStr());
        stForecastDataDto.setForecastId(stForecastEsr.getForecastId());
        stForecastDataDto.setStnm(stForecastEsr.getStnm());
        stForecastDataDto.setRvnm(stForecastEsr.getRvnm());
        stForecastDataDto.setForecastType(stForecastEsr.getForecastType());
        stForecastDataDto.setReason(stForecastEsr.getReason());
        stForecastDataDto.setForecastState(stForecastEsr.getForecastState());
        stForecastDataDto.setTime(new Date());
        stForecastDataDao.insert(stForecastDataDto);
        return converterStForecastDataTtoR.toBean(stForecastDataDto);
    }

    @Override
    public void addStForecastDatas(List<StForecastEsr> stForecastEsrList) {
        List<StForecastDataDto>stForecastDataDtoList=new ArrayList<>();
        if (!CollectionUtils.isEmpty(stForecastEsrList)){
            for (StForecastEsr stForecastEsr : stForecastEsrList) {
                StForecastDataDto stForecastDataDto=new StForecastDataDto();
                stForecastDataDto.setId(UuidUtil.get32UUIDStr());
                stForecastDataDto.setForecastId(stForecastEsr.getForecastId());
                stForecastDataDto.setStnm(stForecastEsr.getStnm());
                stForecastDataDto.setRvnm(stForecastEsr.getRvnm());
                stForecastDataDto.setForecastType(stForecastEsr.getForecastType());
                stForecastDataDto.setReason(stForecastEsr.getReason());
                stForecastDataDto.setForecastState(stForecastEsr.getForecastState());
                stForecastDataDto.setTime(new Date());
                stForecastDataDtoList.add(stForecastDataDto);
            }
            if (!CollectionUtils.isEmpty(stForecastDataDtoList)){
                stForecastDataDao.addStForecastDatas(stForecastDataDtoList);
            }
        }
    }
}
