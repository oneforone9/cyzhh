package com.essence.service.impl;


import com.essence.dao.VideoDrawLineDao;
import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.interfaces.api.VideoDrawLineService;
import com.essence.interfaces.model.VideoDrawLineEsr;
import com.essence.interfaces.model.VideoDrawLineEsu;
import com.essence.interfaces.param.VideoDrawLineEsp;

import com.essence.service.converter.ConverterVideoDrawLineEtoT;
import com.essence.service.converter.ConverterVideoDrawLineTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 视频管理线保护线表(VideoDrawLine)业务层
 * @author BINX
 * @since 2023-02-03 15:22:34
 */
@Service
public class VideoDrawLineServiceImpl extends BaseApiImpl<VideoDrawLineEsu, VideoDrawLineEsp, VideoDrawLineEsr, VideoDrawLineDto> implements VideoDrawLineService {

    @Autowired
    private VideoDrawLineDao videoDrawLineDao;
//    @Autowired
//    private ConverterVideoDrawLineEtoT converterVideoDrawLineEtoT;
//    @Autowired
//    private ConverterVideoDrawLineTtoR converterVideoDrawLineTtoR;

    public VideoDrawLineServiceImpl(VideoDrawLineDao videoDrawLineDao, ConverterVideoDrawLineEtoT converterVideoDrawLineEtoT, ConverterVideoDrawLineTtoR converterVideoDrawLineTtoR) {
        super(videoDrawLineDao, converterVideoDrawLineEtoT, converterVideoDrawLineTtoR);
    }

    /**
     * 根据视频编码code查询画线数据
     * @param videoCode
     * @return
     */
    @Override
    public VideoDrawLineDto findByVideoCode(String videoCode) {
        return videoDrawLineDao.findByVideoCode(videoCode);
    }
}
