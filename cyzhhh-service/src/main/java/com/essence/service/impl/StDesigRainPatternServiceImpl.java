package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StDesigRainPatternDao;
import com.essence.dao.entity.StDesigRainPatternDto;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.StDesigRainPatternService;
import com.essence.interfaces.model.FloodDesigRainPatternEsrVo;
import com.essence.interfaces.model.StDesigRainPatternEsr;
import com.essence.interfaces.model.StDesigRainPatternEsu;
import com.essence.interfaces.param.StDesigRainPatternEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStDesigRainPatternEtoT;
import com.essence.service.converter.ConverterStDesigRainPatternTtoR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 设计雨型(StDesigRainPattern)业务层
 *
 * @author majunjie
 * @since 2023-04-24 09:57:27
 */
@Service
public class StDesigRainPatternServiceImpl extends BaseApiImpl<StDesigRainPatternEsu, StDesigRainPatternEsp, StDesigRainPatternEsr, StDesigRainPatternDto> implements StDesigRainPatternService {

    @Autowired
    private StDesigRainPatternDao stDesigRainPatternDao;
    @Autowired
    private ConverterStDesigRainPatternEtoT converterStDesigRainPatternEtoT;
    @Autowired
    private ConverterStDesigRainPatternTtoR converterStDesigRainPatternTtoR;

    public StDesigRainPatternServiceImpl(StDesigRainPatternDao stDesigRainPatternDao, ConverterStDesigRainPatternEtoT converterStDesigRainPatternEtoT, ConverterStDesigRainPatternTtoR converterStDesigRainPatternTtoR) {
        super(stDesigRainPatternDao, converterStDesigRainPatternEtoT, converterStDesigRainPatternTtoR);
    }

    @Override
    public String addRainFallPattern(StDesigRainPatternEsu stDesigRainPatternEsu) {
        try {
            stDesigRainPatternEsu.setDesignRainPatternId(UuidUtil.get32UUIDStr());
            StDesigRainPatternDto stDesigRainPatternDto = converterStDesigRainPatternEtoT.toBean(stDesigRainPatternEsu);
            stDesigRainPatternDao.insert(stDesigRainPatternDto);
            return stDesigRainPatternDto.getDesignRainPatternId();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public List<FloodDesigRainPatternEsrVo> selectDesigRainPattern(String hourCount) {
        List<FloodDesigRainPatternEsrVo> floodDesigRainPatternEsrVoList = new ArrayList<>();
        List<StDesigRainPatternDto> stDesigRainPatternDtoList = new ArrayList<>();
        if (StringUtils.isNotBlank(hourCount)) {
            stDesigRainPatternDtoList = stDesigRainPatternDao.selectList(new QueryWrapper<StDesigRainPatternDto>().lambda().eq(StDesigRainPatternDto::getHourCount, Integer.valueOf(hourCount)));
        } else {
            stDesigRainPatternDtoList = stDesigRainPatternDao.selectList(new QueryWrapper<StDesigRainPatternDto>().lambda().orderByAsc(StDesigRainPatternDto::getHourCount));
        }
        if (null != stDesigRainPatternDtoList && stDesigRainPatternDtoList.size() > 0) {
            for (StDesigRainPatternDto stDesigRainPatternDto : stDesigRainPatternDtoList) {
                StringBuffer dateStr = new StringBuffer();
                FloodDesigRainPatternEsrVo floodDesigRainPatternEsrVo = new FloodDesigRainPatternEsrVo();
                BeanUtils.copyProperties(stDesigRainPatternDto, floodDesigRainPatternEsrVo);
                floodDesigRainPatternEsrVo.setDesignRainPatternId(stDesigRainPatternDto.getDesignRainPatternId().toString());
                String param = stDesigRainPatternDto.getParam();
                String[] split = param.split(",");
                List<String> paramLiat = Arrays.asList(split);
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : paramLiat) {
                    String paramStr = new Float(Float.valueOf(s) * 100).toString();
                    stringBuffer.append(paramStr.substring(0, paramStr.lastIndexOf('.')) + ",");
                }
                floodDesigRainPatternEsrVo.setParam(stringBuffer.substring(0, stringBuffer.length() - 1));
                Integer start = stDesigRainPatternDto.getHourCount() * 60 / stDesigRainPatternDto.getTimeInterval();
                for (int i = 1; i <= start; i++) {
                    dateStr.append(stDesigRainPatternDto.getTimeInterval() * i + ",");
                }
                floodDesigRainPatternEsrVo.setInterval(dateStr.toString());
                floodDesigRainPatternEsrVoList.add(floodDesigRainPatternEsrVo);
            }
        }
        return floodDesigRainPatternEsrVoList;
    }
}
