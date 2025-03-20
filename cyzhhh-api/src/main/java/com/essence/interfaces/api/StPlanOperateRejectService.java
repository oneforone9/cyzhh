package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StPlanOperateRejectEsr;
import com.essence.interfaces.model.StPlanOperateRejectEsu;
import com.essence.interfaces.param.StPlanOperateRejectEsp;

import java.util.List;

/**
 * 养护内容-驳回记录表服务层
 *
 * @author BINX
 * @since 2023-09-11 17:52:37
 */
public interface StPlanOperateRejectService extends BaseApi<StPlanOperateRejectEsu, StPlanOperateRejectEsp, StPlanOperateRejectEsr> {
    Object addStPlanOperate(List<StPlanOperateRejectEsu> list);
}
