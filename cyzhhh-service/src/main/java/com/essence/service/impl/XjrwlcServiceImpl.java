package com.essence.service.impl;

import com.essence.dao.XjrwlcDao;
import com.essence.dao.entity.XjrwlcDto;
import com.essence.interfaces.api.XjrwlcService;
import com.essence.interfaces.model.XjrwlcEsr;
import com.essence.interfaces.model.XjrwlcEsu;
import com.essence.interfaces.param.XjrwlcEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjrwlcEtoT;
import com.essence.service.converter.ConverterXjrwlcTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备巡检流程(Xjrwlc)业务层
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */
@Service
public class XjrwlcServiceImpl extends BaseApiImpl<XjrwlcEsu, XjrwlcEsp, XjrwlcEsr, XjrwlcDto> implements XjrwlcService {
    @Autowired
    private XjrwlcDao xjrwlcDao;
    @Autowired
    private ConverterXjrwlcEtoT converterXjrwlcEtoT;
    @Autowired
    private ConverterXjrwlcTtoR converterXjrwlcTtoR;

    public XjrwlcServiceImpl(XjrwlcDao xjrwlcDao, ConverterXjrwlcEtoT converterXjrwlcEtoT, ConverterXjrwlcTtoR converterXjrwlcTtoR) {
        super(xjrwlcDao, converterXjrwlcEtoT, converterXjrwlcTtoR);
    }
}
