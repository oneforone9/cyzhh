package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StWaterEngineeringSchedulingLeadDao;
import com.essence.dao.entity.StWaterEngineeringSchedulingLeadDto;
import com.essence.interfaces.api.StWaterEngineeringSchedulingLeadService;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingLeadEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingLeadEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingLeadTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 水系联调-工程调度(StWaterEngineeringSchedulingLead)业务层
 *
 * @author majunjie
 * @since 2023-07-03 17:24:39
 */
@Service
public class StWaterEngineeringSchedulingLeadServiceImpl extends BaseApiImpl<StWaterEngineeringSchedulingLeadEsu, StWaterEngineeringSchedulingLeadEsp, StWaterEngineeringSchedulingLeadEsr, StWaterEngineeringSchedulingLeadDto> implements StWaterEngineeringSchedulingLeadService {
    @Autowired
    private StWaterEngineeringSchedulingLeadDao stWaterEngineeringSchedulingLeadDao;
    @Autowired
    private ConverterStWaterEngineeringSchedulingLeadEtoT converterStWaterEngineeringSchedulingLeadEtoT;
    @Autowired
    private ConverterStWaterEngineeringSchedulingLeadTtoR converterStWaterEngineeringSchedulingLeadTtoR;

    public StWaterEngineeringSchedulingLeadServiceImpl(StWaterEngineeringSchedulingLeadDao stWaterEngineeringSchedulingLeadDao, ConverterStWaterEngineeringSchedulingLeadEtoT converterStWaterEngineeringSchedulingLeadEtoT, ConverterStWaterEngineeringSchedulingLeadTtoR converterStWaterEngineeringSchedulingLeadTtoR) {
        super(stWaterEngineeringSchedulingLeadDao, converterStWaterEngineeringSchedulingLeadEtoT, converterStWaterEngineeringSchedulingLeadTtoR);
    }

    @Override
    public Boolean selectSz(String id) {
        List<StWaterEngineeringSchedulingLeadDto> stWaterEngineeringSchedulingLeadDtos = stWaterEngineeringSchedulingLeadDao.selectList(new QueryWrapper<StWaterEngineeringSchedulingLeadDto>().lambda().eq(StWaterEngineeringSchedulingLeadDto::getSUserId, id));
        Boolean type = false;
        if (!CollectionUtils.isEmpty(stWaterEngineeringSchedulingLeadDtos)) {
            type = true;
        }
        return type;
    }
}
