package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StEmergencyDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 抢险队基本信息表(StEmergency)表数据库访问层
 *
 * @author liwy
 * @since 2023-04-13 16:14:53
 */
@Mapper
public interface StEmergencyDao extends BaseDao<StEmergencyDto> {
}
