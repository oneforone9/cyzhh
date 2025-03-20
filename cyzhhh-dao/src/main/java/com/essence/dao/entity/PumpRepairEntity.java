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
 * 闸坝运行养护记录
 *
 * @author cuirx
 * @since 2022-10-27 15:26:18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_pump_repair")
public class PumpRepairEntity extends Model<PumpRepairEntity> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 作业对象
     */
    private String projectTarget;
    /**
     * 闸坝名称
     */
    private String pumpName;
    /**
     * 问题类型
     */
    private String questionType;
    /**
     * 解决方式
     */
    private String dealMethod;
    /**
     * 更新时间
     */
    private Date updateTime;
}
