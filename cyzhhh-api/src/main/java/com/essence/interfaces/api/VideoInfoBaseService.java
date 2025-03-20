package com.essence.interfaces.api;

import com.essence.common.dto.CameraPortalDTO;
import com.essence.common.utils.PageUtil;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.dot.VideoStatusRecordHistoryDto;
import com.essence.interfaces.dot.VideoYsYDto;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.entity.PieChartDto;
import com.essence.interfaces.entity.VideoInfoBaseDto;
import com.essence.interfaces.model.VideoInfoBaseEsr;
import com.essence.interfaces.model.VideoInfoBaseEsu;
import com.essence.interfaces.model.YuShiCodeModel;
import com.essence.interfaces.model.YuShiVideoModel;
import com.essence.interfaces.param.VideoInfoBaseEsp;

import java.util.List;
import java.util.Map;

/**
 * 视频基础信息表服务层
 *
 * @author zhy
 * @since 2022-10-20 14:20:46
 */
public interface VideoInfoBaseService extends BaseApi<VideoInfoBaseEsu, VideoInfoBaseEsp, VideoInfoBaseEsr> {

    /**
     * 根据河道类别，查询AI摄像列表
     * @param type 0(全部) or 1(河) or  2(沟) or 3(渠)
     * @return
     */
    List<PieChartDto<VideoInfoBaseDto>> queryVideoByRiverType(Integer type);

    /**
     * 根据河道类别，查询AI摄像列表
     * @param type 功能类型｜ 1-功能   2-安防  3-井房
     * @param name
     * @return
     */
    List<PieChartDto<VideoInfoBaseDto>> queryVideoByFunction(Integer type, String name, String unitId);

    List<PieChartDto<VideoInfoBaseDto>> findByFunctionAlarm(Integer type, String name, String unitId, String riverId, Double lgtd, Double lttd);

    /**
     * 根据条件分页查询包含在线状态
     * @param param
     * @return
     */
    Paginator<VideoInfoBaseEsr> searchAll(PaginatorParam param);

    PageUtil<VideoYsYDto> getYinShi(PaginatorParam param, String unitId);

    PageUtil<VideoYsYDto> getYinShiNew(PaginatorParam param, String unitId, String name);

    List<VideoStatusRecordHistoryDto> getVideoStatusHistory(String start, String end);

    List<CameraPortalDTO> getCameraPortal(String start, String end);

    List<String> getCameraWarningInfo();


    List<VideoInfoBaseDto> getYinShiCode();

    VideoInfoBaseEsr insertItem(VideoInfoBaseEsu videoInfoBaseEsu);

    /**
     * 根据code查询摄像头基础信息
     *
     * @param code code
     */
    VideoInfoBaseDto getByCode(String code);

    List<VideoInfoBaseDto> getYuShiCode();

    Object getYuSYUrl(YuShiCodeModel yuShiCodeModel);

    String getYuShiToken();
}
