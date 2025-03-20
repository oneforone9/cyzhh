package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StLevelWaterEsr;
import com.essence.interfaces.model.StLevelWaterEsu;
import com.essence.interfaces.param.StLevelWaterEsp;

import java.util.List;

/**
 * 服务层
 * @author BINX
 * @since 2023-03-08 11:32:45
 */
public interface StLevelWaterService extends BaseApi<StLevelWaterEsu, StLevelWaterEsp, StLevelWaterEsr> {
    Object editStLevelWater(List<StLevelWaterEsu> list);
}
