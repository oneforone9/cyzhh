package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检班组信息参数实体
 *
 * @author majunjie
 * @since 2025-01-08 08:13:26
 */

@Data
public class XjBzxxEsp extends Esp {

    private static final long serialVersionUID = 970028500022968063L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 班组名称
     */        @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 生成时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;
    /**
     * 部门id
     */        @ColumnMean("bmid")
    private String bmid;
    /**
     * 会议室名称
     */        @ColumnMean("hy_name")
    private String hyName;
    /**
     * 会议室id
     */        @ColumnMean("hy_id")
    private String hyId;


}
