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
 * 实体
 *
 * @author BINX
 * @since 2023-04-14 11:36:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_pump_data")
public class StPumpDataDto extends Model<StPumpDataDto> {

    private static final long serialVersionUID = -88438609134185663L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 设备物理地址
     */
    @TableField("device_addr")
    private String deviceAddr;
    
    /**
     * 泵站1 反馈
     */
    @TableField("p1_feed_back")
    private String p1FeedBack;
    
    /**
     * 泵站2 反馈
     */
    @TableField("p2_feed_back")
    private String p2FeedBack;
    
    /**
     * 流量
     */
    @TableField("flow_rate")
    private String flowRate;
    
    /**
     * 压力
     */
    @TableField("pressure")
    private String pressure;
    
    /**
     * 液位
     */
    @TableField("y_position")
    private String yPosition;
    
    /**
     * 泵站1 计时
     */
    @TableField("p1_count_time")
    private String p1CountTime;
    
    /**
     * 泵2 计时
     */
    @TableField("p2_count_time")
    private String p2CountTime;
    
    /**
     * A相电压
     */
    @TableField("a_voltage")
    private String aVoltage;
    
    /**
     * B相电压
     */
    @TableField("b_voltage")
    private String bVoltage;
    
    /**
     *  C相电压
     */
    @TableField("c_voltage")
    private String cVoltage;
    
    /**
     * 电流A
     */
    @TableField("a_electric")
    private String aElectric;
    
    /**
     * 电流B
     */
    @TableField("b_electric")
    private String bElectric;
    
    /**
     * 电流C
     */
    @TableField("c_electric")
    private String cElectric;
    
    /**
     * 电能
     */
    @TableField("electric")
    private String electric;
    
    /**
     * 累计流量
     */
    @TableField("add_flow_rate")
    private String addFlowRate;
    
    /**
     * 液位 高
     */
    @TableField("liquid_high")
    private String liquidHigh;
    
    /**
     * 液位 低
     */
    @TableField("liquid_low")
    private String liquidLow;
    
    /**
     * 泵1 远程
     */
    @TableField("p1_remote")
    private String p1Remote;
    
    /**
     * 泵1 运行
     */
    @TableField("p1_run")
    private String p1Run;
    
    /**
     * 泵1 故障
     */
    @TableField("p1_hitch")
    private String p1Hitch;
    
    /**
     * 泵2 远程
     */
    @TableField("p2_remote")
    private String p2Remote;
    
    /**
     * 泵2 运行
     */
    @TableField("p2_run")
    private String p2Run;
    
    /**
     * 泵2 故障
     */
    @TableField("p2_hitch")
    private String p2Hitch;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("date")
    private Date date;

}
