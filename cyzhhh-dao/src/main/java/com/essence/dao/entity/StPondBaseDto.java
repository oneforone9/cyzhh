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
 * 易积滞水点基础表实体
 *
 * @author liwy
 * @since 2023-04-03 14:45:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_pond_base")
public class StPondBaseDto extends Model<StPondBaseDto> {

    private static final long serialVersionUID = 193951016231206705L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 积水点名称
     */
    @TableField("pond_name")
    private String pondName;
    
    /**
     * 积水类别
     */
    @TableField("pond_type")
    private String pondType;
    
    /**
     * 积水面积(㎡)
     */
    @TableField("pond_area")
    private String pondArea;
    
    /**
     * 积水面积(㎡)
     */
    @TableField("pond_depth")
    private String pondDepth;
    
    /**
     * 消除时间（h）
     */
    @TableField("remove_time")
    private String removeTime;
    
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @TableField("lgtd")
    private Double lgtd;
    
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @TableField("lttd")
    private Double lttd;
    
    /**
     * 所在区域
     */
    @TableField("area")
    private String area;
    
    /**
     * 措施类别
     */
    @TableField("type")
    private String type;
    
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
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 抢险队负责人
     */
    @TableField("qxdfzr")
    private String qxdfzr;
    /**
     *抢险队负责人电话
     */
    @TableField("qxdfzrdh")
    private String qxdfzrdh;

    /**
     *责任单位1
     */
    @TableField("zrdw1")
    private String zrdw1;
    /**
     *责任单位负责人1
     */
    @TableField("zrdwfzr1")
    private String zrdwfzr1;
    /**
     * 责任单位负责人电话1
     */
    @TableField("zrdwdh1")
    private String zrdwdh1;
    /**
     * 责任单位2
     */
    @TableField("zrdw2")
    private String zrdw2;

    /**
     * 责任单位负责人2
     */
    @TableField("zrdwfzr2")
    private String zrdwfzr2;
    /**
     * 责任单位负责人电话2
     */
    @TableField("zrdwdh2")
    private String zrdwdh2;
    /**
     * 责任单位3
     */
    @TableField("zrdw3")
    private String zrdw3;
    /**
     * 责任单位负责人3
     */
    @TableField("zrdwfzr3")
    private String zrdwfzr3;

    /**
     * 责任单位负责人电话3
     */
    @TableField("zrdwdh3")
    private String zrdwdh3;

}
