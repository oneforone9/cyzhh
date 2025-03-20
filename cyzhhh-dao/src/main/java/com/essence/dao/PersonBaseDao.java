package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.PersonBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员基础信息表(PersonBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-20 15:07:19
 */


@Mapper
public interface PersonBaseDao extends BaseDao<PersonBase> {

}
