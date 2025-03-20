package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.interfaces.model.StLevelRainEsr;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.interfaces.param.StLevelRainEsp;

import java.util.List;

/**
 * 服务层
 * @author BINX
 * @since 2023-03-08 11:32:09
 */
public interface StLevelRainService extends BaseApi<StLevelRainEsu, StLevelRainEsp, StLevelRainEsr> {
    /**
     * 编辑小时降雨量
     * @param list
     * @return
     */
    Object editStLevelRain(List<StLevelRainEsu> list);
}
