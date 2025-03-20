package com.essence.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essence.dao.entity.StWaterRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 水位站和流量站数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:10
 */
@Mapper
public interface StWaterRateDao extends BaseMapper<StWaterRateEntity> {
    @Select("<script>" +
            "INSERT INTO st_water_rate (id,pid,type,addr,addrv,ctime,did,moment_rate,pre_moment_rate,moment_river_position,voltage ) VALUES " +
            "( #{stWaterRateEntitys.id}, #{stWaterRateEntitys.pid},#{stWaterRateEntitys.type},#{stWaterRateEntitys.addr},#{stWaterRateEntitys.addrv},#{stWaterRateEntitys.ctime},#{stWaterRateEntitys.did},#{stWaterRateEntitys.momentRate},#{stWaterRateEntitys.preMomentRate},#{stWaterRateEntitys.momentRiverPosition}," +
            "#{stWaterRateEntitys.voltage})" +
            "</script>")
    void insertStWaterRate(@Param("stWaterRateEntitys") StWaterRateEntity stWaterRateEntitys);

    @Select("SELECT * From st_water_rate WHERE did=#{did} AND ctime>#{now} ORDER BY ctime DESC LIMIT 1")
    List<StWaterRateEntity> selectByOneByDid(@Param("did") String did,@Param("now") Date now);
}
