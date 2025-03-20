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
 * 水位站和流量站测站最新数据实体
 *
 * @author BINX
 * @since 2023-06-08 16:42:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_rate_latest")
public class StWaterRateLatestDto extends Model<StWaterRateLatestDto> {

    private static final long serialVersionUID = 921126338029344111L;
    
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * PLCID
     */
    @TableField("pid")
    private String pid;
    
    /**
     * “0” 为模拟量  “1” 为遥信号
     */
    @TableField("type")
    private String type;
    
    /**
     * 采集名称
     */
    @TableField("addr")
    private String addr;
    
    /**
     * 采集数据值
     */
    @TableField("addrv")
    private String addrv;
    
    /**
     * 采集时间
     */
    @TableField("ctime")
    private String ctime;
    
    /**
     * SN码 需要转换一下
     */
    @TableField("did")
    private String did;
    
    /**
     * 瞬时流量
     */
    @TableField("moment_rate")
    private String momentRate;
    
    /**
     * 当前瞬时流量
     */
    @TableField("pre_moment_rate")
    private String preMomentRate;
    
    /**
     * 瞬时河道水深
     */
    @TableField("moment_river_position")
    private String momentRiverPosition;
    
    /**
     * 电压
     */
    @TableField("voltage")
    private String voltage;

}
