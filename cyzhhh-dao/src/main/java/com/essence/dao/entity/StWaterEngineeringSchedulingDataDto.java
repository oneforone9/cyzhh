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
 * 防汛调度方案指令下发记录实体
 *
 * @author majunjie
 * @since 2023-05-14 18:15:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_engineering_scheduling_data")
public class StWaterEngineeringSchedulingDataDto extends Model<StWaterEngineeringSchedulingDataDto> {

    private static final long serialVersionUID = 292060852443861893L;
        
    /**
     * 主键
     */
    @TableId(value = "did", type = IdType.INPUT)
    private String did;
        
    /**
     * 调度主键
     */
    @TableField("st_id")
    private String stId;
    
    /**
     * 闸名称
     */
    @TableField("zbmc")
    private String zbmc;
    
    /**
     * 描述
     */
    @TableField("reason")
    private String reason;

    /**
     * 状态1指令下发2接收3完成
     */
    @TableField("forecast_state")
    private Integer forecastState;
    
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;
    /**
     * 现场负责人
     */
    @TableField("xcfzr")
    private String xcfzr;
    /**
     * 所属单位 id
     */
    @TableField("unit_id")
    private String unitId;
    /**
     * 调度指令id
     */
    @TableField("code_id")
    private String codeId;
    /**
     * 下发级别0逐级1越级
     */
    @TableField("rank")
    private String rank;

}
