package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容记录表参数实体
 *
 * @author liwy
 * @since 2023-07-24 14:18:22
 */

@Data
public class StPlanOperateEsp extends Esp {

    private static final long serialVersionUID = 941620031224411586L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 工单主键(生成事件的工单主键)
     */
    @ColumnMean("order_id")
    private String orderId;
    /**
     * 设备设施名称
     */
    @ColumnMean("equipment_name")
    private String equipmentName;
    /**
     * 日常维护内容
     */
    @ColumnMean("service_content")
    private String serviceContent;
    /**
     * 维护前图片
     */
    @ColumnMean("yh_before")
    private String yhBefore;
    /**
     * 维护后图片
     */
    @ColumnMean("yh_after")
    private String yhAfter;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
