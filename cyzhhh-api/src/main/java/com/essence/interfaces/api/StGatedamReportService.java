package com.essence.interfaces.api;


import com.essence.common.utils.PageUtil;
import com.essence.dao.entity.StGatedamReportDto;
import com.essence.dao.entity.StGreenReportDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.model.StGatedamReportEsr;
import com.essence.interfaces.model.StGatedamReportEsu;
import com.essence.interfaces.model.StGatedamReportParam;
import com.essence.interfaces.model.StGatedamReportParam2;
import com.essence.interfaces.param.StGatedamReportEsp;

import java.util.Map;

/**
 * 闸坝运行维保日志上报表服务层
 * @author liwy
 * @since 2023-03-15 11:56:30
 */
public interface StGatedamReportService extends BaseApi<StGatedamReportEsu, StGatedamReportEsp, StGatedamReportEsr> {

    /**
     * 新增养维护修日志
     * @param stGatedamReportEsu
     * @return
     */
    Object addStGatedamReport(StGatedamReportEsu stGatedamReportEsu);

    /**
     * 日志详情
     * @param id
     * @return
     */
    Object searchById(String id);

    /**
     * PC端-闸坝运行养护工作报告汇总列表
     * @param stGatedamReportParam
     * @return
     */
    Paginator<StGatedamReportEsr> searchAll(StGatedamReportParam stGatedamReportParam);

    /**
     * 作业日历
     * @param stGatedamReportParam
     * @return
     */
    Object searchByCondition(StGatedamReportParam2 stGatedamReportParam);

    /**
     * 闸坝运行养护统计
     * @param stBRiverId
     * @param unitId
     * @return
     */
    Object searchCount(String stBRiverId, String unitId);

    /**
     * 闸坝运行养护统计_从闸坝养护计划工单获取数据
     * @param stBRiverId
     * @param unitId
     * @return
     */
    Object searchCountNew(String stBRiverId, String unitId);

    /**
     * 获取单个测站最新运行养护记录
     * @param stcd
     * @param stnm
     * @return
     */
    Map<String,Object> searchReportNew(String stcd, String stnm);

    /**
     * 获取单个测站最新运行养护记录_从闸坝养护计划工单获取数据
     * @param stcd
     * @param stnm
     * @return
     */
    Object searchNewReportYh(String stcd, String stnm);
}
