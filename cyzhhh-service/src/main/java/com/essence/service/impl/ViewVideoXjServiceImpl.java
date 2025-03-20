package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.ViewVideoXjDao;
import com.essence.dao.entity.ViewVideoXjDto;
import com.essence.interfaces.api.ViewVideoXjService;
import com.essence.interfaces.api.XjSxtJhService;
import com.essence.interfaces.api.XjSxtJhTimeService;
import com.essence.interfaces.api.XjZjhService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.ViewVideoXjEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterViewVideoXjEtoT;
import com.essence.service.converter.ConverterViewVideoXjTtoR;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * (ViewVideoXj)业务层
 *
 * @author majunjie
 * @since 2025-01-08 14:13:45
 */
@Service
public class ViewVideoXjServiceImpl extends BaseApiImpl<ViewVideoXjEsu, ViewVideoXjEsp, ViewVideoXjEsr, ViewVideoXjDto> implements ViewVideoXjService {

    @Autowired
    private ViewVideoXjDao viewVideoXjDao;
    @Autowired
    private ConverterViewVideoXjEtoT converterViewVideoXjEtoT;
    @Autowired
    private ConverterViewVideoXjTtoR converterViewVideoXjTtoR;
    @Autowired
    private XjSxtJhTimeService xjSxtJhTimeService;
    @Autowired
    private XjZjhService xjZjhService;
    @Autowired
    private XjSxtJhService xjSxtJhService;

    public ViewVideoXjServiceImpl(ViewVideoXjDao viewVideoXjDao, ConverterViewVideoXjEtoT converterViewVideoXjEtoT, ConverterViewVideoXjTtoR converterViewVideoXjTtoR) {
        super(viewVideoXjDao, converterViewVideoXjEtoT, converterViewVideoXjTtoR);
    }

    @Override
    public ViewVideoXjEsr selectVideo(ViewVideoQuery viewVideoQuery) {
        List<ViewVideoXjDto> viewVideoXjDtos = viewVideoXjDao.selectList(new QueryWrapper<ViewVideoXjDto>().lambda().eq(ViewVideoXjDto::getId, viewVideoQuery.getId()));
        if (!CollectionUtils.isEmpty(viewVideoXjDtos)) {
            ViewVideoXjDto viewVideoXjDto = viewVideoXjDtos.get(0);
            //获取巡检日期
            ViewVideoXjEsr v = converterViewVideoXjTtoR.toBean(viewVideoXjDto);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String year = sdf.format(new Date()).substring(0, 4);
            List<Integer> yearList = new ArrayList<>();
            yearList.add(Integer.valueOf(year));
            yearList.add(Integer.valueOf(year) + 1);

            //查询已指定计划日期
            List<XjSxtJhTimeEsr> xjSxtJhTimeEsrs = xjSxtJhTimeService.selectData(viewVideoQuery.getId());
            Map<String, String> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(xjSxtJhTimeEsrs)) {
                map = xjSxtJhTimeEsrs.stream().collect(Collectors.toMap(XjSxtJhTimeEsr::getZjhId, XjSxtJhTimeEsr::getZjhId, (o1, o2) -> o2));
            }
            //查询可制定计划日期
            Map<String, List<XjZjhEsr>> xjZjhEsrMap = Optional.ofNullable(xjZjhService.selectData(yearList)).orElse(Maps.newHashMap());
            Map<String, List<XjZjhEsr>> xjZjhEsrMaps = new HashMap<>();
            for (Integer yearNumber : yearList) {
                List<XjZjhEsr> xjZjhEsrs = Optional.ofNullable(xjZjhEsrMap.get(String.valueOf(yearNumber))).orElse(Lists.newArrayList());
                if (!CollectionUtils.isEmpty(xjZjhEsrs)) {
                    xjZjhEsrs = xjZjhEsrs.stream().sorted(Comparator.comparing(XjZjhEsr::getStartTime)).collect(Collectors.toList());
                    for (XjZjhEsr xjZjhEsr : xjZjhEsrs) {
                        xjZjhEsr.setType(StringUtils.isNotBlank(map.get(xjZjhEsr.getId())) ? "1" : "0");
                    }
                }
                xjZjhEsrMaps.put(String.valueOf(yearNumber), xjZjhEsrs);
            }
            v.setXjZjhMap(xjZjhEsrMaps);
            return v;
        }
        return null;
    }

    @Override
    public ViewVideoXjEsr updateVideoJh(ViewVideoXjEsr viewVideoXjEsr) {

        //1.处理巡检日期
        Map<String, List<XjZjhEsr>> xjZjhMap = Optional.ofNullable(viewVideoXjEsr.getXjZjhMap()).orElse(Maps.newHashMap());
        List<XjSxtJhTimeEsu> list = new ArrayList<>();
        String xjrq = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String month = sdf.format(new Date()).substring(0, 7);
        List<XjZjhEsr> xjrqList = new ArrayList<>();
        for (Map.Entry<String, List<XjZjhEsr>> entry : xjZjhMap.entrySet()) {
            List<XjZjhEsr> value = entry.getValue();
            if (!CollectionUtils.isEmpty(value)) {
                for (XjZjhEsr xjZjhEsr : value) {
                    if (xjZjhEsr.getType().equals("1")) {
                        XjSxtJhTimeEsu xjSxtJhTimeEsu = new XjSxtJhTimeEsu();
                        xjSxtJhTimeEsu.setId(UUID.randomUUID().toString().replace("-", ""));
                        xjSxtJhTimeEsu.setZjhId(xjZjhEsr.getId());
                        xjSxtJhTimeEsu.setJhId(String.valueOf(viewVideoXjEsr.getId()));
                        list.add(xjSxtJhTimeEsu);
                        if (xjZjhEsr.getTime().contains(month)) {
                            xjrqList.add(xjZjhEsr);
                        }
                    }
                }
            }
        }
        //填充巡检日期
        if (!CollectionUtils.isEmpty(xjrqList)) {
            xjrqList = xjrqList.stream().sorted(Comparator.comparing(XjZjhEsr::getStartTime)).collect(Collectors.toList());
            for (XjZjhEsr xjZjhEsr : xjrqList) {
                xjrq = xjrq + xjZjhEsr.getMs() + ",";
            }
        }
        viewVideoXjEsr.setXjRq(StringUtils.isNotBlank(xjrq) ? xjrq.substring(0, xjrq.length() - 1) : null);
        if (!CollectionUtils.isEmpty(list)) {
            xjSxtJhTimeService.saveData(list, String.valueOf(viewVideoXjEsr.getId()));
        }
        //2.填充计划数据
        xjSxtJhService.updateData(viewVideoXjEsr);
        return viewVideoXjEsr;
    }
}
