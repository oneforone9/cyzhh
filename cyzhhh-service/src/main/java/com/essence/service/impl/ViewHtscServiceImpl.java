package com.essence.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.ViewHtscDao;
import com.essence.dao.entity.ViewHtscDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.ViewHtscService;
import com.essence.interfaces.model.Hthsz;
import com.essence.interfaces.model.ViewHtscEsr;
import com.essence.interfaces.model.ViewHtscEsu;
import com.essence.interfaces.param.ViewHtscEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterViewHtscEtoT;
import com.essence.service.converter.ConverterViewHtscTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (ViewHtsc)业务层
 * @author majunjie
 * @since 2024-09-13 16:41:08
 */
@Service
public class ViewHtscServiceImpl extends BaseApiImpl<ViewHtscEsu, ViewHtscEsp, ViewHtscEsr, ViewHtscDto> implements ViewHtscService {

    @Autowired
    private ViewHtscDao viewHtscDao;
    @Autowired
    private ConverterViewHtscEtoT converterViewHtscEtoT;
    @Autowired
    private ConverterViewHtscTtoR converterViewHtscTtoR;

    public ViewHtscServiceImpl(ViewHtscDao viewHtscDao, ConverterViewHtscEtoT converterViewHtscEtoT, ConverterViewHtscTtoR converterViewHtscTtoR) {
        super(viewHtscDao, converterViewHtscEtoT, converterViewHtscTtoR);
    }

    @Override
    public Boolean selectHtsc(Hthsz hthsz) {
        Boolean type=false;
        List<ViewHtscDto> viewHtscDtos = viewHtscDao.selectList(new QueryWrapper<ViewHtscDto>().lambda().eq(ViewHtscDto::getId, hthsz.getId()).eq(ViewHtscDto::getUserId, hthsz.getUserId()));
        if (CollectionUtil.isNotEmpty(viewHtscDtos)){
            type=true;
        }
        return type;
    }
}
