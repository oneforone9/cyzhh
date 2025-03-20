package com.essence.interfaces.api;

import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealTableDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsu;
import com.essence.interfaces.param.StCaiyunPrecipitationRealEsp;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsu;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsr;

import java.util.List;

/**
* 彩云预报实时数据 (st_caiyun_precipitation_real)表数据库服务层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
public interface StCaiyunPrecipitationRealService extends BaseApi<StCaiyunPrecipitationRealEsu, StCaiyunPrecipitationRealEsp, StCaiyunPrecipitationRealEsr> {
    public void saveOrUpdate(List<StCaiyunPrecipitationRealEsu> stCaiyunPrecipitationRealEsuList);

    List<Shiduan> getGridRainData();

    List<Shiduan> getGridRainData59();

    List<StCaiyunPrecipitationRealTableDto> getTableRainData59();
}