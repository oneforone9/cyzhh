package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 合同管理更新实体
 *
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */

@Data
public class HtglExcept {
    /**
     * 序号
     */
    @ExcelProperty(value = {"", "序号"})
    private String xh;

    /**
     * 合同编号
     */
    @ExcelProperty(value = {"", "合同编号"})
    private String htbh;

    /**
     * 合同名称
     */
    @ExcelProperty(value = "合同名称")
    private String htmc;


    /**
     * “三重一大”会议讨论情况
     */
    @ExcelProperty(value = "“三重一大”会议讨论情况")
    private String hytlqk;


    /**
     * 上会日期
     */
    @ExcelProperty(value = "上会日期（需党组会审议通过的以党组会日期为准）")
    private String shrq;


    /**
     * 上会内容
     */
    @ExcelProperty(value = "上会内容（可以用会议议题）")
    private String shnr;


    /**
     * 合同内容
     */
    @ExcelProperty(value = "合同内容")
    private String htnr;

    /**
     * 合同甲方
     */
    @ExcelProperty(value = {"合同签订主体", "甲方"})
    private String htjf;

    /**
     * 合同乙方
     */
    @ExcelProperty(value = {"", "乙方"})
    private String htyf;

    /**
     * 其他方
     */
    @ExcelProperty(value = {"合同签订主体", "其他方"})
    private String qtf;

    /**
     * 合同签订日期
     */
    @ExcelProperty(value = "合同签订日期")
    private String htqdrq;

    /**
     * 合同金额（万元）
     */
    @ExcelProperty(value = "合同金额（万元）")
    private String htje;

    /**
     * 起始执行日期
     */
    @ExcelProperty(value = {"执行日期（可以不是具体日期，可以是文字描述）", "起"})
    private String qszxrq;


    /**
     * 终止执行日期
     */
    @ExcelProperty(value = {"执行日期（可以不是具体日期，可以是文字描述）", "止"})
    private String zzzxrq;


    /**
     * 签订科室
     */
    @ExcelProperty(value = {"签订科室"})
    private String qdks;
    /**
     * 经办人名称
     */
    @ExcelProperty(value = { "经办人"})
    private String jbrmc;

}
