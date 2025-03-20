package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCaseProcessEsr;
import com.essence.interfaces.model.StCaseProcessEsu;
import com.essence.interfaces.param.StCaseProcessEsp;

/**
 * 方案执行过程表-存放入参等信息服务层
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
public interface StCaseProcessService extends BaseApi<StCaseProcessEsu, StCaseProcessEsp, StCaseProcessEsr> {
}
