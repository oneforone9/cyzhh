package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频基础信息表参数实体
 *
 * @author zhy
 * @since 2022-10-20 14:20:46
 */

@Data
public class VideoInfoBaseEsp extends Esp {

    private static final long serialVersionUID = -99676049584021268L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;

    /**
     * 编码
     */
    @ColumnMean("code")
    private String code;

    /**
     * 名称
     */
    @ColumnMean("name")
    private String name;

    /**
     * 地址
     */
    @ColumnMean("address")
    private String address;

    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private Double lttd;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    @ColumnMean("source")
    private String source;

    /**
     * 行政区编码
     */
    @ColumnMean("ad_code")
    private String adCode;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 更新者
     */
    @ColumnMean("updater")
    private String updater;

    /**
     * 河系分配表st_b_river的 id
     */
    @ColumnMean("st_b_river_id")
    private String stBRiverId;

    /**
     * AI算法
     */
    @ColumnMean("ai_algorithm")
    private String aiAlgorithm;

    /**
     * 功能类型 1-功能   2-安防  3-井房
     */
    @ColumnMean("function_type")
    private String functionType;

    /**
     * 所在区域
     */
    @ColumnMean("region")
    private String region;

    /**
     * 摄像机类型
     */
    @ColumnMean("camera_type")
    private String cameraType;

    /**
     * 单位id
     */
    @ColumnMean("unit_id")
    private String unitId;


    /**
     * 排序（按河道上中下游排序）
     */
    @ColumnMean("sort")
    private String sort;

    /**
     * 预留字段3
     */
    @ColumnMean("reserve3")
    private String reserve3;

    /**
     * 是否在观鸟 1-北小河  2-亮马河 3-坝河 4-萧太后河  空或其他则不是观鸟区视频
     */
    @ColumnMean("bird_area")
    private String birdArea;
}
