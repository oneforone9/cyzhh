package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检流程参数实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */

@Data
public class XjrwlcEsp extends Esp {

    private static final long serialVersionUID = -25263170629156922L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 任务id
     */        @ColumnMean("rw_id")
    private String rwId;
    /**
     * 描述工单状态
     */        @ColumnMean("ms")
    private String ms;
    /**
     * 操作人
     */        @ColumnMean("czr")
    private String czr;
    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("cjsj")
    private Date cjsj;


}
