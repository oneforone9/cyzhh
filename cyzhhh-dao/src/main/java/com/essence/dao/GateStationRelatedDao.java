package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.GateStationRelatedDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 闸坝对应流量水位站表(GateStationRelated)表数据库访问层
 *
 * @author BINX
 * @since 2023-06-08 09:48:49
 */
@Mapper
public interface GateStationRelatedDao extends BaseDao<GateStationRelatedDto> {
}
