package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WorkorderTrack;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单巡查轨迹(WorkorderTrack)表数据库访问层
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */


@Mapper
public interface WorkorderTrackDao extends BaseDao<WorkorderTrack> {

}
