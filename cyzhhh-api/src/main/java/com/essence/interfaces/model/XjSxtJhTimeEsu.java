package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划日期更新实体
 *
 * @author majunjie
 * @since 2025-01-08 17:51:57
 */

@Data
public class XjSxtJhTimeEsu extends Esu {

    private static final long serialVersionUID = 751301809062230571L;

    /**
     * 主键
     */
    private String id;

    /**
     * 周计划时间id
     */
    private String zjhId;
    /**
     * 计划id
     */
    private String jhId;

}
