package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterDispatchDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StWaterDispatch)表数据库访问层
 *
 * @author majunjie
 * @since 2023-05-08 14:26:18
 */
@Mapper
public interface StWaterDispatchDao extends BaseDao<StWaterDispatchDto> {
}
