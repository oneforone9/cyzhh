package com.essence.service.impl;

import com.essence.dao.EmergencyTeamDao;
import com.essence.dao.entity.EmergencyTeam;
import com.essence.interfaces.api.EmergencyTeamService;
import com.essence.interfaces.model.EmergencyTeamEsr;
import com.essence.interfaces.model.EmergencyTeamEsu;
import com.essence.interfaces.param.EmergencyTeamEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterEmergencyTeamEtoT;
import com.essence.service.converter.ConverterEmergencyTeamTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhy
 * @since 2024-07-17 19:32:43
 */
@Service
public class EmergencyTeamServiceImpl extends BaseApiImpl<EmergencyTeamEsu, EmergencyTeamEsp, EmergencyTeamEsr, EmergencyTeam> implements EmergencyTeamService {
    @Autowired
    private EmergencyTeamDao emergencyTeamDao;
    @Autowired
    private ConverterEmergencyTeamEtoT converterEmergencyTeamEtoT;
    @Autowired
    private ConverterEmergencyTeamTtoR converterEmergencyTeamTtoR;

    public EmergencyTeamServiceImpl(EmergencyTeamDao emergencyTeamDao, ConverterEmergencyTeamEtoT converterEmergencyTeamEtoT, ConverterEmergencyTeamTtoR converterEmergencyTeamTtoR) {
        super(emergencyTeamDao, converterEmergencyTeamEtoT, converterEmergencyTeamTtoR);
    }

}
