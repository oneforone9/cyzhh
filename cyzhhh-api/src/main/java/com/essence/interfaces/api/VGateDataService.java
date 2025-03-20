package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StSideGateEsuParam;
import com.essence.interfaces.model.StSideGateEsuParamv;
import com.essence.interfaces.model.VGateDataEsr;
import com.essence.interfaces.model.VGateDataEsu;
import com.essence.interfaces.param.VGateDataEsp;

/**
 * 服务层
 * @author majunjie
 * @since 2023-04-20 17:35:48
 */
public interface VGateDataService extends BaseApi<VGateDataEsu, VGateDataEsp, VGateDataEsr> {
    /**
     * 闸坝运行工况
     * @param stSideGateEsuParamv
     * @return
     */
    Object selectvGateDataList2(StSideGateEsuParamv stSideGateEsuParamv);

    /**
     *  获取闸坝运行工况根据 stcd
     * @param stcd
     * @return
     */
    Object selectvGateData(String stcd);
}
