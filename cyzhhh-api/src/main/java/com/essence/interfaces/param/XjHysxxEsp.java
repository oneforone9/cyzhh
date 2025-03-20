package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检会议室信息参数实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */

@Data
public class XjHysxxEsp extends Esp {

    private static final long serialVersionUID = -67638627057874113L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 会议室名称
     */        @ColumnMean("mc")
    private String mc;
    /**
     * 生成时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;

    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private Double lttd;
}
