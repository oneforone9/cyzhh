package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StForecastDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预警报表记录(StForecastData)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-17 19:38:58
 */
@Mapper
public interface StForecastDataDao extends BaseDao<StForecastDataDto> {
    @Select("<script>" +
            "INSERT INTO st_forecast_data (id,forecast_id,stnm,rvnm,forecast_type,reason,forecast_state,time) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.id},#{item.forecastId},#{item.stnm}, #{item.rvnm},#{item.forecastType},#{item.reason},#{item.forecastState},#{item.time})" +
            "</foreach> "+
            "</script>")
    void addStForecastDatas(@Param("param")List<StForecastDataDto> stForecastDataDtoList);
}
