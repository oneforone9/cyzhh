package com.essence.interfaces.api;


import com.essence.dao.entity.StCrowdRealDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCrowdRealEsr;
import com.essence.interfaces.model.StCrowdRealEsu;
import com.essence.interfaces.param.StCrowdRealEsp;

import java.util.List;

/**
 * 清水的河 - 实时游客表服务层
 * @author BINX
 * @since 2023-02-28 11:44:46
 */
public interface StCrowdRealService extends BaseApi<StCrowdRealEsu, StCrowdRealEsp, StCrowdRealEsr> {

    /**
     * 实时游客客量
     * @param stCrowdRealEsp
     * @return
     */
    List<StCrowdRealDto> getStCrowdReal(StCrowdRealEsp stCrowdRealEsp);
}
