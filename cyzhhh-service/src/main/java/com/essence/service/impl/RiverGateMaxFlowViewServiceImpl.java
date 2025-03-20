package com.essence.service.impl;

import com.essence.dao.RiverGateMaxFlowViewDao;
import com.essence.dao.entity.RiverGateMaxFlowViewDto;
import com.essence.interfaces.api.RiverGateMaxFlowViewService;
import com.essence.interfaces.model.RiverGateMaxFlowViewEsr;
import com.essence.interfaces.model.RiverGateMaxFlowViewEsu;
import com.essence.interfaces.param.RiverGateMaxFlowViewEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRiverGateMaxFlowViewEtoT;
import com.essence.service.converter.ConverterRiverGateMaxFlowViewTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (RiverGateMaxFlowView)业务层
 * @author BINX
 * @since 2023-04-25 13:51:24
 */
@Service
public class RiverGateMaxFlowViewServiceImpl extends BaseApiImpl<RiverGateMaxFlowViewEsu, RiverGateMaxFlowViewEsp, RiverGateMaxFlowViewEsr, RiverGateMaxFlowViewDto> implements RiverGateMaxFlowViewService {

    @Autowired
    private RiverGateMaxFlowViewDao riverGateMaxFlowViewDao;
    @Autowired
    private ConverterRiverGateMaxFlowViewEtoT converterRiverGateMaxFlowViewEtoT;
    @Autowired
    private ConverterRiverGateMaxFlowViewTtoR converterRiverGateMaxFlowViewTtoR;

    public RiverGateMaxFlowViewServiceImpl(RiverGateMaxFlowViewDao riverGateMaxFlowViewDao, ConverterRiverGateMaxFlowViewEtoT converterRiverGateMaxFlowViewEtoT, ConverterRiverGateMaxFlowViewTtoR converterRiverGateMaxFlowViewTtoR) {
        super(riverGateMaxFlowViewDao, converterRiverGateMaxFlowViewEtoT, converterRiverGateMaxFlowViewTtoR);
    }
}
