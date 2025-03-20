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
 * 卫星云图实体
 *
 * @author BINX
 * @since 2023-04-23 10:14:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "radar_images_wx")
public class RadarImagesWxDto extends Model<RadarImagesWxDto> {

    private static final long serialVersionUID = -89459302375116278L;
    
    /**
     * 数据唯一标识
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
        
    /**
     * 图片地址
     */
    @TableField("image_url")
    private String imageUrl;
    
    /**
     * 图片日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("image_date")
    private Date imageDate;

}
