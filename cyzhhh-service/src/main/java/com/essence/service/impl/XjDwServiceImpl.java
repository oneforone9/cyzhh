package com.essence.service.impl;


import com.essence.dao.XjDwDao;
import com.essence.dao.entity.XjDwDto;
import com.essence.interfaces.api.XjDwService;
import com.essence.interfaces.model.XjDwEsr;
import com.essence.interfaces.model.XjDwEsu;
import com.essence.interfaces.param.XjDwEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjDwEtoT;
import com.essence.service.converter.ConverterXjDwTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备巡检单位(XjDw)业务层
 * @author majunjie
 * @since 2025-01-09 08:56:29
 */
@Service
public class XjDwServiceImpl extends BaseApiImpl<XjDwEsu, XjDwEsp, XjDwEsr, XjDwDto> implements XjDwService {

    @Autowired
    private XjDwDao xjDwDao;
    @Autowired
    private ConverterXjDwEtoT converterXjDwEtoT;
    @Autowired
    private ConverterXjDwTtoR converterXjDwTtoR;

    public XjDwServiceImpl(XjDwDao xjDwDao, ConverterXjDwEtoT converterXjDwEtoT, ConverterXjDwTtoR converterXjDwTtoR) {
        super(xjDwDao, converterXjDwEtoT, converterXjDwTtoR);
    }
}
