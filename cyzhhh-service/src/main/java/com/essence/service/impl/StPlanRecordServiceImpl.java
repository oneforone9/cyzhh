package com.essence.service.impl;

import com.essence.dao.StPlanRecordDao;
import com.essence.dao.entity.StPlanRecordDto;
import com.essence.interfaces.api.StPlanRecordService;
import com.essence.interfaces.model.StPlanRecordEsr;
import com.essence.interfaces.model.StPlanRecordEsu;
import com.essence.interfaces.param.StPlanRecordEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanRecordEtoT;
import com.essence.service.converter.ConverterStPlanRecordTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 闸坝计划生成记录表(StPlanRecord)业务层
 * @author liwy
 * @since 2023-07-18 11:15:20
 */
@Service
public class StPlanRecordServiceImpl extends BaseApiImpl<StPlanRecordEsu, StPlanRecordEsp, StPlanRecordEsr, StPlanRecordDto> implements StPlanRecordService {

    @Autowired
    private StPlanRecordDao stPlanRecordDao;
    @Autowired
    private ConverterStPlanRecordEtoT converterStPlanRecordEtoT;
    @Autowired
    private ConverterStPlanRecordTtoR converterStPlanRecordTtoR;

    public StPlanRecordServiceImpl(StPlanRecordDao stPlanRecordDao, ConverterStPlanRecordEtoT converterStPlanRecordEtoT, ConverterStPlanRecordTtoR converterStPlanRecordTtoR) {
        super(stPlanRecordDao, converterStPlanRecordEtoT, converterStPlanRecordTtoR);
    }
}
