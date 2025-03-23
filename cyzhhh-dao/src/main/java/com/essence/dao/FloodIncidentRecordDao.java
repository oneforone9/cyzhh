package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.FloodIncident;
import com.essence.dao.entity.FloodIncidentRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积水点事件
 *
 * @author zhy
 * @since 2024-07-17 19:32:40
 */
@Mapper
public interface FloodIncidentRecordDao extends BaseDao<FloodIncidentRecord> {
}
