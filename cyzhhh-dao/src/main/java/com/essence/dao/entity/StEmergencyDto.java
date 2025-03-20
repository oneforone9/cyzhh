package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抢险队基本信息表实体
 *
 * @author liwy
 * @since 2023-04-13 16:42:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_emergency")
public class StEmergencyDto extends Model<StEmergencyDto> {

    private static final long serialVersionUID = 284505841807886331L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 抢险队名称
     */
    @TableField("emergency_name")
    private String emergencyName;

    /**
     * 负责人
     */
    @TableField("fzr")
    private String fzr;

    /**
     * 联系方式
     */
    @TableField("fzrr_phone")
    private String fzrrPhone;

    /**
     * 管护河道
     */
    @TableField("manage_river")
    private String manageRiver;

    /**
     * 负责类型
     */
    @TableField("type")
    private String type;

    /**
     * 规模
     */
    @TableField("scope")
    private String scope;

    /**
     * 单位id
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 管理单位
     */
    @TableField("unit_name")
    private String unitName;

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

}
