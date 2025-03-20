package com.essence.interfaces.api;

import com.essence.dao.entity.WorkorderNewest;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.PortalEventCountDto;
import com.essence.interfaces.dot.PortalUserCountDto;
import com.essence.interfaces.dot.WorkMarkRequestDto;
import com.essence.interfaces.dot.WorkMarkResDto;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.WorkorderNewestEsr;
import com.essence.interfaces.model.WorkorderNewestEsu;
import com.essence.interfaces.param.WorkorderNewestEsp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工单最新状态服务
 *
 * @author zhy
 * @since 2022/10/28 14:46
 */
public interface WorkorderNewestService extends BaseApi<WorkorderNewestEsu, WorkorderNewestEsp, WorkorderNewestEsr> {
    List<WorkMarkResDto> getWorkPortal(WorkMarkRequestDto workMarkRequestDto);

    PortalUserCountDto getPortalCount(String unitId);


    List<PortalEventCountDto> getRiverEventStatistic(String start, String end, String type);

    List<PortalEventCountDto> getRiverSignPointStatistic(String start, String end);

    PortalUserCountDto getPortalUserCount(String unitId);

    /**
     * 查询 未巡河河段
     * @param workMarkRequestDto
     * @return
     */
    List<WorkorderNewest>  unRiver(WorkMarkRequestDto workMarkRequestDto);

    Paginator<WorkorderNewestEsr> findByPaginatorByCompanyId(PaginatorParam param);

    List<WorkorderNewest> selectWorkorderBase(String flag, Date start, Date end);

    Map<String,Object> findByPaginatorCount(PaginatorParam param);
}
