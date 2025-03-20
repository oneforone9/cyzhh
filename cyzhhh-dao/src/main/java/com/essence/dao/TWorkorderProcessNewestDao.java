package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.TWorkorderProcessNewestDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 工单处理过程表(TWorkorderProcessNewest)表数据库访问层
 *
 * @author liwy
 * @since 2023-08-01 16:57:18
 */
@Mapper
public interface TWorkorderProcessNewestDao extends BaseDao<TWorkorderProcessNewestDto> {
}
