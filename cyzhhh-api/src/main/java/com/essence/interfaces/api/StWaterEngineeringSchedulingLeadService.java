package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingLeadEsp;

/**
 * 水系联调-工程调度服务层
 * @author majunjie
 * @since 2023-07-03 17:24:53
 */
public interface StWaterEngineeringSchedulingLeadService extends BaseApi<StWaterEngineeringSchedulingLeadEsu, StWaterEngineeringSchedulingLeadEsp, StWaterEngineeringSchedulingLeadEsr> {
    Boolean selectSz(String id);
}
