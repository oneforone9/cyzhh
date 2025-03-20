package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.EventBaseEsu;
import com.essence.interfaces.model.EventBaseStatusEsr;
import com.essence.interfaces.model.EventBaseStatusEsu;
import com.essence.interfaces.param.EventBaseStatusEsp;

/**
 * 事件基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-30 18:06:23
 */
public interface EventBaseStatusService extends BaseApi<EventBaseStatusEsu, EventBaseStatusEsp, EventBaseStatusEsr> {
    /**
     * 新增 字段不做任何处理
     *  仅用于小程序临时事件表添加到事件表中
     * @return
     */
    int add(EventBaseEsu eventBaseEsu);

}
