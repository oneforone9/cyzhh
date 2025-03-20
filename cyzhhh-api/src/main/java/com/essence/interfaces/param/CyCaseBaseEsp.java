package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 案件基础表参数实体
 *
 * @author zhy
 * @since 2023-01-04 18:13:44
 */

@Data
public class CyCaseBaseEsp extends Esp {

    private static final long serialVersionUID = 544640191261680328L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 案件名称
     */
    @ColumnMean("case_name")
    private String caseName;
    /**
     * 所属部门
     */
    @ColumnMean("department")
    private String department;
    /**
     * 处罚决定书文号
     */
    @ColumnMean("punish_doc_no")
    private String punishDocNo;
    /**
     * 处罚决定书文号日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ColumnMean("punish_doc_date")
    private Date punishDocDate;
    /**
     * 立案号
     */
    @ColumnMean("filing_no")
    private String filingNo;
    /**
     * 立案日期
     */
    @ColumnMean("filing_date")
    private String filingDate;
    /**
     * 结案状态(1 正常结案、2 撤销案件、3案件移送、4不予处罚、5 撤销原触发决定、6终结)
     */
    @ColumnMean("closing_status")
    private String closingStatus;
    /**
     * 结案日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ColumnMean("closing_date")
    private Date closingDate;
    /**
     * 触发的职权编号
     */
    @ColumnMean("trig_authority_no")
    private String trigAuthorityNo;
    /**
     * 罚款金额（元）
     */
    @ColumnMean("penalty")
    private BigDecimal penalty;
    /**
     * 没收非法财物金额（元）
     */
    @ColumnMean("confiscate_illegal_property")
    private BigDecimal confiscateIllegalProperty;
    /**
     * 撤销立案时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ColumnMean("revoke_filing_date")
    private Date revokeFilingDate;
    /**
     * 不予处罚日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ColumnMean("no_penalty_date")
    private Date noPenaltyDate;
    /**
     * 入库日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ColumnMean("warehousing_date")
    private Date warehousingDate;
    /**
     * 案件类型（1 简易案件 2 一般案件）
     */
    @ColumnMean("case_type")
    private String caseType;
    /**
     * 没收非法所得金额
     */
    @ColumnMean("confiscate_illegal_gains")
    private Double confiscateIllegalGains;


}
