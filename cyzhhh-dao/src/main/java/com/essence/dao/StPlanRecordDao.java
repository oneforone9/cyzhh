package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPlanRecordDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 闸坝计划生成记录表(StPlanRecord)表数据库访问层
 *
 * @author liwy
 * @since 2023-07-18 11:15:37
 */
@Mapper
public interface StPlanRecordDao extends BaseDao<StPlanRecordDto> {
}
