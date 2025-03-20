package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterEngineeringSchedulingDataDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 防汛调度方案指令下发记录(StWaterEngineeringSchedulingData)表数据库访问层
 *
 * @author majunjie
 * @since 2023-05-14 18:15:41
 */
@Mapper
public interface StWaterEngineeringSchedulingDataDao extends BaseDao<StWaterEngineeringSchedulingDataDto> {
}
