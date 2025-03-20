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
 * @author BINX
 * @since 2023-03-08 11:32:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_level_rain")
public class StLevelRainDto extends Model<StLevelRainDto> {

    private static final long serialVersionUID = -68400306718070106L;
        
    /**
     * 数据唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 等级标识
     */
    @TableField("level")
    private Integer level;
    
    /**
     * 等级名称
     */
    @TableField("level_name")
    private String levelName;
    
    /**
     * 1小时降雨量下边界(包含>=)
     */
    @TableField("lower1")
    private Double lower1;
    
    /**
     * 1小时降雨量上边界（不包含<）
     */
    @TableField("upper1")
    private Double upper1;
    
    /**
     * 3小时降雨量下边界(包含>=)
     */
    @TableField("lower3")
    private Double lower3;
    
    /**
     * 3小时降雨量上边界（不包含<）
     */
    @TableField("upper3")
    private Double upper3;
    
    /**
     * 12小时降雨量下边界(包含>=)
     */
    @TableField("lower12")
    private Double lower12;
    
    /**
     * 12小时降雨量上边界（不包含<）
     */
    @TableField("upper12")
    private Double upper12;
    
    /**
     * 24小时降雨量下边界(包含>=)
     */
    @TableField("lower24")
    private Double lower24;
    
    /**
     * 24小时降雨量上边界（不包含<）
     */
    @TableField("upper24")
    private Double upper24;
    
    /**
     * 图标路径
     */
    @TableField("file_url")
    private String fileUrl;
    
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
    
    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_update")
    private Date gmtUpdate;
    
    /**
     * 修改者
     */
    @TableField("updater")
    private String updater;

}
