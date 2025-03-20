package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StFloodReportDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 汛情上报表(StFloodReport)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-13 14:26:21
 */
@Mapper
public interface StFloodReportDao extends BaseDao<StFloodReportDto> {
}
