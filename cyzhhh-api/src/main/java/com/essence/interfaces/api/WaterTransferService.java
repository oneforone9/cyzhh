package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.WaterTransferEsp;
import com.essence.interfaces.model.WaterTransferEsu;
import com.essence.interfaces.model.WaterTransferEsr;

/**
 * 调水表服务层
 * @author BINX
 * @since 2023-05-09 10:08:53
 */
public interface WaterTransferService extends BaseApi<WaterTransferEsu, WaterTransferEsp, WaterTransferEsr> {
}
