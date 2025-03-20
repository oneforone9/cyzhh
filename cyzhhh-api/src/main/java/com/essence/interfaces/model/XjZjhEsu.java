package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备周计划更新实体
 *
 * @author majunjie
 * @since 2025-01-08 15:52:41
 */

@Data
public class XjZjhEsu extends Esu {

    private static final long serialVersionUID = -34349802973844243L;

    /**
     * 主键
     */
    private String id;

    /**
     * 描述(第x周(mm-dd至mm-dd))
     */
    private String ms;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 年份
     */
    private Integer year;
    /**
     * 日期yyyy-mm-dd
     */
    private String time;

}
