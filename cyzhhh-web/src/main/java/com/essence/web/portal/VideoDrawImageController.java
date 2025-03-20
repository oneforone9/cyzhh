package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.interfaces.api.VideoDrawImageService;
import com.essence.interfaces.model.VideoDrawImageEsr;
import com.essence.interfaces.model.VideoDrawImageEsu;
import com.essence.interfaces.model.VideoDrawLineEsu;
import com.essence.interfaces.param.VideoDrawImageEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 视频管理线保护线图表管理
 * @author BINX
 * @since 2023-02-03 17:09:47
 */
@RestController
@RequestMapping("/videoDrawImage")
public class VideoDrawImageController extends BaseController<Long, VideoDrawImageEsu, VideoDrawImageEsp, VideoDrawImageEsr> {
    @Autowired
    private VideoDrawImageService videoDrawImageService;

    public VideoDrawImageController(VideoDrawImageService videoDrawImageService) {
        super(videoDrawImageService);
    }


    /**
     * 新增视频图片
     *
     * @param videoDrawImageEsu
     * @return
     */
    @PostMapping("/addVideoDrawLine")
    public ResponseResult addVideoDrawLine(HttpServletRequest request, @RequestBody VideoDrawImageEsu videoDrawImageEsu) {

        if (null != videoDrawImageEsu.getVideoCode() && videoDrawImageEsu.getImageLineUrl() != null) {
            Date date = new Date();
            videoDrawImageEsu.setGmtCreate(date);
            return ResponseResult.success("添加成功", videoDrawImageService.insert(videoDrawImageEsu));
        } else {
            return ResponseResult.error("添加失败");
        }
    }

    /**
     * 根据视频编码code获取所有的频图片
     *
     * @param videoCode
     * @return
     */
    @GetMapping("/selectByVideoCode")
    public ResponseResult selectByVideoCode(HttpServletRequest request,@RequestParam String videoCode) {
        if (null != videoCode ) {
            return ResponseResult.success("查询成功", videoDrawImageService.selectByVideoCode(videoCode));
        } else {
            return ResponseResult.error("视频编码code为空");
        }
    }




    /**
     * <p> base64解码 </p>
     * @Param [base64Code, targetPath] 将编码后的字符串解码为文件
     * @Return void
     */
//    public static void decodeBase64File(String base64Code, String targetPath) {
//        // 输出流
//        FileOutputStream out =null;
//        // 将base 64 转为字节数字
//        byte[] buffer = new byte[0];
//        try {
//            buffer = new BASE64Decoder().decodeBuffer(base64Code);
//            // 创建输出流
//            out = new FileOutputStream(targetPath);
//            // 输出
//            out.write(buffer);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
