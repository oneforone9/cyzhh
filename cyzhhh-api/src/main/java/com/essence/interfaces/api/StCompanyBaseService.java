package com.essence.interfaces.api;


import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.StCompanyBaseDtos;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.EventCompanyEsr;
import com.essence.interfaces.model.StCompanyBaseEsr;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.interfaces.param.StCompanyBaseEsp;

import java.util.List;

/**
 * 第三方服务公司基础表服务层
 * @author BINX
 * @since 2023-02-16 11:58:31
 */
public interface StCompanyBaseService extends BaseApi<StCompanyBaseEsu, StCompanyBaseEsp, StCompanyBaseEsr> {

    Object addStCompanyBase(StCompanyBaseEsu stCompanyBaseEsu);

    Object updateStCompanyBase(StCompanyBaseEsu stCompanyBaseEsu);

    Object selectById(String id);

    /**
     * 根据条件分页查询
     * @param param
     * @return
     */
    Paginator<StCompanyBaseEsr> searchAll(PaginatorParam param, String orderType);

    /**
     * 根据条件分页查询
     * @param param
     * @param orderType
     * @return
     */
    Paginator<EventCompanyEsr> searchAllR(PaginatorParam param, String orderType, String userId);

    /**
     * 河湖管理-河道绿化保洁（绿化、保洁工单统计）
     * @param unitId
     * @return
     */
    List<StCompanyBaseRelationDto> searchCount(String unitId);

    /**
     * 管河成效-第三方服务公司处理工单统计
     * @param stCompanyBaseDtos
     * @return
     */
    List<StCompanyBaseRelationDto> searchStCompanyBase(StCompanyBaseDtos stCompanyBaseDtos);
    

    /**
     * 管河成效-第三方服务公司案件处理效能统计
     * @param stCompanyBaseDtos
     * @return
     */
    List<StCompanyBaseRelationDto> searchStCompanyBaseDeal(StCompanyBaseDtos stCompanyBaseDtos);

    /**
     * 第三方服务公司管河成效统计-绿化保洁工单
     * @param stCompanyBaseDtos
     * @return
     */
    List<StCompanyBaseRelationDto> searchStCompanyBaseCount(StCompanyBaseDtos stCompanyBaseDtos);

    /**
     * 第三方服务公司处理工单统计 每年的12个月
     * @param stCompanyBaseDtos
     * @return
     */
    List searchStCompanyBaseMouth(StCompanyBaseDtos stCompanyBaseDtos);

    List<StCompanyBaseRelationDto> searchStCompanyBaseDealNew(StCompanyBaseDtos stCompanyBaseDtos);

    List<StCompanyBaseRelationDto> searchStCompanyBaseNew(StCompanyBaseDtos stCompanyBaseDtos);
}
