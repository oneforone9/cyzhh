package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 养护内容记录表返回实体
 *
 * @author liwy
 * @since 2023-07-24 14:18:24
 */

@Data
public class StPlanOperateEsr extends Esr {

    private static final long serialVersionUID = 661407229767877004L;

    private Integer id;


    /**
     * 工单主键(生成事件的工单主键)
     */
    private String orderId;


    /**
     * 设备设施名称
     */
    private String equipmentName;


    /**
     * 日常维护内容
     */
    private String serviceContent;


    /**
     * 维护前图片
     */
    private String yhBefore;


    /**
     * 维护后图片
     */
    private String yhAfter;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
