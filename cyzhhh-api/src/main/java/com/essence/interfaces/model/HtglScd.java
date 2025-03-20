package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 合同管理返回实体
 *
 * @author majunjie
 * @since 2024-09-09 17:48:33
 */

@Data
public class HtglScd extends Esr {

    private static final long serialVersionUID = 984533482413267853L;
    /**
     * 主键
     */        private String id;

    /**
     * 申请类型0合同文件1其他文件
     */        private Integer sqlx;

    /**
     * 合同名称
     */        private String htmc;

    /**
     * 合同内容
     */        private String htnr;

    /**
     * 合同甲方
     */        private String htjf;

    /**
     * 合同乙方
     */        private String htyf;

    /**
     * 其他方
     */        private String qtf;

    /**
     * 合同金额（万元）
     */        private String htje;

    /**
     * 签订科室
     */        private String qdks;

    /**
     * 经办人
     */        private String jbr;

    /**
     * 经办人名称
     */        private String jbrmc;

    /**
     * 经办人部门
     */        private String jbrbm;

    /**
     * 法律顾问审查
     */        private String flgwsc;

    /**
     * 提单人确认审查单
     */        private String tdrqrscd;
    /**
     * 其他
     */        private String qt;
    /**
     * 状态0申请人（保存）待提交1待法律提交2审查单子待申请人确认
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成,
     */        private Integer zt;
    /**
     * 合同审核流程0无1局机关2局属单位
     */
    private Integer htshlc;
    /**
     * 律师id
     */
    private String lsid;
    /**
     * 合同附件
     */
    private List<HtglfjEsu> fjData;
}
