package com.essence.service.impl;

import com.essence.dao.StLibraryTypeDao;
import com.essence.dao.entity.StLibraryTypeDto;
import com.essence.interfaces.api.StLibraryTypeService;
import com.essence.interfaces.model.StLibraryTypeEsr;
import com.essence.interfaces.model.StLibraryTypeEsu;
import com.essence.interfaces.param.StLibraryTypeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStLibraryTypeEtoT;
import com.essence.service.converter.ConverterStLibraryTypeTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 文库类型基础表(StLibraryType)业务层
 * @author liwy
 * @since 2023-08-17 10:31:11
 */
@Service
public class StLibraryTypeServiceImpl extends BaseApiImpl<StLibraryTypeEsu, StLibraryTypeEsp, StLibraryTypeEsr, StLibraryTypeDto> implements StLibraryTypeService {

    @Autowired
    private StLibraryTypeDao stLibraryTypeDao;
    @Autowired
    private ConverterStLibraryTypeEtoT converterStLibraryTypeEtoT;
    @Autowired
    private ConverterStLibraryTypeTtoR converterStLibraryTypeTtoR;

    public StLibraryTypeServiceImpl(StLibraryTypeDao stLibraryTypeDao, ConverterStLibraryTypeEtoT converterStLibraryTypeEtoT, ConverterStLibraryTypeTtoR converterStLibraryTypeTtoR) {
        super(stLibraryTypeDao, converterStLibraryTypeEtoT, converterStLibraryTypeTtoR);
    }
}
