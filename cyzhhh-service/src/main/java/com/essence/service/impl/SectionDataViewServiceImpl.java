package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.SectionBaseDao;
import com.essence.dao.SectionDataViewDao;
import com.essence.dao.SectionWaterQualityDao;
import com.essence.dao.entity.SectionBase;
import com.essence.dao.entity.SectionDataViewDto;
import com.essence.dao.entity.SectionWaterQuality;
import com.essence.interfaces.api.SectionDataViewService;

import com.essence.interfaces.model.SectionDataViewEsr;
import com.essence.interfaces.model.SectionDataViewEsu;
import com.essence.interfaces.param.SectionDataViewEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterSectionDataViewEtoT;
import com.essence.service.converter.ConverterSectionDataViewTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (SectionDataView)业务层
 * @author BINX
 * @since 2023-01-05 14:59:48
 */
@Service
public class SectionDataViewServiceImpl extends BaseApiImpl<SectionDataViewEsu, SectionDataViewEsp, SectionDataViewEsr, SectionDataViewDto> implements SectionDataViewService {
    @Resource
    private SectionBaseDao sectionBaseDao;
    @Resource
    private SectionWaterQualityDao sectionWaterQualityDao;
    @Autowired
    private SectionDataViewDao sectionDataViewDao;
    @Autowired
    private ConverterSectionDataViewEtoT converterSectionDataViewEtoT;
    @Autowired
    private ConverterSectionDataViewTtoR converterSectionDataViewTtoR;

    public SectionDataViewServiceImpl(SectionDataViewDao sectionDataViewDao, ConverterSectionDataViewEtoT converterSectionDataViewEtoT, ConverterSectionDataViewTtoR converterSectionDataViewTtoR) {
        super(sectionDataViewDao, converterSectionDataViewEtoT, converterSectionDataViewTtoR);
    }
    @Override
    public int insert(SectionDataViewEsu esu) {
        SectionWaterQuality anzWaterQualityDto = new SectionWaterQuality();
        BeanUtil.copyProperties(esu,anzWaterQualityDto);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("section_name",esu.getSectionName());
        SectionBase sectionBase = sectionBaseDao.selectOne(wrapper);
        if (sectionBase != null){
            Integer id = sectionBase.getId();
            anzWaterQualityDto.setSectionId(id);
        }else {

            SectionBase sectionBase1 = new SectionBase();
            BeanUtil.copyProperties(esu,sectionBase1);
            sectionBaseDao.insert(sectionBase1);
            QueryWrapper wrapperNew = new QueryWrapper();
            wrapperNew.eq("section_name",esu.getSectionName());
            SectionBase sectionBaseNew = sectionBaseDao.selectOne(wrapperNew);
            Integer id = sectionBaseNew.getId();
            anzWaterQualityDto.setSectionId(id);
        }

        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("quality_period",anzWaterQualityDto.getQualityPeriod());
        wrapper1.eq("section_id",anzWaterQualityDto.getSectionId());
        SectionWaterQuality sectionWaterQuality = sectionWaterQualityDao.selectOne(wrapper1);
        if (sectionWaterQuality != null){
            sectionWaterQualityDao.update(anzWaterQualityDto,wrapper1);
        }else {
            int insert = sectionWaterQualityDao.insert(anzWaterQualityDto);
        }
        return 0;
    }

    @Override
    public int update(SectionDataViewEsu esu) {
//
        SectionWaterQuality anzWaterQualityDto = new SectionWaterQuality();
        BeanUtil.copyProperties(esu,anzWaterQualityDto);
        int insert = sectionWaterQualityDao.updateById(anzWaterQualityDto);
        return 0;
    }


    @Override
    public int deleteById(String id) {
        sectionWaterQualityDao.deleteById(id);
        return 0;
    }
}
