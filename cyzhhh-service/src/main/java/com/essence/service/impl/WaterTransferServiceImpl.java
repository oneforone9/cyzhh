package com.essence.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.WaterTransferDao;
import com.essence.dao.entity.WaterTransferDto;
import com.essence.interfaces.api.WaterTransferService;
import com.essence.interfaces.param.WaterTransferEsp;
import com.essence.interfaces.model.WaterTransferEsu;
import com.essence.interfaces.model.WaterTransferEsr;

import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWaterTransferEtoT;
import com.essence.service.converter.ConverterWaterTransferTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调水表(WaterTransfer)业务层
 * @author BINX
 * @since 2023-05-09 10:05:59
 */
@Service
public class WaterTransferServiceImpl extends BaseApiImpl<WaterTransferEsu, WaterTransferEsp, WaterTransferEsr, WaterTransferDto> implements WaterTransferService {

    @Autowired
    private WaterTransferDao waterTransferDao;
    @Autowired
    private ConverterWaterTransferEtoT converterWaterTransferEtoT;
    @Autowired
    private ConverterWaterTransferTtoR converterWaterTransferTtoR;

    public WaterTransferServiceImpl(WaterTransferDao waterTransferDao, ConverterWaterTransferEtoT converterWaterTransferEtoT, ConverterWaterTransferTtoR converterWaterTransferTtoR) {
        super(waterTransferDao, converterWaterTransferEtoT, converterWaterTransferTtoR);
    }
}
