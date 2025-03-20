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
 * @author majunjie
 * @since 2023-05-08 14:26:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_water_dispatch")
public class StWaterDispatchDto extends Model<StWaterDispatchDto> {

    private static final long serialVersionUID = 485313773407203735L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
        
    /**
     * 0正常运行工况1特殊运行工况
     */
    @TableField("type")
    private Integer type;

    /**
     * 河道id
     */
    @TableField("river_id")
    private String riverId;
    /**
     * 河道名称
     */
    @TableField("river_name")
    private String riverName;
    /**
     * 方案id
     */
    @TableField("case_id")
    private String caseId;
    /**
     * 文件路径”,“号拆分
     */
    @TableField("file")
    private String file;
    /**
     * 示意图
     */
    @TableField("files")
    private String files;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_update")
    private Date gmtUpdate;

}
