package com.essence.service.impl;

import com.essence.dao.RadarImagesLddzDao;
import com.essence.dao.entity.RadarImagesLddzDto;
import com.essence.interfaces.api.RadarImagesLddzService;
import com.essence.interfaces.model.RadarImagesLddzEsr;
import com.essence.interfaces.model.RadarImagesLddzEsu;
import com.essence.interfaces.param.RadarImagesLddzEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRadarImagesLddzEtoT;
import com.essence.service.converter.ConverterRadarImagesLddzTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 雷达回波图  --单站雷达-大兴的(RadarImagesLddz)业务层
 * @author BINX
 * @since 2023-04-23 13:43:46
 */
@Service
public class RadarImagesLddzServiceImpl extends BaseApiImpl<RadarImagesLddzEsu, RadarImagesLddzEsp, RadarImagesLddzEsr, RadarImagesLddzDto> implements RadarImagesLddzService {

    @Autowired
    private RadarImagesLddzDao radarImagesLddzDao;
    @Autowired
    private ConverterRadarImagesLddzEtoT converterRadarImagesLddzEtoT;
    @Autowired
    private ConverterRadarImagesLddzTtoR converterRadarImagesLddzTtoR;

    public RadarImagesLddzServiceImpl(RadarImagesLddzDao radarImagesLddzDao, ConverterRadarImagesLddzEtoT converterRadarImagesLddzEtoT, ConverterRadarImagesLddzTtoR converterRadarImagesLddzTtoR) {
        super(radarImagesLddzDao, converterRadarImagesLddzEtoT, converterRadarImagesLddzTtoR);
    }
}
