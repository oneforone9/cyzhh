package com.essence.service.impl;

import com.essence.dao.VideoDrawImageDao;
import com.essence.dao.entity.VideoDrawImageDto;
import com.essence.interfaces.api.VideoDrawImageService;
import com.essence.interfaces.model.VideoDrawImageEsr;
import com.essence.interfaces.model.VideoDrawImageEsu;
import com.essence.interfaces.param.VideoDrawImageEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterVideoDrawImageEtoT;
import com.essence.service.converter.ConverterVideoDrawImageTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 视频管理线保护线图表(VideoDrawImage)业务层
 * @author BINX
 * @since 2023-02-03 17:09:52
 */
@Service
public class VideoDrawImageServiceImpl extends BaseApiImpl<VideoDrawImageEsu, VideoDrawImageEsp, VideoDrawImageEsr, VideoDrawImageDto> implements VideoDrawImageService {


    @Autowired
    private VideoDrawImageDao videoDrawImageDao;
//    @Autowired
//    private ConverterVideoDrawImageEtoT converterVideoDrawImageEtoT;
//    @Autowired
//    private ConverterVideoDrawImageTtoR converterVideoDrawImageTtoR;

    public VideoDrawImageServiceImpl(VideoDrawImageDao videoDrawImageDao, ConverterVideoDrawImageEtoT converterVideoDrawImageEtoT, ConverterVideoDrawImageTtoR converterVideoDrawImageTtoR) {
        super(videoDrawImageDao, converterVideoDrawImageEtoT, converterVideoDrawImageTtoR);
    }

    /**
     * 根据视频编码code获取所有的频图片
     * @param videoCode
     * @return
     */
    @Override
    public List<VideoDrawImageDto> selectByVideoCode(String videoCode) {
        return videoDrawImageDao.selectByVideoCode(videoCode);
    }
}
