package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 防汛调度-方案基础表(StCaseBaseInfo)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-17 16:29:54
 */
@Mapper
public interface StCaseBaseInfoDao extends BaseDao<StCaseBaseInfoDto> {
}
