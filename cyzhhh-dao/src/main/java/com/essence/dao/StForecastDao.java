package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StForecastDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 预警报表(StForecast)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-17 19:38:19
 */
@Mapper
public interface StForecastDao extends BaseDao<StForecastDto> {
    @Update("<script>" +
            "update st_forecast set state = 1 WHERE forecast_id = #{forecastId} " +
            "</script>")
    void updateStForecastDao(@Param("forecastId") String forecastId);
    @Update("<script>" +
            "update st_forecast set reception = 1 ,forecast_state = 1  WHERE forecast_id = #{forecastId} " +
            "</script>")
    void updateStForecastDaos(@Param("forecastId")String forecastId);

    @Select("<script>" +
            "INSERT INTO st_forecast (forecast_id,forecast_type,stcd,stnm,rvnm,source_type,xcfzr,xcfzr_phone,xcfzr_user_id,police_time,forecast_state" +
            ",device_type,reason,stcd_id,river,lgtd ,lttd,dispose,dispose_phone,state,reception) VALUES " +
            "<foreach item='item' index='index' collection='param'  separator=','  >" +
            "( #{item.forecastId},#{item.forecastType},#{item.stcd}, #{item.stnm},#{item.rvnm},#{item.sourceType},#{item.xcfzr},#{item.xcfzrPhone},#{item.xcfzrUserId},#{item.policeTime},#{item.forecastState}," +
            "#{item.deviceType}, #{item.reason},#{item.stcdId}, #{item.river},#{item.lgtd},#{item.lttd}, #{item.dispose},#{item.disposePhone}, #{item.state},#{item.reception} )" +
            "</foreach> "+
            "</script>")
    void insertStForecast(@Param("param")List<StForecastDto> stForecastDtoList);
}
