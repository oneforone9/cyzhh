package com.essence.interfaces.api;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWellcollectFeeEsr;
import com.essence.interfaces.model.StWellcollectFeeEsu;
import com.essence.interfaces.param.StWellcollectFeeEsp;

/**
 * 服务层
 * @author bird
 * @since 2023-01-04 18:01:12
 */
public interface StWellcollectFeeService extends BaseApi<StWellcollectFeeEsu, StWellcollectFeeEsp, StWellcollectFeeEsr> {
    ResponseResult selectStWellcollectFee(String year);

}
