package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StPumpDataEsr;
import com.essence.interfaces.model.StPumpDataEsu;
import com.essence.interfaces.param.PumpFlowChartEsp;
import com.essence.interfaces.param.StPumpDataEsp;

/**
 * 服务层
 * @author BINX
 * @since 2023-04-14 11:36:09
 */
public interface StPumpDataService extends BaseApi<StPumpDataEsu, StPumpDataEsp, StPumpDataEsr> {
    Object getPumpFlowChart(PumpFlowChartEsp pumpFlowChartEsp);

    Object getPumpList(PaginatorParam param);
}
