package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 补水口基础表参数实体
 *
 * @author zhy
 * @since 2023-02-22 17:14:48
 */

@Data
public class StWaterPortEsp extends Esp {

    private static final long serialVersionUID = 751165402145963160L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 是否上图（1-是， 0-否）
     */
    @ColumnMean("ismap")
    private Integer ismap;
    /**
     * 河道名称
     */
    @ColumnMean("river_name")
    private String riverName;
    /**
     * 补水口名称
     */
    @ColumnMean("water_port_name")
    private String waterPortName;
    /**
     * 补水口位置
     */
    @ColumnMean("water_address")
    private String waterAddress;
    /**
     * 供水能力（万m3/日）
     */
    @ColumnMean("supply")
    private Double supply;
    /**
     * 水源
     */
    @ColumnMean("source")
    private String source;
    /**
     * 现况供水量
     */
    @ColumnMean("supply_current")
    private Double supplyCurrent;
    /**
     * 放水口管径
     */
    @ColumnMean("fskgj")
    private String fskgj;
    /**
     * 放水口管径
     */
    @ColumnMean("fskgj_c")
    private String fskgjC;
    /**
     * 管底高程
     */
    @ColumnMean("socle_altitude")
    private Double socleAltitude;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @ColumnMean("lgtd")
    private Double lgtd;
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ColumnMean("lttd")
    private Double lttd;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
