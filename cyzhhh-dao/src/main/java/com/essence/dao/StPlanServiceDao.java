package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanServiceDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 闸坝维护计划基础表(StPlanService)表数据库访问层
 *
 * @author liwy
 * @since 2023-07-13 14:46:23
 */
@Mapper
public interface StPlanServiceDao extends BaseDao<StPlanServiceDto> {
}
