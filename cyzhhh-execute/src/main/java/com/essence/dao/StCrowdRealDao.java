package com.essence.dao;

import com.essence.dao.basedao.BaseDao;

import com.essence.entity.StCrowdRealDto;
import org.apache.ibatis.annotations.Mapper;


/**
 * 清水的河 - 实时游客表(StCrowdReal)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-28 11:44:22
 */
@Mapper
public interface StCrowdRealDao extends BaseDao<StCrowdRealDto> {

}
