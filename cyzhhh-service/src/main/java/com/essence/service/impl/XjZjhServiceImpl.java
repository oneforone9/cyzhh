package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.XjZjhDao;
import com.essence.dao.entity.XjZjhDto;
import com.essence.interfaces.api.XjZjhService;

import com.essence.interfaces.model.WeekRange;
import com.essence.interfaces.model.XjZjhData;
import com.essence.interfaces.model.XjZjhEsr;
import com.essence.interfaces.model.XjZjhEsu;
import com.essence.interfaces.param.XjZjhEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterXjZjhEtoT;
import com.essence.service.converter.ConverterXjZjhTtoR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备周计划(XjZjh)业务层
 *
 * @author majunjie
 * @since 2025-01-08 15:52:39
 */
@Service
@Slf4j
public class XjZjhServiceImpl extends BaseApiImpl<XjZjhEsu, XjZjhEsp, XjZjhEsr, XjZjhDto> implements XjZjhService {

    @Autowired
    private XjZjhDao xjZjhDao;
    @Autowired
    private ConverterXjZjhEtoT converterXjZjhEtoT;
    @Autowired
    private ConverterXjZjhTtoR converterXjZjhTtoR;

    public XjZjhServiceImpl(XjZjhDao xjZjhDao, ConverterXjZjhEtoT converterXjZjhEtoT, ConverterXjZjhTtoR converterXjZjhTtoR) {
        super(xjZjhDao, converterXjZjhEtoT, converterXjZjhTtoR);
    }

    @Override
    public String getWeekData(XjZjhData xjZjhData) {
        String type = "";
        try {
            List<XjZjhDto> xjZjhDtos = saveWeekData(xjZjhData.getStartTime(),xjZjhData.getEndTime(),xjZjhData.getYear());
            if (!CollectionUtils.isEmpty(xjZjhDtos)) {
                xjZjhDao.delete(new QueryWrapper<XjZjhDto>().lambda().eq(XjZjhDto::getYear,xjZjhData.getYear()));
                xjZjhDao.saveData(xjZjhDtos);
            }
        } catch (Exception e) {
            log.error("巡检计划周数据保存异常" + e);
            type = "异常";
        }
        return type;
    }

    public List<XjZjhDto> saveWeekData(String startTime,String endTime,Integer year) {
        List<XjZjhDto> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LocalDate startDate=LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate=LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<WeekRange> weeksBetween = getWeeksBetween(startDate, endDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i=0;i< weeksBetween.size();i++) {
                WeekRange weekRange=  weeksBetween.get(i);
                XjZjhDto xjZjhDto=new XjZjhDto();
                xjZjhDto.setId(UUID.randomUUID().toString().replace("-", ""));
                xjZjhDto.setYear(year);
                xjZjhDto.setStartTime(sdf.parse(weekRange.getStart().format(formatter)+" 00:00:00"));
                xjZjhDto.setEndTime(sdf.parse(weekRange.getEnd().format(formatter)+" 23:59:59"));
                xjZjhDto.setMs("第"+(i+1)+"周("+weekRange.getStart().format(formatter).substring(5,10)+"至"+weekRange.getEnd().format(formatter).substring(5,10)+")");
                xjZjhDto.setTime(weekRange.getStart().format(formatter));
                list.add(xjZjhDto);
            }
        } catch (Exception e) {
            log.error("计划时间生成异常" + e);
        }
        return list;
    }

    public static List<WeekRange> getWeeksBetween(LocalDate startDate, LocalDate endDate) {
        List<WeekRange> weekRanges = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            LocalDate weekStart = currentDate;
            while (weekStart.getDayOfWeek()!= DayOfWeek.MONDAY) {
                weekStart = weekStart.minusDays(1);
            }
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(endDate)) {
                weekEnd = endDate;
            }
            weekRanges.add(new WeekRange(weekStart, weekEnd));
            currentDate = weekEnd.plusDays(1);
        }
        return weekRanges;
    }

    @Override
    public Map<String, List<XjZjhEsr>> selectData(List<Integer> yearList) {
        Map<String, List<XjZjhEsr>>map = new HashMap<>();
        List<XjZjhDto> xjZjhDtos = xjZjhDao.selectList(new QueryWrapper<XjZjhDto>().lambda().in(XjZjhDto::getYear, yearList));
        if (!CollectionUtils.isEmpty(xjZjhDtos)) {
            List<XjZjhEsr> list = converterXjZjhTtoR.toList(xjZjhDtos);
            map = list.stream().collect(Collectors.groupingBy(x->String.valueOf(x.getYear())));
        }
        return map;
    }
}
