package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.FloodDesigRainPatternEsrVo;
import com.essence.interfaces.model.StDesigRainPatternEsr;
import com.essence.interfaces.model.StDesigRainPatternEsu;
import com.essence.interfaces.param.StDesigRainPatternEsp;

import java.util.List;

;

/**
 * 设计雨型服务层
 * @author majunjie
 * @since 2023-04-24 09:57:38
 */
public interface StDesigRainPatternService extends BaseApi<StDesigRainPatternEsu, StDesigRainPatternEsp, StDesigRainPatternEsr> {
    String addRainFallPattern(StDesigRainPatternEsu stDesigRainPatternEsu);

    List<FloodDesigRainPatternEsrVo> selectDesigRainPattern(String hourCount);
}
