package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StBRiverDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 河系分配表(StBRiver)表数据库访问层
 *
 * @author majunjie
 * @since 2025-01-09 09:08:03
 */
@Mapper
public interface StBRiverDao extends BaseDao<StBRiverDto> {
}
