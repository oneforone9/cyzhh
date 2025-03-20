package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsr;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsu;
import com.essence.interfaces.param.StWaterEngineeringDispatchEsp;

/**
 * 水系联调-工程调度-调度预案服务层
 * @author majunjie
 * @since 2023-06-02 12:39:16
 */
public interface StWaterEngineeringDispatchService extends BaseApi<StWaterEngineeringDispatchEsu, StWaterEngineeringDispatchEsp, StWaterEngineeringDispatchEsr> {
    StWaterEngineeringDispatchEsr selectDispatchByStId(String stId);
}
