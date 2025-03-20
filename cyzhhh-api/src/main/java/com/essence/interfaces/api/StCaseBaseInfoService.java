package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCaseBaseInfoEsr;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.interfaces.param.StCaseBaseInfoEsp;

import java.util.List;

/**
 * 防汛调度-方案基础表服务层
 * @author BINX
 * @since 2023-04-17 16:29:55
 */
public interface StCaseBaseInfoService extends BaseApi<StCaseBaseInfoEsu, StCaseBaseInfoEsp, StCaseBaseInfoEsr> {
    List<String> getForecastSectionName();

    void execute(StCaseBaseInfoEsu stCaseBaseInfoEsu);

    String insertCase(StCaseBaseInfoEsu e);
}
