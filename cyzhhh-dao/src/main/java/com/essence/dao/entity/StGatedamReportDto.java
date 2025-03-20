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
 * 闸坝运行维保日志上报表实体
 *
 * @author liwy
 * @since 2023-03-15 11:56:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_gatedam_report")
public class StGatedamReportDto extends Model<StGatedamReportDto> {

    private static final long serialVersionUID = -72115128796788147L;
        
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
     * 作业类型（ 4养维护修  5运行巡查）
     */
    @TableField("work_type")
    private String workType;

    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */
    @TableField("sttp")
    private String sttp;

    /**
     * 测站编码
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 测站名称(闸坝名称)
     */
    @TableField("stnm")
    private String stnm;
    
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
     * 检查结论
     */
    @TableField("conclusion")
    private String conclusion;

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
    private  String unitName;
    /**
     *最新作业内容
     */
    @TableField(exist = false)
    private String workContent;


}
