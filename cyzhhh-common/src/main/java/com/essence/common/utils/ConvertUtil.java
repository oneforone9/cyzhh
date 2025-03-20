package com.essence.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.List;

public class ConvertUtil {
    /**
     * 将老数组中的位置 数据 替换到新数组中的位置 数据
     */
    public static List<Integer> convertIntegerListValue(String oldListStr, String newListStr,Integer value){
        List<Integer> newList = JSONObject.parseArray(newListStr,Integer.class);
        return newList;
    }

    public static List<BigDecimal> convertBigDecimalListValue(String oldListStr, String newListStr, BigDecimal value){
        List<BigDecimal> newList = JSONObject.parseArray(newListStr,BigDecimal.class);

        return newList;
    }
}
