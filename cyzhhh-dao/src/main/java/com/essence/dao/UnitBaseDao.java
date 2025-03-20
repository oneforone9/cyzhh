package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.UnitBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 单位信息表(UnitBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-20 14:16:34
 */


@Mapper
public interface UnitBaseDao extends BaseDao<UnitBase> {

}
