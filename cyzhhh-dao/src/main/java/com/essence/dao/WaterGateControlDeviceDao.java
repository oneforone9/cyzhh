package com.essence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.WatchAreaEntity;
import com.essence.dao.entity.WaterGateControlDevice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WaterGateControlDeviceDao extends BaseMapper<WaterGateControlDevice> {
}
