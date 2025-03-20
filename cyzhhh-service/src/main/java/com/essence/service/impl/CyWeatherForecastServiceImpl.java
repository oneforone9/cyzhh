package com.essence.service.impl;

import com.essence.dao.CyWeatherForecastDao;
import com.essence.dao.entity.CyWeatherForecastDto;
import com.essence.interfaces.api.CyWeatherForecastService;

import com.essence.interfaces.model.CyWeatherForecastEsr;
import com.essence.interfaces.model.CyWeatherForecastEsu;
import com.essence.interfaces.param.CyWeatherForecastEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterCyWeatherForecastEtoT;
import com.essence.service.converter.ConverterCyWeatherForecastTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (CyWeatherForecast)业务层
 * @author BINX
 * @since 2023-03-16 16:41:53
 */
@Service
public class CyWeatherForecastServiceImpl extends BaseApiImpl<CyWeatherForecastEsu, CyWeatherForecastEsp, CyWeatherForecastEsr, CyWeatherForecastDto> implements CyWeatherForecastService {


    @Autowired
    private CyWeatherForecastDao cyWeatherForecastDao;
    @Autowired
    private ConverterCyWeatherForecastEtoT converterCyWeatherForecastEtoT;
    @Autowired
    private ConverterCyWeatherForecastTtoR converterCyWeatherForecastTtoR;

    public CyWeatherForecastServiceImpl(CyWeatherForecastDao cyWeatherForecastDao, ConverterCyWeatherForecastEtoT converterCyWeatherForecastEtoT, ConverterCyWeatherForecastTtoR converterCyWeatherForecastTtoR) {
        super(cyWeatherForecastDao, converterCyWeatherForecastEtoT, converterCyWeatherForecastTtoR);
    }
}
