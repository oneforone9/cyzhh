package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 合同管理导出实体
 *
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */

@Data
public class HtglExport {
    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    private String xh;

    /**
     * 合同编号
     */
    @ExcelProperty(value = "合同编号", index = 1)
    private String htbh;

    /**
     * 合同名称
     */
    @ExcelProperty(value = "合同名称", index = 2)
    private String htmc;


    /**
     * “三重一大”会议讨论情况
     */
    @ExcelProperty(value = "“三重一大”会议讨论情况", index = 3)
    private String hytlqk;


    /**
     * 上会日期
     */
    @ExcelProperty(value = "上会日期", index = 4)
    private String shrq;


    /**
     * 上会内容
     */
    @ExcelProperty(value = "上会内容", index = 5)
    private String shnr;


    /**
     * 合同内容
     */
    @ExcelProperty(value = "合同内容", index = 6)
    private String htnr;

    /**
     * 合同甲方
     */
    @ExcelProperty(value = "甲方", index = 7)
    private String htjf;

    /**
     * 合同乙方
     */
    @ExcelProperty(value = "乙方", index = 8)
    private String htyf;

    /**
     * 其他方
     */
    @ExcelProperty(value = "其他方", index = 9)
    private String qtf;

    /**
     * 合同签订日期
     */
    @ExcelProperty(value = "合同签订日期", index = 10)
    private String htqdrq;

    /**
     * 合同金额（万元）
     */
    @ExcelProperty(value = "合同金额（万元）", index = 11)
    private String htje;

    /**
     * 起始执行日期
     */
    @ExcelProperty(value = "起始执行日期", index = 12)
    private String qszxrq;


    /**
     * 终止执行日期
     */
    @ExcelProperty(value = "终止执行日期", index = 13)
    private String zzzxrq;


    /**
     * 签订科室
     */
    @ExcelProperty(value = "签订科室", index = 14)
    private String qdks;
    /**
     * 经办人名称
     */
    @ExcelProperty(value = "经办人", index = 15)
    private String jbrmc;

}
