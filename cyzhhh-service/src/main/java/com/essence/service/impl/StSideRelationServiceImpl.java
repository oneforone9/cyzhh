package com.essence.service.impl;

import com.essence.dao.StSideRelationDao;
import com.essence.dao.entity.StSideRelationDto;
import com.essence.interfaces.api.StSideRelationService;
import com.essence.interfaces.model.StSideRelationEsr;
import com.essence.interfaces.model.StSideRelationEsu;
import com.essence.interfaces.param.StSideRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSideRelationEtoT;
import com.essence.service.converter.ConverterStSideRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (StSideRelation)业务层
 * @author majunjie
 * @since 2023-05-10 17:31:23
 */
@Service
public class StSideRelationServiceImpl extends BaseApiImpl<StSideRelationEsu, StSideRelationEsp, StSideRelationEsr, StSideRelationDto> implements StSideRelationService {

    @Autowired
    private StSideRelationDao stSideRelationDao;
    @Autowired
    private ConverterStSideRelationEtoT converterStSideRelationEtoT;
    @Autowired
    private ConverterStSideRelationTtoR converterStSideRelationTtoR;

    public StSideRelationServiceImpl(StSideRelationDao stSideRelationDao, ConverterStSideRelationEtoT converterStSideRelationEtoT, ConverterStSideRelationTtoR converterStSideRelationTtoR) {
        super(stSideRelationDao, converterStSideRelationEtoT, converterStSideRelationTtoR);
    }
}
