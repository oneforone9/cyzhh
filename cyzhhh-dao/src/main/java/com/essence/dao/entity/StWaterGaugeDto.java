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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子水尺积水台账
 *
 * @author liwy
 * @since 2023-05-11 18:39:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_gauge")
public class StWaterGaugeDto extends Model<StWaterGaugeDto> {

    private static final long serialVersionUID = -61859137567089756L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 水务感知码（32位）
     */
    @TableField("water_code")
    private String waterCode;

    /**
     * 站点名称
     */
    @TableField("name")
    private String name;
    
    /**
     * 测点名称
     */
    @TableField("pond_name")
    private String pondName;
    
    /**
     * 积水类别
     */
    @TableField("pond_type")
    private String pondType;
    
    /**
     * 运行状态（良好/待改造/报废）
     */
    @TableField("run_state")
    private String runState;
    
    /**
     * 感知对象名称（注：水利工程或水利设施的名称）
     */
    @TableField("object_name")
    private String objectName;
    
    /**
     * 感知位置（注：测站相对感知对象的位置，如xx闸上游）
     */
    @TableField("location")
    private String location;
    
    /**
     * 建设时间
     */
    @TableField("build_time")
    private String buildTime;
    
    /**
     * 站点地址
     */
    @TableField("site_address")
    private String siteAddress;
    
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
     * 发生时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurTime;

    /**
     * 报警状态
     */
    @TableField(exist = false)
    private String warnstatus;


    /**
     * 积水深度
     */
    @TableField(exist = false)
    private BigDecimal depthWater;

    /**
     * 积水深度-历史最大
     */
    @TableField(exist = false)
    private BigDecimal depthWaterMax;



    /**
     * 测量方式
     */
    @TableField("guage_way")
    private String guageWay;

    /**
     * 信息传输方式
     */
    @TableField("info_method")
    private String infoMethod;

    /**
     * 管理单位名称
     */
    @TableField("manage_unit")
    private String manageUnit;

    /**
     * 管理人员
     */
    @TableField("manage_name")
    private String manageName;

    /**
     * 联系方式
     */
    @TableField("manage_number")
    private String manageNumber;


}
