package com.essence.service.impl;

import com.essence.dao.XjHysxxDao;
import com.essence.dao.entity.XjHysxxDto;
import com.essence.interfaces.api.XjHysxjxxService;
import com.essence.interfaces.api.XjHysxxService;
import com.essence.interfaces.model.XjHysxjxxEsr;
import com.essence.interfaces.model.XjHysxjxxQuery;
import com.essence.interfaces.model.XjHysxxEsr;
import com.essence.interfaces.model.XjHysxxEsu;
import com.essence.interfaces.param.XjHysxxEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjHysxxEtoT;
import com.essence.service.converter.ConverterXjHysxxTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备巡检会议室信息(XjHysxx)业务层
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */
@Service
public class XjHysxxServiceImpl extends BaseApiImpl<XjHysxxEsu, XjHysxxEsp, XjHysxxEsr, XjHysxxDto> implements XjHysxxService {

    @Autowired
    private XjHysxxDao xjHysxxDao;
    @Autowired
    private ConverterXjHysxxEtoT converterXjHysxxEtoT;
    @Autowired
    private ConverterXjHysxxTtoR converterXjHysxxTtoR;
@Autowired
private XjHysxjxxService xjHysxjxxService;
    public XjHysxxServiceImpl(XjHysxxDao xjHysxxDao, ConverterXjHysxxEtoT converterXjHysxxEtoT, ConverterXjHysxxTtoR converterXjHysxxTtoR) {
        super(xjHysxxDao, converterXjHysxxEtoT, converterXjHysxxTtoR);
    }

    @Override
    public List<XjHysxjxxEsr> selectHyxcXx(XjHysxjxxQuery xjHysxjxxQuery) {
        return xjHysxjxxService.selectHyxcXx(xjHysxjxxQuery);
    }
}
