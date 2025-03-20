package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理历程实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htgllc")
public class HtgllcDto extends Model<HtgllcDto> {

    private static final long serialVersionUID = 522133383272358902L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同主键
     */
    @TableField("htid")
    private String htid;

    /**
     * 描述
     */
    @TableField("ms")
    private String ms;

    /**
     * 经办人名称
     */
    @TableField("jbrmc")
    private String jbrmc;

    /**
     * 经办人科室
     */
    @TableField("jbrks")
    private String jbrks;
    /**
     * 经办人
     */
    @TableField("jbr")
    private String jbr;

    /**
     * 待办理日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("dblrq")
    private Date dblrq;

    /**
     * 办理完成日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("blwcrq")
    private Date blwcrq;

    /**
     * 办理状态0未办1已办
     */
    @TableField("blzt")
    private Integer blzt;

    /**
     *  第一个版本
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成,
     */

    /** 第二个版本
     * 新合同状态，0:录入人新增合同； 1:合同人，录入合同编码之后； 2:录入人，上传合同附件之后；  3: 合同人勾选已完成，合同结束
     */

    /**
     * 第三个版本
     * 新合同状态，-1：新增会签单； 0:提交会签单之后，录入人签登记表； 1:合同人，录入合同编码之后； 2:录入人，上传合同附件之后；  3: 合同人勾选已完成，合同结束
     */
    @TableField("zt")
    private Integer zt;
}
