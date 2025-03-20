package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.dao.UseWaterDao;
import com.essence.dao.entity.UseWaterDto;
import com.essence.interfaces.api.UseWaterService;

import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.UseWaterEsr;
import com.essence.interfaces.model.UseWaterEsu;
import com.essence.interfaces.param.UseWaterEsp;
import com.essence.service.baseconverter.PaginatorConverter;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterUseWaterEtoT;
import com.essence.service.converter.ConverterUseWaterTtoR;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用水量(UseWater)业务层
 * @author BINX
 * @since 2023-01-04 17:18:01
 */
@Service
public class UseWaterServiceImpl extends BaseApiImpl<UseWaterEsu, UseWaterEsp, UseWaterEsr, UseWaterDto> implements UseWaterService {


    @Autowired
    private UseWaterDao useWaterDao;
    @Autowired
    private ConverterUseWaterEtoT converterUseWaterEtoT;
    @Autowired
    private ConverterUseWaterTtoR converterUseWaterTtoR;

    public UseWaterServiceImpl(UseWaterDao useWaterDao, ConverterUseWaterEtoT converterUseWaterEtoT, ConverterUseWaterTtoR converterUseWaterTtoR) {
        super(useWaterDao, converterUseWaterEtoT, converterUseWaterTtoR);
    }

    @Override
    public int insert(UseWaterEsu e) {
        UseWaterDto useWaterDto = new UseWaterDto();
        BeanUtil.copyProperties(e,useWaterDto);
        useWaterDto.setUpdateTime(DateUtil.format(new Date(),"yyyy-MM-dd").substring(0,7));
        return useWaterDao.insert(useWaterDto);
    }

    @Override
    public Paginator<UseWaterEsr> findByPaginator(PaginatorParam param) {
        Paginator<UseWaterEsr> byPaginator = super.findByPaginator(param);
        if (CollUtil.isEmpty(byPaginator.getItems())){
            DateTime offset = DateUtil.offset(new Date(), DateField.YEAR, -1);
            PaginatorParam paginatorParam = new PaginatorParam();
            List<Criterion> conditions = new ArrayList<>();
            Criterion criterion = new Criterion();
            criterion.setFieldName("date");
            criterion.setOperator("LIKE");
            criterion.setValue(DateUtil.format(offset,"yyyy"));
            conditions.add(criterion);
            paginatorParam.setConditions(conditions);
            byPaginator = super.findByPaginator(paginatorParam);
            List<UseWaterEsr> items = byPaginator.getItems();
            items = items.parallelStream().sorted(Comparator.comparing(UseWaterEsr::getDate)).collect(Collectors.toList());
            byPaginator.setItems(items);
        }
        return byPaginator;
    }
}
