package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.HtglysxmDao;
import com.essence.dao.entity.HtglysxmDto;
import com.essence.interfaces.api.HtglysxmService;
import com.essence.interfaces.model.HtglysxmEsr;
import com.essence.interfaces.model.HtglysxmEsu;
import com.essence.interfaces.param.HtglysxmEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterHtglysxmEtoT;
import com.essence.service.converter.ConverterHtglysxmTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 合同管理预审项目(Htglysxm)业务层
 *
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
@Service
public class HtglysxmServiceImpl extends BaseApiImpl<HtglysxmEsu, HtglysxmEsp, HtglysxmEsr, HtglysxmDto> implements HtglysxmService {


    @Autowired
    private HtglysxmDao htglysxmDao;
    @Autowired
    private ConverterHtglysxmEtoT converterHtglysxmEtoT;
    @Autowired
    private ConverterHtglysxmTtoR converterHtglysxmTtoR;
    public HtglysxmServiceImpl(HtglysxmDao htglysxmDao, ConverterHtglysxmEtoT converterHtglysxmEtoT, ConverterHtglysxmTtoR converterHtglysxmTtoR) {
        super(htglysxmDao, converterHtglysxmEtoT, converterHtglysxmTtoR);
    }

    @Override
    public void saveYsxnData(List<HtglysxmEsu> ysxmData, String htid) {
        htglysxmDao.delete(new QueryWrapper<HtglysxmDto>().lambda().eq(HtglysxmDto::getHtid, htid));
        List<HtglysxmDto> list = converterHtglysxmEtoT.toList(ysxmData);
        for (HtglysxmDto htglysxmDto : list) {
            htglysxmDto.setId(UUID.randomUUID().toString().replace("-", ""));
            htglysxmDto.setHtid(htid);
        }
        htglysxmDao.saveYsxnData(list);
    }

    @Override
    public void deleteData(List<String> ids) {
        htglysxmDao.delete(new QueryWrapper<HtglysxmDto>().lambda().in(HtglysxmDto::getHtid,ids));
    }
}
