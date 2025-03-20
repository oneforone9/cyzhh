package com.essence.interfaces.api;

import com.essence.common.dto.StatisticsDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.CyCaseBaseEsr;
import com.essence.interfaces.model.CyCaseBaseEsu;
import com.essence.interfaces.param.CyCaseBaseEsp;

import java.util.List;


/**
 * 案件基础表服务层
 *
 * @author zhy
 * @since 2023-01-04 18:13:40
 */
public interface CyCaseBaseService extends BaseApi<CyCaseBaseEsu, CyCaseBaseEsp, CyCaseBaseEsr> {
    /**
     * 根据案件类型统计
     * @return
     */
    List<StatisticsDto> statisticsByCasetype();

    /**
     * 统计一般案件的结案状态
     * @return
     */
    List<StatisticsDto> statisticsByClosingstatus();

}
