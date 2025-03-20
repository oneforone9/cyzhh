package com.essence.service.impl;


import com.essence.dao.StWaterPortDao;
import com.essence.dao.entity.StWaterPortDto;
import com.essence.interfaces.api.StWaterPortService;
import com.essence.interfaces.model.StWaterPortEsr;
import com.essence.interfaces.model.StWaterPortEsu;
import com.essence.interfaces.param.StWaterPortEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterPortEtoT;
import com.essence.service.converter.ConverterStWaterPortTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 补水口基础表(StWaterPort)业务层
 * @author BINX
 * @since 2023-02-22 17:12:35
 */
@Service
public class StWaterPortServiceImpl extends BaseApiImpl<StWaterPortEsu, StWaterPortEsp, StWaterPortEsr, StWaterPortDto> implements StWaterPortService {

    @Autowired
    private StWaterPortDao stWaterPortDao;
    @Autowired
    private ConverterStWaterPortEtoT converterStWaterPortEtoT;
    @Autowired
    private ConverterStWaterPortTtoR converterStWaterPortTtoR;

    public StWaterPortServiceImpl(StWaterPortDao stWaterPortDao, ConverterStWaterPortEtoT converterStWaterPortEtoT, ConverterStWaterPortTtoR converterStWaterPortTtoR) {
        super(stWaterPortDao, converterStWaterPortEtoT, converterStWaterPortTtoR);
    }
}
