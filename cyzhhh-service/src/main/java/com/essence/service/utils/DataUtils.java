package com.essence.service.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.util.*;

public class DataUtils {

    public static List<Integer> getInitList(int num,Integer value){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(value);
        }
        return list;
    }


    public static List<BigDecimal> getInitBigdecimalList(int num, BigDecimal value){
        List<BigDecimal> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(value);
        }
        return list;
    }

    public static List<String> getInitStrList(int num,String value){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(value);
        }
        return list;
    }

    /**
     * 获取一个按照时间维度返回的时间列表
     * @param start 开始时间
     * @param end 结束时间
     * @param split 分割 建议是分钟
     */
    public static  List<Date> getTimeSplit(Date start, Date end, Integer split, DateField dateField){
        List<Date> splitTimeList = new ArrayList<>();
        while (start.compareTo(end)<=0){
            splitTimeList.add(start);
            start = DateUtil.offset(start, dateField, split);
        }
        return splitTimeList;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * @param data
     * @return
     */
    public static Map<String, String> getTimeFormat(String data) {
        DateTime parse = DateUtil.parse(data);
        String format = "";
        String cDate = "";
        if (data.contains("-")) {
            data=data.replace("-","/");
        }
        if (data.contains("/")) {
            format = DateUtil.format(parse, "yyyy/MM/dd HH:mm:ss");
            cDate = format.substring(5, 7) + "/" + format.substring(8, 10) + "/" + format.substring(0, 4);
        }


        String cTime = format.substring(11, 16);
        if (cTime.startsWith("0")) {
            cTime = cTime.substring(1, 5);
        }
        Map<String, String> map = new HashMap<>();
        map.put("cDate", cDate);
        map.put("cTime", cTime);
        return map;
    }


    /**
     * 水位站流量站 实时数据按照步长进行页面时间展示
     * 1.首先查询时间范围的的所有数据 0 10 20 30 40 50 60
     * 2.开始for 循环上述的时间列表.size-1 并且将水位站雨量站的列表进行 parallstream 进行filter 时间 <> 时间. +1 获取时间范围内的最后一个点位
     * 3.如此雨量站 水位站就此进行分割
     */
}
