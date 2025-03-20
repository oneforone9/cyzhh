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
 * 设备巡检任务实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "xjrw")
public class XjrwDto extends Model<XjrwDto> {

    private static final long serialVersionUID = 683951302381005958L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 工单名称
     */
    @TableField("gdmc")
    private String gdmc;

    /**
     * 工单编号
     */
    @TableField("bh")
    private String bh;

    /**
     * 工单来源0计划生成1临时生成2问题上报
     */
    @TableField("ly")
    private String ly;

    /**
     * 名称
     */
    @TableField("mc")
    private String mc;

    /**
     * 任务类型0摄像头1会议室
     */
    @TableField("type")
    private String type;

    /**
     * 任务类型0巡检工单1问题工单
     */
    @TableField("lx")
    private String lx;

    /**
     * 所属河道
     */
    @TableField("river_name")
    private String riverName;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 经度
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    private Double lttd;

    /**
     * 巡检内容
     */
    @TableField("xjnr")
    private String xjnr;

    /**
     * 负责人
     */
    @TableField("fzr")
    private String fzr;

    /**
     * 负责人id
     */
    @TableField("fzr_id")
    private String fzrId;

    /**
     * 联系方式
     */
    @TableField("lxfs")
    private String lxfs;

    /**
     * 计划巡检时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("jhsj")
    private Date jhsj;

    /**
     * 实际巡检时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("sjsj")
    private Date sjsj;

    /**
     * 截至时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("jssj")
    private Date jssj;

    /**
     * 状态0未开始1已终止2进行中3待审核4审核不通过5已归档6问题工单待审核
     */
    @TableField("zt")
    private Integer zt;

    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */
    @TableField("wcqk")
    private Integer wcqk;

    /**
     * 发现问题0否1是
     */
    @TableField("fxwt")
    private Integer fxwt;

    /**
     * 审核意见
     */
    @TableField("yj")
    private String yj;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("cjsj")
    private Date cjsj;

    /**
     * 打卡经度
     */
    @TableField("dkjd")
    private Double dkjd;

    /**
     * 打卡纬度
     */
    @TableField("dkwd")
    private Double dkwd;

    /**
     * 打卡地址
     */
    @TableField("dkdz")
    private String dkdz;

    /**
     * 打卡记录图片
     */
    @TableField("dkjltp")
    private String dkjltp;

    /**
     * 问题描述
     */
    @TableField("wtms")
    private String wtms;

    /**
     * 处理前图片
     */
    @TableField("dktp")
    private String dktp;

    /**
     * 处理后图片
     */
    @TableField("cltp")
    private String cltp;

    /**
     * 处理措施
     */
    @TableField("clcs")
    private String clcs;

    /**
     * 发现时间（上报时间）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("fxsj")
    private Date fxsj;

    /**
     * 派发时间（审核时间）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("pfsj")
    private Date pfsj;

    /**
     * 解决时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("jjsj")
    private Date jjsj;

    /**
     * 班组名称
     */
    @TableField("bzmc")
    private String bzmc;

    /**
     * 派发人员
     */
    @TableField("pfry")
    private String pfry;

    /**
     * 班组id
     */
    @TableField("bz_id")
    private String bzId;
    /**
     * 上报人
     */
    @TableField("sbr")
    private String sbr;
    /**
     * 上级id(用于问题工单关联正常巡检任务工单)
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 终止理由
     */
    @TableField("zzly")
    private String zzly;
    /**
     * 解决时限
     */
    @TableField("jjsx")
    private Integer jjsx;
}
