package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StPumpDataDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (StPumpData)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-14 11:36:07
 */
@Mapper
public interface StPumpDataDao extends BaseDao<StPumpDataDto> {
}
