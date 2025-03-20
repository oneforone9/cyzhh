package com.essence.dao;


import com.essence.entity.VideoStatusRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 视频状态记录表(VideoStatusRecord)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */


@Mapper
public interface VideoStatusRecordDao extends BaseDao<VideoStatusRecord> {

    /**
     * 获取最新摄像头状态
     * @return
     */
    @Select("select a.* from video_status_record a , ( select video_id, max(tm) as tm  from video_status_record group by video_id) b where a.video_id = b.video_id and a.tm = b.tm")
    List<VideoStatusRecord>  latestStauts();

}
