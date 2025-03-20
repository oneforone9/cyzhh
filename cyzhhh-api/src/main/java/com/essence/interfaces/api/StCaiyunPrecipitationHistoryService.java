package com.essence.interfaces.api;

import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StCaseResRainQuery;
import com.essence.interfaces.param.StCaiyunPrecipitationHistoryEsp;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsu;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsr;

import java.util.List;

/**
* 彩云预报历史数据 (st_caiyun_precipitation_history)表数据库服务层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
public interface StCaiyunPrecipitationHistoryService extends BaseApi<StCaiyunPrecipitationHistoryEsu, StCaiyunPrecipitationHistoryEsp, StCaiyunPrecipitationHistoryEsr> {

    public void saveOrUpdate(List<StCaiyunPrecipitationHistoryEsu> stCaiyunPrecipitationHistoryEsuList);

    List<StCaiyunPrecipitationHistoryDto> selectRain(StCaseResRainQuery stCaseResRainQuery);
}