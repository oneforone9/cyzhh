package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.ViewHysXjEsr;
import com.essence.interfaces.model.XjHysJhEsr;
import com.essence.interfaces.model.XjHysJhEsu;
import com.essence.interfaces.param.XjHysJhEsp;


/**
 * 设备巡检会议室巡检计划服务层
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */
public interface XjHysJhService extends BaseApi<XjHysJhEsu, XjHysJhEsp, XjHysJhEsr> {
    ViewHysXjEsr updateHysJh(ViewHysXjEsr viewHysXjEsr);
}
