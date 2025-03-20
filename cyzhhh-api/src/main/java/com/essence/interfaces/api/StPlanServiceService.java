package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.EventCompanyEsu;
import com.essence.interfaces.model.StPlanServiceEsr;
import com.essence.interfaces.model.StPlanServiceEsu;
import com.essence.interfaces.param.StPlanServiceEsp;

import java.util.List;

/**
 * 闸坝维护计划基础表服务层
 * @author liwy
 * @since 2023-07-13 14:46:35
 */
public interface StPlanServiceService extends BaseApi<StPlanServiceEsu, StPlanServiceEsp, StPlanServiceEsr> {
    /**
     * 根据类型获取设备设施名称
     * @param stPlanServiceEsu
     * @return
     */
    List<StPlanServiceEsr> selectEquipmentName(StPlanServiceEsu stPlanServiceEsu);

    /**
     * 根据设施名称和类型获取日常维护内容
     * @param stPlanServiceEsu
     * @return
     */
    List<StPlanServiceEsr> selectServiceContent(StPlanServiceEsu stPlanServiceEsu);
}
