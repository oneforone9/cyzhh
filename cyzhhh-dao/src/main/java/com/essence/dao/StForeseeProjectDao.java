package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StForeseeProjectDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预设调度方案(StForeseeProject)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-24 11:21:08
 */
@Mapper
public interface StForeseeProjectDao extends BaseDao<StForeseeProjectDto> {
    @Select("<script>" +
            "INSERT INTO st_foresee_project (id,stcd,sttp,section_name,pre_water_level,open_high,case_id,rvid,rvnm,holes_number,d_heigh" +
            ",d_wide,b_long,b_high,seria_name) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.stcd},#{item.sttp}, #{item.sectionName},#{item.preWaterLevel},#{item.openHigh},#{item.caseId},#{item.rvid},#{item.rvnm},#{item.holesNumber}," +
            "#{item.dHeigh}, #{item.dWide},#{item.bLong}, #{item.bHigh} , #{item.seriaName})" +
            "</foreach> "+
            "</script>")
    void saveStForeseeProject(@Param("param") List<StForeseeProjectDto> stForeseeProjectDtoList);

    @Delete("delete from st_foresee_project where case_id= #{caseId}")
    void deleteStForeseeProject(@Param("caseId") String caseId);
}
