package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.HtglfjDao;
import com.essence.dao.entity.HtglfjDto;
import com.essence.interfaces.api.HtglfjService;
import com.essence.interfaces.model.HtglfjEsr;
import com.essence.interfaces.model.HtglfjEsu;
import com.essence.interfaces.param.HtglfjEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtglfjEtoT;
import com.essence.service.converter.ConverterHtglfjTtoR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 合同管理附件(Htglfj)业务层
 * @author majunjie
 * @since 2024-09-10 14:02:23
 */
@Service
public class HtglfjServiceImpl extends BaseApiImpl<HtglfjEsu, HtglfjEsp, HtglfjEsr, HtglfjDto> implements HtglfjService {
    @Autowired
    private HtglfjDao htglfjDao;
    @Autowired
    private ConverterHtglfjEtoT converterHtglfjEtoT;
    @Autowired
    private ConverterHtglfjTtoR converterHtglfjTtoR;

    public HtglfjServiceImpl(HtglfjDao htglfjDao, ConverterHtglfjEtoT converterHtglfjEtoT, ConverterHtglfjTtoR converterHtglfjTtoR) {
        super(htglfjDao, converterHtglfjEtoT, converterHtglfjTtoR);
    }

    @Override
    public void saveFjData(List<HtglfjEsu> fjData, String htid) {
        htglfjDao.delete(new QueryWrapper<HtglfjDto>().lambda().eq(HtglfjDto::getHtid,htid));
        List<HtglfjDto> list = converterHtglfjEtoT.toList(fjData);
        for (HtglfjDto htglfjDto : list) {
            htglfjDto.setId(UUID.randomUUID().toString().replace("-", ""));
            htglfjDto.setHtid(htid);
        }
        htglfjDao.saveFjData(list);
    }

    @Override
    public void deleteData(List<String> ids) {
        htglfjDao.delete(new QueryWrapper<HtglfjDto>().lambda().in(HtglfjDto::getHtid,ids));
    }

    @Override
    public void saveFjDatas(List<HtglfjEsu> fjData) {
        List<HtglfjDto> list = converterHtglfjEtoT.toList(fjData);
        htglfjDao.saveFjData(list);
    }
}
