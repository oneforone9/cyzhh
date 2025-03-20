package com.essence.dao;


import com.essence.entity.StCrowdDateDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 清水的河 - 用水人数量(StCrowdDate)表数据库访问层
 *
 * @author BINX
 * @since 2023-01-12 17:36:45
 */
@Mapper
public interface StCrowdDateDao extends BaseDao<StCrowdDateDto> {
}
