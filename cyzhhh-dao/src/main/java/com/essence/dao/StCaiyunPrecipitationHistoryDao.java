package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 彩云预报历史数据 (st_caiyun_precipitation_history)表数据库访问层
 *
 * @author BINX
 * @since 2023年5月4日 下午3:47:15
 */
@Mapper
public interface StCaiyunPrecipitationHistoryDao extends BaseDao<StCaiyunPrecipitationHistoryDto> {

    @Insert("REPLACE INTO st_caiyun_precipitation_history(id,mesh_id,drp,drp_time,create_time) " +
            "VALUES (#{stCaiyunPrecipitationHistoryDto.id},#{stCaiyunPrecipitationHistoryDto.meshId}," +
            "#{stCaiyunPrecipitationHistoryDto.drp},#{stCaiyunPrecipitationHistoryDto.drpTime},#{stCaiyunPrecipitationHistoryDto.createTime})")
    public void saveOrUpdate(@Param("stCaiyunPrecipitationHistoryDto") StCaiyunPrecipitationHistoryDto stCaiyunPrecipitationHistoryDto);

    @Select("SELECT sth.*,scm.stcd,scm.stnm,lgtd,lttd FROM `st_caiyun_precipitation_history` AS sth  LEFT JOIN st_caiyun_mesh AS scm ON sth.mesh_id = scm.mesh_id  WHERE sth.mesh_id IN(SELECT mesh_id FROM st_caiyun_mesh WHERE type='1' ) AND sth.drp_time >= #{startTime} AND  sth.drp_time <= #{endTime}")
    public List<StCaiyunPrecipitationHistoryDto> selectByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
