package com.essence.service.impl;

import com.essence.dao.StWaterRiskForecastDao;
import com.essence.dao.entity.water.StWaterRiskForecastDto;
import com.essence.interfaces.api.StWaterRiskForecastService;
import com.essence.interfaces.model.StWaterRiskForecastEsr;
import com.essence.interfaces.model.StWaterRiskForecastEsu;
import com.essence.interfaces.param.StWaterRiskForecastEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterRiskForecastEtoT;
import com.essence.service.converter.ConverterStWaterRiskForecastTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* 水系联调-模型风险预报 (st_water_risk_forecast)表数据库业务层
*
* @author BINX
* @since 2023年5月11日 下午4:00:24
*/
@Service
public class StWaterRiskForecastServiceImpl extends BaseApiImpl<StWaterRiskForecastEsu, StWaterRiskForecastEsp, StWaterRiskForecastEsr, StWaterRiskForecastDto> implements StWaterRiskForecastService {


    @Autowired
    private StWaterRiskForecastDao stWaterRiskForecastDao;
    @Autowired
    private ConverterStWaterRiskForecastEtoT converterStWaterRiskForecastEtoT;
    @Autowired
    private ConverterStWaterRiskForecastTtoR converterStWaterRiskForecastTtoR;

    public StWaterRiskForecastServiceImpl(StWaterRiskForecastDao stWaterRiskForecastDao, ConverterStWaterRiskForecastEtoT converterStWaterRiskForecastEtoT, ConverterStWaterRiskForecastTtoR converterStWaterRiskForecastTtoR) {
        super(stWaterRiskForecastDao, converterStWaterRiskForecastEtoT, converterStWaterRiskForecastTtoR);
    }
}
