package com.essence.web.portal;

import com.alibaba.excel.EasyExcel;
import com.essence.common.dto.CameraPortalDTO;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.VideoInfoBaseService;
import com.essence.interfaces.api.VideoStatusRecordService;
import com.essence.interfaces.dot.VideoStatusRecordHistoryDto;
import com.essence.interfaces.dot.VideoYsYDto;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.entity.PieChartDto;
import com.essence.interfaces.entity.VideoInfoBaseDto;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.VideoInfoBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 视频基础信息表管理
 *
 * @author zhy
 * @since 2022-10-20 14:20:48
 */
@RestController
@RequestMapping("/video")
public class VideoInfoBaseController extends BaseController<Long, VideoInfoBaseEsu, VideoInfoBaseEsp, VideoInfoBaseEsr> {
    @Autowired
    private VideoInfoBaseService videoInfoBaseService;

    @Autowired
    private VideoStatusRecordService videoStatusRecordService;

    public VideoInfoBaseController(VideoInfoBaseService videoInfoBaseService) {
        super(videoInfoBaseService);
    }

    /**
     * 根据河道类别，查询AI摄像列表
     *
     * @param type 河道类型｜0(全部) or 1(河) or  2(沟) or 3(渠)
     * @return
     */
    @GetMapping("/find/{type}")
    @ResponseBody
    public ResponseResult<List<PieChartDto<VideoInfoBaseDto>>> find(@PathVariable Integer type) {
        return ResponseResult.success("查询成功", videoInfoBaseService.queryVideoByRiverType(type));
    }


    /**
     * 根据河道类别，查询AI摄像列表
     *
     * @param type 功能类型｜ 1-功能   2-安防  3-井房 4-观鸟区   5-专线  6-内网
     * @return
     */
    @GetMapping("/findByFunction/{type}")
    @ResponseBody
    public ResponseResult<List<PieChartDto<VideoInfoBaseDto>>> findByFunction(@PathVariable Integer type, @RequestParam String name, String unitId) {
        return ResponseResult.success("查询成功", videoInfoBaseService.queryVideoByFunction(type, name, unitId));
    }

    /**
     * 报警关联监控
     *
     * @param unitId
     * @return
     */
    @GetMapping("/findByFunctionAlarm")
    @ResponseBody
    public ResponseResult<List<PieChartDto<VideoInfoBaseDto>>> findByFunctionAlarm(String unitId, @RequestParam String riverId,
                                                                                   @RequestParam Double lgtd, @RequestParam Double lttd) {
        return ResponseResult.success("查询成功", videoInfoBaseService.findByFunctionAlarm(1, null, unitId, riverId, lgtd, lttd));
    }

    /**
     * 摄像头在线/离线统计（总）
     *
     * @return
     */
    @GetMapping("/count")
    @ResponseBody
    public ResponseResult<List<StatisticsBase>> count() {
        return ResponseResult.success("查询成功", videoStatusRecordService.countLatestStatus());
    }

    /**
     * 摄像头在线/离线统计（总）智能摄像头+利旧摄像头
     *
     * @return
     */
    @GetMapping("/countAll")
    @ResponseBody
    public ResponseResult<List<StatisticsBase>> countAll() {
        return ResponseResult.success("查询成功", videoStatusRecordService.countLatestStatusAll());
    }

    /**
     * 摄像头在线/离线统计（根据河流名称）
     *
     * @return
     */
    @GetMapping("/count/reaname")
    @ResponseBody
    public ResponseResult<List<StatisticsBase>> countByReaname() {
        return ResponseResult.success("查询成功", videoStatusRecordService.countLatestStatus());
    }

    /**
     * 摄像头在线/离线统计（根据河流类型）
     *
     * @return
     */
    @GetMapping("/count/type")
    @ResponseBody
    public ResponseResult<List<StatisticsBase>> countBytype() {
        return ResponseResult.success("查询成功", videoStatusRecordService.countLatestStatus());
    }

