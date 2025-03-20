package com.essence.service.impl;


import com.essence.dao.StEmergencyDao;
import com.essence.dao.entity.StEmergencyDto;
import com.essence.interfaces.api.StEmergencyService;
import com.essence.interfaces.model.StEmergencyEsr;
import com.essence.interfaces.model.StEmergencyEsu;
import com.essence.interfaces.param.StEmergencyEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStEmergencyEtoT;
import com.essence.service.converter.ConverterStEmergencyTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 抢险队基本信息表(StEmergency)业务层
 * @author liwy
 * @since 2023-04-13 16:14:48
 */
@Service
public class StEmergencyServiceImpl extends BaseApiImpl<StEmergencyEsu, StEmergencyEsp, StEmergencyEsr, StEmergencyDto> implements StEmergencyService {

    @Autowired
    private StEmergencyDao stEmergencyDao;
    @Autowired
    private ConverterStEmergencyEtoT converterStEmergencyEtoT;
    @Autowired
    private ConverterStEmergencyTtoR converterStEmergencyTtoR;

    public StEmergencyServiceImpl(StEmergencyDao stEmergencyDao, ConverterStEmergencyEtoT converterStEmergencyEtoT, ConverterStEmergencyTtoR converterStEmergencyTtoR) {
        super(stEmergencyDao, converterStEmergencyEtoT, converterStEmergencyTtoR);
    }
}
