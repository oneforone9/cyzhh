package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 设备巡检会议室巡检计划参数实体
 *
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */

@Data
public class XjHysJhEsp extends Esp {

    private static final long serialVersionUID = 526552958870879396L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
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
     * 巡检日期巡检日期(1,2,3,4,5,6,7)
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
