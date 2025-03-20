package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 设备巡检会议室巡检计划返回实体
 *
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */

@Data
public class XjHysJhEsr extends Esr {

    private static final long serialVersionUID = 610278202677779720L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 巡检内容
     */
        private String xjnr;


    /**
     * 班组名称
     */
    private String bzmc;
    /**
     * 负责人
     */
        private String fzr;
    
    
    /**
     * 负责人id
     */
        private String fzrId;
    
    
    /**
     * 巡检日期巡检日期(1,2,3,4,5,6,7)
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
    


}
