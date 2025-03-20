package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StLevelWaterDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * (StLevelWater)表数据库访问层
 *
 * @author BINX
 * @since 2023-03-08 11:32:36
 */
@Mapper
public interface StLevelWaterDao extends BaseDao<StLevelWaterDto> {
    /**
     * 编辑积水深度
     * @param id
     * @param lower
     * @param upper
     * @return
     */
    @Update("UPDATE st_level_water SET lower=#{lower},upper=#{upper} WHERE id=#{id}")
    int editStLevelWater(@Param("id")Integer id, @Param("lower")Double lower, @Param("upper")Double upper);
}
