package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 闸坝维护计划基础表参数实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:42
 */

@Data
public class StPlanServiceEsp extends Esp {

    private static final long serialVersionUID = 530398098684255062L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 设备设施名称
     */
    @ColumnMean("equipment_name")
    private String equipmentName;
    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 父id
     */
    @ColumnMean("pid")
    private Integer pid;
    /**
     * 日常维护内容
     */
    @ColumnMean("service_content")
    private String serviceContent;
    /**
     * 排序
     */
    @ColumnMean("sort")
    private Integer sort;


}
