package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StPlanOperateEsr;
import com.essence.interfaces.model.StPlanOperateEsu;
import com.essence.interfaces.param.StPlanOperateEsp;

import java.util.List;

/**
 * 养护内容记录表服务层
 * @author liwy
 * @since 2023-07-24 14:17:23
 */
public interface StPlanOperateService extends BaseApi<StPlanOperateEsu, StPlanOperateEsp, StPlanOperateEsr> {

    /**
     * 新增养护工单作业录
     * @param list
     * @return
     */
    Object addStPlanOperate(List<StPlanOperateEsu> list);
}
