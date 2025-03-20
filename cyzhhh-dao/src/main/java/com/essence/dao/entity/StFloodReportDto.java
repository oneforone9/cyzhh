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
 * 汛情上报表实体
 *
 * @author liwy
 * @since 2023-03-13 14:26:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_flood_report")
public class StFloodReportDto extends Model<StFloodReportDto> {

    private static final long serialVersionUID = -73026145138580077L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    
    /**
     * 事件描述
     */
    @TableField("event_describe")
    private String eventDescribe;
    
    /**
     * 所属河流id
     */
    @TableField("st_b_river_id")
    private String stBRiverId;

    /**
     * 所属河道名称
     */
    @TableField("rea_name")
    private String reaName;
    
    /**
     * 来源
     */
    @TableField("source")
    private String source;
    
    /**
     * 上报人
     */
    @TableField("report_person")
    private String reportPerson;
    
    /**
     * 上报人联系方式
     */
    @TableField("report_phone")
    private String reportPhone;
    
    /**
     * 经度（小程序端上传火星坐标系)
     */
    @TableField("lgtd")
    private Double lgtd;
    
    /**
     * 纬度（小程序端上传火星坐标系)
     */
    @TableField("lttd")
    private Double lttd;
    
    /**
     * 转换之后的经度(pc展示84坐标系)
     */
    @TableField("change_lgtd")
    private Double changeLgtd;
    
    /**
     * 转换之后的纬度(pc展示84坐标系)
     */
    @TableField("change_lttd")
    private Double changeLttd;
    
    /**
     * 是否删除（1是 0否）
     */
    @TableField("is_delete")
    private String isDelete;
    
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
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 具体位置
     * @mock 北京
     */
    @TableField("position")
    private String position;

}
