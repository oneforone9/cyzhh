package com.essence.web.weather;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.RadarImagesService;
import com.essence.interfaces.entity.PaginatorParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 卫星图雷达图图片获取
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/23 11:34
 */
@RestController
@RequestMapping("/radarImage")
@Slf4j
public class RadarImagesController {

    @Autowired
    RadarImagesService radarImagesService;

    /**
     * 华北地区
     * @return
     */
    @PostMapping("/ld")
    public ResponseResult radarLdImage(@RequestBody PaginatorParam param) {
        return ResponseResult.success("获取雷达回波图成功！", radarImagesService.getRadarLdImage(param));
    }

    /**
     * 单站雷达-大兴
     * @return
     */
    @PostMapping("/lddz")
    public ResponseResult radarLddzImage(@RequestBody PaginatorParam param) {
        return ResponseResult.success( "获取雷达回波图成功！", radarImagesService.getRadarLddzImage(param));
    }

    /**
     * 卫星云图
     * @return
     */
    @PostMapping("/wx")
    public ResponseResult radarWxImage(@RequestBody PaginatorParam param) {
        return ResponseResult.success("获取雷达回波图成功！", radarImagesService.getRadarWxImage(param));
    }
}
