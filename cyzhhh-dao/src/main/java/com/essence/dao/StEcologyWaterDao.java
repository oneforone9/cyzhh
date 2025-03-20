package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StEcologyWaterDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * (StEcologyWater)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-21 16:34:12
 */
@Mapper
public interface StEcologyWaterDao extends BaseDao<StEcologyWaterDto> {
    /**
     * 北部、中部、南部 汇总查询
     * @return
     */
//    @Select("select sum(count_one) as count_one,sum(count_two) as count_two,sum(count_three) as count_three, stew.area from st_ecology_water stew  GROUP BY stew.area")
//    List<StEcologyWaterDto> count();
    @Select("select '工况1'as type,sum(stew.count_one) as north,(select sum(stew.count_one)  from st_ecology_water stew where stew.area =2)as center, " +
            "(select sum(stew.count_one)  from st_ecology_water stew where stew.area =3)as south from st_ecology_water stew where stew.area =1 " +
            "UNION all " +
            "select '工况2'as type,sum(stew.count_two) as north,(select sum(stew.count_two)  from st_ecology_water stew where stew.area =2)as center, " +
            "(select sum(stew.count_two)  from st_ecology_water stew where stew.area =3)as south from st_ecology_water stew where stew.area =1 " +
            "UNION all " +
            "select'工况3'as type,sum(stew.count_three) as north,(select sum(stew.count_three)  from st_ecology_water stew where stew.area =2)as center, " +
            "(select sum(stew.count_three)  from st_ecology_water stew where stew.area =3)as south from st_ecology_water stew where stew.area =1")
    List<Map> count();
}
