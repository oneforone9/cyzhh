package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RiverGateMaxFlowViewDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (RiverGateMaxFlowView)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-25 13:37:01
 */
@Mapper
public interface RiverGateMaxFlowViewDao extends BaseDao<RiverGateMaxFlowViewDto> {
}
