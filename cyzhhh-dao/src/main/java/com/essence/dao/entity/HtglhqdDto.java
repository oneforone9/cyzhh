package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 合同管理 - 会签单 （本质就是合同的草稿）
 *
 * @author majunjie
 * @since 2024-09-09 17:45:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htglhqd")
public class HtglhqdDto extends Model<HtglhqdDto> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同名称
     */
    @TableField("htmc")
    private String htmc;

    /**
     * '合同申请日期'
     */
    @TableField("htsqrq")
    private String htsqrq;


    /**
     * 合同内容
     */
    @TableField("htnr")
    private String htnr;

    /**
     * 合同甲方
     */
    @TableField("htjf")
    private String htjf;

    /**
     * 合同乙方
     */
    @TableField("htyf")
    private String htyf;

    /**
     * 其他方
     */
    @TableField("qtf")
    private String qtf;

    /**
     * 合同签订日期
     */
    @TableField("htqdrq")
    private String htqdrq;

    /**
     * 合同金额（万元）
     */
    @TableField("htje")
    private String htje;

    /**
     * 合同份数
     */
    @TableField("htfs")
    private Integer htfs;

    /**
     * 律师审查编号
     */
    @TableField("lsscbh")
    private String lsscbh;

    /**
     * 上会内容
     */
    @TableField("shnr")
    private String shnr;

    /**
     * 预审情况
     */
    @TableField("ysqk")
    private String ysqk;


    /**
     * 重大事项集体讨论情况
     */
    @TableField("zdsxjttlqk")
    private String zdsxjttlqk;


    /**
     * 起始执行日期
     */
    @TableField("qszxrq")
    private String qszxrq;

    /**
     * 终止执行日期
     */
    @TableField("zzzxrq")
    private String zzzxrq;


    /**
     * 签订科室
     */
    @TableField("qdks")
    private String qdks;

    /**
     * 经办人
     */
    @TableField("jbr")
    private String jbr;

    /**
     * 经办人名称
     */
    @TableField("jbrmc")
    private String jbrmc;

    /**
     * 经办人部门
     */
    @TableField("jbrbm")
    private String jbrbm;


    /**
     * 录入时间
     */
    @TableField("lrsj")
    private Date lrsj;

}
