package com.essence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.WeatherForecastEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeatherForecastDao extends BaseMapper<WeatherForecastEntity> {
}
