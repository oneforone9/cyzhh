package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.StCaseResParamEsp;
import com.essence.interfaces.model.StCaseResParamEsu;
import com.essence.interfaces.model.StCaseResParamEsr;

import java.util.List;

/**
* 方案执行结果表入参表 (st_case_res_param)表数据库服务层
*
* @author BINX
* @since 2023年4月28日 下午5:45:25
*/
public interface StCaseResParamService extends BaseApi<StCaseResParamEsu, StCaseResParamEsp, StCaseResParamEsr> {

    public void saveOrUpdate(List<StCaseResParamEsu> list);
}