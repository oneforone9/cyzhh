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
 * 水系联调-闸坝模型风险预报实体
 *
 * @author BINX
 * @since 2023-06-20 14:29:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_risk_forecast_gate")
public class StWaterRiskForecastGateDto extends Model<StWaterRiskForecastGateDto> {

    private static final long serialVersionUID = 766403226055658314L;
    
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 案件id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 测站id
     */
    @TableField("stcd")
    private String stcd;
    
    /**
     * 测站名称
     */
    @TableField("stnm")
    private String stnm;
    
    /**
     * 风险类型
     */
    @TableField("risk")
    private String risk;
    
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;

    /**
     * 风险类型
     */
    @TableField("sttp")
    private String sttp;

}
