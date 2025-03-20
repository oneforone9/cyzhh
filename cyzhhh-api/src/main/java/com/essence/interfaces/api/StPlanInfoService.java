package com.essence.interfaces.api;


import com.essence.common.utils.PageUtil;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.model.StPlanInfoEsr;
import com.essence.interfaces.model.StPlanInfoEsrRes;
import com.essence.interfaces.model.StPlanInfoEsu;
import com.essence.interfaces.model.StPlanInfoEsuParam;
import com.essence.interfaces.param.StPlanInfoEsp;

/**
 * 闸坝计划排班信息表服务层
 * @author liwy
 * @since 2023-07-13 14:46:10
 */
public interface StPlanInfoService extends BaseApi<StPlanInfoEsu, StPlanInfoEsp, StPlanInfoEsr> {

    /**
     * 查询维护计划列表
     * @param param
     * @return
     */
    PageUtil<StPlanInfoEsrRes> getStPlanInfo(StPlanInfoEsuParam param);
}
