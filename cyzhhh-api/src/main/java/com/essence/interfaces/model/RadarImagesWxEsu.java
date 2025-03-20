package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 卫星云图更新实体
 *
 * @author BINX
 * @since 2023-04-23 10:16:17
 */
@Data
public class RadarImagesWxEsu extends Esu {

    private static final long serialVersionUID = 225880179698271585L;
    
    /**
     * 数据唯一标识
     */
    private Long id;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 图片日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date imageDate;

}
