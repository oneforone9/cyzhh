package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.StWaterRiskForecastEsp;
import com.essence.interfaces.model.StWaterRiskForecastEsu;
import com.essence.interfaces.model.StWaterRiskForecastEsr;

/**
* 水系联调-模型风险预报 (st_water_risk_forecast)表数据库服务层
*
* @author BINX
* @since 2023年5月11日 下午4:00:24
*/
public interface StWaterRiskForecastService extends BaseApi<StWaterRiskForecastEsu, StWaterRiskForecastEsp, StWaterRiskForecastEsr> {
}