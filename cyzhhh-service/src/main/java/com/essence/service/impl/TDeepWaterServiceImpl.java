package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.essence.dao.TDeepWaterDao;
import com.essence.dao.entity.TDeepWaterDto;
import com.essence.interfaces.api.TDeepWaterService;

import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.TDeepWaterEsr;
import com.essence.interfaces.model.TDeepWaterEsu;
import com.essence.interfaces.param.TDeepWaterEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTDeepWaterEtoT;
import com.essence.service.converter.ConverterTDeepWaterTtoR;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地下水埋深(TDeepWater)业务层
 * @author BINX
 * @since 2023-01-04 14:46:12
 */
@Service
public class TDeepWaterServiceImpl extends BaseApiImpl<TDeepWaterEsu, TDeepWaterEsp, TDeepWaterEsr, TDeepWaterDto> implements TDeepWaterService {


    @Autowired
    private TDeepWaterDao tDeepWaterDao;
    @Autowired
    private ConverterTDeepWaterEtoT converterTDeepWaterEtoT;
    @Autowired
    private ConverterTDeepWaterTtoR converterTDeepWaterTtoR;

    public TDeepWaterServiceImpl(TDeepWaterDao tDeepWaterDao, ConverterTDeepWaterEtoT converterTDeepWaterEtoT, ConverterTDeepWaterTtoR converterTDeepWaterTtoR) {
        super(tDeepWaterDao, converterTDeepWaterEtoT, converterTDeepWaterTtoR);
    }

    @Override
    public int insert(TDeepWaterEsu e) {
        TDeepWaterDto deepWaterDto = new TDeepWaterDto();
        BeanUtil.copyProperties(e,deepWaterDto);
        deepWaterDto.setUpdateTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
        String type = e.getType();
        String year = e.getYear();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type",type);
        queryWrapper.eq("year",year);
        List<TDeepWaterDto> tDeepWaterDtos = tDeepWaterDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(tDeepWaterDtos)){
            tDeepWaterDao.update(deepWaterDto,queryWrapper);
        }else {
            return tDeepWaterDao.insert(deepWaterDto);
        }
      return 1;
    }

    @Override
    public Paginator<TDeepWaterEsr> findByPaginator(PaginatorParam param) {
        List<Criterion> conditions = param.getConditions();
        String type = "";
        if (CollUtil.isNotEmpty(conditions)){
            for (Criterion condition : conditions) {
                if (condition.getFieldName().equals("type")){
                    //如果月度数据
                    if (condition.getValue().toString().equals("2")){
                        type = "2";
                    }
                }
            }
            Paginator<TDeepWaterEsr> byPaginator = super.findByPaginator(param);
            if (type.equals("2")){
                if (CollUtil.isEmpty(byPaginator.getItems())){
                    //时间往前推 8个月查询
                    Date date = new Date();
                    DateTime dateTime = DateUtil.offsetMonth(date, -8);
                    String start = DateUtil.format(dateTime, "yyyy-MM");
                    String end = DateUtil.format(date, "yyyy-MM");
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.between("year",start,end);
                    List<TDeepWaterDto> tDeepWaterDtos = tDeepWaterDao.selectList(queryWrapper);
                    List<TDeepWaterEsr> tDeepWaterEsrs = converterTDeepWaterTtoR.toList(tDeepWaterDtos);
                    tDeepWaterEsrs = tDeepWaterEsrs.stream().sorted(Comparator.comparing(TDeepWaterEsr::getYear)).collect(Collectors.toList());
                    byPaginator.setItems(tDeepWaterEsrs);
                }
            }
            return byPaginator;
        }
        Paginator<TDeepWaterEsr> byPaginator = super.findByPaginator(param);
        return byPaginator;
    }
}
