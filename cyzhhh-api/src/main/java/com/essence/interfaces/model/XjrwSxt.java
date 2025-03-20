package com.essence.interfaces.model;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 设备巡检任务返回实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:07
 */

@Data
public class XjrwSxt {
    /**
     * 工单编号
     */
    @ExcelProperty(value = "工单编号", index = 0)
    private String bh;
    /**
     * 工单来源0计划生成1临时生成2问题上报
     */
    @ExcelProperty(value = "工单来源", index = 1)
    private String ly;
    /**
     * 设备名称
     */
    @ExcelProperty(value = "摄像头名称", index = 2)
    private String mc;
    /**
     * 所属河道
     */
    @ExcelProperty(value = "所属河道", index = 3)
    private String riverName;
    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人", index = 4)
    private String fzr;
    /**
     * 计划巡检时间
     */
    @ExcelProperty(value = "计划巡检时间", index = 5)
    private String jhsj;


    /**
     * 实际巡检时间
     */
    @ExcelProperty(value = "实际巡检完成时间", index = 6)
    private String sjsj;

    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */
    @ExcelProperty(value = "巡检完成情况", index = 7)
    private String wcqk;
    /**
     * 发现问题0否1是
     */
    @ExcelProperty(value = "是否发现问题", index = 8)
    private String fxwt;
}
