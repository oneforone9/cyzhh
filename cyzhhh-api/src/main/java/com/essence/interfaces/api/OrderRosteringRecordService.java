package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.OrderRosteringRecordEsr;
import com.essence.interfaces.model.OrderRosteringRecordEsu;
import com.essence.interfaces.param.OrderRosteringRecordEsp;

/**
 * 值班表生成工单记录表(避免重复生成工单)服务层
 *
 * @author zhy
 * @since 2022-10-31 17:53:18
 */
public interface OrderRosteringRecordService extends BaseApi<OrderRosteringRecordEsu, OrderRosteringRecordEsp, OrderRosteringRecordEsr> {

}
