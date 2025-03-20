package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.CyWeatherForecastDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * (CyWeatherForecast)表数据库访问层
 *
 * @author BINX
 * @since 2023-03-16 16:41:54
 */
@Mapper
public interface CyWeatherForecastDao extends BaseDao<CyWeatherForecastDto> {
}
