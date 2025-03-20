package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StLibraryTypeDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文库类型基础表(StLibraryType)表数据库访问层
 *
 * @author liwy
 * @since 2023-08-17 10:31:14
 */
@Mapper
public interface StLibraryTypeDao extends BaseDao<StLibraryTypeDto> {
}
