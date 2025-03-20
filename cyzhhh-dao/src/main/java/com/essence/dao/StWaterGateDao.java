package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StWaterGateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (StWaterGate)表数据库访问层
 *
 * @author majunjie
 * @since 2023-04-20 15:36:28
 */
@Mapper
public interface StWaterGateDao extends BaseDao<StWaterGateDto> {

    /**
     * 获取远传数据最新的数据时间
     * @param dateStart
     * @return
     */
    @Select("select max(ctime) FROM st_water_gate  where  ctime >= #{dateStart}")
    String selectMaxTime(@Param("dateStart") String dateStart);
}
