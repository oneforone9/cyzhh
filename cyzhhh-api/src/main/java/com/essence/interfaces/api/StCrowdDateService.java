package com.essence.interfaces.api;


import com.essence.common.dto.CrowdDateRequest;
import com.essence.dao.entity.StCrowdDateDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.CrowDataDto;
import com.essence.interfaces.model.StCrowdDateEsr;
import com.essence.interfaces.model.StCrowdDateEsu;
import com.essence.interfaces.param.StCrowdDateEsp;

import java.util.List;

/**
 * 清水的河 - 用水人数量服务层
 * @author BINX
 * @since 2023-01-12 17:36:46
 */
public interface StCrowdDateService extends BaseApi<StCrowdDateEsu, StCrowdDateEsp, StCrowdDateEsr> {
    CrowDataDto getManageRiverList(CrowdDateRequest crowdDateRequest);

    /**
     * 日游客量-根据日期查询
     * @param stCrowdDateEsp
     * @return
     */
    List<StCrowdDateDto> getStCrowdDate(StCrowdDateEsp stCrowdDateEsp);
}
