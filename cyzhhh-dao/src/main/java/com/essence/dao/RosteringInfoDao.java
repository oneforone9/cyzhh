package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RosteringInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员巡河排班信息表(RosteringInfo)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-25 11:22:51
 */


@Mapper
public interface RosteringInfoDao extends BaseDao<RosteringInfo> {

}
