package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 闸坝维护计划基础表返回实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:43
 */

@Data
public class StPlanServiceEsr extends Esr {

    private static final long serialVersionUID = -13352275807532847L;

    private Integer id;


    /**
     * 设备设施名称
     */
    private String equipmentName;


    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    private String sttp;


    /**
     * 父id
     */
    private Integer pid;


    /**
     * 日常维护内容
     */
    private String serviceContent;


    /**
     * 排序
     */
    private Integer sort;


}
