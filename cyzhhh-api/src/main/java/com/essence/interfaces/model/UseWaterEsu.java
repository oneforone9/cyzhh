package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 用水量更新实体
 *
 * @author zhy
 * @since 2023-01-04 17:18:04
 */

@Data
public class UseWaterEsu extends Esu {

    private static final long serialVersionUID = 734851332176305752L;

    private String id;

    /**
     * 日期
     */
    private String date;

    /**
     * 用水量
     */
    private String useInfo;
    /**
     * 更新时间
     */
    private String updateTime;


}
