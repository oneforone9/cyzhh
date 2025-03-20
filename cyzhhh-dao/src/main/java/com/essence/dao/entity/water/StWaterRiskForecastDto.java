package com.essence.dao.entity.water;

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
* 水系联调-模型风险预报
*
* @authorBINX
* @since 2023年5月11日 下午4:00:24
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_risk_forecast")
public class StWaterRiskForecastDto extends Model<StWaterRiskForecastDto> {

    private static final long serialVersionUID = 594542939525990062L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
    * 测站id
    */
    @TableField("stcd")
    private String stcd;

    /**
    * 类型
    */
    @TableField("type")
    private String type;

    /**
    * 时间
    */
    @TableField("time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

}
