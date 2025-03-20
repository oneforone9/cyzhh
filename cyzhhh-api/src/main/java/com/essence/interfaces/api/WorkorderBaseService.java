package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.WorkorderBaseEsr;
import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.param.WorkorderBaseEsp;

/**
 * 工单基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-27 15:26:27
 */
public interface WorkorderBaseService extends BaseApi<WorkorderBaseEsu, WorkorderBaseEsp, WorkorderBaseEsr> {

    String insertWorkorder(WorkorderBaseEsu workorderBaseEsu);

    void updateRejectTime(WorkorderBaseEsu workorderBaseEsu);

    Integer updateWorkorder(WorkorderBaseEsu workorderBaseEsu);
}
