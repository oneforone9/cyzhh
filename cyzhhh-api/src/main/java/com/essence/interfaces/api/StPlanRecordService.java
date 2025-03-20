package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StPlanRecordEsr;
import com.essence.interfaces.model.StPlanRecordEsu;
import com.essence.interfaces.param.StPlanRecordEsp;

/**
 * 闸坝计划生成记录表服务层
 * @author liwy
 * @since 2023-07-18 11:16:28
 */
public interface StPlanRecordService extends BaseApi<StPlanRecordEsu, StPlanRecordEsp, StPlanRecordEsr> {
}
