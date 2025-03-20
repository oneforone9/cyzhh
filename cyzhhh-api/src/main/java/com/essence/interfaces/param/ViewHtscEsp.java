package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2024-09-13 16:41:10
 */

@Data
public class ViewHtscEsp extends Esp {

    private static final long serialVersionUID = -70475753550112682L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 合同编号
     */        @ColumnMean("htbh")
    private String htbh;
    /**
     * 申请类型0合同文件1其他文件
     */        @ColumnMean("sqlx")
    private Integer sqlx;
    /**
     * 合同名称
     */        @ColumnMean("htmc")
    private String htmc;
    /**
     * “三重一大”会议讨论情况
     */        @ColumnMean("hytlqk")
    private String hytlqk;
    /**
     * 上会日期
     */        @ColumnMean("shrq")
    private String shrq;
    /**
     * 上会内容
     */        @ColumnMean("shnr")
    private String shnr;
    /**
     * 采购方式0直选1比选2竞争性磋商3公开招标4其他
     */        @ColumnMean("cgfs")
    private Integer cgfs;
    /**
     * 资金来源0预算资金1基建资金2其他资金3其他
     */        @ColumnMean("zjly")
    private Integer zjly;
    /**
     * 合同内容
     */        @ColumnMean("htnr")
    private String htnr;
    /**
     * 合同甲方
     */        @ColumnMean("htjf")
    private String htjf;
    /**
     * 合同乙方
     */        @ColumnMean("htyf")
    private String htyf;
    /**
     * 其他方
     */        @ColumnMean("qtf")
    private String qtf;
    /**
     * 合同签订日期
     */        @ColumnMean("htqdrq")
    private String htqdrq;
    /**
     * 合同金额（万元）
     */        @ColumnMean("htje")
    private String htje;
    /**
     * 首付款（万元）
     */        @ColumnMean("sfk")
    private String sfk;
    /**
     * 中期款（万元）
     */        @ColumnMean("zqk")
    private String zqk;
    /**
     * 尾款（万元）
     */        @ColumnMean("wkqk")
    private String wkqk;
    /**
     * 起始执行日期
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("qszxrq")
    private Date qszxrq;
    /**
     * 终止执行日期
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("zzzxrq")
    private Date zzzxrq;
    /**
     * 签订科室
     */        @ColumnMean("qdks")
    private String qdks;
    /**
     * 经办人
     */        @ColumnMean("jbr")
    private String jbr;
    /**
     * 经办人名称
     */        @ColumnMean("jbrmc")
    private String jbrmc;
    /**
     * 经办人部门
     */        @ColumnMean("jbrbm")
    private String jbrbm;
    /**
     * 合同扫描件上传状态(0否1是)
     */        @ColumnMean("htsmjsczt")
    private Integer htsmjsczt;
    /**
     * 合同生效状态0未生效1已生效2已执行完毕
     */        @ColumnMean("htsxzt")
    private Integer htsxzt;
    /**
     * 提单时间(合法性审查)
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tdsj")
    private Date tdsj;
    /**
     * 来源0自动1手动导入
     */        @ColumnMean("ly")
    private Integer ly;
    /**
     * 合同生效状态修改原因
     */        @ColumnMean("htsxztxgyy")
    private String htsxztxgyy;
    /**
     * 其他
     */        @ColumnMean("qt")
    private String qt;
    /**
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成
     */        @ColumnMean("zt")
    private Integer zt;
    /**
     * 法律顾问审查
     */        @ColumnMean("flgwsc")
    private String flgwsc;
    /**
     * 提单人确认审查单
     */        @ColumnMean("tdrqrscd")
    private String tdrqrscd;
    /**
     * 承办机构负责人意见
     */        @ColumnMean("cbjgfzryj")
    private String cbjgfzryj;
    /**
     * 签发领导类型0主要领导1主管领导
     */        @ColumnMean("qfldlx")
    private Integer qfldlx;
    /**
     * 合同份数
     */        @ColumnMean("htfs")
    private Integer htfs;
    /**
     * 律师审查编号
     */        @ColumnMean("lsscbh")
    private String lsscbh;
    /**
     * 预审情况
     */        @ColumnMean("ysqk")
    private String ysqk;
    /**
     * 重大事项集体讨论情况
     */        @ColumnMean("zdsxjttlqk")
    private String zdsxjttlqk;
    /**
     * 法制意见0无1有
     */        @ColumnMean("fzyj")
    private Integer fzyj;
    /**
     * 财务意见0无1有
     */        @ColumnMean("cwyj")
    private Integer cwyj;
    /**
     * 主管领导意见
     */        @ColumnMean("zgldyj")
    private String zgldyj;
    /**
     * 主要领导意见
     */        @ColumnMean("zyldyj")
    private String zyldyj;
    /**
     * 合同审核流程0无1局机关2局属单位
     */        @ColumnMean("htshlc")
    private Integer htshlc;
    /**
     * 财务室审批用户id
     */        @ColumnMean("cwsid")
    private String cwsid;
    /**
     * 办公室审批用户id
     */        @ColumnMean("bgsid")
    private String bgsid;
    /**
     * 主管领导id
     */        @ColumnMean("zgldid")
    private String zgldid;
    /**
     * 科长1id
     */        @ColumnMean("kzid")
    private String kzid;
    /**
     * 科长2id
     */        @ColumnMean("kzids")
    private String kzids;
    /**
     * 科长3id
     */        @ColumnMean("kzidss")
    private String kzidss;
    /**
     * 财务科会签确认0否1是
     */        @ColumnMean("cwshqqr")
    private Integer cwshqqr;
    /**
     * 办公室会签确认0否1是
     */        @ColumnMean("bgshqqr")
    private Integer bgshqqr;
    /**
     * 科长审核确认0否1是
     */        @ColumnMean("keshqr")
    private Integer keshqr;
    /**
     * 科长2审核确认0否1是
     */        @ColumnMean("keshqrs")
    private Integer keshqrs;
    /**
     * 科长3审核确认0否1是
     */        @ColumnMean("keshqrss")
    private Integer keshqrss;
    /**
     * 回收站0否1是
     */        @ColumnMean("hsz")
    private Integer hsz;
    /**
     * 退回状态0否1是
     */        @ColumnMean("th")
    private Integer th;
    /**
     * 是否预审0否1是
     */        @ColumnMean("sfys")
    private Integer sfys;
    /**
     * 律师id
     */        @ColumnMean("lsid")
    private String lsid;
    /**
     * 提单时间（预审单）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tdslys")
    private Date tdslys;
    /**
     * 提单时间（会签）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tdsjhq")
    private Date tdsjhq;
    /**
     * 是否挂起0否1是
     */        @ColumnMean("gq")
    private Integer gq;
    /**
     * 用户id
     */        @ColumnMean("user_id")
    private String userId;


}
