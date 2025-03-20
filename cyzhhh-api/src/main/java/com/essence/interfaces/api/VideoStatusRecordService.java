package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StatisticsBase;
import com.essence.interfaces.model.VideoStatusRecordEsr;
import com.essence.interfaces.model.VideoStatusRecordEsu;
import com.essence.interfaces.param.VideoStatusRecordEsp;

import java.util.List;

/**
 * 视频状态记录表服务层
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */
public interface VideoStatusRecordService extends BaseApi<VideoStatusRecordEsu, VideoStatusRecordEsp, VideoStatusRecordEsr> {

    /**
     * 获取摄像头最新状态
     * @return
     */
    List<VideoStatusRecordEsr> latestStauts();

    /**
     * 获取的摄像头的在线状态（不包含利旧摄像头）
     * @return
     */
    List<VideoStatusRecordEsr> latestStauts2();

    /**
     * 统计摄像最近时间内在线/离线个数
     * @return
     */
    List<StatisticsBase> countLatestStatus();

    /**
     * 摄像头在线/离线统计（总）智能摄像头+利旧摄像头
     * @return
     */
    List<StatisticsBase> countLatestStatusAll();

    /**
     * 获取萤石云转code摄像头最新状态
     * @return
     */
    List<VideoStatusRecordEsr> latestYSYStauts();

    /**
     * 获取宇视云转code摄像头最新状态
     * @return
     */
    List<VideoStatusRecordEsr> latestYUSYStauts();
}
