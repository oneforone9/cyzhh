package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 清水的河 - 实时游客表更新实体
 *
 * @author zhy
 * @since 2023-02-28 11:44:47
 */

@Data
public class StCrowdRealEsu extends Esu {

    private static final long serialVersionUID = -94721511457296803L;

    /**
     * 主见
     */
    private String id;

    /**
     * 热点区域
     */
    private String area;

    /**
     * 河流名称
     */
    private String rvnm;

    /**
     * 水管单位
     */
    private String waterUnit;

    /**
     * 人数
     */
    private Integer num;

    /**
     * 时间 :yyyy-MM-dd HH:mm
     */
    private String date;


}
