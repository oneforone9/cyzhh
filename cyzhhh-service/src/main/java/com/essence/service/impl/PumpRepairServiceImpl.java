package com.essence.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.RainDateHourDto;
import com.essence.common.dto.RainInfoStatisticDto;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.dao.*;
import com.essence.dao.entity.PumpRepairEntity;
import com.essence.dao.entity.RainDayCountDto;
import com.essence.dao.entity.StRainDateDto;
import com.essence.dao.entity.StStbprpBEntity;
import com.essence.interfaces.api.PumpRepairService;
import com.essence.interfaces.api.StRainDateService;
import com.essence.interfaces.model.PumpRepairEsr;
import com.essence.interfaces.model.PumpRepairEsu;
import com.essence.interfaces.model.StRainDateEsr;
import com.essence.interfaces.model.StRainDateEsu;
import com.essence.interfaces.param.PumpRepairEsp;
import com.essence.interfaces.param.StRainDateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterPumpRepairEtoT;
import com.essence.service.converter.ConverterPumpRepairTtoR;
import com.essence.service.converter.ConverterStRainDateEtoT;
import com.essence.service.converter.ConverterStRainDateTtoR;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (StRainDate)业务层
 * @author BINX
 * @since 2023-02-20 14:33:07
 */
@Service
public class PumpRepairServiceImpl extends BaseApiImpl<PumpRepairEsu, PumpRepairEsp, PumpRepairEsr, PumpRepairEntity> implements PumpRepairService {


    @Autowired
    private PumpRepairDao pumpRepairDao;
    @Autowired
    private ConverterPumpRepairEtoT converterPumpRepairEtoT;
    @Autowired
    private ConverterPumpRepairTtoR converterPumpRepairTtoR;

    public PumpRepairServiceImpl(PumpRepairDao pumpRepairDao, ConverterPumpRepairEtoT converterPumpRepairEtoT, ConverterPumpRepairTtoR converterPumpRepairTtoR) {
        super(pumpRepairDao, converterPumpRepairEtoT, converterPumpRepairTtoR);
    }


}
