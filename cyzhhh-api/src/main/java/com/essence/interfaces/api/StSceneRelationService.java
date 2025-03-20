package com.essence.interfaces.api;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StSceneRelationEsr;
import com.essence.interfaces.model.StSceneRelationEsu;
import com.essence.interfaces.param.StSceneRelationEsp;

/**
 * 场景关联表服务层
 * @author liwy
 * @since 2023-06-01 14:48:29
 */
public interface StSceneRelationService extends BaseApi<StSceneRelationEsu, StSceneRelationEsp, StSceneRelationEsr> {
    /**
     * 收藏摄像头到场景
     * @param stSceneRelationEsu
     * @return
     */
    ResponseResult addStSceneRelation(StSceneRelationEsu stSceneRelationEsu);
}
