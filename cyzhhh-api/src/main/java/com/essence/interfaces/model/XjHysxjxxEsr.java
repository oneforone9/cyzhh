package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 设备巡检会议室关联巡检信息返回实体
 *
 * @author majunjie
 * @since 2025-01-09 14:00:54
 */

@Data
public class XjHysxjxxEsr extends Esr {

    private static final long serialVersionUID = 146336353267764155L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 会议室id
     */
        private String hysId;
    
    
    /**
     * 会议室巡检项目
     */
        private String hysXjxm;
    
    
    /**
     * 排序
     */
        private Integer px;
    


}
