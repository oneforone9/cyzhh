package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝计划生成记录表更新实体
 *
 * @author liwy
 * @since 2023-07-18 11:16:30
 */

@Data
public class StPlanRecordEsu extends Esu {

    private static final long serialVersionUID = 315409185955346743L;

    private Integer id;

    /**
     * 闸坝计划排班信息表id
     */
    private Integer planId;

    /**
     * 维护日期排班表已按计划生成的标记
     */
    private String whTime;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
