package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StLevelRainDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * (StLevelRain)表数据库访问层
 *
 * @author BINX
 * @since 2023-03-08 11:31:50
 */
@Mapper
public interface StLevelRainDao extends BaseDao<StLevelRainDto> {

    /**
     * 编辑小时降雨量
     * @param id
     * @param lower1
     * @param upper1
     * @return
     */
    @Update("UPDATE st_level_rain SET lower1=#{lower1},upper1=#{upper1} WHERE id=#{id}")
    int editStLevelRain(@Param("id")Integer id, @Param("lower1")Double lower1, @Param("upper1")Double upper1);

    /**
     * 编辑3小时降雨量
     * @param id
     * @param lower3
     * @param upper3
     * @return
     */
    @Update("UPDATE st_level_rain SET lower3=#{lower3},upper3=#{upper3} WHERE id=#{id}")
    int editStLevelRain3(@Param("id")Integer id, @Param("lower3")Double lower3, @Param("upper3")Double upper3);

    /**
     * 编辑12小时降雨量
     * @param id
     * @param lower12
     * @param upper12
     * @return
     */
    @Update("UPDATE st_level_rain SET lower12=#{lower12},upper12=#{upper12} WHERE id=#{id}")
    int editStLevelRain12(@Param("id")Integer id, @Param("lower12")Double lower12, @Param("upper12")Double upper12);

    /**
     * 编辑24小时降雨量
     * @param id
     * @param lower24
     * @param upper24
     * @return
     */
    @Update("UPDATE st_level_rain SET lower24=#{lower24},upper24=#{upper24} WHERE id=#{id}")
    int editStLevelRain24(@Param("id")Integer id, @Param("lower24")Double lower24, @Param("upper24")Double upper24);
}
