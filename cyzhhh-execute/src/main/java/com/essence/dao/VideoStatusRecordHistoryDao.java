package com.essence.dao;


import com.essence.entity.VideoStatusRecordHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频状态记录表(VideoStatusRecord)表数据库访问层
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */


@Mapper
public interface VideoStatusRecordHistoryDao extends BaseDao<VideoStatusRecordHistory> {

}
