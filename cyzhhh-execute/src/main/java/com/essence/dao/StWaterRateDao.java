package com.essence.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.entity.StWaterRateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水位站和流量站数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:10
 */
@Mapper
public interface StWaterRateDao extends BaseMapper<StWaterRateEntity> {
	
}
