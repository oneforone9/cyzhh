package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-04-22 10:55:04
 */

@Data
public class StForecastSectionEsp extends Esp {

    private static final long serialVersionUID = 368090548529174016L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 类型 ZZ 水位站 ZQ流量站
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 录入时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("date")
    private Date date;
    /**
     * 站点名称
     */
    @ColumnMean("站点断面名称")
    private String sectionName;
    /**
     * 站点数据值
     */
    @ColumnMean("value")
    private String value;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("update_time")
    private Date updateTime;

    /**
     * 方案id
     */
    @ColumnMean("case_id")
    private String caseId;
    /**
     * 流量1水位2
     */
    @ColumnMean("type")
    private String type;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ColumnMean("wd")
    private BigDecimal wd;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @ColumnMean("jd")
    private BigDecimal jd;
}
