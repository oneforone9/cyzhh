package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.alg.StCaseResDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 方案执行结果表(StCaseRes)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-18 14:39:13
 */
@Mapper
public interface StCaseResDao extends BaseDao<StCaseResDto> {
    @Select("<script>" +
            "INSERT INTO st_case_res (id,case_id,step,rv_id,section_name,river_z,river_h,river_q,river_v,river_a,river_w" +
            ",create_time,stcd,stnm,step_time,data_type) VALUES " +
            "<foreach item='item' index='index' collection='StCaseResDto'  separator=','  >" +
            "( #{item.id}, #{item.caseId},#{item.step},#{item.rvId},#{item.sectionName},#{item.riverZ},#{item.riverH},#{item.riverQ},#{item.riverV},#{item.riverA}," +
            "#{item.riverW}, #{item.createTime},#{item.stcd}, #{item.stnm} , #{item.stepTime}, #{item.dataType})" +
            "</foreach> "+
            "</script>")
    void saveFloodModelOutCellStatistic(@Param("StCaseResDto") List<StCaseResDto> StCaseResDto);
}
