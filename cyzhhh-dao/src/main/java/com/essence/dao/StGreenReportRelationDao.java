package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StGreenReportRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 绿化保洁工作日志上报表-关联表(StGreenReportRelation)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-17 17:19:46
 */
@Mapper
public interface StGreenReportRelationDao extends BaseDao<StGreenReportRelationDto> {
}
