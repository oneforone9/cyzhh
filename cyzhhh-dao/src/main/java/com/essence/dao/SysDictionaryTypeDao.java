package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.SysDictionaryType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表(SysDictionaryType)表数据库访问层
 *
 * @author zhy
 * @since 2022-11-03 17:12:28
 */


@Mapper
public interface SysDictionaryTypeDao extends BaseDao<SysDictionaryType> {

}
