package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPondBaseDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 易积滞水点基础表(StPondBase)表数据库访问层
 *
 * @author liwy
 * @since 2023-04-03 14:45:31
 */
@Mapper
public interface StPondBaseDao extends BaseDao<StPondBaseDto> {
}
