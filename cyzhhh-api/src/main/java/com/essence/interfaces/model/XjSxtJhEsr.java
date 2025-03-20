package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划返回实体
 *
 * @author majunjie
 * @since 2025-01-08 22:46:39
 */

@Data
public class XjSxtJhEsr extends Esr {

    private static final long serialVersionUID = -51040206659829624L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 摄像头id
     */
        private Integer sxtId;
    
    
    /**
     * 巡检内容
     */
        private String xjnr;
    
    
    /**
     * 负责人
     */
        private String fzr;
    
    
    /**
     * 负责人id
     */
        private String fzrId;
    
    
    /**
     * 巡检日期
     */
        private String xjRq;
    
    
    /**
     * 巡检单位id
     */
        private String xjdwId;
    
    
    /**
     * 班组id
     */
        private String bzId;

    /**
     * 班组名称
     */
    private String bzmc;

}
