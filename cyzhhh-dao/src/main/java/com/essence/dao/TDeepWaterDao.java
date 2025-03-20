package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.TDeepWaterDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地下水埋深(TDeepWater)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-04 14:46:12
 */
@Mapper
public interface TDeepWaterDao extends BaseDao<TDeepWaterDto> {
}
