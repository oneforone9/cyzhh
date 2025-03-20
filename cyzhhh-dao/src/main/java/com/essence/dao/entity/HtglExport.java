package com.essence.dao.entity;

import cn.hutool.core.date.DateUtil;
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
public class HtglExport {

    /**
     * 合同编号
     */
    private String htbh;

    /**
     * 申请类型0合同文件1其他文件
     */
    private Integer sqlx;
    private String sqlxStr;

    public String getSqlxStr() {
        if(sqlx.equals(0)){
            return "合同文件";
        }else if(sqlx.equals(1)){
            return "其他文件";
        }
        return "";
    }

    /**
     * 提单时间(合法性审查)
     */
    private Date tdsj;
    private Date tdsjStr;
    public String getTdsjStr() {
        if(null != tdsj){
            return DateUtil.format(tdsj, "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }
    private Date tdsjStr1;
    public String getTdsjStr1() {
        if(null != tdsj){
            return DateUtil.format(tdsj, "yyyy年MM月dd日");
        }
        return "";
    }

    /**
     * 经办人名称
     */
    private String jbrmc;

    /**
     * 经办人部门
     */
    private String jbrbm;

    /**
     * 合同名称
     */
    private String htmc;

    /**
     * 合同甲方
     */
    private String htjf;

    /**
     * 合同乙方
     */
    private String htyf;

    /**
     * 合同金额（万元）
     */
    private String htje;

    /**
     * 其他方
     */
    private String qtf;

    /**
     * 合同内容
     */
    private String htnr;

    /**
     * 法律顾问审查
     */
    private String flgwsc;

    /**
     * 提单人确认审查单
     */
    private String tdrqrscd;

    /**
     * 其他
     */
    private String qt;

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
     * 科长1id  通过视图的名称  负责人名字
     */
    private String kzid;

    /**
     * 科长Name
     */
    private String kzName;

    /**
     * 签发领导类型0主要领导1主管领导
     */
    private Integer qfldlx;
    private String qfldlxTitle;

    /**
     * 主管领导意见
     */
    private String zgldyj;

    /**
     * 主要领导意见 有主要的意见就一定有主管的意见
     */
    private String zyldyj;

    /**
     * 经办人名称 会签 财务科
     */
    private String hqjbrmc;
    private String hqjbrmcDate;

    /**
     * 经办人 会签 局办
     */
    private String hqjbr;
    private String hqjbrDate;

    /**
     * 财务室审批用户id
     */
    private String cwsid;

    /**
     * 办公室审批用户id
     */
    private String bgsid;






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
     * 合同签订日期
     */
    @TableField("htqdrq")
    private String htqdrq;


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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("qszxrq")
    private Date qszxrq;

    /**
     * 终止执行日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("zzzxrq")
    private Date zzzxrq;

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
     * 合同扫描件上传状态(0否1是)
     */
    @TableField("htsmjsczt")
    private Integer htsmjsczt;

    /**
     * 合同生效状态0未生效1已生效2已执行完毕
     */
    @TableField("htsxzt")
    private Integer htsxzt;


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
     * 状态0申请人（保存）待提交1律师（保存）待提交2申请人待确认审查单3科长待确认预审单4主管领导待确认预审单5申请人待填写会签单6科长待确认会签单7申请人待选择财务科人员和办公室人员8财务科和办公室待确认会签单9申请人待确认会签单并选择领导10主管领导待确认会签单11主要领导待确认会签单12办公室待审查13申请人待确认会签单14合同完成
     */
    @TableField("zt")
    private Integer zt;


    /**
     * 承办机构负责人意见
     */
    @TableField("cbjgfzryj")
    private String cbjgfzryj;

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
     * 合同审核流程0无1局机关2局属单位
     */
    @TableField("htshlc")
    private Integer htshlc;

    /**
     * 主管领导id
     */
    @TableField("zgldid")
    private String zgldid;


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
}
