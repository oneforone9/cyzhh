package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterRiskForecastGateDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水系联调-闸坝模型风险预报(StWaterRiskForecastGate)表数据库访问层
 *
 * @author BINX
 * @since 2023-06-20 14:29:30
 */
@Mapper
public interface StWaterRiskForecastGateDao extends BaseDao<StWaterRiskForecastGateDto> {
}
