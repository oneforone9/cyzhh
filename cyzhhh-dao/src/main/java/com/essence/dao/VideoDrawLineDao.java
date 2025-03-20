package com.essence.dao;


import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.dao.basedao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 视频管理线保护线表(VideoDrawLine)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-03 14:50:55
 */
@Mapper
public interface VideoDrawLineDao extends BaseDao<VideoDrawLineDto>{

    /**
     * 根据视频编码code查询画线数据
     * @param videoCode
     * @return
     */
    @Select("select * from video_draw_line  where  video_code = #{videoCode} ")
    VideoDrawLineDto findByVideoCode(@Param("videoCode") String videoCode);
}
