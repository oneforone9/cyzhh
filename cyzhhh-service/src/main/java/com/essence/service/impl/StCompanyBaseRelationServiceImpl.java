package com.essence.service.impl;

import com.essence.dao.StCompanyBaseRelationDao;
import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.interfaces.api.StCompanyBaseRelationService;
import com.essence.interfaces.model.StCompanyBaseRelationEsr;
import com.essence.interfaces.model.StCompanyBaseRelationEsu;
import com.essence.interfaces.param.StCompanyBaseRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCompanyBaseRelationEtoT;
import com.essence.service.converter.ConverterStCompanyBaseRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (StCompanyBaseRelation)业务层
 * @author BINX
 * @since 2023-02-16 11:59:36
 */
@Service
public class StCompanyBaseRelationServiceImpl extends BaseApiImpl<StCompanyBaseRelationEsu, StCompanyBaseRelationEsp, StCompanyBaseRelationEsr, StCompanyBaseRelationDto> implements StCompanyBaseRelationService {

    @Autowired
    private StCompanyBaseRelationDao stCompanyBaseRelationDao;
    @Autowired
    private ConverterStCompanyBaseRelationEtoT converterStCompanyBaseRelationEtoT;
    @Autowired
    private ConverterStCompanyBaseRelationTtoR converterStCompanyBaseRelationTtoR;

    public StCompanyBaseRelationServiceImpl(StCompanyBaseRelationDao stCompanyBaseRelationDao, ConverterStCompanyBaseRelationEtoT converterStCompanyBaseRelationEtoT, ConverterStCompanyBaseRelationTtoR converterStCompanyBaseRelationTtoR) {
        super(stCompanyBaseRelationDao, converterStCompanyBaseRelationEtoT, converterStCompanyBaseRelationTtoR);
    }
}
