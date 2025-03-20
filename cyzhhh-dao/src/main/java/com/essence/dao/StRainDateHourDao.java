package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StRainDateHour;
import org.apache.ibatis.annotations.Mapper;

/**
 * 小时雨量(StRainDateHour)表数据库访问层
 *
 * @author tyy
 * @since 2024-07-20 11:04:28
 */
@Mapper
public interface StRainDateHourDao extends BaseDao<StRainDateHour> {
}
