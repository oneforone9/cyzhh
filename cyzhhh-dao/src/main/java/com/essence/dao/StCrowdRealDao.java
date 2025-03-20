package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.StCrowdRealDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 清水的河 - 实时游客表(StCrowdReal)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-28 11:44:22
 */
@Mapper
public interface StCrowdRealDao extends BaseDao<StCrowdRealDto> {

    @Select("select tab.* from st_crowd_real tab, " +
            " (select scr.area,max(scr.date) date  from st_crowd_real scr where scr.date like concat('%',#{date},'%')  GROUP BY scr.area) as aaa " +
            " where tab.area = aaa.area and tab.date = aaa.date  and tab.date like concat('%',#{date},'%')  ORDER BY tab.num DESC")
    List<StCrowdRealDto> getStCrowdReal(@Param("date") String date);


    //@Select("<script> select video.*,region as river_name from video_info_base video where  video.source =  'ZX' <when test='name !=null'> and video.name like concat('%',#{name},'%') </when> </script>")

}
