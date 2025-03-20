package com.essence.service.impl;

import com.essence.dao.ViewHysXjDao;
import com.essence.dao.entity.ViewHysXjDto;

import com.essence.interfaces.api.ViewHysXjService;
import com.essence.interfaces.api.XjHysJhService;
import com.essence.interfaces.model.ViewHysXjEsr;
import com.essence.interfaces.model.ViewHysXjEsu;
import com.essence.interfaces.param.ViewHysXjEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterViewHysXjEtoT;
import com.essence.service.converter.ConverterViewHysXjTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (ViewHysXj)业务层
 * @author majunjie
 * @since 2025-01-09 10:22:26
 */
@Service
public class ViewHysXjServiceImpl extends BaseApiImpl<ViewHysXjEsu, ViewHysXjEsp, ViewHysXjEsr, ViewHysXjDto> implements ViewHysXjService {

    @Autowired
    private ViewHysXjDao viewHysXjDao;
    @Autowired
    private ConverterViewHysXjEtoT converterViewHysXjEtoT;
    @Autowired
    private ConverterViewHysXjTtoR converterViewHysXjTtoR;
@Autowired
private XjHysJhService xjHysJhService;
    public ViewHysXjServiceImpl(ViewHysXjDao viewHysXjDao, ConverterViewHysXjEtoT converterViewHysXjEtoT, ConverterViewHysXjTtoR converterViewHysXjTtoR) {
        super(viewHysXjDao, converterViewHysXjEtoT, converterViewHysXjTtoR);
    }

    @Override
    public ViewHysXjEsr updateHysJh(ViewHysXjEsr viewHysXjEsr) {
       return xjHysJhService.updateHysJh(viewHysXjEsr);

    }
}
