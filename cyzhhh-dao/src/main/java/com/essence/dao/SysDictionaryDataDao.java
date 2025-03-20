package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.SysDictionaryData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典数据表(SysDictionaryData)表数据库访问层
 *
 * @author zhy
 * @since 2022-11-03 17:12:25
 */


@Mapper
public interface SysDictionaryDataDao extends BaseDao<SysDictionaryData> {

}
