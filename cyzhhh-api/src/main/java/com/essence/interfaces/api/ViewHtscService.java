package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.Hthsz;
import com.essence.interfaces.model.ViewHtscEsr;
import com.essence.interfaces.model.ViewHtscEsu;
import com.essence.interfaces.param.ViewHtscEsp;


/**
 * 服务层
 * @author majunjie
 * @since 2024-09-13 16:41:10
 */
public interface ViewHtscService extends BaseApi<ViewHtscEsu, ViewHtscEsp, ViewHtscEsr> {
    Boolean selectHtsc(Hthsz hthsz);
}
