package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-02-20 14:33:11
 */

@Data
public class PumpRepairEsp extends Esp {


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    @ColumnMean("id")
    private String id;
    /**
     * 作业对象
     */
    @ColumnMean("project_target")
    private String projectTarget;
    /**
     * 闸坝名称
     */
    @ColumnMean("pump_name")
    private String pumpName;
    /**
     * 问题类型
     */
    @ColumnMean("question_type")
    private String questionType;
    /**
     * 解决方式
     */
    @ColumnMean("deal_method")
    private String dealMethod;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("update_time")
    private Date updateTime;


}
