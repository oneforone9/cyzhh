package com.essence.service.impl.third;


import com.essence.dao.StThirdUserRelationDao;
import com.essence.dao.entity.StThirdUserRelationDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.third.StThirdUserRelationService;
import com.essence.interfaces.model.StThirdUserRelationEsr;
import com.essence.interfaces.model.StThirdUserRelationEsu;
import com.essence.interfaces.param.StThirdUserRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStThirdUserRelationEtoT;
import com.essence.service.converter.ConverterStThirdUserRelationTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (StThirdUserRelation)业务层
 * @author BINX
 * @since 2023-04-04 14:45:09
 */
@Service
public class StThirdUserRelationServiceImpl extends BaseApiImpl<StThirdUserRelationEsu, StThirdUserRelationEsp, StThirdUserRelationEsr, StThirdUserRelationDto> implements StThirdUserRelationService {


    @Autowired
    private StThirdUserRelationDao stThirdUserRelationDao;
    @Autowired
    private ConverterStThirdUserRelationEtoT converterStThirdUserRelationEtoT;
    @Autowired
    private ConverterStThirdUserRelationTtoR converterStThirdUserRelationTtoR;

    public StThirdUserRelationServiceImpl(StThirdUserRelationDao stThirdUserRelationDao, ConverterStThirdUserRelationEtoT converterStThirdUserRelationEtoT, ConverterStThirdUserRelationTtoR converterStThirdUserRelationTtoR) {
        super(stThirdUserRelationDao, converterStThirdUserRelationEtoT, converterStThirdUserRelationTtoR);
    }
}
