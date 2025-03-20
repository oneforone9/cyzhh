package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StStbprpAlarmDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水位流量站报警阀值配置表(StStbprpAlarm)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-25 16:56:04
 */
@Mapper
public interface StStbprpAlarmDao extends BaseDao<StStbprpAlarmDto> {
}
