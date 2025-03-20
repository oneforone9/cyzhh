package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 水系联调-模型风险预报 返回实体
*
* @authorBINX
* @since 2023年5月11日 下午4:00:24
*/
@Data
public class StWaterRiskForecastEsr extends Esr {

    private static final long serialVersionUID = 594542939525990062L;

    /**
    * id
    */
    private String id;

    /**
    * 测站id
    */
    private String stcd;

    /**
    * 类型
    */
    private String type;

    /**
    * 时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

}
