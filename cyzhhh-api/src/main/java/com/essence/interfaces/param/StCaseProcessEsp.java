package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 方案执行过程表-存放入参等信息参数实体
 *
 * @author zhy
 * @since 2023-04-17 16:30:06
 */

@Data
public class StCaseProcessEsp extends Esp {

    private static final long serialVersionUID = 492859006622159157L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 方案id
     */            @ColumnMean("case_id")
    private String caseId;
        /**
     * 入参站点类型
     */            @ColumnMean("type")
    private String type;
        /**
     * 入参站点id
     */            @ColumnMean("stcd")
    private String stcd;
        /**
     * 入参站点名称
     */            @ColumnMean("st_name")
    private String stName;
        /**
     * 入参数的时间点位数据 
     */            @ColumnMean("input_data")
    private String inputData;
        /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("create_time")
    private Date createTime;


}
