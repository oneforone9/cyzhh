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
 * 清水的河 - 实时游客表实体
 *
 * @author BINX
 * @since 2023-02-28 13:37:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_crowd_real")
public class StCrowdRealDto extends Model<StCrowdRealDto> {

    private static final long serialVersionUID = 599433342434688874L;

    /**
     * 主见
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 热点区域
     */
    @TableField("area")
    private String area;

    /**
     * 河流名称
     */
    @TableField("rvnm")
    private String rvnm;

    /**
     * 水管单位
     */
    @TableField("water_unit")
    private String waterUnit;

    /**
     * 人数
     */
    @TableField("num")
    private Integer num;

    /**
     * 时间 :yyyy-MM-dd HH:mm
     */
    @TableField("date")
    private String date;

    /**
     * 热点区域绑定id
     */
    @TableField(exist = false)
    private Integer bindId;

}
