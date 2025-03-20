package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检任务参数实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:06
 */

@Data
public class XjrwEsp extends Esp {

    private static final long serialVersionUID = -51401039329803492L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 工单名称
     */        @ColumnMean("gdmc")
    private String gdmc;
    /**
     * 工单编号
     */        @ColumnMean("bh")
    private String bh;
    /**
     * 工单来源0计划生成1临时生成2问题上报
     */        @ColumnMean("ly")
    private String ly;
    /**
     * 名称
     */        @ColumnMean("mc")
    private String mc;
    /**
     * 任务类型0摄像头1会议室
     */        @ColumnMean("type")
    private String type;
    /**
     * 任务类型0巡检工单1问题工单
     */        @ColumnMean("lx")
    private String lx;
    /**
     * 所属河道
     */        @ColumnMean("river_name")
    private String riverName;
    /**
     * 地址
     */        @ColumnMean("address")
    private String address;
    /**
     * 经度
     */        @ColumnMean("lgtd")
    private Double lgtd;
    /**
     * 纬度
     */        @ColumnMean("lttd")
    private Double lttd;
    /**
     * 巡检内容
     */        @ColumnMean("xjnr")
    private String xjnr;
    /**
     * 负责人
     */        @ColumnMean("fzr")
    private String fzr;
    /**
     * 负责人id
     */        @ColumnMean("fzr_id")
    private String fzrId;
    /**
     * 联系方式
     */        @ColumnMean("lxfs")
    private String lxfs;
    /**
     * 计划巡检时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("jhsj")
    private Date jhsj;
    /**
     * 实际巡检时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("sjsj")
    private Date sjsj;
    /**
     * 截至时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("jssj")
    private Date jssj;
    /**
     * 状态0未开始1已终止2进行中3待审核4审核不通过5已归档6问题工单待审核
     */        @ColumnMean("zt")
    private Integer zt;
    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */        @ColumnMean("wcqk")
    private Integer wcqk;
    /**
     * 发现问题0否1是
     */        @ColumnMean("fxwt")
    private Integer fxwt;
    /**
     * 审核意见
     */        @ColumnMean("yj")
    private String yj;
    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("cjsj")
    private Date cjsj;
    /**
     * 打卡经度
     */        @ColumnMean("dkjd")
    private Double dkjd;
    /**
     * 打卡纬度
     */        @ColumnMean("dkwd")
    private Double dkwd;
    /**
     * 打卡地址
     */        @ColumnMean("dkdz")
    private String dkdz;
    /**
     * 打卡记录图片
     */        @ColumnMean("dkjltp")
    private String dkjltp;
    /**
     * 问题描述
     */        @ColumnMean("wtms")
    private String wtms;
    /**
     * 处理前图片
     */        @ColumnMean("dktp")
    private String dktp;
    /**
     * 处理后图片
     */        @ColumnMean("cltp")
    private String cltp;
    /**
     * 处理措施
     */        @ColumnMean("clcs")
    private String clcs;
    /**
     * 发现时间（上报时间）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("fxsj")
    private Date fxsj;
    /**
     * 派发时间（审核时间）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("pfsj")
    private Date pfsj;
    /**
     * 解决时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("jjsj")
    private Date jjsj;
    /**
     * 班组名称
     */        @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 派发人员
     */        @ColumnMean("pfry")
    private String pfry;
    /**
     * 班组id
     */        @ColumnMean("bz_id")
    private String bzId;
    /**
     * 上报人
     */
    @ColumnMean("sbr")
    private String sbr;
    /**
     * 上级id(用于问题工单关联正常巡检任务工单)
     */
    @ColumnMean("parent_id")
    private String parentId;
    /**
     * 终止理由
     */
    @ColumnMean("zzly")
    private String zzly;
    /**
     * 解决时限
     */
    @ColumnMean("jjsx")
    private Integer jjsx;
}
