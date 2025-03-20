package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjBzxxEsr;
import com.essence.interfaces.model.XjBzxxEsu;
import com.essence.interfaces.param.XjBzxxEsp;

/**
 * 设备巡检班组信息服务层
 * @author majunjie
 * @since 2025-01-08 08:13:26
 */
public interface XjBzxxService extends BaseApi<XjBzxxEsu,XjBzxxEsp,XjBzxxEsr> {
    XjBzxxEsr addBz(XjBzxxEsu xjBzxxEsu);
}
