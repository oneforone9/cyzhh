package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 闸坝维护计划基础表更新实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:38
 */

@Data
public class StPlanServiceEsu extends Esu {

    private static final long serialVersionUID = -51578931841182889L;

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
