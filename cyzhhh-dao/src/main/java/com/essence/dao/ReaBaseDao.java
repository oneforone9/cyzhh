package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.ReaBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 河道信息表(ReaBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-18 17:22:21
 */


@Mapper
public interface ReaBaseDao extends BaseDao<ReaBase> {

}
