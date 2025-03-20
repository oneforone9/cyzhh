package com.essence.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.StWellcollectFeeDao;
import com.essence.dao.entity.StWellcollectFeeDto;
import com.essence.interfaces.api.StWellcollectFeeService;
import com.essence.interfaces.model.StWellcollectFeeEsr;
import com.essence.interfaces.model.StWellcollectFeeEsu;
import com.essence.interfaces.model.StWellcollectFeeEsuVo;
import com.essence.interfaces.param.StWellcollectFeeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWellcollectFeeEtoT;
import com.essence.service.converter.ConverterStWellcollectFeeTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (StWellcollectFee)业务层
 *
 * @author bird
 * @since 2023-01-04 18:01:04
 */
@Service
public class StWellcollectFeeServiceImpl extends BaseApiImpl<StWellcollectFeeEsu, StWellcollectFeeEsp, StWellcollectFeeEsr, StWellcollectFeeDto> implements StWellcollectFeeService {

    @Autowired
    private StWellcollectFeeDao stWellcollectFeeDao;
    @Autowired
    private ConverterStWellcollectFeeEtoT converterStWellcollectFeeEtoT;
    @Autowired
    private ConverterStWellcollectFeeTtoR converterStWellcollectFeeTtoR;

    public StWellcollectFeeServiceImpl(StWellcollectFeeDao stWellcollectFeeDao, ConverterStWellcollectFeeEtoT converterStWellcollectFeeEtoT, ConverterStWellcollectFeeTtoR converterStWellcollectFeeTtoR) {
        super(stWellcollectFeeDao, converterStWellcollectFeeEtoT, converterStWellcollectFeeTtoR);
    }

    /**
     *
     * @param  year 此处传递季度 年-季度
     * @return
     */
    @Override
    public ResponseResult selectStWellcollectFee(String year) {
        StWellcollectFeeEsuVo stWellcollectFeeEsuVo = new StWellcollectFeeEsuVo();
        List<StWellcollectFeeDto> stWellcollectFeeDtos = stWellcollectFeeDao.selectList(new QueryWrapper<StWellcollectFeeDto>().lambda().eq(StWellcollectFeeDto::getTjJd, year));
        if (CollUtil.isEmpty(stWellcollectFeeDtos)){
            String[] split = year.split("-");
            String yearD = split[0];
            String monthD = split[1];
            Integer integer = Integer.valueOf(monthD);
            if (integer -1 <=0){
                Integer integerYear = Integer.valueOf(yearD);
                Integer yearInteger = integerYear - 1 ;
                year = String.valueOf(yearInteger+"-4");
            }else {
                Integer monthI = integer - 1;
                year = yearD+"-"+monthI;
            }
            stWellcollectFeeDtos = stWellcollectFeeDao.selectList(new QueryWrapper<StWellcollectFeeDto>().lambda().eq(StWellcollectFeeDto::getTjJd, year));
        }
        if (null != stWellcollectFeeDtos && stWellcollectFeeDtos.size() > 0) {


            List<StWellcollectFeeDto> xSumList = Optional.ofNullable(stWellcollectFeeDtos.stream().filter(x -> x.getType().equals("1")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            DecimalFormat decimalFormats = new DecimalFormat("0.0000");
            double xSumPayment = 0.00;
            double shPaymentSum = 0.00;
            if (null != xSumList && xSumList.size() > 0) {
                double xgPaymentSum = xSumList.stream().mapToDouble(StWellcollectFeeDto::getXzjjSj).sum();
                double xgShPaymentSum = xSumList.stream().mapToDouble(StWellcollectFeeDto::getShdwjjSj).sum();
                stWellcollectFeeEsuVo.setXgPayment(decimalFormats.format(xgPaymentSum));
                stWellcollectFeeEsuVo.setXgShPayment(decimalFormats.format(xgShPaymentSum));
                xSumPayment = xgPaymentSum + xgShPaymentSum;
                stWellcollectFeeEsuVo.setXSumPayment(decimalFormats.format(xSumPayment));
            }
            List<StWellcollectFeeDto> shSumList = Optional.ofNullable(stWellcollectFeeDtos.stream().filter(x -> x.getType().equals("2")).collect(Collectors.toList())).orElse(Lists.newArrayList());
            if (null != shSumList && shSumList.size() > 0) {
                shPaymentSum = shSumList.stream().mapToDouble(StWellcollectFeeDto::getShdwjjSj).sum();
                stWellcollectFeeEsuVo.setShPayment(decimalFormats.format(shPaymentSum));
                stWellcollectFeeEsuVo.setJSumPayment(decimalFormats.format(shPaymentSum));
            }
            double sum = xSumPayment + shPaymentSum;
            stWellcollectFeeEsuVo.setSumPayment(decimalFormats.format(sum));
            StWellcollectFeeDto stWellcollectFeeDto = stWellcollectFeeDtos.get(stWellcollectFeeDtos.size() - 1);
            stWellcollectFeeEsuVo.setUpdate_time(StrUtil.isNotEmpty(stWellcollectFeeDto.getTjJd()) ? stWellcollectFeeDto.getTjJd() : null );
        }
        return ResponseResult.success("查询成功", stWellcollectFeeEsuVo);
    }
}
