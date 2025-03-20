package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSideRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StSideRelation)表数据库访问层
 *
 * @author majunjie
 * @since 2023-05-10 17:31:28
 */
@Mapper
public interface StSideRelationDao extends BaseDao<StSideRelationDto> {
}
