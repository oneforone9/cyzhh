package com.essence.service.impl;

import com.essence.dao.StMaterialBaseDao;
import com.essence.dao.entity.StMaterialBaseDto;
import com.essence.interfaces.api.StMaterialBaseService;
import com.essence.interfaces.model.StMaterialBaseEsr;
import com.essence.interfaces.model.StMaterialBaseEsu;
import com.essence.interfaces.param.StMaterialBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStMaterialBaseEtoT;
import com.essence.service.converter.ConverterStMaterialBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 防汛物资基础表(StMaterialBase)业务层
 * @author liwy
 * @since 2023-04-13 14:57:34
 */
@Service
public class StMaterialBaseServiceImpl extends BaseApiImpl<StMaterialBaseEsu, StMaterialBaseEsp, StMaterialBaseEsr, StMaterialBaseDto> implements StMaterialBaseService {

    @Autowired
    private StMaterialBaseDao stMaterialBaseDao;
    @Autowired
    private ConverterStMaterialBaseEtoT converterStMaterialBaseEtoT;
    @Autowired
    private ConverterStMaterialBaseTtoR converterStMaterialBaseTtoR;

    public StMaterialBaseServiceImpl(StMaterialBaseDao stMaterialBaseDao, ConverterStMaterialBaseEtoT converterStMaterialBaseEtoT, ConverterStMaterialBaseTtoR converterStMaterialBaseTtoR) {
        super(stMaterialBaseDao, converterStMaterialBaseEtoT, converterStMaterialBaseTtoR);
    }
}
