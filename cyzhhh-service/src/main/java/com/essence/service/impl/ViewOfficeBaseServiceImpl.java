package com.essence.service.impl;


import com.essence.dao.ViewOfficeBaseDao;
import com.essence.dao.entity.ViewOfficeBaseDto;
import com.essence.interfaces.api.ViewOfficeBaseService;
import com.essence.interfaces.model.ViewOfficeBaseEsr;
import com.essence.interfaces.model.ViewOfficeBaseEsu;
import com.essence.interfaces.param.ViewOfficeBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterViewOfficeBaseEtoT;
import com.essence.service.converter.ConverterViewOfficeBaseTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (ViewOfficeBase)业务层
 * @author majunjie
 * @since 2024-09-11 14:21:32
 */
@Service
public class ViewOfficeBaseServiceImpl extends BaseApiImpl<ViewOfficeBaseEsu, ViewOfficeBaseEsp, ViewOfficeBaseEsr, ViewOfficeBaseDto> implements ViewOfficeBaseService {

    @Autowired
    private ViewOfficeBaseDao viewOfficeBaseDao;
    @Autowired
    private ConverterViewOfficeBaseEtoT converterViewOfficeBaseEtoT;
    @Autowired
    private ConverterViewOfficeBaseTtoR converterViewOfficeBaseTtoR;

    public ViewOfficeBaseServiceImpl(ViewOfficeBaseDao viewOfficeBaseDao, ConverterViewOfficeBaseEtoT converterViewOfficeBaseEtoT, ConverterViewOfficeBaseTtoR converterViewOfficeBaseTtoR) {
        super(viewOfficeBaseDao, converterViewOfficeBaseEtoT, converterViewOfficeBaseTtoR);
    }
}
