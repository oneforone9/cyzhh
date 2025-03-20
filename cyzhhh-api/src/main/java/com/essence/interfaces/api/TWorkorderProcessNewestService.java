package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.TWorkorderProcessNewestEsr;
import com.essence.interfaces.model.TWorkorderProcessNewestEsu;
import com.essence.interfaces.param.TWorkorderProcessNewestEsp;

/**
 * 工单处理过程表服务层
 * @author liwy
 * @since 2023-08-01 16:59:49
 */
public interface TWorkorderProcessNewestService extends BaseApi<TWorkorderProcessNewestEsu, TWorkorderProcessNewestEsp, TWorkorderProcessNewestEsr> {
}
