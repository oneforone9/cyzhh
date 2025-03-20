package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.WorkorderBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 工单基础信息表(WorkorderBase)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-27 15:26:21
 */


@Mapper
public interface WorkorderBaseDao extends BaseDao<WorkorderBase> {

    /**
     * 根据案件ids关联查询到查询工单的负责人以及工单的状态
     * @param typeIdsr
     * @return
     */
    @Select("<script> SELECT base.order_type,base.order_manager," +
            " (select newest.order_status from t_workorder_process_newest newest  where newest.order_id = base.id) order_status " +
            " FROM t_workorder_base base WHERE base.event_id in  " +
            " <foreach collection='params' index='index' item='item' open='(' separator=',' close =')'> #{item} </foreach> </script>")
    List<WorkorderBase> selectAll(@Param("params") List<String> typeIdsr);

    @Select("<script> UPDATE t_workorder_base SET  reject_time=#{rejectTime} WHERE id=#{id}  </script>")
    void updateRejectTime(@Param("id") String id, @Param("rejectTime") Date rejectTime);
}
