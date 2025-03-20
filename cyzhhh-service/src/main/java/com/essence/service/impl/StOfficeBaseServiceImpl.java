package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StOfficeBaseDao;
import com.essence.dao.entity.StOfficeBaseDto;
import com.essence.interfaces.api.StOfficeBaseService;
import com.essence.interfaces.model.StOfficeBaseEsr;
import com.essence.interfaces.model.StOfficeBaseEsu;
import com.essence.interfaces.param.StOfficeBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStOfficeBaseEtoT;
import com.essence.service.converter.ConverterStOfficeBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科室基础表(StOfficeBase)业务层
 *
 * @author liwy
 * @since 2023-03-29 14:20:47
 */
@Service
public class StOfficeBaseServiceImpl extends BaseApiImpl<StOfficeBaseEsu, StOfficeBaseEsp, StOfficeBaseEsr, StOfficeBaseDto> implements StOfficeBaseService {

    @Autowired
    private StOfficeBaseDao stOfficeBaseDao;
    @Autowired
    private ConverterStOfficeBaseEtoT converterStOfficeBaseEtoT;
    @Autowired
    private ConverterStOfficeBaseTtoR converterStOfficeBaseTtoR;

    public StOfficeBaseServiceImpl(StOfficeBaseDao stOfficeBaseDao, ConverterStOfficeBaseEtoT converterStOfficeBaseEtoT, ConverterStOfficeBaseTtoR converterStOfficeBaseTtoR) {
        super(stOfficeBaseDao, converterStOfficeBaseEtoT, converterStOfficeBaseTtoR);
    }

}
