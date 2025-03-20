package com.essence.interfaces.api;


import com.essence.dao.entity.StLevelWaterDto;
import com.essence.dao.entity.StWaterGaugeDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StWaterGaugeEsr;
import com.essence.interfaces.model.StWaterGaugeEsu;
import com.essence.interfaces.model.StWaterGaugeEsuParam;
import com.essence.interfaces.param.StWaterGaugeEsp;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电子水尺积水台账服务层
 * @author liwy
 * @since 2023-05-11 18:39:50
 */
public interface StWaterGaugeService extends BaseApi<StWaterGaugeEsu, StWaterGaugeEsp, StWaterGaugeEsr> {
    /**
     * 获取道路积水点实时报警状态
     * @return
     */
    List<StWaterGaugeDto> stWaterGaugeNow(String name, String warnStatus);

    /**
     * 根据积水深度判断报警状态
     * @param amount
     * @param stLevelWaterDtoList
     * @return
     */
    String getWarnstatus(BigDecimal amount, List<StLevelWaterDto> stLevelWaterDtoList);

    /**
     * 根据水务感知码获取道路积水点数据
     * @param stWaterGaugeEsuParam
     * @return
     */
    Object stWaterGaugeNowByCondition(StWaterGaugeEsuParam stWaterGaugeEsuParam);
}
