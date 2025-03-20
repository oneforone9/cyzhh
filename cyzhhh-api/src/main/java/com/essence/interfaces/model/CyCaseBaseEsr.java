package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 案件基础表返回实体
 *
 * @author zhy
 * @since 2023-01-04 18:13:44
 */

@Data
public class CyCaseBaseEsr extends Esr {

    private static final long serialVersionUID = 944180007117003413L;


    /**
     * 主键
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;


    /**
     * 案件名称
     */
    private String caseName;


    /**
     * 所属部门
     */
    private String department;


    /**
     * 处罚决定书文号
     */
    private String punishDocNo;


    /**
     * 处罚决定书文号日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date punishDocDate;


    /**
     * 立案号
     */
    private String filingNo;


    /**
     * 立案日期
     */
    private String filingDate;


    /**
     * 结案状态(1 正常结案、2 撤销案件、3案件移送、4不予处罚、5 撤销原触发决定、6终结)
     */
    private String closingStatus;


    /**
     * 结案日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date closingDate;


    /**
     * 触发的职权编号
     */
    private String trigAuthorityNo;


    /**
     * 罚款金额（元）
     */
    private BigDecimal penalty;


    /**
     * 没收非法财物金额（元）
     */
    private BigDecimal confiscateIllegalProperty;


    /**
     * 撤销立案时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date revokeFilingDate;


    /**
     * 不予处罚日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date noPenaltyDate;


    /**
     * 入库日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date warehousingDate;


    /**
     * 案件类型（1 简易案件 2 一般案件）
     */
    @NotEmpty(message = "案件类型不能为空")
    private String caseType;


    /**
     * 没收非法所得金额
     */
    private Double confiscateIllegalGains;


}
