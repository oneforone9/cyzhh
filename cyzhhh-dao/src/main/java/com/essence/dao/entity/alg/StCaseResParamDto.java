package com.essence.dao.entity.alg;

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
* 方案执行结果表入参表
*
* @authorBINX
* @since 2023年4月28日 下午5:45:25
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_case_res_param")
public class StCaseResParamDto extends Model<StCaseResParamDto> {

    private static final long serialVersionUID = 444639733069995443L;

    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
    * 测站名称：测站编码所代表测站的中文名称
    */
    @TableField("stnm")
    private String stnm;

    /**
    * 泵站数量
    */
    @TableField("holes_number")
    private String holesNumber;

    /**
    * 装机流量(m³/s）
    */
    @TableField("design_flow")
    private String designFlow;

    /**
    * 断面名称
    */
    @TableField("section_name")
    private String sectionName;

    /**
    * 下游断面名称
    */
    @TableField("section_name_down")
    private String sectionNameDown;

    /**
    * 序列名称
    */
    @TableField("seria_name")
    private String seriaName;

    /**
    * case_id
    */
    @TableField("case_id")
    private String caseId;

    /**
    * 起液位
    */
    @TableField("liquid_level")
    private String liquidLevel;

    /**
    * 停液位
    */
    @TableField("stop_level")
    private String stopLevel;

    /**
    * 写入时间
    */
    @TableField("create_time")
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

    private Double jd;

    private Double wd;
}