    /**
     * 根据条件分页查询包含在线状态
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchAll")
    @ResponseBody
    public ResponseResult<Paginator<VideoInfoBaseEsr>> searchAll(@RequestBody PaginatorParam param) {
        Paginator<VideoInfoBaseEsr> p = videoInfoBaseService.searchAll(param);
        return ResponseResult.success("查询成功", p);
    }


    /**
     * 根据条件分页查询包含在线状态
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/yinshi/urlNew")
    @ResponseBody
    public ResponseResult<PageUtil<VideoYsYDto>> getYinShiNew(@RequestBody PaginatorParam param, String unitId, String name) {
        PageUtil<VideoYsYDto> yinShi = videoInfoBaseService.getYinShiNew(param, unitId, name);
        return ResponseResult.success("查询成功", yinShi);
    }

    /**
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end   结束时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    @GetMapping("/history/download")
    @ResponseBody
    public ResponseResult getVideoStatusHistory(String start, String end, HttpServletResponse response) throws IOException {
        List<VideoStatusRecordHistoryDto> videoStatusRecordHistoryDto = videoInfoBaseService.getVideoStatusHistory(start, end);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测站列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), VideoStatusRecordHistoryDto.class).sheet("模板").doWrite(videoStatusRecordHistoryDto);
        return ResponseResult.success("查询成功", null);
    }

    /**
     * 获取摄像头巡河 统计
     *
     * @return
     */
    @GetMapping("camera/portal")
    public ResponseResult<List<CameraPortalDTO>> getCameraPortal(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        List<CameraPortalDTO> res = videoInfoBaseService.getCameraPortal(start, end);
        return ResponseResult.success("查询成功", res);
    }

    /**
     * AI摄像头抓拍到的报警
     *
     * @return
     */
    @GetMapping("camera/warning")
    public ResponseResult<List<String>> getCameraWarningInfo() {
        List<String> res = videoInfoBaseService.getCameraWarningInfo();
        return ResponseResult.success("查询成功", res);
    }

    /**
     * 获取由萤石云转换海康code后的接口数据
     *
     * @return Paginator<R>
     */
    @PostMapping("/yinshi/urlCode")
    @ResponseBody
    public ResponseResult<List<VideoInfoBaseDto>> getYinShiCode() {
        List<VideoInfoBaseDto> yinShi = videoInfoBaseService.getYinShiCode();
        return ResponseResult.success("查询成功", yinShi);
    }

    /**
     * 获取由宇视云接口数据
     *
     * @return Paginator<R>
     */
    @PostMapping("/yushi/urlCode")
    @ResponseBody
    public ResponseResult<List<VideoInfoBaseDto>> getYuShiCode() {
        List<VideoInfoBaseDto> yuShi = videoInfoBaseService.getYuShiCode();
        return ResponseResult.success("查询成功", yuShi);
    }

    /**
     * 新增
     *
     * @param videoInfoBaseEsu
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @Override
    public ResponseResult insert(@RequestBody VideoInfoBaseEsu videoInfoBaseEsu) {
        return ResponseResult.success("添加成功", videoInfoBaseService.insertItem(videoInfoBaseEsu));
    }

    /**
     * 根据code查询摄像头基础信息
     *
     * @param code code
     */
    @GetMapping("/getByCode")
    public ResponseResult<VideoInfoBaseDto> getByCode(@RequestParam(value = "code") String code) {
        return ResponseResult.success("添加成功", videoInfoBaseService.getByCode(code));
    }

    /**
     * 获取由宇视云转发地址
     *
     */
    @PostMapping("/yushi/url")
    @ResponseBody
    public ResponseResult<Object> getYuShiCode(@RequestBody YuShiCodeModel yshiCodeModel) {
        return ResponseResult.success("转发成功", videoInfoBaseService.getYuSYUrl(yshiCodeModel));
    }

    /**
     * 获取由宇视云token
     *
     */
    @PostMapping("/yushi/token")
    @ResponseBody
    public ResponseResult<Object> getYuShiToken() {
        return ResponseResult.success("请求成功", videoInfoBaseService.getYuShiToken());
    }
}
