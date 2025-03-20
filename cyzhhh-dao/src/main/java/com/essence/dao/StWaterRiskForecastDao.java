package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.water.StWaterRiskForecastDto;
import org.apache.ibatis.annotations.Mapper;

/**
* 水系联调-模型风险预报 (st_water_risk_forecast)表数据库访问层
*
* @author BINX
* @since 2023年5月11日 下午4:00:24
*/
@Mapper
public interface StWaterRiskForecastDao extends BaseDao<StWaterRiskForecastDto> {

}
