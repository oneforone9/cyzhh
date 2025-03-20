package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StForecastDataEsr;
import com.essence.interfaces.model.StForecastDataEsu;
import com.essence.interfaces.model.StForecastEsr;
import com.essence.interfaces.param.StForecastDataEsp;

import java.util.List;


/**
 * 预警报表记录服务层
 * @author majunjie
 * @since 2023-04-17 19:39:01
 */
public interface StForecastDataService extends BaseApi<StForecastDataEsu, StForecastDataEsp, StForecastDataEsr> {
    StForecastDataEsr addStForecastData(StForecastEsr stForecastEsr);

    void addStForecastDatas(List<StForecastEsr> stForecastEsrList);
}
