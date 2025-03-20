package com.essence.dao;


import com.essence.dao.basedao.BaseDao;
import com.essence.dao.entity.VideoStatusRecord;
import com.essence.dao.entity.VideoStatusRecordHistory;
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
public interface VideoStatusRecordHistoryDao extends BaseDao<VideoStatusRecordHistory> {

}
