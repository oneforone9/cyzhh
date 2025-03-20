package com.essence.dao;

import com.essence.common.dto.SectionWaterQualityDTO;
import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.SectionWaterQuality;
import com.essence.dao.entity.SectionWaterQualityEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author bird
 */
@Mapper
public interface SectionWaterQualityDao extends BaseDao<SectionWaterQuality> {

    /**
     * 根据期间，查询所有断面水质情况
     * @param period
     * @return
     */
    @Select("select base.section_name,base.river_id as riverId,quality.section_id ,base.section_type,quality.quality_period, quality.gmt_create as update_time,quality.quality_level,quality.average_level,base.lgtd, base.lttd " +
            " from section_water_quality  quality "+
            " inner join section_base base  on quality.section_id = base.id "+
            "  where quality.quality_period =  #{period}   "
    )
    List<SectionWaterQualityEx> queryAllSectionWaterQualityByPeriod(@Param("period") String period);

    /**
     * 根据时间查询断面水质状况
     * @param period
     * @return
     */
    @Select("select base.section_name,base.section_type,quality.quality_period,quality.quality_level,quality.average_level,quality.level_remark,base.type" +
            "            from section_water_quality  quality " +
            "            inner join section_base base  on quality.section_id = base.id " +
            "            where quality.quality_period = #{period} "
    )
    List<SectionWaterQualityDTO> findBytime(String period);
}