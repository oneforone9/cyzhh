package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterEngineeringDispatchDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水系联调-工程调度-调度预案(StWaterEngineeringDispatch)表数据库访问层
 *
 * @author majunjie
 * @since 2023-06-02 12:39:07
 */
@Mapper
public interface StWaterEngineeringDispatchDao extends BaseDao<StWaterEngineeringDispatchDto> {
}
