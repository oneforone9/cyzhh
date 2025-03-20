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
 * 闸坝计划排班信息表实体
 *
 * @author liwy
 * @since 2023-07-13 14:40:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_plan_info")
public class StPlanInfoDto extends Model<StPlanInfoDto> {

    private static final long serialVersionUID = 988065568413625005L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 边闸表的主键
     */
    @TableField("side_gate_id")
    private Integer sideGateId;
    
    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 设备设施名称
     */
    @TableField("equipment_name")
    private String equipmentName;
    
    /**
     * 日常维护内容
     */
    @TableField("service_content")
    private String serviceContent;
    
    /**
     * 日常维护频次
     */
    @TableField("ycwhpc")
    private String ycwhpc;
    
    /**
     * 河系id
     */
    @TableField("river_id")
    private Integer riverId;

    /**
     * 河道名称
     */
    @TableField("river_name")
    private String riverName;
    
    /**
     * 第三方服务公司主键id
     */
    @TableField("company_id")
    private String companyId;
    
    /**
     * 公司名称
     */
    @TableField("company")
    private String company;
    
    /**
     * 负责人id
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 负责人
     */
    @TableField("name")
    private String name;
    
    /**
     * 负责人手机号
     */
    @TableField("phone")
    private String phone;
    
    /**
     * 维护日期类型 1-年季度月  2-月/周
     */
    @TableField("type")
    private String type;
    
    /**
     * 维护日期排班表 （1type   例如0406-0419,0420-0430,0510-0515        2type  05,08,10 ）
     */
    @TableField("wh_time")
    private String whTime;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 所属单位主键
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 所属单位名称
     */
    @TableField("unit_name")
    private String unitName;

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

}
