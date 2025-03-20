package com.essence.interfaces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author majunjie
 * @description public
 * @date 2022-12-26
 */
@Data
public class FloodDesigRainPatternEsrVo {

    private static final long serialVersionUID = 911223328227584957L;
    /**
     * 雨型id
     */
    private String designRainPatternId;

    /**
     * 雨型名称
     */
    private String designRainPatternName;

    /**
     * 小时数
     */
    private Integer hourCount;

    /**
     * 时间间隔（分钟）
     */
    private Integer timeInterval;

    /**
     * 雨型参数（参数之间用英文逗号隔开）
     */
    private String param;
    /**
     * 雨型时间间隔
     */
    private String interval;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 备注
     */
    private String remark;
}