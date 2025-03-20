package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 方案执行进度表参数实体
 *
 * @author zhy
 * @since 2023-04-18 17:03:06
 */

@Data
public class StCaseProgressEsp extends Esp {

    private static final long serialVersionUID = 983526869845081139L;

                    @ColumnMean("id")
    private String id;
        /**
     * 方案id
     */            @ColumnMean("case_id")
    private String caseId;
        /**
     * 进度百分比
     */            @ColumnMean("progress")
    private BigDecimal progress;
        /**
     * 备注
     */            @ColumnMean("message")
    private String message;
        /**
     * 新增或者更新
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("update_time")
    private Date updateTime;

    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */
    @ColumnMean("status")
    private Integer status;


    /**
     * 方案名称
     */
    @ColumnMean("caseName")
    private String caseName;
    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    @ColumnMean("model_type")
    private Integer modelType;
}
