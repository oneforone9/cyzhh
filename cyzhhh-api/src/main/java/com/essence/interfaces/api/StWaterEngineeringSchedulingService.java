package com.essence.interfaces.api;

import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StWaterEngineeringSchedulingEsp;

import java.util.List;

/**
* 水系联调-工程调度 (st_water_engineering_scheduling)表数据库服务层
*
* @author BINX
* @since 2023年5月13日 下午3:50:06
*/
public interface StWaterEngineeringSchedulingService extends BaseApi<StWaterEngineeringSchedulingEsu, StWaterEngineeringSchedulingEsp, StWaterEngineeringSchedulingEsr> {
    List<StWaterEngineeringSchedulingDto> selectEngineeringScheduling(String caseId);

    void selectFloodDispatch(StFloodDispatch stFloodDispatch);

    StWaterEngineeringSchedulingEsr selectFloodDispatchById(String id);

    StWaterEngineeringSchedulingCodeEsr updateFloodDispatch(StWaterEngineeringSchedulingCodeEsu stWaterEngineeringSchedulingCodeEsu);

    List<StWaterEngineeringSchedulingQuery> getRiverList();

    List<StWaterEngineeringSchedulingQuery> getStcdList();

    void getStcdLists();

    void getStcdListss(String stcd);

    void selectFloodDispatchs(StFloodDispatch stFloodDispatch);

}