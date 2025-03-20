package com.essence.interfaces.param;

import com.essence.interfaces.entity.Criterion;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 雷达回波图  --单站雷达-大兴的参数实体
 *
 * @author BINX
 * @since 2023-04-23 10:18:38
 */
@Data
public class RadarImagesLddzEsp extends Esp {

    private static final long serialVersionUID = -19103659725045679L;

    /**
     * 页码 默认 1
     * @mock 1
     */
    private int currentPage = 1;

    /**
     * 个数 默认 10
     * @mock 10
     */
    private int pageSize = 10;

    /**
     * 查询条件
     */
    private List<Criterion> conditions;
    
    /**
     * 排序条件
     */
    private List<Criterion> orders;

    /**
     * 共通查询条件
     * 加载or关键字前，建议后端追加参数使用此集合，不暴露给前端
     * @ignore
     */
    private List<Criterion> currency;
    
    
    /**
     * 数据唯一标识
     */
    @ColumnMean("id")
    private Long id;

    /**
     * 批量操作
     */
    private List<Long> ids;

    /**
     * 图片地址
     */
    @ColumnMean("image_url")
    private String imageUrl;

    /**
     * 图片日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("image_date")
    private Date imageDate;

}
