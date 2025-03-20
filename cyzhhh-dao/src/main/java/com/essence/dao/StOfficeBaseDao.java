package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StOfficeBaseDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室基础表(StOfficeBase)表数据库访问层
 *
 * @author liwy
 * @since 2023-03-29 14:20:50
 */
@Mapper
public interface StOfficeBaseDao extends BaseDao<StOfficeBaseDto> {
}
