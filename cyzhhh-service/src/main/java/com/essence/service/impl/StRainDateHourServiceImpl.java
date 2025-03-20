package com.essence.service.impl;

import com.essence.dao.StRainDateHourDao;
import com.essence.dao.entity.StRainDateHour;
import com.essence.interfaces.api.StRainDateHourService;
import com.essence.interfaces.model.StRainDateHourEsr;
import com.essence.interfaces.model.StRainDateHourEsu;
import com.essence.interfaces.param.StRainDateHourEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStRainDateHourEtoT;
import com.essence.service.converter.ConverterStRainDateHourTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小时雨量(StRainDateHour)业务层
 *
 * @author tyy
 * @since 2024-07-20 11:04:27
 */
@Service
public class StRainDateHourServiceImpl extends BaseApiImpl<StRainDateHourEsu, StRainDateHourEsp, StRainDateHourEsr, StRainDateHour> implements StRainDateHourService {

    @Autowired
    private StRainDateHourDao stRainDateHourDao;
    @Autowired
    private ConverterStRainDateHourEtoT converterStRainDateHourEtoT;
    @Autowired
    private ConverterStRainDateHourTtoR converterStRainDateHourTtoR;

    public StRainDateHourServiceImpl(StRainDateHourDao stRainDateHourDao, ConverterStRainDateHourEtoT converterStRainDateHourEtoT, ConverterStRainDateHourTtoR converterStRainDateHourTtoR) {
        super(stRainDateHourDao, converterStRainDateHourEtoT, converterStRainDateHourTtoR);
    }
}
