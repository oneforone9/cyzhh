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

import javax.annotation.Resource;
import java.util.Date;

/**
 * 视频基础信息表(VideoInfoBase)实体类
 *
 * @author zhy
 * @since 2022-10-20 14:20:42
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "video_info_base")
public class VideoInfoBase extends Model<VideoInfoBase> {

    private static final long serialVersionUID = 263541357284812015L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

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
     * 视频来源（HIV 海康 HUAWEI 华为）
     */
    @TableField("source")
    private String source;

    /**
     * 行政区编码
     */
    @TableField("ad_code")
    private String adCode;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;

    /**
     * 河系分配表st_b_river的 id
     */
    @TableField("st_b_river_id")
    private String stBRiverId;

    /**
     * AI算法
     */
    @TableField("ai_algorithm")
    private String aiAlgorithm;
    /**
     * 功能类型 1-功能   2-安防  3-井房
     */
    @TableField("function_type")
    private String functionType;
    /**
     * 预留字段3
     */
    @TableField("camera_type")
    private String cameraType;

    /**
     * 预留字段4
     */
    @TableField("reserve4")
    private String reserve4;

    @TableField("st_b_river_id")
    private String reaId;
    /**
     * 单位id
     */
    @TableField("unit_id")
    private String unitId;
    @TableField("ip_address")
    private String ipAddress;
    /**
     * 排序（按河道上中下游排序）
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 所在区域
     */
    @TableField("region")
    private String region;

    /**
     * 预留字段3
     */
    @TableField("reserve3")
    private String reserve3;

    /**
     * 是否在观鸟 1-北小河  2-亮马河 3-坝河 4-萧太后河  空或其他则不是观鸟区视频
     */
    @TableField("bird_area")
    private String birdArea;
}
