package com.essence.service.impl;

import com.essence.dao.TReaFocusPointDao;
import com.essence.dao.entity.TReaFocusPointDto;
import com.essence.interfaces.api.TReaFocusPointService;
import com.essence.interfaces.model.TReaFocusPointEsr;
import com.essence.interfaces.model.TReaFocusPointEsu;
import com.essence.interfaces.param.TReaFocusPointEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTReaFocusPointEtoT;
import com.essence.service.converter.ConverterTReaFocusPointTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * (TReaFocusPoint)业务层
 * @author liwy
 * @since 2023-05-06 10:02:01
 */
@Service
public class TReaFocusPointServiceImpl extends BaseApiImpl<TReaFocusPointEsu, TReaFocusPointEsp, TReaFocusPointEsr, TReaFocusPointDto> implements TReaFocusPointService {

    @Autowired
    private TReaFocusPointDao tReaFocusPointDao;
    @Autowired
    private ConverterTReaFocusPointEtoT converterTReaFocusPointEtoT;
    @Autowired
    private ConverterTReaFocusPointTtoR converterTReaFocusPointTtoR;

    public TReaFocusPointServiceImpl(TReaFocusPointDao tReaFocusPointDao, ConverterTReaFocusPointEtoT converterTReaFocusPointEtoT, ConverterTReaFocusPointTtoR converterTReaFocusPointTtoR) {
        super(tReaFocusPointDao, converterTReaFocusPointEtoT, converterTReaFocusPointTtoR);
    }
}
