package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjRyxQuery;
import com.essence.interfaces.model.XjRyxxEsr;
import com.essence.interfaces.model.XjRyxxEsu;
import com.essence.interfaces.param.XjRyxxEsp;

import java.util.List;

/**
 * 设备巡检人员信息服务层
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */
public interface XjRyxxService extends BaseApi<XjRyxxEsu, XjRyxxEsp, XjRyxxEsr> {
    XjRyxxEsr updateRyxx(XjRyxxEsu xjRyxxEsu);

    XjRyxxEsr addRyxx(XjRyxxEsu xjRyxxEsu);

    List<XjRyxxEsr> searchRyxxById(XjRyxQuery xjRyxQuery);

    List<XjRyxxEsr> searchRyxxByIds(XjRyxQuery xjRyxQuery);
}
