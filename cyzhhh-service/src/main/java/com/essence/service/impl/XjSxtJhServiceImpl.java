package com.essence.service.impl;

import com.essence.dao.XjSxtJhDao;
import com.essence.dao.entity.XjSxtJhDto;

import com.essence.interfaces.api.XjSxtJhService;
import com.essence.interfaces.model.ViewVideoXjEsr;
import com.essence.interfaces.model.XjSxtJhEsr;
import com.essence.interfaces.model.XjSxtJhEsu;
import com.essence.interfaces.param.XjSxtJhEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjSxtJhEtoT;
import com.essence.service.converter.ConverterXjSxtJhTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备巡检摄像头巡检计划(XjSxtJh)业务层
 *
 * @author majunjie
 * @since 2025-01-08 22:46:36
 */
@Service
public class XjSxtJhServiceImpl extends BaseApiImpl<XjSxtJhEsu, XjSxtJhEsp, XjSxtJhEsr, XjSxtJhDto> implements XjSxtJhService {

    @Autowired
    private XjSxtJhDao xjSxtJhDao;
    @Autowired
    private ConverterXjSxtJhEtoT converterXjSxtJhEtoT;
    @Autowired
    private ConverterXjSxtJhTtoR converterXjSxtJhTtoR;

    public XjSxtJhServiceImpl(XjSxtJhDao xjSxtJhDao, ConverterXjSxtJhEtoT converterXjSxtJhEtoT, ConverterXjSxtJhTtoR converterXjSxtJhTtoR) {
        super(xjSxtJhDao, converterXjSxtJhEtoT, converterXjSxtJhTtoR);
    }

    @Override
    public void updateData(ViewVideoXjEsr viewVideoXjEsr) {
        XjSxtJhDto xjSxtJhDto = xjSxtJhDao.selectById(viewVideoXjEsr.getJhid());
        if (null != xjSxtJhDto) {
            xjSxtJhDto.setXjnr(viewVideoXjEsr.getXjnr());
            xjSxtJhDto.setFzr(viewVideoXjEsr.getFzr());
            xjSxtJhDto.setFzrId(viewVideoXjEsr.getFzrId());
            xjSxtJhDto.setXjdwId(viewVideoXjEsr.getXjdwId());
            xjSxtJhDto.setBzId(viewVideoXjEsr.getBzId());
            xjSxtJhDto.setXjRq(viewVideoXjEsr.getXjRq());
            xjSxtJhDto.setBzmc(viewVideoXjEsr.getBzmc());
            xjSxtJhDao.updateById(xjSxtJhDto);
        } else {
            xjSxtJhDto=new XjSxtJhDto();
            xjSxtJhDto.setId(String.valueOf(viewVideoXjEsr.getId()));
            xjSxtJhDto.setSxtId(viewVideoXjEsr.getId());
            xjSxtJhDto.setXjnr(viewVideoXjEsr.getXjnr());
            xjSxtJhDto.setFzr(viewVideoXjEsr.getFzr());
            xjSxtJhDto.setFzrId(viewVideoXjEsr.getFzrId());
            xjSxtJhDto.setXjdwId(viewVideoXjEsr.getXjdwId());
            xjSxtJhDto.setBzId(viewVideoXjEsr.getBzId());
            xjSxtJhDto.setXjRq(viewVideoXjEsr.getXjRq());
            xjSxtJhDto.setBzmc(viewVideoXjEsr.getBzmc());
            xjSxtJhDao.insert(xjSxtJhDto);
        }
    }
}
