package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.StQpModelEsp;
import com.essence.interfaces.model.StQpModelEsu;
import com.essence.interfaces.model.StQpModelEsr;

/**
* 水系联通-预报水位-河段断面关联表 (st_qp_model)表数据库服务层
*
* @author BINX
* @since 2023年5月11日 下午4:34:54
*/
public interface StQpModelService extends BaseApi<StQpModelEsu, StQpModelEsp, StQpModelEsr> {
}