package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 清水的河 - 实时游客表返回实体
 *
 * @author zhy
 * @since 2023-02-28 11:45:01
 */

@Data
public class StCrowdRealEsr extends Esr {

    private static final long serialVersionUID = -28262322066845140L;


    /**
     * 主键
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
