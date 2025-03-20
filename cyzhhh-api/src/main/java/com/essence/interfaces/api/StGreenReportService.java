package com.essence.interfaces.api;


import com.essence.common.utils.PageUtil;
import com.essence.dao.entity.StGreenReportDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StGreenReportEsr;
import com.essence.interfaces.model.StGreenReportEsu;
import com.essence.interfaces.model.StGreenReportParam;
import com.essence.interfaces.model.StGreenReportParam2;
import com.essence.interfaces.param.StGreenReportEsp;

/**
 * 绿化保洁工作日志上报表服务层
 * @author liwy
 * @since 2023-03-14 15:36:45
 */
public interface StGreenReportService extends BaseApi<StGreenReportEsu, StGreenReportEsp, StGreenReportEsr> {
    /**
     * 新增日志
     * @param stGreenReportEsu
     * @return
     */
    Object addStGreenReport(StGreenReportEsu stGreenReportEsu);

    /**
     * 日志详情
     * @param id
     * @return
     */
    Object searchById(String id);

    /**
     * 绿化保洁工作报告汇总列表
     * @param stGreenReportParam
     * @return
     */
    Paginator<StGreenReportEsr>  searchAll(StGreenReportParam stGreenReportParam);

    /**
     * 作业日历
     * @param stGreenReportParam
     * @return
     */
    Object searchByCondition(StGreenReportParam2 stGreenReportParam);
}
