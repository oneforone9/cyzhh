package com.essence.service.impl.alg;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

/**
 * @author mjj
 * @description public
 * @date 2022-12-22
 */
@Data
public class FloodDate {
    /**
     * 年月日
     */

    private String dateTime;
    /**
     * 时分秒
     */

    private String time;
    /**
     * 降雨量
     */

    private Double rainData;

}