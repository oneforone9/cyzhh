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
 * 实体
 *
 * @author BINX
 * @since 2023-04-25 11:37:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "river_gate_max_flow_view")
public class RiverGateMaxFlowViewDto extends Model<RiverGateMaxFlowViewDto> {

    private static final long serialVersionUID = 698454444212138631L;

    /**
     * 主键
     */
    @TableField("id")
    private Integer id;

    /**
     * 河道名称
     */
    @TableField("river_name")
    private String riverName;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @TableField("stnm")
    private String stnm;

    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;

    /**
     * 案件id
     */
    @TableField("case_id")
    private String caseId;

    /**
     * 洪峰流量
     */
    @TableField("max_q")
    private BigDecimal maxQ;

    /**
     * 洪峰时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
}
