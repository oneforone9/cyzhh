package com.essence.interfaces.api;

import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingCodeEsp;


/**
 * 水系联调-工程调度调度指令记录服务层
 * @author majunjie
 * @since 2023-07-04 14:57:22
 */
public interface StWaterEngineeringSchedulingCodeService extends BaseApi<StWaterEngineeringSchedulingCodeEsu, StWaterEngineeringSchedulingCodeEsp, StWaterEngineeringSchedulingCodeEsr> {
    void saveStWaterEngineeringSchedulingCode(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto);

    StWaterEngineeringSchedulingCodeEsr selectFloodDispatchCodeById(String id);
}
