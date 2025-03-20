package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterEngineeringSchedulingLeadDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水系联调-工程调度(StWaterEngineeringSchedulingLead)表数据库访问层
 *
 * @author majunjie
 * @since 2023-07-03 17:24:45
 */
@Mapper
public interface StWaterEngineeringSchedulingLeadDao extends BaseDao<StWaterEngineeringSchedulingLeadDto> {
}
