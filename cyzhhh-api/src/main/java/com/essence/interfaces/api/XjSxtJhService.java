package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.ViewVideoXjEsr;
import com.essence.interfaces.model.XjSxtJhEsr;
import com.essence.interfaces.model.XjSxtJhEsu;
import com.essence.interfaces.param.XjSxtJhEsp;

/**
 * 设备巡检摄像头巡检计划服务层
 * @author majunjie
 * @since 2025-01-08 22:46:38
 */
public interface XjSxtJhService extends BaseApi<XjSxtJhEsu, XjSxtJhEsp, XjSxtJhEsr> {
    void updateData(ViewVideoXjEsr viewVideoXjEsr);
}
