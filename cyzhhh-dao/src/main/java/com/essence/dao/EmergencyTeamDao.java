package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.EmergencyTeam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抢险队伍(EmergencyTeam)表数据库访问层
 *
 * @author zhy
 * @since 2024-07-17 19:32:40
 */
@Mapper
public interface EmergencyTeamDao extends BaseDao<EmergencyTeam> {
}
