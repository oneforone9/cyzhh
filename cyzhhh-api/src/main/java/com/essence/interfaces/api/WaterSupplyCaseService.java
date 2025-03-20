package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.WaterSupplyCaseEsp;
import com.essence.interfaces.model.WaterSupplyCaseEsu;
import com.essence.interfaces.model.WaterSupplyCaseEsr;

/**
 * 补水口案件计算入参表服务层
 * @author BINX
 * @since 2023-05-04 19:16:15
 */
public interface WaterSupplyCaseService extends BaseApi<WaterSupplyCaseEsu, WaterSupplyCaseEsp, WaterSupplyCaseEsr> {
}
