package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.DepartmentBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 部门信息表(DepartmentBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-24 16:50:27
 */


@Mapper
public interface DepartmentBaseDao extends BaseDao<DepartmentBase> {

    /**
     * 设置巡河组长
     * @param id
     * @param personBaseId
     * @param name
     * @return
     */
    @Update("UPDATE t_department_base SET person_base_id=#{personBaseId},name=#{name} WHERE id=#{id}")
    Integer setDepart(@Param("id") String id, @Param("personBaseId") String personBaseId, @Param("name") String name);

    /**
     * 判断是不是巡河组长
     * @param personBaseId
     * @return
     */
    @Select("SELECT * from t_department_base WHERE person_base_id = #{personBaseId}")
    DepartmentBase selectDepart(@Param("personBaseId") String personBaseId);
}
