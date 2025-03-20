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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 案件基础表(CyCaseBase)实体类
 *
 * @author zhy
 * @since 2023-01-04 18:13:45
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "cy_case_base")
public class CyCaseBase extends Model<CyCaseBase> {

    private static final long serialVersionUID = 315202540818081703L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 案件名称
     */
    @TableField("case_name")
    private String caseName;

    /**
     * 所属部门
     */
    @TableField("department")
    private String department;

    /**
     * 处罚决定书文号
     */
    @TableField("punish_doc_no")
    private String punishDocNo;

    /**
     * 处罚决定书文号日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("punish_doc_date")
    private Date punishDocDate;

    /**
     * 立案号
     */
    @TableField("filing_no")
    private String filingNo;

    /**
     * 立案日期
     */
    @TableField("filing_date")
    private String filingDate;

    /**
     * 结案状态(1 正常结案、2 撤销案件、3案件移送、4不予处罚、5 撤销原触发决定、6终结)
     */
    @TableField("closing_status")
    private String closingStatus;

    /**
     * 结案日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("closing_date")
    private Date closingDate;

    /**
     * 触发的职权编号
     */
    @TableField("trig_authority_no")
    private String trigAuthorityNo;

    /**
     * 罚款金额（元）
     */
    @TableField("penalty")
    private BigDecimal penalty;

    /**
     * 没收非法财物金额（元）
     */
    @TableField("confiscate_illegal_property")
    private BigDecimal confiscateIllegalProperty;

    /**
     * 撤销立案时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("revoke_filing_date")
    private Date revokeFilingDate;

    /**
     * 不予处罚日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("no_penalty_date")
    private Date noPenaltyDate;

    /**
     * 入库日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("warehousing_date")
    private Date warehousingDate;

    /**
     * 案件类型（1 简易案件 2 一般案件）
     */
    @TableField("case_type")
    private String caseType;

    /**
     * 没收非法所得金额
     */
    @TableField("confiscate_illegal_gains")
    private Double confiscateIllegalGains;

}
