package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.alg.StCaseProcessDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 方案执行过程表-存放入参等信息(StCaseProcess)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
@Mapper
public interface StCaseProcessDao extends BaseDao<StCaseProcessDto> {
}
