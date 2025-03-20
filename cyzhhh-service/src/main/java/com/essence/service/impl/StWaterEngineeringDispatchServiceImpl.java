package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StWaterEngineeringDispatchDao;
import com.essence.dao.entity.StWaterEngineeringDispatchDto;
import com.essence.interfaces.api.StWaterEngineeringDispatchService;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsr;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsu;
import com.essence.interfaces.param.StWaterEngineeringDispatchEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterEngineeringDispatchEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringDispatchTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 水系联调-工程调度-调度预案(StWaterEngineeringDispatch)业务层
 *
 * @author majunjie
 * @since 2023-06-02 12:39:06
 */
@Service
public class StWaterEngineeringDispatchServiceImpl extends BaseApiImpl<StWaterEngineeringDispatchEsu, StWaterEngineeringDispatchEsp, StWaterEngineeringDispatchEsr, StWaterEngineeringDispatchDto> implements StWaterEngineeringDispatchService {

    @Autowired
    private StWaterEngineeringDispatchDao stWaterEngineeringDispatchDao;
    @Autowired
    private ConverterStWaterEngineeringDispatchEtoT converterStWaterEngineeringDispatchEtoT;
    @Autowired
    private ConverterStWaterEngineeringDispatchTtoR converterStWaterEngineeringDispatchTtoR;

    public StWaterEngineeringDispatchServiceImpl(StWaterEngineeringDispatchDao stWaterEngineeringDispatchDao, ConverterStWaterEngineeringDispatchEtoT converterStWaterEngineeringDispatchEtoT, ConverterStWaterEngineeringDispatchTtoR converterStWaterEngineeringDispatchTtoR) {
        super(stWaterEngineeringDispatchDao, converterStWaterEngineeringDispatchEtoT, converterStWaterEngineeringDispatchTtoR);
    }

    @Override
    public StWaterEngineeringDispatchEsr selectDispatchByStId(String stId) {
        StWaterEngineeringDispatchEsr stWaterEngineeringDispatchEsr = new StWaterEngineeringDispatchEsr();
        List<StWaterEngineeringDispatchDto> stWaterEngineeringDispatchDtos = Optional.ofNullable( stWaterEngineeringDispatchDao.selectList(new QueryWrapper<StWaterEngineeringDispatchDto>().lambda().eq(StWaterEngineeringDispatchDto::getStId, stId))).orElse(Lists.newArrayList());
        if (!CollectionUtils.isEmpty(stWaterEngineeringDispatchDtos)) {
            StWaterEngineeringDispatchDto stWaterEngineeringDispatchDto = stWaterEngineeringDispatchDtos.get(0);
            stWaterEngineeringDispatchEsr = converterStWaterEngineeringDispatchTtoR.toBean(stWaterEngineeringDispatchDto);
        }
        return stWaterEngineeringDispatchEsr;
    }
}
