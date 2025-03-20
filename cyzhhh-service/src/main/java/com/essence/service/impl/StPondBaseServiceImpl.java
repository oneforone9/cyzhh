package com.essence.service.impl;

import com.essence.dao.StPondBaseDao;
import com.essence.dao.entity.StPondBaseDto;
import com.essence.interfaces.api.StPondBaseService;
import com.essence.interfaces.model.StPondBaseEsr;
import com.essence.interfaces.model.StPondBaseEsu;
import com.essence.interfaces.param.StPondBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPondBaseEtoT;
import com.essence.service.converter.ConverterStPondBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 易积滞水点基础表(StPondBase)业务层
 * @author liwy
 * @since 2023-04-03 14:45:30
 */
@Service
public class StPondBaseServiceImpl extends BaseApiImpl<StPondBaseEsu, StPondBaseEsp, StPondBaseEsr, StPondBaseDto> implements StPondBaseService {

    @Autowired
    private StPondBaseDao stPondBaseDao;
    @Autowired
    private ConverterStPondBaseEtoT converterStPondBaseEtoT;
    @Autowired
    private ConverterStPondBaseTtoR converterStPondBaseTtoR;

    public StPondBaseServiceImpl(StPondBaseDao stPondBaseDao, ConverterStPondBaseEtoT converterStPondBaseEtoT, ConverterStPondBaseTtoR converterStPondBaseTtoR) {
        super(stPondBaseDao, converterStPondBaseEtoT, converterStPondBaseTtoR);
    }
}
