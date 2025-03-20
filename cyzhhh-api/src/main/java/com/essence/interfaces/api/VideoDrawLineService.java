package com.essence.interfaces.api;


import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.VideoDrawLineEsr;
import com.essence.interfaces.model.VideoDrawLineEsu;
import com.essence.interfaces.param.VideoDrawLineEsp;

/**
 * 视频管理线保护线表服务层
 * @author BINX
 * @since 2023-02-03 14:51:16
 */
public interface VideoDrawLineService extends BaseApi<VideoDrawLineEsu, VideoDrawLineEsp, VideoDrawLineEsr>{

    /**
     * 根据视频编码code查询画线数据
     * @param videoCode
     * @return
     */
    VideoDrawLineDto findByVideoCode(String videoCode);
}
