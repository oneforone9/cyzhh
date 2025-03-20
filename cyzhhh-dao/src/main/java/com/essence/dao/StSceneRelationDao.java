package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSceneRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StSceneRelation)表数据库访问层
 *
 * @author liwy
 * @since 2023-06-01 14:48:21
 */
@Mapper
public interface StSceneRelationDao extends BaseDao<StSceneRelationDto> {
}
