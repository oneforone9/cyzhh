package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-03-16 16:41:57
 */

@Data
public class CyWeatherForecastEsr extends Esr {

    private static final long serialVersionUID = -38443452772995588L;

                        private String id;
    
        
    /**
     * 气象发布部门
     */
                private String publishDepartment;
    
        
    /**
     * 发布时间
     */
                private String publishTime;
    
        
    /**
     * 气象类型
     */
                private String weatherType;
    
        
    /**
     * 气象等级
     */
                private String weatherLevel;
    
        
    /**
     * 状态
     */
                private String status;
    
        
    /**
     * 内容
     */
                private String context;
    
        
    /**
     * 防护措施
     */
                private String defence;
    
        
    /**
     * 消息
     */
                private String msg;
    
        
    /**
     * 图标
     */
                private String icon;
    
        
    /**
     * 预警类型
     */
                private String nowWaring;
    


}
