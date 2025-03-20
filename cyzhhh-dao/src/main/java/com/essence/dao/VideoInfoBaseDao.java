package com.essence.dao;

import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.dao.entity.VideoInfoBaseEX;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bird
 */
@Mapper
public interface VideoInfoBaseDao extends BaseDao<VideoInfoBase> {


    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("select video.*,river.rea_name as river_name from video_info_base video right join t_rea_base river on video.st_b_river_id = river.id " +
            " where rea_type= #{riverType} and  river.id <=31")
    List<VideoInfoBaseEX> queryVideoByRiverType(@Param("riverType") Integer riverType);

    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,river.rea_name as river_name from video_info_base video right join t_rea_base river on video.st_b_river_id = river.id " +
            " where function_type= #{functionType} and  river.id &lt;=31 <when test='name !=null'> and video.name like concat('%',#{name},'%') </when>  " +
            " <when test='unitId !=null'> and video.unit_id = #{unitId} </when>  " +
            " ORDER BY video.sort ASC </script>")
    List<VideoInfoBaseEX> queryVideoByFunction(@Param("functionType") Integer functionType, @Param("name") String name, @Param("unitId") String unitId );
    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,river.rea_name as river_name from video_info_base video right join t_rea_base river on video.st_b_river_id = river.id  " +
            " where function_type= #{functionType} and  river.id = #{riverId} <when test='name !=null'> and video.name like concat('%',#{name},'%') </when>  " +
            " <when test='unitId !=null'> and video.unit_id = #{unitId} </when>  " +
            " ORDER BY video.sort ASC </script>")
    List<VideoInfoBaseEX> findByFunctionAlarm(@Param("functionType") Integer functionType, @Param("name") String name, @Param("unitId") String unitId,
                                              @Param("riverId") String riverId);


    @Results({
            @Result(property = "river_name", column = "river_name"),

    })
    @Select("select video.*,river.rea_name as river_name from video_info_base video right join t_rea_base river on video.st_b_river_id = river.id where river.id <=31 ")
    List<VideoInfoBaseEX> queryAllVideoByRiverType();

    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,region as river_name from video_info_base video where  video.function_type =  #{functionType} <when test='name !=null'> and video.name like concat('%',#{name},'%') </when> </script>")
    List<VideoInfoBaseEX> queryVideoByFunction2(@Param("functionType") Integer functionType, @Param("name") String name);

    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,bird_area as river_name from video_info_base video where  video.bird_area is not null  <when test='name !=null'> and video.name like concat('%',#{name},'%') </when> </script>")
    List<VideoInfoBaseEX> queryVideoByFunction4(@Param("functionType") Integer functionType,  @Param("name") String name);


    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,region as river_name from video_info_base video where  video.source =  'ZX' <when test='name !=null'> and video.name like concat('%',#{name},'%') </when>  " +
            " <when test='unitId !=null'> and video.unit_id = #{unitId} </when> </script>")
    List<VideoInfoBaseEX> queryVideoByFunction5(@Param("source") Integer source,  @Param("name") String name, @Param("unitId") String unitId);


    @Results({
            @Result(property = "river_name", column = "river_name"),
    })
    @Select("<script> select video.*,region as river_name from video_info_base video where  video.source =  'NW' <when test='name !=null'> and video.name like concat('%',#{name},'%') </when> " +
            " <when test='unitId !=null'> and video.unit_id = #{unitId} </when> </script>")
    List<VideoInfoBaseEX> queryVideoByFunction6(@Param("source") Integer source,  @Param("name") String name, @Param("unitId") String unitId);
}
