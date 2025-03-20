package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StGatedamReportRelationEsr;
import com.essence.interfaces.model.StGatedamReportRelationEsu;
import com.essence.interfaces.param.StGatedamReportRelationEsp;

/**
 * 闸坝运行维保日志上报-关联表服务层
 * @author liwy
 * @since 2023-03-15 11:57:13
 */
public interface StGatedamReportRelationService extends BaseApi<StGatedamReportRelationEsu, StGatedamReportRelationEsp, StGatedamReportRelationEsr> {
}
