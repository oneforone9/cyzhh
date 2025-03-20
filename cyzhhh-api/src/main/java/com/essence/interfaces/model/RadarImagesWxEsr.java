package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 卫星云图返回实体
 * @author BINX
 * @since 2023-04-23 10:16:19
 */
@Data
public class RadarImagesWxEsr extends Esr {

    private static final long serialVersionUID = 518110793055324743L;
    
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
