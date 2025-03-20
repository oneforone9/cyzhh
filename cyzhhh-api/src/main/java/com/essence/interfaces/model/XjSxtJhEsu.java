package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划更新实体
 *
 * @author majunjie
 * @since 2025-01-08 22:46:38
 */

@Data
public class XjSxtJhEsu extends Esu {

    private static final long serialVersionUID = 588377078881178055L;

    /**
     * 主键
     */        private String id;    

    /**
     * 摄像头id
     */        private Integer sxtId;    

    /**
     * 巡检内容
     */        private String xjnr;    

    /**
     * 负责人
     */        private String fzr;    

    /**
     * 负责人id
     */        private String fzrId;    

    /**
     * 巡检日期
     */        private String xjRq;    

    /**
     * 巡检单位id
     */        private String xjdwId;    

    /**
     * 班组id
     */        private String bzId;

    /**
     * 班组名称
     */
    private String bzmc;

}
