package com.essence.interfaces.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.essence.interfaces.entity.Esu;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 雷达回波图  --单站雷达-大兴的更新实体
 *
 * @author BINX
 * @since 2023-04-23 10:18:39
 */
@Data
public class RadarImagesLddzEsu extends Esu {

    private static final long serialVersionUID = -98728723206194776L;
    
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
