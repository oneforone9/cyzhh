package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StSideGateRelationEsr;
import com.essence.interfaces.model.StSideGateRelationEsu;
import com.essence.interfaces.param.StSideGateRelationEsp;

/**
 * 闸坝负责人关联表服务层
 * @author liwy
 * @since 2023-04-13 17:51:20
 */
public interface StSideGateRelationService extends BaseApi<StSideGateRelationEsu, StSideGateRelationEsp, StSideGateRelationEsr> {

    /**
     * 编辑闸坝负责人信息
     * @param stSideGateRelationEsu
     * @return
     */
    Object updateStSideGateRelation(StSideGateRelationEsu stSideGateRelationEsu);
}
