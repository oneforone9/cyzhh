package com.essence.service.impl;

import com.essence.common.constant.ItemConstant;
import com.essence.dao.XjBzxxDao;
import com.essence.dao.entity.XjBzxxDto;
import com.essence.interfaces.api.XjBzxxService;
import com.essence.interfaces.model.XjBzxxEsr;
import com.essence.interfaces.model.XjBzxxEsu;
import com.essence.interfaces.param.XjBzxxEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjBzxxEtoT;
import com.essence.service.converter.ConverterXjBzxxTtoR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * 设备巡检班组信息(XjBzxx)业务层
 * @author majunjie
 * @since 2025-01-08 08:13:24
 */
@Service
public class XjBzxxServiceImpl extends BaseApiImpl<XjBzxxEsu, XjBzxxEsp, XjBzxxEsr, XjBzxxDto> implements XjBzxxService {

    @Autowired
    private XjBzxxDao xjBzxxDao;
    @Autowired
    private ConverterXjBzxxEtoT converterXjBzxxEtoT;
    @Autowired
    private ConverterXjBzxxTtoR converterXjBzxxTtoR;

    public XjBzxxServiceImpl(XjBzxxDao xjBzxxDao, ConverterXjBzxxEtoT converterXjBzxxEtoT, ConverterXjBzxxTtoR converterXjBzxxTtoR) {
        super(xjBzxxDao, converterXjBzxxEtoT, converterXjBzxxTtoR);
    }

    @Override
    public XjBzxxEsr addBz(XjBzxxEsu xjBzxxEsu) {
        XjBzxxDto xjBzxxDto = converterXjBzxxEtoT.toBean(xjBzxxEsu);
        xjBzxxDto.setBmid(ItemConstant.USER_XJ_PUT_DEFAUTL);
        if (StringUtils.isNotBlank(xjBzxxEsu.getId())){
            xjBzxxDao.updateById(xjBzxxDto);
        }else {
            xjBzxxDto.setId(UUID.randomUUID().toString().replace("-", ""));
            xjBzxxDto.setTime(new Date());
            xjBzxxDao.insert(xjBzxxDto);
        }
        return converterXjBzxxTtoR.toBean(xjBzxxDto);
    }
}
