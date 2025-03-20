package com.essence.interfaces.api;


import com.essence.dao.entity.StPlanPersonDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StPlanPersonEsr;
import com.essence.interfaces.model.StPlanPersonEsu;
import com.essence.interfaces.param.StPlanPersonEsp;

import java.util.List;

/**
 * 三方养护人员信息表服务层
 * @author liwy
 * @since 2023-07-17 14:53:45
 */
public interface StPlanPersonService extends BaseApi<StPlanPersonEsu, StPlanPersonEsp, StPlanPersonEsr> {

    /**
     * 新增三方养护人员信息
     * @param stPlanPersonEsu
     * @return
     */
    Object addStPlanPerson(StPlanPersonEsu stPlanPersonEsu);

    /**
     * 修改三方养护人员信息
     * @param stPlanPersonEsu
     * @return
     */
    Object updateStPlanPerson(StPlanPersonEsu stPlanPersonEsu);

    /**
     * 获取三方养护人员的手机号等
     * @param orderManager
     */
    List<StPlanPersonDto> selectStPlanPerson(String orderManager);
}
