package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 方案执行结果表入参表 返回实体
*
* @authorBINX
* @since 2023年4月28日 下午5:45:25
*/
@Data
public class StCaseResParamEsr extends Esr {

    private static final long serialVersionUID = 444639733069995443L;

    /**
    * 主键
    */
    private String id;

    /**
    * 测站名称：测站编码所代表测站的中文名称
    */
    private String stnm;

    /**
    * 泵站数量
    */
    private String holesNumber;

    /**
    * 装机流量(m³/s）
    */
    private String designFlow;

    /**
    * 断面名称
    */
    private String sectionName;

    /**
    * 下游断面名称
    */
    private String sectionNameDown;

    /**
    * 序列名称
    */
    private String seriaName;

    /**
    * case_id
    */
    private String caseId;

    /**
    * 起液位
    */
    private String liquidLevel;

    /**
    * 停液位
    */
    private String stopLevel;

    /**
    * 写入时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 1 固定值 2 策略控制
     */
    private int controlType;
    /**
     * 1 开 2 关
     */
    private String controlValue;
}
