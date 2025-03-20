package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WorkorderRecordGeom;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单地理信息记录表-记录工单创建时的重点位地理信息(WorkorderRecordGeom)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-30 16:57:21
 */


@Mapper
public interface WorkorderRecordGeomDao extends BaseDao<WorkorderRecordGeom> {

}
