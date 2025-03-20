package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝计划生成记录表参数实体
 *
 * @author liwy
 * @since 2023-07-18 11:16:36
 */

@Data
public class StPlanRecordEsp extends Esp {

    private static final long serialVersionUID = -54650243773171851L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 闸坝计划排班信息表id
     */
    @ColumnMean("plan_id")
    private Integer planId;
    /**
     * 维护日期排班表已按计划生成的标记
     */
    @ColumnMean("wh_time")
    private String whTime;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
