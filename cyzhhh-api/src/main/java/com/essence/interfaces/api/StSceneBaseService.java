package com.essence.interfaces.api;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StSceneBaseEsr;
import com.essence.interfaces.model.StSceneBaseEsu;
import com.essence.interfaces.param.StSceneBaseEsp;

import java.util.List;

/**
 * 场景基本表服务层
 * @author liwy
 * @since 2023-06-01 14:48:05
 */
public interface StSceneBaseService extends BaseApi<StSceneBaseEsu, StSceneBaseEsp, StSceneBaseEsr> {
    /**
     * 新增场景
     * @param stSceneBaseEsu
     * @return
     */
    ResponseResult addStSceneBase(StSceneBaseEsu stSceneBaseEsu);

    /**
     * 删除场景
     * @param stSceneBaseEsu
     * @return
     */
    ResponseResult deleteStSceneBase(StSceneBaseEsu stSceneBaseEsu);

    ResponseResult<List<StSceneBaseEsr>> selectStSceneBase(PaginatorParam param);
}
