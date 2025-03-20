package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.WorkorderRecordGeomEsr;
import com.essence.interfaces.model.WorkorderRecordGeomEsu;
import com.essence.interfaces.param.WorkorderRecordGeomEsp;

import java.util.List;

/**
 * 工单地理信息记录表-记录工单创建时的重点位地理信息服务层
 *
 * @author zhy
 * @since 2022-10-30 16:57:21
 */
public interface WorkorderRecordGeomService extends BaseApi<WorkorderRecordGeomEsu, WorkorderRecordGeomEsp, WorkorderRecordGeomEsr> {

    /**
     * 根据工单主键查询
     * @param orderId
     * @return
     */
    List<WorkorderRecordGeomEsr> findByOrderId(String orderId);
}
