package com.essence.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.RainDayCountDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RainDayCountDao extends BaseMapper<RainDayCountDto> {
}
