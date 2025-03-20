package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.ViewVideoQuery;
import com.essence.interfaces.model.ViewVideoXjEsr;
import com.essence.interfaces.model.ViewVideoXjEsu;
import com.essence.interfaces.param.ViewVideoXjEsp;



/**
 * 服务层
 * @author majunjie
 * @since 2025-01-08 14:13:47
 */
public interface ViewVideoXjService extends BaseApi<ViewVideoXjEsu, ViewVideoXjEsp, ViewVideoXjEsr> {
    ViewVideoXjEsr selectVideo(ViewVideoQuery viewVideoQuery);

    ViewVideoXjEsr updateVideoJh(ViewVideoXjEsr viewVideoXjEsr);
}
