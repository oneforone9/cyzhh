package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.FileBaseEsr;
import com.essence.interfaces.model.FileBaseEsu;
import com.essence.interfaces.param.FileBaseEsp;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理表服务层
 *
 * @author zhy
 * @since 2022-10-28 16:20:14
 */
public interface FileBaseService extends BaseApi<FileBaseEsu, FileBaseEsp, FileBaseEsr> {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return
     */
    FileBaseEsr upload(MultipartFile file);

    /**
     * 文件上传 （增加水印）
     *
     * @param file 文件
     * @return
     */
    FileBaseEsr uploadForWatermark(MultipartFile file, String waterMark);

    /**
     * 查看缩略图
     *
     * @param id
     * @param size
     * @return
     */
    void previewFile(HttpServletRequest request, HttpServletResponse response, String id, String size) throws Exception;


    void makeWaterMark(String fildId, String waterMark,String htbh);

}
