package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StForeseeProjectEsr;
import com.essence.interfaces.model.StForeseeProjectEsu;
import com.essence.interfaces.model.StForeseeProjectSelect;
import com.essence.interfaces.param.StForeseeProjectEsp;

import java.util.List;


/**
 * 预设调度方案服务层
 * @author majunjie
 * @since 2023-04-24 11:21:18
 */
public interface StForeseeProjectService extends BaseApi<StForeseeProjectEsu, StForeseeProjectEsp, StForeseeProjectEsr> {
    List<StForeseeProjectEsr> selectStForeseeProject(StForeseeProjectSelect stForeseeProjectSelect);

    String saveStForeseeProject(List<StForeseeProjectEsr> stForeseeProjectEsrList);
}
