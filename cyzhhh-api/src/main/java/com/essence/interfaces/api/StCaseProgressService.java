package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCaseProgressEsr;
import com.essence.interfaces.model.StCaseProgressEsu;
import com.essence.interfaces.param.StCaseProgressEsp;

/**
 * 方案执行进度表服务层
 * @author BINX
 * @since 2023-04-18 17:03:05
 */
public interface StCaseProgressService extends BaseApi<StCaseProgressEsu, StCaseProgressEsp, StCaseProgressEsr> {
}
