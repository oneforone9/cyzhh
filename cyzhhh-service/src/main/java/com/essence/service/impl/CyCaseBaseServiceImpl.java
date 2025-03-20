package com.essence.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StatisticsDto;
import com.essence.dao.CyCaseBaseDao;
import com.essence.dao.entity.CyCaseBase;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.CyCaseBaseService;
import com.essence.interfaces.model.CyCaseBaseEsr;
import com.essence.interfaces.model.CyCaseBaseEsu;
import com.essence.interfaces.param.CyCaseBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterCyCaseBaseEtoT;
import com.essence.service.converter.ConverterCyCaseBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 案件管理
 * @author zhy
 * @since 2023-01-04 18:13:40
 */
@Service
public class CyCaseBaseServiceImpl extends BaseApiImpl<CyCaseBaseEsu, CyCaseBaseEsp, CyCaseBaseEsr, CyCaseBase> implements CyCaseBaseService {

    @Autowired
    private CyCaseBaseDao cyCaseBaseDao;
    @Autowired
    private ConverterCyCaseBaseEtoT converterCyCaseBaseEtoT;
    @Autowired
    private ConverterCyCaseBaseTtoR converterCyCaseBaseTtoR;

    public CyCaseBaseServiceImpl(CyCaseBaseDao cyCaseBaseDao, ConverterCyCaseBaseEtoT converterCyCaseBaseEtoT, ConverterCyCaseBaseTtoR converterCyCaseBaseTtoR) {
        super(cyCaseBaseDao, converterCyCaseBaseEtoT, converterCyCaseBaseTtoR);
    }


    @Override
    public int insert(CyCaseBaseEsu cyCaseBaseEsu) {
        cyCaseBaseEsu.setId(UuidUtil.get32UUIDStr());
        return super.insert(cyCaseBaseEsu);
    }

    @Override
    public List<StatisticsDto> statisticsByCasetype() {
        DateTime start = DateUtil.beginOfYear(new Date());
        DateTime end = DateUtil.endOfYear(new Date());
        String startStr = DateUtil.format(start, "yyyy-MM-dd");
        String endStr = DateUtil.format(end, "yyyy-MM-dd");
        List<StatisticsDto> statisticsDtos = cyCaseBaseDao.mstatisticsByCasetype(startStr, endStr);
        if (CollUtil.isEmpty(statisticsDtos)){
            start = DateUtil.beginOfYear(DateUtil.offset(new Date(),DateField.YEAR,-1 ));
            end = DateUtil.endOfYear(DateUtil.offset(new Date(),DateField.YEAR,-1 ));
            startStr = DateUtil.format(start, "yyyy-MM-dd");
            endStr = DateUtil.format(end, "yyyy-MM-dd");
            statisticsDtos = cyCaseBaseDao.mstatisticsByCasetype(startStr, endStr);
        }
        return statisticsDtos;
    }

    @Override
    public List<StatisticsDto> statisticsByClosingstatus() {
        DateTime start = DateUtil.beginOfYear(new Date());
        DateTime end = DateUtil.endOfYear(new Date());
        String startStr = DateUtil.format(start, "yyyy-MM-dd");
        String endStr = DateUtil.format(end, "yyyy-MM-dd");
//        List<StatisticsDto> statisticsDto = cyCaseBaseDao.statisticsByClosingstatus(startStr,endStr);
        List<StatisticsDto> statisticsDto = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("case_type",2);
        wrapper.le("filing_date",endStr);
        wrapper.ge("filing_date",startStr);
        List<CyCaseBase> caseBases = cyCaseBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(caseBases)){
            Map<String, List<CyCaseBase>> map = caseBases.parallelStream().collect(Collectors.groupingBy(CyCaseBase::getClosingStatus));
            for (String s : map.keySet()) {
                List<CyCaseBase> caseBases1 = map.get(s);
                int size = caseBases1.size();
                StatisticsDto statisticsDto1 = new StatisticsDto();
                for (CyCaseBase cyCaseBase : caseBases1) {
                    Date warehousingDate = cyCaseBase.getWarehousingDate();
                    if (warehousingDate == null){
                        warehousingDate = new Date();
                    }
                    String substring = DateUtil.format(warehousingDate, "yyyy-MM-dd").substring(0, 4);
                    statisticsDto1.setUpdateDate(substring);
                }
                statisticsDto1.setType(s);
                statisticsDto1.setValue(size);
                statisticsDto.add(statisticsDto1);
            }
        }
        if (CollUtil.isEmpty(statisticsDto)){
            start = DateUtil.beginOfYear(DateUtil.offset(new Date(), DateField.YEAR,-1));
            end = DateUtil.endOfYear(DateUtil.offset(new Date(), DateField.YEAR,-1));;
            startStr = DateUtil.format(start, "yyyy-MM-dd");
            endStr = DateUtil.format(end, "yyyy-MM-dd");
            QueryWrapper wrapperPre = new QueryWrapper();
            wrapperPre.eq("case_type",2);
            wrapperPre.le("filing_date",endStr);
            wrapperPre.ge("filing_date",startStr);
            List<CyCaseBase> caseBasesPre = cyCaseBaseDao.selectList(wrapperPre);
            if (CollUtil.isNotEmpty(caseBasesPre)){
                Map<String, List<CyCaseBase>> map = caseBasesPre.parallelStream().collect(Collectors.groupingBy(CyCaseBase::getClosingStatus));
                for (String s : map.keySet()) {
                    List<CyCaseBase> caseBases1 = map.get(s);
                    int size = caseBases1.size();
                    StatisticsDto statisticsDto1 = new StatisticsDto();
                    for (CyCaseBase cyCaseBase : caseBases1) {
                        Date warehousingDate = cyCaseBase.getWarehousingDate();
                        if (warehousingDate == null){
                            warehousingDate = new Date();
                        }
                        String substring = DateUtil.format(warehousingDate, "yyyy-MM-dd").substring(0, 4);
                        statisticsDto1.setUpdateDate(substring);
                    }
                    statisticsDto1.setType(s);
                    statisticsDto1.setValue(size);
                    statisticsDto.add(statisticsDto1);
                }
            }
        }
        int sum = statisticsDto.stream().mapToInt(StatisticsDto::getValue).sum();
        statisticsDto.forEach(
                p->p.setRate(p.getValue() *100.0/ sum)
        );
        return statisticsDto;
    }
}
