package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.RelPersonDepartment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人员部门关系表(RelPersonDepartment)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-24 17:42:54
 */


@Mapper
public interface RelPersonDepartmentDao extends BaseDao<RelPersonDepartment> {

}
