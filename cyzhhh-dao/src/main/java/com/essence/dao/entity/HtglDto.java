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
 * 合同管理实体
 *
 * @author majunjie
 * @since 2024-09-09 17:45:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htgl")
public class HtglDto extends Model<HtglDto> {

    private static final long serialVersionUID = 346618786455445182L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同编号
     */
    @TableField("htbh")
    private String htbh;

    /**
     * 申请类型0合同文件1其他文件
     */
    @TableField("sqlx")
    private Integer sqlx;

    /**
     * 合同名称
     */
    @TableField("htmc")
    private String htmc;

    /**
     * “三重一大”会议讨论情况
     */
    @TableField("hytlqk")
    private String hytlqk;

    /**
     * 上会日期
     */
    @TableField("shrq")
    private String shrq;

    /**
     * 上会内容
     */
    @TableField("shnr")
    private String shnr;

    /**
     * 采购方式0直选1比选2竞争性磋商3公开招标4其他
     */
    @TableField("cgfs")
    private Integer cgfs;

    /**
     * 资金来源0预算资金1基建资金2其他资金3其他
     */
    @TableField("zjly")
    private Integer zjly;

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
     * 首付款（万元）
     */
    @TableField("sfk")
    private String sfk;

    /**
     * 中期款（万元）
     */
    @TableField("zqk")
    private String zqk;

    /**
     * 尾款（万元）
     */
    @TableField("wkqk")
    private String wkqk;

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
     * 签订科室
     */
    @TableField("qdks_id")
    private String qdksId;

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
     * 合同扫描件上传状态(0否1是)
     */
    @TableField("htsmjsczt")
    private Integer htsmjsczt;

    /**
     * 合同上传状态(0否1是)
     */
    @TableField("htsczt")
    private Integer htsczt;

    /**
     * 合同生效状态0未生效1已生效2已执行完毕
     */
    @TableField("htsxzt")
    private Integer htsxzt;

    /**
     * 提单时间(合法性审查)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("tdsj")
    private Date tdsj;

    /**
     * 来源0自动1手动导入
     */
    @TableField("ly")
    private Integer ly;

    /**
     * 合同生效状态修改原因
     */
    @TableField("htsxztxgyy")
    private String htsxztxgyy;

    /**
     * 其他
     */
    @TableField("qt")
    private String qt;

    /**
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成
     */

    /**
     * 新合同状态，-1：新增会签单；0:录入合同之后； 1:录入人提交合同之后  ； 2:合同人，录入合同编码之后  3:录入人，上传合同附件之后 4: 合同人勾选已完成之后，合同结束
     */
    @TableField("zt")
    private Integer zt;


    /**
     * 备注
     */
    @TableField("bz")
    private String bz;


    ////////////////////////////////////////  下面是新合同的逻辑，新增加字段; 主要兼容之后的逻辑  ////////////////////////////////////////    //////////////////////


    /**
     * 当前人员
     */
    @TableField("dqry")
    private String dqry;

    @TableField("dqrymc")
    private String dqrymc;

    /**
     * 下一个操作人员
     */
    @TableField("xyczry")
    private String xyczry;

    @TableField("xyczrymc")
    private String xyczrymc;

    /**
     * 录入时间
     */
    @TableField("lrsj")
    private Date lrsj;


    /**
     * 是否签订补充协议
     */
    @TableField("sfqdbcxy")
    private Integer sfqdbcxy;

    /**
     * 补充协议名称
     */
    @TableField("bcxymc")
    private String bcxymc;

    /**
     * 补充协议内容
     */
    @TableField("bcxynr")
    private String bcxynr;

    /**
     * 补充协议甲方
     */
    @TableField("bcxyjf")
    private String bcxyjf;

    /**
     * 补充协议乙方
     */
    @TableField("bcxyyf")
    private String bcxyyf;

    /**
     * 补充协议其他方
     */
    @TableField("bcxyqtf")
    private String bcxyqtf;

