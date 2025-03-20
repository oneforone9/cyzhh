package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.TWorkorderFlowEsr;
import com.essence.interfaces.model.TWorkorderFlowEsu;
import com.essence.interfaces.param.TWorkorderFlowEsp;


/**
 * 工单处理过程表服务层
 * @author majunjie
 * @since 2023-06-07 10:31:23
 */
public interface TWorkorderFlowService extends BaseApi<TWorkorderFlowEsu, TWorkorderFlowEsp, TWorkorderFlowEsr> {
    //新增
    String addTWorkOrderFlow(TWorkorderFlowEsu tWorkorderFlowEsu);
    //系统生成工单并派发
    void addTWorkOrderFlows(TWorkorderFlowEsu tWorkorderFlowEsu);
    //生成工单并派发
    void addTWorkOrderFlowP(TWorkorderFlowEsu tWorkorderFlowEsu);
}
