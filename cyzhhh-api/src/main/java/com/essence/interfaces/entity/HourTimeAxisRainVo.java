package com.essence.interfaces.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 小时时间轴降雨Vo
 *
 * @author huangyong
 * Create at 2024/7/15
 */
@Data
public class HourTimeAxisRainVo {

    /**
     * 时间轴名称
     */
    private String name;

    /**
     * 时间轴小时
     */
    private String hour;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime date;

    /**
     * 等值面
     */
    private String dzm;

    /**
     * 明细数据
     */
    private List<Detail> detailList;

    @Data
    public static class Detail {

        /**
         * 站点名称
         */
        private String stnm;

        /**
         * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
         */
        private BigDecimal lgtd;

        /**
         * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
         */
        private BigDecimal lttd;

        /**
         * 时间
         */
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private LocalDateTime date;

        /**
         * 雨量
         */
        private Double rain;
    }

}
