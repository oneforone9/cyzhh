package com.essence.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StLevelWaterDao;
import com.essence.dao.StWaterGaugeDao;
import com.essence.dao.SyncWaterRulerDao;
import com.essence.dao.entity.StLevelWaterDto;
import com.essence.dao.entity.StWaterGaugeDto;
import com.essence.dao.entity.SyncWaterRulerDto;
import com.essence.interfaces.api.StWaterGaugeService;
import com.essence.interfaces.model.StWaterGaugeEsr;
import com.essence.interfaces.model.StWaterGaugeEsu;
import com.essence.interfaces.model.StWaterGaugeEsuParam;
import com.essence.interfaces.param.StWaterGaugeEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterGaugeEtoT;
import com.essence.service.converter.ConverterStWaterGaugeTtoR;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * (StWaterGauge)电子水尺积水台账业务层
 *
 * @author liwy
 * @since 2023-05-11 18:39:13
 */
@Service
public class StWaterGaugeServiceImpl extends BaseApiImpl<StWaterGaugeEsu, StWaterGaugeEsp, StWaterGaugeEsr, StWaterGaugeDto> implements StWaterGaugeService {

    @Autowired
    private StWaterGaugeDao stWaterGaugeDao;
    @Autowired
    private ConverterStWaterGaugeEtoT converterStWaterGaugeEtoT;
    @Autowired
    private ConverterStWaterGaugeTtoR converterStWaterGaugeTtoR;
    @Autowired
    private SyncWaterRulerDao syncWaterRulerDao;
    @Autowired
    private StLevelWaterDao stLevelWaterDao;

    public StWaterGaugeServiceImpl(StWaterGaugeDao stWaterGaugeDao, ConverterStWaterGaugeEtoT converterStWaterGaugeEtoT, ConverterStWaterGaugeTtoR converterStWaterGaugeTtoR) {
        super(stWaterGaugeDao, converterStWaterGaugeEtoT, converterStWaterGaugeTtoR);
    }

    /**
     * 获取道路积水点实时报警状态
     *
     * @return
     */
    @Override
    public List<StWaterGaugeDto> stWaterGaugeNow(String name, String warnStatus) {
        List<StWaterGaugeDto> waterList = stWaterGaugeDao.selectList(new LambdaQueryWrapper<StWaterGaugeDto>()
                .like(StringUtils.isNotBlank(name), StWaterGaugeDto::getName, name)
        );
        //两天之前到现在的所有数据
        List<SyncWaterRulerDto> waterRuleList = syncWaterRulerDao.selectList(new LambdaQueryWrapper<SyncWaterRulerDto>()
                .ge(SyncWaterRulerDto::getTime, DateUtil.offsetDay(new Date(), -2))
        );
        Map<String, SyncWaterRulerDto> waterRuleMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(waterRuleList)) {
            waterRuleMap = waterRuleList.parallelStream().collect(Collectors.toMap(SyncWaterRulerDto::getWaterCode, Function.identity(), (o1, o2) -> o2));
        }
        List<StLevelWaterDto> waterLevelList = stLevelWaterDao.selectList(null)
                .stream().sorted(Comparator.comparing(StLevelWaterDto::getLower)).collect(Collectors.toList());
        for (int i = 0; i < waterList.size(); i++) {
            StWaterGaugeDto stWaterGaugeDto = waterList.get(i);
            SyncWaterRulerDto rule = waterRuleMap.get(stWaterGaugeDto.getWaterCode());
            if (rule != null) {
                stWaterGaugeDto.setDepthWater(rule.getAmount());
                stWaterGaugeDto.setOccurTime(rule.getTime());
                String warnstatus = this.getWarnstatus(rule.getAmount(), waterLevelList);
                stWaterGaugeDto.setWarnstatus(warnstatus);
            } else {
                stWaterGaugeDto.setWarnstatus(waterLevelList.get(0).getLevelName());
            }
        }
        //报警状态选择筛选条件
        if (StrUtil.isNotEmpty(warnStatus)) {
            waterList = waterList.stream().filter(stWaterGaugeDto -> stWaterGaugeDto.getWarnstatus() != null)
                    .filter(stWaterGaugeDto -> stWaterGaugeDto.getWarnstatus().equals(warnStatus)).collect(Collectors.toList());
        }
        return waterList;
    }

    /**
     * 根据积水深度判断报警状态
     *
     * @param amount
     * @param stLevelWaterDtoList
     * @return
     */
    @Override
    public String getWarnstatus(BigDecimal amount, List<StLevelWaterDto> stLevelWaterDtoList) {
        String warnstatus = "";
        for (int i = 0; i < stLevelWaterDtoList.size(); i++) {
            StLevelWaterDto stLevelWaterDto = stLevelWaterDtoList.get(i);
            Double lower = stLevelWaterDto.getLower();
            Double upper = stLevelWaterDto.getUpper();
            String levelName = stLevelWaterDto.getLevelName();
            int falg1 = amount.compareTo(BigDecimal.valueOf(lower));
            if (null == upper) {
                if (falg1 >= 0) {
                    warnstatus = levelName;
                    break;
                }
            } else {
                int falg2 = amount.compareTo(BigDecimal.valueOf(upper));
                if (falg1 >= 0 && falg2 == -1) {
                    warnstatus = levelName;
                    break;
                }
            }
        }
        return warnstatus;
    }

    /**
     * 根据水务感知码获取道路积水点数据
     *
     * @param stWaterGaugeEsuParam
     * @return
     */
    @Override
    public Object stWaterGaugeNowByCondition(StWaterGaugeEsuParam stWaterGaugeEsuParam) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("water_code", stWaterGaugeEsuParam.getWaterCode());
        wrapper.ge("time", stWaterGaugeEsuParam.getStartTime());
        wrapper.le("time", stWaterGaugeEsuParam.getEndTime());
        wrapper.orderByAsc("time");
        List<SyncWaterRulerDto> resList = syncWaterRulerDao.selectList(wrapper);
        return resList;
    }

}
