package com.essence.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.XjSxtJhTimeDao;
import com.essence.dao.entity.XjSxtJhTimeDto;
import com.essence.interfaces.api.XjSxtJhTimeService;
import com.essence.interfaces.model.XjSxtJhTimeEsr;
import com.essence.interfaces.model.XjSxtJhTimeEsu;
import com.essence.interfaces.param.XjSxtJhTimeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjSxtJhTimeEtoT;
import com.essence.service.converter.ConverterXjSxtJhTimeTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备巡检摄像头巡检计划日期(XjSxtJhTime)业务层
 * @author majunjie
 * @since 2025-01-08 17:51:55
 */
@Service
public class XjSxtJhTimeServiceImpl extends BaseApiImpl<XjSxtJhTimeEsu, XjSxtJhTimeEsp, XjSxtJhTimeEsr, XjSxtJhTimeDto> implements XjSxtJhTimeService {

    @Autowired
    private XjSxtJhTimeDao xjSxtJhTimeDao;
    @Autowired
    private ConverterXjSxtJhTimeEtoT converterXjSxtJhTimeEtoT;
    @Autowired
    private ConverterXjSxtJhTimeTtoR converterXjSxtJhTimeTtoR;

    public XjSxtJhTimeServiceImpl(XjSxtJhTimeDao xjSxtJhTimeDao, ConverterXjSxtJhTimeEtoT converterXjSxtJhTimeEtoT, ConverterXjSxtJhTimeTtoR converterXjSxtJhTimeTtoR) {
        super(xjSxtJhTimeDao, converterXjSxtJhTimeEtoT, converterXjSxtJhTimeTtoR);
    }

    @Override
    public List<XjSxtJhTimeEsr> selectData(Integer id) {
        List<XjSxtJhTimeDto> xjSxtJhTimeDtos = xjSxtJhTimeDao.selectList(new QueryWrapper<XjSxtJhTimeDto>().lambda().eq(XjSxtJhTimeDto::getJhId, id));
        return converterXjSxtJhTimeTtoR.toList(xjSxtJhTimeDtos);
    }

    @Override
    public void saveData(List<XjSxtJhTimeEsu> list,String jhid) {
        xjSxtJhTimeDao.delete(new QueryWrapper<XjSxtJhTimeDto>().lambda().eq(XjSxtJhTimeDto::getJhId,jhid));
        xjSxtJhTimeDao.saveData(converterXjSxtJhTimeEtoT.toList(list));
    }
}
