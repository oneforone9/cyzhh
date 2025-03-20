package com.essence.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.exception.BusinessException;
import com.essence.dao.StPumpDataDao;
import com.essence.dao.StSideGateDao;
import com.essence.dao.entity.StPumpDataDto;
import com.essence.dao.entity.StSideGateDto;
import com.essence.interfaces.api.StPumpDataService;
import com.essence.interfaces.api.StSideGateService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.PumpFlowChartEsr;
import com.essence.interfaces.model.StPumpDataEsr;
import com.essence.interfaces.model.StPumpDataEsu;
import com.essence.interfaces.param.PumpFlowChartEsp;
import com.essence.interfaces.param.StPumpDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPumpDataEtoT;
import com.essence.service.converter.ConverterStPumpDataTtoR;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (StPumpData)业务层
 * @author BINX
 * @since 2023-04-14 11:36:07
 */
@Service
public class StPumpDataServiceImpl extends BaseApiImpl<StPumpDataEsu, StPumpDataEsp, StPumpDataEsr, StPumpDataDto> implements StPumpDataService {

    @Autowired
    StSideGateService stSideGateService;
    @Autowired
    private StPumpDataDao stPumpDataDao;
    @Autowired
    StSideGateDao stSideGateDao;
    @Autowired
    private ConverterStPumpDataEtoT converterStPumpDataEtoT;
    @Autowired
    private ConverterStPumpDataTtoR converterStPumpDataTtoR;

    public StPumpDataServiceImpl(StPumpDataDao stPumpDataDao, ConverterStPumpDataEtoT converterStPumpDataEtoT, ConverterStPumpDataTtoR converterStPumpDataTtoR) {
        super(stPumpDataDao, converterStPumpDataEtoT, converterStPumpDataTtoR);
    }

    @SneakyThrows
    @Override
    public Object getPumpFlowChart(PumpFlowChartEsp pumpFlowChartEsp) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StrUtil.isEmpty(pumpFlowChartEsp.getStcd())) {
            throw new BusinessException("测站编码不能为空! 字段: stcd 类型: String");
        }
        if(null == pumpFlowChartEsp.getStartTime()) {
            throw new BusinessException("开始时间不能为空! 字段: startTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        }
        if(null == pumpFlowChartEsp.getEndTime()) {
            throw new BusinessException("结束时间不能为空! 字段: endTime 类型: Date 格式: yyyy-MM-dd HH:mm:ss");
        }
        QueryWrapper<StSideGateDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stcd", pumpFlowChartEsp.getStcd());
        queryWrapper.eq("sttp", "DP");
        queryWrapper.eq("remote_control", 1);
        StSideGateDto stSideGateDto = stSideGateDao.selectOne(queryWrapper);
        QueryWrapper<StPumpDataDto> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("device_addr", stSideGateDto.getStcd());
        queryWrapper1.ge("date", pumpFlowChartEsp.getStartTime());
        queryWrapper1.le("date", pumpFlowChartEsp.getEndTime());
        queryWrapper1.orderByAsc("date");
        List<StPumpDataDto> stPumpDataDtos = stPumpDataDao.selectList(queryWrapper1);
        List<String> addFlowRateList = stPumpDataDtos.stream().map(StPumpDataDto::getAddFlowRate).collect(Collectors.toList());
        List<String> flowRateList = stPumpDataDtos.stream().map(StPumpDataDto::getFlowRate).collect(Collectors.toList());
        List<Date> dateList = stPumpDataDtos.stream().map(StPumpDataDto::getDate).collect(Collectors.toList());
        List<String> dateFormatList = new ArrayList<>();
        dateList.forEach(date -> {
            dateFormatList.add(timeFormat.format(date));
        });
        PumpFlowChartEsr flowChartEsr = new PumpFlowChartEsr();
        flowChartEsr.setStcd(stSideGateDto.getStcd());
        flowChartEsr.setStnm(stSideGateDto.getStnm());
        flowChartEsr.setStlc(stSideGateDto.getStlc());
        flowChartEsr.setLgtd(stSideGateDto.getLgtd());
        flowChartEsr.setLttd(stSideGateDto.getLttd());
        flowChartEsr.setRiverId(stSideGateDto.getRiverId());
        flowChartEsr.setFlowRateList(flowRateList);
        flowChartEsr.setAddFlowRateList(addFlowRateList);
        flowChartEsr.setDateList(dateFormatList);
        return flowChartEsr;
    }

    @Override
    public Object getPumpList(PaginatorParam param) {
        ArrayList<Criterion> currency = new ArrayList<>();
        Criterion e = new Criterion();
        e.setFieldName("sttp");
        e.setOperator(Criterion.EQ);
        e.setValue("DP");
        currency.add(e);
        Criterion e1 = new Criterion();
        e1.setFieldName("remoteControl");
        e1.setOperator(Criterion.EQ);
        e1.setValue("1");
        currency.add(e1);
        param.setCurrency(currency);
        return stSideGateService.findByPaginator(param);
    }
}
