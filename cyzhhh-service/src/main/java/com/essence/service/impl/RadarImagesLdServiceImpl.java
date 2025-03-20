package com.essence.service.impl;

import com.essence.dao.RadarImagesLdDao;
import com.essence.dao.entity.RadarImagesLdDto;
import com.essence.interfaces.api.RadarImagesLdService;
import com.essence.interfaces.model.RadarImagesLdEsr;
import com.essence.interfaces.model.RadarImagesLdEsu;
import com.essence.interfaces.param.RadarImagesLdEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRadarImagesLdEtoT;
import com.essence.service.converter.ConverterRadarImagesLdTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 雷达回波图  --华北地区的(RadarImagesLd)业务层
 * @author BINX
 * @since 2023-04-23 13:43:29
 */
@Service
public class RadarImagesLdServiceImpl extends BaseApiImpl<RadarImagesLdEsu, RadarImagesLdEsp, RadarImagesLdEsr, RadarImagesLdDto> implements RadarImagesLdService {

    @Autowired
    private RadarImagesLdDao radarImagesLdDao;
    @Autowired
    private ConverterRadarImagesLdEtoT converterRadarImagesLdEtoT;
    @Autowired
    private ConverterRadarImagesLdTtoR converterRadarImagesLdTtoR;

    public RadarImagesLdServiceImpl(RadarImagesLdDao radarImagesLdDao, ConverterRadarImagesLdEtoT converterRadarImagesLdEtoT, ConverterRadarImagesLdTtoR converterRadarImagesLdTtoR) {
        super(radarImagesLdDao, converterRadarImagesLdEtoT, converterRadarImagesLdTtoR);
    }
}
