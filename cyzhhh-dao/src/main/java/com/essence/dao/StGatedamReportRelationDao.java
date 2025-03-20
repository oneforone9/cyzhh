package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StGatedamReportRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 闸坝运行维保日志上报-关联表(StGatedamReportRelation)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-15 11:57:01
 */
@Mapper
public interface StGatedamReportRelationDao extends BaseDao<StGatedamReportRelationDto> {
}
