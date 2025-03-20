package com.essence.service.impl;

import com.essence.dao.RadarImagesWxDao;
import com.essence.dao.entity.RadarImagesWxDto;
import com.essence.interfaces.api.RadarImagesWxService;
import com.essence.interfaces.model.RadarImagesWxEsr;
import com.essence.interfaces.model.RadarImagesWxEsu;
import com.essence.interfaces.param.RadarImagesWxEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRadarImagesWxEtoT;
import com.essence.service.converter.ConverterRadarImagesWxTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 卫星云图(RadarImagesWx)业务层
 * @author BINX
 * @since 2023-04-23 13:43:52
 */
@Service
public class RadarImagesWxServiceImpl extends BaseApiImpl<RadarImagesWxEsu, RadarImagesWxEsp, RadarImagesWxEsr, RadarImagesWxDto> implements RadarImagesWxService {

    @Autowired
    private RadarImagesWxDao radarImagesWxDao;
    @Autowired
    private ConverterRadarImagesWxEtoT converterRadarImagesWxEtoT;
    @Autowired
    private ConverterRadarImagesWxTtoR converterRadarImagesWxTtoR;

    public RadarImagesWxServiceImpl(RadarImagesWxDao radarImagesWxDao, ConverterRadarImagesWxEtoT converterRadarImagesWxEtoT, ConverterRadarImagesWxTtoR converterRadarImagesWxTtoR) {
        super(radarImagesWxDao, converterRadarImagesWxEtoT, converterRadarImagesWxTtoR);
    }
}
