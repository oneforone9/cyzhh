package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StWaterDispatchEsp;

import java.util.List;


/**
 * 服务层
 * @author majunjie
 * @since 2023-05-08 14:26:25
 */
public interface StWaterDispatchService extends BaseApi<StWaterDispatchEsu, StWaterDispatchEsp, StWaterDispatchEsr> {
    StWaterDispatchEsr addWaterPlan(StWaterDispatchEsu stWaterDispatchEsu);

    List< StWaterDispatchFlow> stWaterDispatchFlowList(String stcd);

    List<StWaterDispatchDP> stWaterDispatchDp(String riverId);

    List<StWaterDispatchLevelList> stWaterLevelByZB(String riverId);

    List<StWaterDispatchLevel> stWaterLevelByStcd(Integer stcd);

    List<StWaterDispatchZBLevel> stWaterLevelZBByStcd(StWaterDispatchZBLevelQuery stWaterDispatchZBLevelQuery);

}
