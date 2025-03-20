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
 * 积水等级
 *
 * @author BINX
 * @since 2023-03-08 11:32:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_level_water")
public class StLevelWaterDto extends Model<StLevelWaterDto> {

    private static final long serialVersionUID = -82610904574112779L;
        
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
     * 积水深度下边界(包含>=)
     */
    @TableField("lower")
    private Double lower;
    
    /**
     * 积水深度上边界（不包含<）
     */
    @TableField("upper")
    private Double upper;
    
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
