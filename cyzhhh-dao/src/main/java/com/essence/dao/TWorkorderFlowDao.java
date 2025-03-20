package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.TWorkorderFlowDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工单处理过程表(TWorkorderFlow)表数据库访问层
 *
 * @author majunjie
 * @since 2023-06-07 10:31:11
 */
@Mapper
public interface TWorkorderFlowDao extends BaseDao<TWorkorderFlowDto> {
    @Select("<script>" +
            "INSERT INTO t_workorder_flow (id,order_id,person_id,person_name,order_time,operation) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.orderId},#{item.personId}, #{item.personName},#{item.orderTime},#{item.operation} )" +
            "</foreach> "+
            "</script>")
    void insertTWorkOrderFlow(@Param("param") List<TWorkorderFlowDto> list);
}
