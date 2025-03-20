package com.essence.interfaces.model;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.essence.interfaces.entity.Esr;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 雷达回波图  --华北地区的返回实体
 * @author BINX
 * @since 2023-04-23 10:18:16
 */
@Data
public class RadarImagesLdEsr extends Esr {

    private static final long serialVersionUID = 748476129216399267L;
    
    /**
     * 数据唯一标识
     */
    private String id;

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
