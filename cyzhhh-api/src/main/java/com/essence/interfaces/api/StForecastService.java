package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StForecastEsp;

import java.util.List;

/**
 * 预警报表服务层
 * @author majunjie
 * @since 2023-04-17 19:38:25
 */
public interface StForecastService extends BaseApi<StForecastEsu, StForecastEsp, StForecastEsr> {

    StForecastEsr addStForecast(StForecastEsu stForecastEsu);

    void addStForecasts(List<StForecastEsu> stForecastEsu);

    StForecastEsr updateStForecast(StForecastEsu stForecastEsu);

    List<StForecastPerson> selectStForecastPerson(StForecast stForecast);

    List<StForecastRelevanceList> selectStForecastRelevanceList(StForecastRelevance stForecastRelevance);

    void receptionStForecast(StForecastReception stForecastReception);

    StForecastEsr selectStForecast(String forecastId);

    //通过预警设备类型查询
    List<StForecastEsr> selectStForecastList(String type);

}
