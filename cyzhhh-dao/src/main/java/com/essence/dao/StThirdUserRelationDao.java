package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StThirdUserRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StThirdUserRelation)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-04 14:45:10
 */
@Mapper
public interface StThirdUserRelationDao extends BaseDao<StThirdUserRelationDto> {
}
