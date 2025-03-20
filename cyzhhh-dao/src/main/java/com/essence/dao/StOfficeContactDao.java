package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StOfficeContactDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StOfficeContact)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-29 18:49:47
 */
@Mapper
public interface StOfficeContactDao extends BaseDao<StOfficeContactDto> {
}
