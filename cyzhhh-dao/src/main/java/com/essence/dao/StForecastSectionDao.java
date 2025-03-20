package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StForecastSectionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (StForecastSection)表数据库访问层
 *
 * @author BINX
 * @since 2023-04-22 10:55:01
 */
@Mapper
public interface StForecastSectionDao extends BaseDao<StForecastSectionDto> {
    @Select("<script>" +
            "INSERT INTO st_forecast_section (id,sttp,date,section_name,value,update_time,case_id,type,seria_name) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.sttp},#{item.date}, #{item.sectionName},#{item.value},#{item.updateTime},#{item.caseId},#{item.type},#{item.seriaName} )" +
            "</foreach> "+
            "</script>")
    void saveForecastSection(@Param("param")List<StForecastSectionDto> stForecastSectionDtoList);
}
