package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StSideGateDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 边闸基础表(StSideGate)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-17 11:05:21
 */
@Mapper
public interface StSideGateDao extends BaseDao<StSideGateDto> {
}
