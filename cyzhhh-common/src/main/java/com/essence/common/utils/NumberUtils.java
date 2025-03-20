package com.essence.common.utils;

import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * @author zhy
 * @since 2023/3/23 13:44
 */
public class NumberUtils {

    /**
     * 保留2位小数 不补位
     *
     * @param value 值
     * @return
     */

    public static String reserved2Whole(double value) {
        return reserved(value, 2, false);
    }

    /**
     * 保留2位小数 小数位不够补0
     *
     * @param value 值
     * @return
     */
    public static String reserved2(double value) {
        return reserved(value, 2, true);
    }

    /**
     * 保留小数位 不补位
     *
     * @param value  值
     * @param digits 保留位数
     * @return
     */

    public static String reservedWhole(double value, int digits) {
        return reserved(value, digits, false);
    }

    /**
     * 保留小数位 小数位不够补0
     *
     * @param value  值
     * @param digits 保留位数
     * @return
     */
    public static String reserved(double value, int digits) {
        return reserved(value, digits, true);
    }

    /**
     * 保留小数位
     *
     * @param value  值
     * @param digits 保留位数
     * @param whole  整数是否保留
     * @return
     */
    public static String reserved(double value, int digits, boolean whole) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(digits);
        /*
         * setMinimumFractionDigits设置成2
         * 如果不这么做，那么当value的值是100.00的时候返回100
         * 而不是100.00
         */
        if (whole) {
            nf.setMinimumFractionDigits(3);

        }
        /*
         * 如果想输出的格式用逗号隔开，可以设置成true
         */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    /**
     * 是否数字（小数和整数）
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (null == str) {
            return false;
        }
        String pattern = "^[-]?[0-9]+(\\.[0-9]+)?$";

        return Pattern.matches(pattern, str);
    }

    /**
     * 是否整数
     *
     * @param str
     * @return
     */
    public static boolean isWhole(String str) {
        if (null == str) {
            return false;
        }
        String pattern = "^[0-9]+$";

        return Pattern.matches(pattern, str);
    }
}
