package com.essence.service.utils;

import com.essence.common.constant.ItemConstant;

/**
 * @author zhy
 * @since 2022/10/27 17:54
 */
public class OrderCodeGenerateUtil {
    // 根据工单类型拼音缩写
    public static String renPing(String type) {
        switch (type) {
            case ItemConstant.ORDER_TYPE_XC:
                return "XC";
            case ItemConstant.ORDER_TYPE_BJ:
                return "BJ";

            case ItemConstant.ORDER_TYPE_LH:
                return "LH";

            case ItemConstant.ORDER_TYPE_WB:
                return "WB";

            case ItemConstant.ORDER_TYPE_YX:
                return "YX";
            case ItemConstant.ORDER_TYPE_YH:
                return "YH";
            default:
                return null;
        }

    }

    /**
     * 数字转三位顺序码
     *
     * @return
     */
    public static String toSequenceCode(int num) {
        if (num > 99) {
            return num + "";
        }
        String str = "000" + num;
        return str.substring(str.length() -3, str.length());
    }

}
