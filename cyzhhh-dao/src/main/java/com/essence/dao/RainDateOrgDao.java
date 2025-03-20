package com.essence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.RainDateDto;
import com.essence.dao.entity.RainDateOrgDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RainDateOrgDao extends BaseMapper<RainDateOrgDto> {
}
