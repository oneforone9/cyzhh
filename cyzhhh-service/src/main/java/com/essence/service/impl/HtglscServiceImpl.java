package com.essence.service.impl;

import com.essence.dao.HtglscDao;
import com.essence.dao.entity.HtglscDto;
import com.essence.interfaces.api.HtglscService;
import com.essence.interfaces.model.HtglscEsr;
import com.essence.interfaces.model.HtglscEsu;
import com.essence.interfaces.param.HtglscEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtglscEtoT;
import com.essence.service.converter.ConverterHtglscTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 合同管理收藏(Htglsc)业务层
 * @author majunjie
 * @since 2024-09-13 16:18:29
 */
@Service
public class HtglscServiceImpl extends BaseApiImpl<HtglscEsu, HtglscEsp, HtglscEsr, HtglscDto> implements HtglscService {

    @Autowired
    private HtglscDao htglscDao;
    @Autowired
    private ConverterHtglscEtoT converterHtglscEtoT;
    @Autowired
    private ConverterHtglscTtoR converterHtglscTtoR;

    public HtglscServiceImpl(HtglscDao htglscDao, ConverterHtglscEtoT converterHtglscEtoT, ConverterHtglscTtoR converterHtglscTtoR) {
        super(htglscDao, converterHtglscEtoT, converterHtglscTtoR);
    }
}
