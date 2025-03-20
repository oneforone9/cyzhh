package com.essence.interfaces.api;

import com.baomidou.mybatisplus.extension.api.R;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.WorkorderNewestEsr;
import com.essence.interfaces.model.WorkorderProcessEsr;
import com.essence.interfaces.model.WorkorderProcessEsu;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.interfaces.param.WorkorderProcessEsp;

import java.util.List;

/**
 * 工单处理过程表服务层
 *
 * @author zhy
 * @since 2022-10-27 15:26:31
 */
public interface WorkorderProcessService extends BaseApi<WorkorderProcessEsu, WorkorderProcessEsp, WorkorderProcessEsr> {

    List<WorkorderNewestEsr> selectWorkorderProcessList();

    List<WorkorderTrackEsr> selectWorkorderProcessTrack(List<String> collect);

    /**
     * 通过用户id 和 工单状态 查询是否存在数据
     * @param userId
     * @param orderStatusRunning
     * @param orderId
     */
    Boolean findByPersonIdAndWorkOrderStatus(String userId, String orderStatusRunning, String orderId);

    /**
     * 将工单的状态表拆分成所有流程表和最近工单状态表
     * @param e
     * @return
     */
    int insertDeal(WorkorderProcessEsu e);
}
