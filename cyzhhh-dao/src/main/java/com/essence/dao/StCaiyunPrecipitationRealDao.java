package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealDto;
import com.essence.dao.entity.vo.CaiyunPrecipitationRealVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 彩云预报实时数据 (st_caiyun_precipitation_real)表数据库访问层
 *
 * @author BINX
 * @since 2023年5月4日 下午3:47:15
 */
@Mapper
public interface StCaiyunPrecipitationRealDao extends BaseDao<StCaiyunPrecipitationRealDto> {
    @Insert("REPLACE INTO st_caiyun_precipitation_real(id,mesh_id,drp,drp_time,create_time) " +
            "VALUES (#{stCaiyunPrecipitationRealDto.id},#{stCaiyunPrecipitationRealDto.meshId}," +
            "#{stCaiyunPrecipitationRealDto.drp},#{stCaiyunPrecipitationRealDto.drpTime},#{stCaiyunPrecipitationRealDto.createTime})")
    public void saveOrUpdate(@Param("stCaiyunPrecipitationRealDto") StCaiyunPrecipitationRealDto stCaiyunPrecipitationRealDto);

    //WHERE type>='1'
    @Select("SELECT sth.*,scm.stcd,scm.stnm FROM `st_caiyun_precipitation_real` AS sth  LEFT JOIN st_caiyun_mesh AS scm ON sth.mesh_id = scm.mesh_id  WHERE sth.mesh_id IN(SELECT mesh_id FROM st_caiyun_mesh  )")
    public List<StCaiyunPrecipitationRealDto> selectOfGrid();

    @Select("SELECT sth.*,scm.stcd,scm.stnm FROM `st_caiyun_precipitation_real` AS sth  LEFT JOIN st_caiyun_mesh AS scm ON sth.mesh_id = scm.mesh_id  WHERE sth.mesh_id IN(SELECT mesh_id FROM st_caiyun_mesh WHERE type>='1' )")
    public List<StCaiyunPrecipitationRealDto> selectOfGrid59();


    /**
     * 返回彩云天气对应的雨量和测站相关信息
     */
    List<CaiyunPrecipitationRealVo> getStationAndRainGrid(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
