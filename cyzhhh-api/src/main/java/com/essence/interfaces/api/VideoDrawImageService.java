package com.essence.interfaces.api;


import com.essence.dao.entity.VideoDrawImageDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.VideoDrawImageEsr;
import com.essence.interfaces.model.VideoDrawImageEsu;
import com.essence.interfaces.param.VideoDrawImageEsp;

import java.util.List;

/**
 * 视频管理线保护线图表服务层
 * @author BINX
 * @since 2023-02-03 17:10:54
 */
public interface VideoDrawImageService extends BaseApi<VideoDrawImageEsu, VideoDrawImageEsp, VideoDrawImageEsr> {

    /**
     * 根据视频编码code获取所有的频图片
     * @param videoCode
     * @return
     */
    List<VideoDrawImageDto> selectByVideoCode(String videoCode);
}
