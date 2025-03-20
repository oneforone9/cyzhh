package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.XjrwEsr;
import com.essence.interfaces.model.XjrwEsu;
import com.essence.interfaces.model.XjrwlcEsr;
import com.essence.interfaces.param.XjrwEsp;


/**
 * 设备巡检任务服务层
 * @author majunjie
 * @since 2025-01-09 15:09:06
 */
public interface XjrwService extends BaseApi<XjrwEsu, XjrwEsp, XjrwEsr> {
    Paginator<XjrwlcEsr> searchXjlc(PaginatorParam param);

    XjrwEsr addXjrw(XjrwEsu xjrwEsu,String userName);

    XjrwEsr updateXjrw(XjrwEsu xjrwEsu,String userName);
}
