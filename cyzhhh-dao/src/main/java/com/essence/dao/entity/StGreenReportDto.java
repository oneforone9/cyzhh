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

import java.beans.Transient;
import java.util.Date;

/**
 * 绿化保洁工作日志上报表实体
 *
 * @author liwy
 * @since 2023-03-14 15:36:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_green_report")
public class StGreenReportDto extends Model<StGreenReportDto> {

    private static final long serialVersionUID = -73866668886764289L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
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
     * 作业类型
     */
    @TableField("work_type")
    private String workType;
    
    /**
     * 人员投入
     */
    @TableField("work_num")
    private Integer workNum;
    
    /**
     * 作业单位
     */
    @TableField("work_unit")
    private String workUnit;
    
    /**
     * 负责人
     */
    @TableField("work_manage")
    private String workManage;
    
    /**
     * 负责人联系方式
     */
    @TableField("manage_phone")
    private String managePhone;
    
    /**
     * 具体位置（位置信息）
     */
    @TableField("position")
    private String position;
    
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
     * 累计巡查次数
     */
    @TableField(exist = false)
    private String sumCount;
    /**
     * 所属管辖单位id
     */
    @TableField(exist = false)
    private String unitId;
    /**
     * 所属管辖单位
     */
    @TableField(exist = false)
    private String unitName;
    /**
     *最新作业内容
     */
    @TableField(exist = false)
    private String workContent;

}
