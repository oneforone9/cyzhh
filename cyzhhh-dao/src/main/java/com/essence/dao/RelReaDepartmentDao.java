package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RelReaDepartment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 河道部门关系表(RelReaDepartment)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-24 17:39:04
 */


@Mapper
public interface RelReaDepartmentDao extends BaseDao<RelReaDepartment> {

}
