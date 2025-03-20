package com.essence.service.utils;


import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;

public class RainFallLevelHd {

    public static final BigDecimal LEVEL_0 = new BigDecimal(0);
    public static final BigDecimal LEVEL_1 = new BigDecimal(2.5);
    public static final BigDecimal LEVEL_2 = new BigDecimal(8);
    public static final BigDecimal LEVEL_3 = new BigDecimal(16);
    public static final BigDecimal LEVEL_4 = new BigDecimal(20);



    /**
     * 降雨等级  0 无雨， 1 小雨  2 中雨  3 大雨  4 暴雨  5 大暴雨  6 特大暴雨
     */
    public static Integer getRainFallLevelNew(BigDecimal multiply) {

        if (multiply.compareTo(BigDecimal.ZERO) == 0 ) {
            return 0;
        }
        if (multiply.compareTo(LEVEL_0) >= 0 && multiply.compareTo(LEVEL_1) <= 0) {
            return 1;
        }
        if (multiply.compareTo(LEVEL_1) >= 0 && multiply.compareTo(LEVEL_2) <= 0) {
            return 2;
        }
        if (multiply.compareTo(LEVEL_2) >= 0 && multiply.compareTo(LEVEL_3) <= 0) {
            return 3;
        }
        if (multiply.compareTo(LEVEL_3) >= 0 && multiply.compareTo(LEVEL_4) <= 0) {
            return 4;
        }else {
            return 5;
        }

    }


}
