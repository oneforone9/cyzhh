package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StCaseProgressDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 方案执行进度表(StCaseProgress)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-18 17:03:03
 */
@Mapper
public interface StCaseProgressDao extends BaseDao<StCaseProgressDto> {
}
