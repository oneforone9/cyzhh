package com.essence.web.portal;


import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.interfaces.api.VideoDrawLineService;
import com.essence.interfaces.model.VideoDrawLineEsr;
import com.essence.interfaces.model.VideoDrawLineEsu;
import com.essence.interfaces.param.VideoDrawLineEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 视频管理线保护线表管理
 * @author BINX
 * @since 2023-02-03 14:50:45
 */
@RestController
@RequestMapping("/videoDrawLine")
public class VideoDrawLineController extends BaseController<Long, VideoDrawLineEsu, VideoDrawLineEsp, VideoDrawLineEsr> {
    @Autowired
    private VideoDrawLineService videoDrawLineService;

    public VideoDrawLineController(VideoDrawLineService videoDrawLineService) {
        super(videoDrawLineService);
    }



    /**
     * 根据视频编码code查询画线数据
     *
     * @param videoCode
     * @return
     */
    @GetMapping("/selectVideoDrawLine")
    public ResponseResult selectVideoDrawLine(HttpServletRequest request, @RequestParam String videoCode) {
        if (null != videoCode) {
            return ResponseResult.success("查询成功", videoDrawLineService.findByVideoCode(videoCode));
        } else {
            return ResponseResult.error("视频编码不能为空", null);
        }
    }

    /**
     * 新增视频画线数据
     *
     * @param videoDrawLineEsu
     * @return
     */
    @PostMapping("/addVideoDrawLine")
    public ResponseResult addVideoDrawLine(HttpServletRequest request, @RequestBody VideoDrawLineEsu videoDrawLineEsu) {
        //先去查看是否已经该视频code的画线数据
        VideoDrawLineDto videoDrawLineDto = videoDrawLineService.findByVideoCode(videoDrawLineEsu.getVideoCode());
        if (null != videoDrawLineDto) {
            //进行修改
            videoDrawLineEsu.setId(videoDrawLineDto.getId());
            return ResponseResult.success("修改成功", videoDrawLineService.update(videoDrawLineEsu));
        } else {
            return ResponseResult.success("添加成功", videoDrawLineService.insert(videoDrawLineEsu));
        }
    }
}
