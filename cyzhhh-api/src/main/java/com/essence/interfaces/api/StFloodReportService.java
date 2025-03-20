package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StFloodReportEsr;
import com.essence.interfaces.model.StFloodReportEsu;
import com.essence.interfaces.param.StFloodReportEsp;

/**
 * 汛情上报表服务层
 * @author liwy
 * @since 2023-03-13 14:26:48
 */
public interface StFloodReportService extends BaseApi<StFloodReportEsu, StFloodReportEsp, StFloodReportEsr> {

    /**
     * 添加汛情
     * @param stFloodReportEsu
     * @return
     */
    Object addStFloodReport(StFloodReportEsu stFloodReportEsu);

    /**
     * 汛情详情
     * @param id
     * @return
     */
    Object searchById(String id);
}
