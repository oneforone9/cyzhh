package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频基础信息表返回实体
 *
 * @author zhy
 * @since 2022-10-20 14:20:46
 */

@Data
public class VideoInfoBaseEsr extends Esr {

    private static final long serialVersionUID = -13167443576305022L;


    /**
     * 主键
     */
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    private String source;

    /**
     * 行政区编码
     */
    private String adCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 河系分配表st_b_river的 id
     */
    private String stBRiverId;

    /**
     * AI算法
     */
    private String aiAlgorithm;

    /**
     * 功能类型 1-功能   2-安防  3-井房
     */
    private String functionType;

    /**
     * 所在区域
     */
    private String region;

    /**
     * 摄像机类型
     */
    private String cameraType;


    /**
     * 视频状态(0 离线 1 在线)
     */
    private String status;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 河道id
     */
    private String reaId;


    /**
     * 预留字段3
     */
    private String reserve3;

    /**
     * 是否在观鸟 1-北小河  2-亮马河 3-坝河 4-萧太后河  空或其他则不是观鸟区视频
     */
    private String birdArea;
}
