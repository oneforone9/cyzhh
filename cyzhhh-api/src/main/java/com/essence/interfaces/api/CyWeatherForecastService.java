package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.CyWeatherForecastEsr;
import com.essence.interfaces.model.CyWeatherForecastEsu;
import com.essence.interfaces.param.CyWeatherForecastEsp;

/**
 * 服务层
 * @author BINX
 * @since 2023-03-16 16:41:56
 */
public interface CyWeatherForecastService extends BaseApi<CyWeatherForecastEsu, CyWeatherForecastEsp, CyWeatherForecastEsr> {
}
