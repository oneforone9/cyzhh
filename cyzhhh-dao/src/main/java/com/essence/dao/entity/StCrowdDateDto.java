package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 清水的河 - 用水人数量实体
 *
 * @author BINX
 * @since 2023-01-12 17:36:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_crowd_date")
public class StCrowdDateDto extends Model<StCrowdDateDto> {

    private static final long serialVersionUID = -98076042264154593L;


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
     * 时间 yyyy-mm-dd
     */
    @TableField("date")
    private String date;

    /**
     * 热点区域绑定id
     */
    @TableField(exist = false)
    private Integer bindId;

}
