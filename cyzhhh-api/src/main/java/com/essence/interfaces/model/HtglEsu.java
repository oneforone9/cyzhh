package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理更新实体
 *
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */

@Data
public class HtglEsu extends Esu {

    private static final long serialVersionUID = 118591735980689711L;

    /**
     * 主键
     */
    private String id;

    /**
     * 合同编号
     */
    private String htbh;

    /**
     * 申请类型0合同文件1其他文件
     */
    private Integer sqlx;

    /**
     * 合同名称
     */
    private String htmc;

    /**
     * “三重一大”会议讨论情况
     */
    private String hytlqk;

    /**
     * 上会日期
     */
    private String shrq;

    /**
     * 上会内容
     */
    private String shnr;

    /**
     * 采购方式0直选1比选2竞争性磋商3公开招标4其他
     */
    private Integer cgfs;

    /**
     * 资金来源0预算资金1基建资金2其他资金3其他
     */
    private Integer zjly;

    /**
     * 合同内容
     */
    private String htnr;

    /**
     * 合同甲方
     */
    private String htjf;

    /**
     * 合同乙方
     */
    private String htyf;

    /**
     * 其他方
     */
    private String qtf;

    /**
     * 合同签订日期
     */
    private String htqdrq;

    /**
     * 合同金额（万元）
     */
    private String htje;

    /**
     * 首付款（万元）
     */
    private String sfk;

    /**
     * 中期款（万元）
     */
    private String zqk;

    /**
     * 尾款（万元）
     */
    private String wkqk;

    /**
     * 起始执行日期
     */
    private String qszxrq;

    /**
     * 终止执行日期
     */
    private String zzzxrq;

    /**
     * 签订科室
     */
    private String qdks;

    /**
     * 签订科室
     */
    private String qdksId;

    /**
     * 经办人
     */
    private String jbr;

    /**
     * 经办人名称
     */
    private String jbrmc;

    /**
     * 经办人部门
     */
    private String jbrbm;

    /**
     * 合同扫描件上传状态(0否1是)
     */
    private Integer htsmjsczt;


    /**
     * 合同上传状态(0否1是)
     */
    private Integer htsczt;

    /**
     * 合同生效状态0未生效1已生效2已执行完毕
     */
    private Integer htsxzt;

    /**
     * 提单时间(合法性审查)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tdsj;

    /**
     * 来源0自动1手动导入
     */
    private Integer ly;

    /**
     * 合同生效状态修改原因
     */
    private String htsxztxgyy;

    /**
     * 其他
     */
    private String qt;

    /**
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成
     */
    private Integer zt;

    /**
     * 法律顾问审查
     */
    private String flgwsc;

    /**
     * 提单人确认审查单
     */
    private String tdrqrscd;

    /**
     * 承办机构负责人意见
     */
    private String cbjgfzryj;

    /**
     * 签发领导类型0主要领导1主管领导
     */
    private Integer qfldlx;

    /**
     * 合同份数
     */
    private Integer htfs;

    /**
     * 律师审查编号
     */
    private String lsscbh;

    /**
     * 预审情况
     */
    private String ysqk;

    /**
     * 重大事项集体讨论情况
     */
    private String zdsxjttlqk;

    /**
     * 法制意见0无1有
     */
    private Integer fzyj;

    /**
     * 财务意见0无1有
     */
    private Integer cwyj;

    /**
     * 主管领导意见
     */
    private String zgldyj;

    /**
     * 主要领导意见
     */
    private String zyldyj;

    /**
     * 合同审核流程0无1局机关2局属单位
     */
    private Integer htshlc;

    /**
     * 财务室审批用户id
     */
    private String cwsid;

    /**
     * 办公室审批用户id
     */
    private String bgsid;

    /**
     * 主管领导id
     */
    private String zgldid;

    /**
     * 科长id
     */
    private String kzid;
    /**
     * 科长2id
     */
    private String kzids;
    /**
     * 科长3id
     */
    private String kzidss;

    /**
     * 财务科会签确认0否1是
     */
    private Integer cwshqqr;

    /**
     * 办公室会签确认0否1是
     */
    private Integer bgshqqr;

    /**
     * 科长审核确认0否1是
     */
    private Integer keshqr;

    /**
     * 科长2审核确认0否1是
     */
    private Integer keshqrs;
    /**
     * 科长3审核确认0否1是
     */
    private Integer keshqrss;
    /**
     * 回收站0否1是
     */
    private Integer hsz;

    /**
     * 退回状态0否1是
     */
    private Integer th;

    /**
     * 是否预审0否1是
     */
    private Integer sfys;
    /**
     * 律师id
     */
    private String lsid;
    /**
     * 提单时间（预审单）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tdslys;
    /**
     * 提单时间（会签）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tdsjhq;
    /**
     * 是否挂起0否1是
     */
    private Integer gq;
    /**
     * 备注
     */
    private String bz;


    /**
     * 当前人员
     */
    private String dqry;

    private String dqrymc;

    /**
     * 下一个操作人员
     */
    private String xyczry;

    private String xyczrymc;

//    /**
//     * 录入时间
//     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date lrsj;


    /**
     * 是否签订补充协议
     */
    private Integer sfqdbcxy;

    /**
     * 补充协议名称
     */
    private String bcxymc;

    /**
     * 补充协议内容
     */
    private String bcxynr;

    /**
     * 补充协议甲方
     */
    private String bcxyjf;

    /**
     * 补充协议乙方
     */
    private String bcxyyf;

    /**
     * 补充协议其他方
     */
    private String bcxyqtf;

    /**
     * 补充协议签订日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bcxyqdrq;

    /**
     * 补充协议金额
     */
    private String bcxyje;

    /**
     * '合同申请日期'
     */
    private String htsqrq;


    /**
     * 输入框类型（给前端判断，日历和文字)
     */
    private Integer srklx;

    /**
     * 输入框类型（给前端判断，日历和文字)
     */
    private Integer srklxSh;

    /**
     * 水印名称
     */
    private String symc;


    /**
     * 回退意见
     */
    private String htyj;
}
