package com.essence.common.utils;

import java.util.regex.Pattern;

/**
 * @author zhy
 * @since 2023/3/30 11:29
 */
public class SDateUtils {
    /**
     * 是否是日期
     *
     * @param str
     * @return
     */
    public static boolean isYM(String str) {
        if (null == str) {
            return false;
        }

        String pattern = "^\\d{4}-(0[1-9]|1[0-2])$";

        return Pattern.matches(pattern, str);
    }
    public static boolean isYMD(String str) {
        if (null == str) {
            return false;
        }
        String pattern = "^\\d{4}-(0[1-9]|1[0-2])-(0?[1-9]|[1-2]\\d|3[0-1])$";
        return Pattern.matches(pattern, str);
    }
    public static boolean isY(String str) {
        if (null == str) {
            return false;
        }
        String pattern = "^\\d{4}$";
        return Pattern.matches(pattern, str);
    }
    public static boolean isYMDSFM(String str) {
        if (null == str) {
            return false;
        }
        String pattern = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        return Pattern.matches(pattern, str);
    }
}
