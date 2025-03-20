package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.interfaces.model.WorkorderTrackEsu;
import com.essence.interfaces.param.WorkorderTrackEsp;

import java.util.List;

/**
 * 工单巡查轨迹服务层
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */
public interface WorkorderTrackService extends BaseApi<WorkorderTrackEsu, WorkorderTrackEsp, WorkorderTrackEsr> {
    /**
     * 根据工单主键查全部
     * @param orderId
     * @return
     */
    List<WorkorderTrackEsr> findListByOrderId(String orderId);
    /**
     * 根据工单主键查最新一条
     * @param orderId
     * @return
     */
    WorkorderTrackEsr findOneByOrderId(String orderId);

}
