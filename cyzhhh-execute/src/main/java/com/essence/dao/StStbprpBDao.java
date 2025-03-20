package com.essence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.entity.StStbprpBEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测站基本属性表用于存储测站的基本信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:11
 */
@Mapper
public interface StStbprpBDao extends BaseMapper<StStbprpBEntity> {
	
}
