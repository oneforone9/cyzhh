package com.essence.service.impl;

import com.essence.dao.StBRiverDao;
import com.essence.dao.entity.StBRiverDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.StBRiverService;
import com.essence.interfaces.model.StBRiverEsr;
import com.essence.interfaces.model.StBRiverEsu;
import com.essence.interfaces.param.StBRiverEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStBRiverEtoT;
import com.essence.service.converter.ConverterStBRiverTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 河系分配表(StBRiver)业务层
 * @author majunjie
 * @since 2025-01-09 09:08:01
 */
@Service
public class StBRiverServiceImpl extends BaseApiImpl<StBRiverEsu, StBRiverEsp, StBRiverEsr, StBRiverDto> implements StBRiverService {

    @Autowired
    private StBRiverDao stBRiverDao;
    @Autowired
    private ConverterStBRiverEtoT converterStBRiverEtoT;
    @Autowired
    private ConverterStBRiverTtoR converterStBRiverTtoR;

    public StBRiverServiceImpl(StBRiverDao stBRiverDao, ConverterStBRiverEtoT converterStBRiverEtoT, ConverterStBRiverTtoR converterStBRiverTtoR) {
        super(stBRiverDao, converterStBRiverEtoT, converterStBRiverTtoR);
    }
}