    /**
     * 补充协议签订日期
     */
    @TableField("bcxyqdrq")
    private Date bcxyqdrq;

    /**
     * 补充协议金额
     */
    @TableField("bcxyje")
    private String bcxyje;

    ////////////////////////////////////////  下面是之前合同的逻辑，相关字段  ////////////////////////////////////////    ////////////////////////////////////////


    /**
     * 法律顾问审查
     */
    @TableField("flgwsc")
    private String flgwsc;

    /**
     * 提单人确认审查单
     */
    @TableField("tdrqrscd")
    private String tdrqrscd;

    /**
     * 承办机构负责人意见
     */
    @TableField("cbjgfzryj")
    private String cbjgfzryj;

    /**
     * 签发领导类型0主要领导1主管领导；
     * 是否会签 0： 否  1: 是
     */
    @TableField("qfldlx")
    private Integer qfldlx;

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
     * 法制意见0无1有
     */
    @TableField("fzyj")
    private Integer fzyj;

    /**
     * 财务意见0无1有
     */
    @TableField("cwyj")
    private Integer cwyj;

    /**
     * 主管领导意见
     */
    @TableField("zgldyj")
    private String zgldyj;

    /**
     * 主要领导意见
     */
    @TableField("zyldyj")
    private String zyldyj;

    /**
     * 合同审核流程0无1局机关2局属单位
     */
    @TableField("htshlc")
    private Integer htshlc;

    /**
     * 财务室审批用户id
     */
    @TableField("cwsid")
    private String cwsid;

    /**
     * 办公室审批用户id
     */
    @TableField("bgsid")
    private String bgsid;

    /**
     * 主管领导id
     */
    @TableField("zgldid")
    private String zgldid;

    /**
     * 科长1id
     */
    @TableField("kzid")
    private String kzid;

    /**
     * 科长2id
     */
    @TableField("kzids")
    private String kzids;
    /**
     * 科长3id
     */
    @TableField("kzidss")
    private String kzidss;

    /**
     * 财务科会签确认0否1是
     */
    @TableField("cwshqqr")
    private Integer cwshqqr;

    /**
     * 办公室会签确认0否1是
     */
    @TableField("bgshqqr")
    private Integer bgshqqr;

    /**
     * 科长审核确认0否1是
     */
    @TableField("keshqr")
    private Integer keshqr;

    /**
     * 科长2审核确认0否1是
     */
    @TableField("keshqrs")
    private Integer keshqrs;
    /**
     * 科长3审核确认0否1是
     */
    @TableField("keshqrss")
    private Integer keshqrss;
    /**
     * 回收站0否1是
     */
    @TableField("hsz")
    private Integer hsz;

    /**
     * 退回状态0否1是
     */
    @TableField("th")
    private Integer th;

    /**
     * 是否预审0否1是
     */
    @TableField("sfys")
    private Integer sfys;
    /**
     * 律师id
     */
    @TableField("lsid")
    private String lsid;
    /**
     * 提单时间（预审单）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("tdslys")
    private Date tdslys;
    /**
     * 提单时间（会签）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("tdsjhq")
    private Date tdsjhq;
    /**
     * 是否挂起0否1是
     */
    @TableField("gq")
    private Integer gq;

    ////////////////////////////////////////  第三版本合同，新增字段  ////////////////////////////////////////    ////////////////////////////////////////


    /**
     * '合同申请日期'
     */
    @TableField("htsqrq")
    private String htsqrq;


    /**
     * 输入框类型（给前端判断，日历和文字) 1: 日历 2 ：文字
     */
    @TableField("srklx")
    private Integer srklx;


    /**
     * 输入框类型（给前端判断，日历和文字) 1: 日历 2 ：文字
     */
    @TableField("srklx_sh")
    private Integer srklxSh;

    /**
     * 水印名称
     */
    @TableField("symc")
    private String symc;

    /**
     * 回退意见
     */
    @TableField("htyj")
    private String htyj;

}
