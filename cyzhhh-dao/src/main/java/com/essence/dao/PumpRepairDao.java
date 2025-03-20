package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.PumpRepairEntity;
import com.essence.dao.entity.WorkorderBase;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author cuiruixiang
 * @since 2022-10-27 15:26:21
 */


@Mapper
public interface PumpRepairDao extends BaseDao<PumpRepairEntity> {

}
