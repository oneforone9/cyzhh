package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 设备巡检会议室关联巡检信息参数实体
 *
 * @author majunjie
 * @since 2025-01-08 08:13:47
 */

@Data
public class XjHysxjxxEsp extends Esp {

    private static final long serialVersionUID = -64102633384112030L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 会议室id
     */        @ColumnMean("hys_id")
    private String hysId;
    /**
     * 会议室巡检项目
     */        @ColumnMean("hys_xjxm")
    private String hysXjxm;
    /**
     * 排序
     */        @ColumnMean("px")
    private Integer px;


}
