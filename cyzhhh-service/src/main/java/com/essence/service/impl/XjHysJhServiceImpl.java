package com.essence.service.impl;

import com.essence.dao.XjHysJhDao;
import com.essence.dao.entity.XjHysJhDto;
import com.essence.interfaces.api.XjHysJhService;
import com.essence.interfaces.model.ViewHysXjEsr;
import com.essence.interfaces.model.XjHysJhEsr;
import com.essence.interfaces.model.XjHysJhEsu;
import com.essence.interfaces.param.XjHysJhEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjHysJhEtoT;
import com.essence.service.converter.ConverterXjHysJhTtoR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备巡检会议室巡检计划(XjHysJh)业务层
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */
@Service
public class XjHysJhServiceImpl extends BaseApiImpl<XjHysJhEsu, XjHysJhEsp, XjHysJhEsr, XjHysJhDto> implements XjHysJhService {

    @Autowired
    private XjHysJhDao xjHysJhDao;
    @Autowired
    private ConverterXjHysJhEtoT converterXjHysJhEtoT;
    @Autowired
    private ConverterXjHysJhTtoR converterXjHysJhTtoR;

    public XjHysJhServiceImpl(XjHysJhDao xjHysJhDao, ConverterXjHysJhEtoT converterXjHysJhEtoT, ConverterXjHysJhTtoR converterXjHysJhTtoR) {
        super(xjHysJhDao, converterXjHysJhEtoT, converterXjHysJhTtoR);
    }

    @Override
    public ViewHysXjEsr updateHysJh(ViewHysXjEsr viewHysXjEsr) {
        if (StringUtils.isNotBlank(viewHysXjEsr.getJhid())){
            XjHysJhDto xjHysJhDto=xjHysJhDao.selectById(viewHysXjEsr.getJhid());
            xjHysJhDto.setXjnr(viewHysXjEsr.getXjnr());
            xjHysJhDto.setFzr(viewHysXjEsr.getFzr());
            xjHysJhDto.setFzrId(viewHysXjEsr.getFzrId());
            xjHysJhDto.setXjRq(viewHysXjEsr.getXjRq());
            xjHysJhDto.setXjdwId(viewHysXjEsr.getXjdwId());
            xjHysJhDto.setBzId(viewHysXjEsr.getBzId());
            xjHysJhDto.setBzmc(viewHysXjEsr.getBzmc());
            xjHysJhDao.updateById(xjHysJhDto);
        }else {
            XjHysJhDto xjHysJhDto=new XjHysJhDto();
            xjHysJhDto.setId(viewHysXjEsr.getId());
            xjHysJhDto.setXjnr(viewHysXjEsr.getXjnr());
            xjHysJhDto.setFzr(viewHysXjEsr.getFzr());
            xjHysJhDto.setFzrId(viewHysXjEsr.getFzrId());
            xjHysJhDto.setXjRq(viewHysXjEsr.getXjRq());
            xjHysJhDto.setXjdwId(viewHysXjEsr.getXjdwId());
            xjHysJhDto.setBzId(viewHysXjEsr.getBzId());
            xjHysJhDto.setBzmc(viewHysXjEsr.getBzmc());
            xjHysJhDao.insert(xjHysJhDto);
            viewHysXjEsr.setJhid(xjHysJhDto.getId());
        }
        return viewHysXjEsr;
    }
}
