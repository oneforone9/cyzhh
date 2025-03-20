package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StCompanyBaseRelation)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-16 12:01:23
 */
@Mapper
public interface StCompanyBaseRelationDao extends BaseDao<StCompanyBaseRelationDto> {
}
