package com.essence.interfaces.api;


import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWaterEngineeringSchedulingDataEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingDataEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingDataEsp;


/**
 * 防汛调度方案指令下发记录服务层
 * @author majunjie
 * @since 2023-05-14 18:15:44
 */
public interface StWaterEngineeringSchedulingDataService extends BaseApi<StWaterEngineeringSchedulingDataEsu, StWaterEngineeringSchedulingDataEsp, StWaterEngineeringSchedulingDataEsr> {

    void insertFloodDispatch(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto );

    void insertFloodDispatchs(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto);

    void insertFloodDispatchX(StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto);

    void insertFloodDispatchReceive(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto );
}
