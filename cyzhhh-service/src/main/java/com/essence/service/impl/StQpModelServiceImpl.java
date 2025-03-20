package com.essence.service.impl;

import com.essence.dao.StQpModelDao;
import com.essence.dao.entity.water.StQpModelDto;
import com.essence.interfaces.api.StQpModelService;
import com.essence.interfaces.model.StQpModelEsr;
import com.essence.interfaces.model.StQpModelEsu;
import com.essence.interfaces.param.StQpModelEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStQpModelEtoT;
import com.essence.service.converter.ConverterStQpModelTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* 水系联通-预报水位-河段断面关联表 (st_qp_model)表数据库业务层
*
* @author BINX
* @since 2023年5月11日 下午4:34:54
*/
@Service
public class StQpModelServiceImpl extends BaseApiImpl<StQpModelEsu, StQpModelEsp, StQpModelEsr, StQpModelDto> implements StQpModelService {


    @Autowired
    private StQpModelDao stQpModelDao;
    @Autowired
    private ConverterStQpModelEtoT converterStQpModelEtoT;
    @Autowired
    private ConverterStQpModelTtoR converterStQpModelTtoR;

    public StQpModelServiceImpl(StQpModelDao stQpModelDao, ConverterStQpModelEtoT converterStQpModelEtoT, ConverterStQpModelTtoR converterStQpModelTtoR) {
        super(stQpModelDao, converterStQpModelEtoT, converterStQpModelTtoR);
    }
}
