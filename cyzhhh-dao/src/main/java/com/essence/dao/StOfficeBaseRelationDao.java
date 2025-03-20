package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StOfficeBaseRelationDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室联系人表(StOfficeBaseRelation)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-29 14:21:25
 */
@Mapper
public interface StOfficeBaseRelationDao extends BaseDao<StOfficeBaseRelationDto> {
}
