package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StOpenBaseDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * (StOpenBase)表数据库访问层
 *
 * @author liwy
 * @since 2023-08-23 14:28:23
 */
@Mapper
public interface StOpenBaseDao extends BaseDao<StOpenBaseDto> {
}
