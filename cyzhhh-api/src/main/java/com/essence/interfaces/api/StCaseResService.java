package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.*;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StCaseResEsp;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 方案执行结果表服务层
 * @author BINX
 * @since 2023-04-18 14:39:14
 */
public interface StCaseResService extends BaseApi<StCaseResEsu, StCaseResEsp, StCaseResEsr> {
    List<ForecastPerformanceDto> getRainTendency(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    WaringStatisticDto getWaterWarning(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    List<RiverStepSectionDto> getRiverSection(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    StCaseResRainLists selectRain(StCaseResRainQuery stCaseResRainQuery);

    List<StCaseResFlowList> selectFlow(StCaseResRainQuery stCaseResRainQuery);

    List<StCaseResWaterList> selectWater(StCaseResRainQuery stCaseResRainQuery);

    List<WaterForcastDto> getWaterForecast(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    Object getFloodPeak(PaginatorParam param);

    List<RiverGateMaxFlowViewEsr>  getFloodFlowPeak(StCaseResEsu cs);

    OverFlowListDto getOverFlowList(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    StCaseResRainLists selectRainFallPattern(String caseId);

    List<RiverGateWaterDto> getRiverGateWaterLevel(String rvid,String caseId);

    void strategyFixCurve(String oldCaseId, String newCaseName);

    List<RiverStepSectionDto> getRiverFactSection(StCaseBaseInfoEsu stCaseBaseInfoEsu) throws ParseException, ExecutionException, InterruptedException;
}
