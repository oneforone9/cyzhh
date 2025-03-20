package com.essence.interfaces.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检任务更新实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:06
 */

@Data
public class XjrwEsu extends Esu {

    private static final long serialVersionUID = -48999380549948279L;

    /**
     * 主键
     */        private String id;    

    /**
     * 工单名称
     */        private String gdmc;    

    /**
     * 工单编号
     */        private String bh;    

    /**
     * 工单来源0计划生成1临时生成2问题上报
     */        private String ly;    

    /**
     * 名称
     */        private String mc;    

    /**
     * 任务类型0摄像头1会议室
     */        private String type;    

    /**
     * 任务类型0巡检工单1问题工单
     */        private String lx;    

    /**
     * 所属河道
     */        private String riverName;    

    /**
     * 地址
     */        private String address;    

    /**
     * 经度
     */        private Double lgtd;    

    /**
     * 纬度
     */        private Double lttd;    

    /**
     * 巡检内容
     */        private String xjnr;    

    /**
     * 负责人
     */        private String fzr;    

    /**
     * 负责人id
     */        private String fzrId;    

    /**
     * 联系方式
     */        private String lxfs;    

    /**
     * 计划巡检时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date jhsj;    

    /**
     * 实际巡检时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sjsj;    

    /**
     * 截至时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date jssj;    

    /**
     * 状态0未开始1已终止2进行中3待审核4审核不通过5已归档6问题工单待审核
     */        private Integer zt;    

    /**
     * 巡检完成情况0未完成1已完成2超期完成
     */        private Integer wcqk;    

    /**
     * 发现问题0否1是
     */        private Integer fxwt;    

    /**
     * 审核意见
     */        private String yj;    

    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cjsj;    

    /**
     * 打卡经度
     */        private Double dkjd;    

    /**
     * 打卡纬度
     */        private Double dkwd;    

    /**
     * 打卡地址
     */        private String dkdz;

    /**
     * 打卡记录图片
     */        private String dkjltp;    

    /**
     * 问题描述
     */        private String wtms;    

    /**
     * 处理前图片
     */        private String dktp;    

    /**
     * 处理后图片
     */        private String cltp;    

    /**
     * 处理措施
     */        private String clcs;    

    /**
     * 发现时间（上报时间）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fxsj;    

    /**
     * 派发时间（审核时间）
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pfsj;    

    /**
     * 解决时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date jjsj;    

    /**
     * 班组名称
     */        private String bzmc;    

    /**
     * 派发人员
     */        private String pfry;    

    /**
     * 班组id
     */        private String bzId;

    /**
     * 上报人
     */
    private String sbr;
    /**
     * 上级id(用于问题工单关联正常巡检任务工单)
     */
    private String parentId;
    /**
     * 终止理由
     */
    private String zzly;
    /**
     * 解决时限
     */
    private Integer jjsx;
}
