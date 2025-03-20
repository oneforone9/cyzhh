package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterGaugeDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * (StWaterGauge)电子水尺积水台账表数据库访问层
 *
 * @author liwy
 * @since 2023-05-11 18:39:17
 */
@Mapper
public interface StWaterGaugeDao extends BaseDao<StWaterGaugeDto> {
}
