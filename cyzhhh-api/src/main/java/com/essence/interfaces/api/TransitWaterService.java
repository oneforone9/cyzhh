package com.essence.interfaces.api;

import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.WaterTransitDto;

/**
 * 巡查一览Api
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 10:53
 */
public interface TransitWaterService {
    WaterTransitDto getWaterTransit(StStbprpBEntityDTO stStbprpBEntityDTO);
}
