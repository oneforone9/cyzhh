package com.essence.service.impl;

import com.essence.dao.TWorkorderProcessNewestDao;
import com.essence.dao.entity.TWorkorderProcessNewestDto;
import com.essence.interfaces.api.TWorkorderProcessNewestService;
import com.essence.interfaces.model.TWorkorderProcessNewestEsr;
import com.essence.interfaces.model.TWorkorderProcessNewestEsu;
import com.essence.interfaces.param.TWorkorderProcessNewestEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTWorkorderProcessNewestEtoT;
import com.essence.service.converter.ConverterTWorkorderProcessNewestTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 工单处理过程表(TWorkorderProcessNewest)业务层
 * @author liwy
 * @since 2023-08-01 16:57:10
 */
@Service
public class TWorkorderProcessNewestServiceImpl extends BaseApiImpl<TWorkorderProcessNewestEsu, TWorkorderProcessNewestEsp, TWorkorderProcessNewestEsr, TWorkorderProcessNewestDto> implements TWorkorderProcessNewestService {

    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;
    @Autowired
    private ConverterTWorkorderProcessNewestEtoT converterTWorkorderProcessNewestEtoT;
    @Autowired
    private ConverterTWorkorderProcessNewestTtoR converterTWorkorderProcessNewestTtoR;

    public TWorkorderProcessNewestServiceImpl(TWorkorderProcessNewestDao tWorkorderProcessNewestDao, ConverterTWorkorderProcessNewestEtoT converterTWorkorderProcessNewestEtoT, ConverterTWorkorderProcessNewestTtoR converterTWorkorderProcessNewestTtoR) {
        super(tWorkorderProcessNewestDao, converterTWorkorderProcessNewestEtoT, converterTWorkorderProcessNewestTtoR);
    }
}
