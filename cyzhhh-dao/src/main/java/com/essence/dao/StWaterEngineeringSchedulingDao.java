package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 水系联调-工程调度 (st_water_engineering_scheduling)表数据库访问层
 *
 * @author BINX
 * @since 2023年5月13日 下午3:50:06
 */
@Mapper
public interface StWaterEngineeringSchedulingDao extends BaseDao<StWaterEngineeringSchedulingDto> {
    @Select("SELECT DISTINCT river_id ,rvnm from st_water_engineering_scheduling ")
    List<StWaterEngineeringSchedulingDto> getRiverList();

    @Select("SELECT DISTINCT stcd ,stnm from st_water_engineering_scheduling ")
    List<StWaterEngineeringSchedulingDto> getStcdList();
    @Update("UPDATE st_water_engineering_scheduling SET state =0 WHERE id=#{stId}")
    void updateStWaterEngineeringScheduling(@Param("stId") String stId);
}
