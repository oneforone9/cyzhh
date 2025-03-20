package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 补水口基础表返回实体
 *
 * @author zhy
 * @since 2023-02-22 17:14:51
 */

@Data
public class StWaterPortEsr extends Esr {

    private static final long serialVersionUID = 838901673358972088L;

    private Integer id;
    /**
     * 是否上图（1-是， 0-否）
     */
    private Integer ismap;

    /**
     * 河道名称
     */
    private String riverName;


    /**
     * 补水口名称
     */
    private String waterPortName;


    /**
     * 补水口位置
     */
    private String waterAddress;


    /**
     * 供水能力（万m3/日）
     */
    private Double supply;


    /**
     * 水源
     */
    private String source;


    /**
     * 现况供水量
     */
    private Double supplyCurrent;


    /**
     * 放水口管径
     */
    private String fskgj;


    /**
     * 放水口管径
     */
    private String fskgjC;


    /**
     * 管底高程
     */
    private Double socleAltitude;


    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;


    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;


    /**
     * 创建者
     */
    private String creator;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
