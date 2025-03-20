package com.essence.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StPlanServiceDao;
import com.essence.dao.entity.StPlanServiceDto;
import com.essence.interfaces.api.StPlanServiceService;
import com.essence.interfaces.model.EventCompanyEsu;
import com.essence.interfaces.model.EventCompanyRes;
import com.essence.interfaces.model.StPlanServiceEsr;
import com.essence.interfaces.model.StPlanServiceEsu;
import com.essence.interfaces.param.StPlanServiceEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanServiceEtoT;
import com.essence.service.converter.ConverterStPlanServiceTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 闸坝维护计划基础表(StPlanService)业务层
 * @author liwy
 * @since 2023-07-13 14:46:20
 */
@Service
public class StPlanServiceServiceImpl extends BaseApiImpl<StPlanServiceEsu, StPlanServiceEsp, StPlanServiceEsr, StPlanServiceDto> implements StPlanServiceService {

    @Autowired
    private StPlanServiceDao stPlanServiceDao;
    @Autowired
    private ConverterStPlanServiceEtoT converterStPlanServiceEtoT;
    @Autowired
    private ConverterStPlanServiceTtoR converterStPlanServiceTtoR;

    public StPlanServiceServiceImpl(StPlanServiceDao stPlanServiceDao, ConverterStPlanServiceEtoT converterStPlanServiceEtoT, ConverterStPlanServiceTtoR converterStPlanServiceTtoR) {
        super(stPlanServiceDao, converterStPlanServiceEtoT, converterStPlanServiceTtoR);
    }

    /**
     * 根据类型获取设备设施名称
     * @param stPlanServiceEsu
     * @return
     */
    @Override
    public List<StPlanServiceEsr> selectEquipmentName(StPlanServiceEsu stPlanServiceEsu) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sttp",stPlanServiceEsu.getSttp());
        queryWrapper.last( " and (pid is null or pid  = '') ");
        List<StPlanServiceDto> list = stPlanServiceDao.selectList(queryWrapper);
        List<StPlanServiceEsr> resList = BeanUtil.copyToList(list, StPlanServiceEsr.class);
        resList = resList.stream().sorted(Comparator.comparing(StPlanServiceEsr::getSort)).collect(Collectors.toList());
        return resList;
    }

    /**
     * 根据设施名称和类型获取日常维护内容
     * @param stPlanServiceEsu
     * @return
     */
    @Override
    public List<StPlanServiceEsr> selectServiceContent(StPlanServiceEsu stPlanServiceEsu) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sttp",stPlanServiceEsu.getSttp());
        queryWrapper.eq("pid",stPlanServiceEsu.getPid());
        queryWrapper.orderByAsc("sort");
        List<StPlanServiceEsr> list = stPlanServiceDao.selectList(queryWrapper);
        return list;
    }
}
