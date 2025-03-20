package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.UseWaterEsr;
import com.essence.interfaces.model.UseWaterEsu;
import com.essence.interfaces.param.UseWaterEsp;

/**
 * 用水量服务层
 * @author BINX
 * @since 2023-01-04 17:18:04
 */
public interface UseWaterService extends BaseApi<UseWaterEsu, UseWaterEsp, UseWaterEsr> {
}
