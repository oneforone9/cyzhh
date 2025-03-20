package video.controller;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.web.bind.annotation.*;
import video.common.ConvertParamDTO;

import java.io.IOException;

/**
 * @author essence
 * @Classname FormatConvertController
 * @Description TODO
 * @Date 2022/10/9 17:10
 * @Created by essence
 */
@RestController
@RequestMapping("cy")
public class FormatConvertController {


    @PostMapping("convert")
    public void convert(@RequestBody ConvertParamDTO convert) throws IOException {

//        videoConvertUtils.dealRtspToM3U8(convert.getRtspUrl(),convert.getConvertName());
    }

    @GetMapping("test/convert")
    public void testConvert(String cookie) throws IOException {
//        String rtspUrl = ConnectUtils.dealVideoFormat(cookie);
//        System.out.println("controller层rtspUrl："+rtspUrl);
//        VideoConvertUtils.push(rtspUrl,"D:\\u3m8\\0.m3u8");
//
//        System.out.println("controller层处理完成");
    }
}
