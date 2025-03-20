package com.essence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.DeviceStatusEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceStatusDao extends BaseMapper<DeviceStatusEntity> {
}
