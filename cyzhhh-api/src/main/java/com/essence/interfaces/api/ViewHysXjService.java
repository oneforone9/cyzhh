package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.ViewHysXjEsr;
import com.essence.interfaces.model.ViewHysXjEsu;
import com.essence.interfaces.param.ViewHysXjEsp;


/**
 * 服务层
 * @author majunjie
 * @since 2025-01-09 10:22:28
 */
public interface ViewHysXjService extends BaseApi<ViewHysXjEsu, ViewHysXjEsp, ViewHysXjEsr> {
    ViewHysXjEsr updateHysJh(ViewHysXjEsr viewHysXjEsr);
}
