package com.essence.web.rest.recive;

import com.essence.common.dto.DtoData;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.third.VideoReceiveService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频漂浮物等监控
 */
@Controller
@RequestMapping("/video")
public class VideoReceiveController {
    @Resource
    private VideoReceiveService videoReceiveService;
    /**
     * 对接第三方数据推送
     *
     * @param dtoDataList
     * @return
     */
    @PostMapping("/warning/info")
    @ResponseBody
    public ResponseResult insert(@RequestBody List<DtoData> dtoDataList) {
        if (dtoDataList != null){
            List<DtoData> list = new ArrayList<>();
            list.addAll(dtoDataList);
            videoReceiveService.dealReceiveDate(list);
        }
        return ResponseResult.success("添加成功",null);
    }
}
