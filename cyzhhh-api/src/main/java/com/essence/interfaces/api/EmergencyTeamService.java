package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.EmergencyTeamEsr;
import com.essence.interfaces.model.EmergencyTeamEsu;
import com.essence.interfaces.param.EmergencyTeamEsp;

/**
 * 抢险队伍服务层
 *
 * @author zhy
 * @since 2024-07-17 19:32:42
 */
public interface EmergencyTeamService extends BaseApi<EmergencyTeamEsu, EmergencyTeamEsp, EmergencyTeamEsr> {
}
