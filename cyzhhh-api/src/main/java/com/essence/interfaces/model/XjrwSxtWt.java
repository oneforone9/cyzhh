package com.essence.interfaces.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检任务返回实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:07
 */

@Data
public class XjrwSxtWt {
    /**
     * 工单编号
     */
    @ExcelProperty(value = "工单编号", index = 0)
    private String bh;
    /**
     * 设备名称
     */
    @ExcelProperty(value = "摄像头名称", index = 1)
    private String mc;
    /**
     * 所属河道
     */
    @ExcelProperty(value = "所属河道", index = 2)
    private String riverName;
    /**
     * 问题描述
     */
    @ExcelProperty(value = "问题描述", index = 3)
    private String wtms;
    /**
     * 打卡地址
     */
    @ExcelProperty(value = "具体位置", index = 4)
    private String dkdz;
    /**
     * 计划巡检时间
     */
    @ExcelProperty(value = "发现时间", index = 5)
    private String fxsj;

    /**
     * 实际巡检时间
     */
    @ExcelProperty(value = "派发时间", index = 6)
    private String pfsj;
    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */
    @ExcelProperty(value = "解决时间", index = 7)
    private String jjsj;

    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */
    @ExcelProperty(value = "巡检完成情况", index = 8)
    private String wcqk;


}
