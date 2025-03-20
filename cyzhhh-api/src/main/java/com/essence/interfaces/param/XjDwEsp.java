package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检单位参数实体
 *
 * @author majunjie
 * @since 2025-01-09 08:56:31
 */

@Data
public class XjDwEsp extends Esp {

    private static final long serialVersionUID = 512139289882871374L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 单位名称
     */        @ColumnMean("dwmc")
    private String dwmc;
    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;


}
