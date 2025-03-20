package com.essence.dao;


import com.essence.entity.UserWaterDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用水户取水量(UserWater)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-04 17:50:29
 */
@Mapper
public interface UserWaterDao extends BaseDao<UserWaterDto> {
}
