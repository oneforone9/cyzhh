package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.XjHysxjxxDao;
import com.essence.dao.entity.XjHysxjxxDto;
import com.essence.interfaces.api.XjHysxjxxService;
import com.essence.interfaces.model.XjHysxjxxEsr;
import com.essence.interfaces.model.XjHysxjxxEsu;
import com.essence.interfaces.model.XjHysxjxxQuery;
import com.essence.interfaces.param.XjHysxjxxEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjHysxjxxEtoT;
import com.essence.service.converter.ConverterXjHysxjxxTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 设备巡检会议室关联巡检信息(XjHysxjxx)业务层
 * @author majunjie
 * @since 2025-01-09 14:00:52
 */
@Service
public class XjHysxjxxServiceImpl extends BaseApiImpl<XjHysxjxxEsu, XjHysxjxxEsp, XjHysxjxxEsr, XjHysxjxxDto> implements XjHysxjxxService {

    @Autowired
    private XjHysxjxxDao xjHysxjxxDao;
    @Autowired
    private ConverterXjHysxjxxEtoT converterXjHysxjxxEtoT;
    @Autowired
    private ConverterXjHysxjxxTtoR converterXjHysxjxxTtoR;

    public XjHysxjxxServiceImpl(XjHysxjxxDao xjHysxjxxDao, ConverterXjHysxjxxEtoT converterXjHysxjxxEtoT, ConverterXjHysxjxxTtoR converterXjHysxjxxTtoR) {
        super(xjHysxjxxDao, converterXjHysxjxxEtoT, converterXjHysxjxxTtoR);
    }

    @Override
    public List<XjHysxjxxEsr> selectHyxcXx(XjHysxjxxQuery xjHysxjxxQuery) {
        List<XjHysxjxxDto> xjHysxjxxDtos = xjHysxjxxDao.selectList(new QueryWrapper<XjHysxjxxDto>().lambda().eq(XjHysxjxxDto::getHysId, xjHysxjxxQuery.getId()));
        List<XjHysxjxxEsr> list = Optional.ofNullable(converterXjHysxjxxTtoR.toList(xjHysxjxxDtos)).orElse(Lists.newArrayList());
        return list;
    }
}
