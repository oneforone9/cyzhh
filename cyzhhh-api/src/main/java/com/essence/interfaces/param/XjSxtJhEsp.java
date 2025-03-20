package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划参数实体
 *
 * @author majunjie
 * @since 2025-01-08 22:46:39
 */

@Data
public class XjSxtJhEsp extends Esp {

    private static final long serialVersionUID = 893306461020900248L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 摄像头id
     */        @ColumnMean("sxt_id")
    private Integer sxtId;
    /**
     * 巡检内容
     */        @ColumnMean("xjnr")
    private String xjnr;
    /**
     * 班组名称
     */  @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 负责人
     */        @ColumnMean("fzr")
    private String fzr;
    /**
     * 负责人id
     */        @ColumnMean("fzr_id")
    private String fzrId;
    /**
     * 巡检日期
     */        @ColumnMean("xj_rq")
    private String xjRq;
    /**
     * 巡检单位id
     */        @ColumnMean("xjdw_id")
    private String xjdwId;
    /**
     * 班组id
     */        @ColumnMean("bz_id")
    private String bzId;


}
