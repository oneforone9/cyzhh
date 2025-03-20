package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StEcologyWaterEsr;
import com.essence.interfaces.model.StEcologyWaterEsu;
import com.essence.interfaces.param.StEcologyWaterEsp;

/**
 * 服务层
 * @author BINX
 * @since 2023-02-21 16:34:26
 */
public interface StEcologyWaterService extends BaseApi<StEcologyWaterEsu, StEcologyWaterEsp, StEcologyWaterEsr> {

    /**
     * 北部、中部、南部 汇总查询
     * @return
     */
    Object count();
}
