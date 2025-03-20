package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.VideoDrawImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 视频管理线保护线图表(VideoDrawImage)表数据库访问层
 *
 * @author BINX
 * @since 2023-02-03 17:10:18
 */
@Mapper
public interface VideoDrawImageDao extends BaseDao<VideoDrawImageDto> {

    /**
     * 根据视频编码code获取所有的频图片
     * @param videoCode
     * @return
     */
    @Select("select * from video_draw_image  where  video_code = #{videoCode} order by gmt_create desc")
    List<VideoDrawImageDto> selectByVideoCode(String videoCode);
}
