package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StMaterialBaseDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 防汛物资基础表(StMaterialBase)表数据库访问层
 *
 * @author liwy
 * @since 2023-04-13 14:59:23
 */
@Mapper
public interface StMaterialBaseDao extends BaseDao<StMaterialBaseDto> {
}
