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
 * 方案执行结果表实体
 *
 * @author BINX
 * @since 2023-04-18 14:39:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_case_res")
public class StCaseResDto extends Model<StCaseResDto> {

    private static final long serialVersionUID = -55463593718064570L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 案件id
     */
    @TableField("case_id")
    private String caseId;
    
    /**
     * 步长结果
     */
    @TableField("step")
    private Integer step;
    
    /**
     * 河流id
     */
    @TableField("rv_id")
    private String rvId;

    
    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;
    
    /**
     * 河道断面水位
     */
    @TableField("river_z")
    private String riverZ;
    
    /**
     * 河道断面水深
     */
    @TableField("river_h")
    private String riverH;
    
    /**
     * 河道断面流量
     */
    @TableField("river_q")
    private String riverQ;
    
    /**
     * 河道断面过水流速

     */
    @TableField("river_v")
    private String riverV;
    
    /**
     * 河道断面过水面积数组

     */
    @TableField("river_a")
    private String riverA;
    
    /**
     * 河道断面过水宽度

     */
    @TableField("river_w")
    private String riverW;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    /**
     * 站点id
     */
    private String stcd;
    /**
     * 站点名称
     */
    private String stnm;
    /**
     * 步长时间
     */
    @TableField("step_time")
    private Date stepTime;
    /**
     * 1 数据库设置类型为 入库不展示 2 数据库设置类型为 入库 展示
     */
    @TableField("data_type")
    private String dataType;
}
