package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.GateStationOutDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (GateStationOut)表数据库访问层
 *
 * @author BINX
 * @since 2023-05-25 13:37:43
 */
@Mapper
public interface GateStationOutDao extends BaseDao<GateStationOutDto> {
}
