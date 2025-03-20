package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 河道风险点
 */
@Data
public class RiverRiskPoint {

    /**
     * 站点名称
     */
    private String stnm;


    /**
     * 瞬时河道水深
     */
    private String momentRiverPosition;

    /**
     * 距警戒（m）
     */
    private String fromWaring;


    /**
     * 距漫堤（m）
     */
    private String fromMandi;


    /**
     * 预报；类型
     */
    private String type;

    /**
     *  预报；时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 左岸高程
     */

    private transient BigDecimal altitudeLeft;

    /**
     * 右岸高程
     */

    private transient BigDecimal altitudeRight;

    /**
     * 河底高程
     */

    private  transient BigDecimal altitudeBottom;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;
}
