package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.EventDepositEsr;
import com.essence.interfaces.model.EventDepositEsu;
import com.essence.interfaces.param.EventDepositEsp;

/**
 * 事件基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-30 18:06:23
 */
public interface EventDepositService extends BaseApi<EventDepositEsu, EventDepositEsp, EventDepositEsr> {

    /**
     * 将小程序临时表中国的事件更新到事件表中
     * @param orderId
     * @return
     */
    int updateEventFromDepositByOrderId(String orderId);
}
