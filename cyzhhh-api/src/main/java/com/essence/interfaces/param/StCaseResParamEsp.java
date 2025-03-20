package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
* 方案执行结果表入参表 参数实体
*
* @authorBINX
* @since 2023年4月28日 下午5:45:25
*/
@Data
public class StCaseResParamEsp extends Esp {

    private static final long serialVersionUID = 444639733069995443L;
    
    /**
    * 主键
    */
    @ColumnMean("id")
    private String id;
    
    /**
    * 测站名称：测站编码所代表测站的中文名称
    */
    @ColumnMean("stnm")
    private String stnm;
    
    /**
    * 泵站数量
    */
    @ColumnMean("holes_number")
    private String holesNumber;
    
    /**
    * 装机流量(m³/s）
    */
    @ColumnMean("design_flow")
    private String designFlow;
    
    /**
    * 断面名称
    */
    @ColumnMean("section_name")
    private String sectionName;
    
    /**
    * 下游断面名称
    */
    @ColumnMean("section_name_down")
    private String sectionNameDown;
    
    /**
    * 序列名称
    */
    @ColumnMean("seria_name")
    private String seriaName;
    
    /**
    * case_id
    */
    @ColumnMean("case_id")
    private String caseId;
    
    /**
    * 起液位
    */
    @ColumnMean("liquid_level")
    private String liquidLevel;
    
    /**
    * 停液位
    */
    @ColumnMean("stop_level")
    private String stopLevel;
    
    /**
    * 写入时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("create_time")
    private Date createTime;
    /**
     * 1 固定值 2 策略控制
     */
    @ColumnMean("control_type")
    private int controlType;
    /**
     * 1 开 2 关
     */
    @ColumnMean("control_value")
    private String controlValue;

}
