package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StEmergencyEsr;
import com.essence.interfaces.model.StEmergencyEsu;
import com.essence.interfaces.param.StEmergencyEsp;

/**
 * 抢险队基本信息表服务层
 * @author liwy
 * @since 2023-04-13 16:15:35
 */
public interface StEmergencyService extends BaseApi<StEmergencyEsu, StEmergencyEsp, StEmergencyEsr> {
}
