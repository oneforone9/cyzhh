package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 清水的河 - 用水人数量返回实体
 *
 * @author zhy
 * @since 2023-01-12 17:36:47
 */

@Data
public class StCrowdDateEsr extends Esr {

    private static final long serialVersionUID = 780149439432449907L;

            
    /**
     * 主见
     */
                private String id;
    
        
    /**
     * 热点区域
     */
                private String area;
    
        
    /**
     * 河流名称
     */
                private String rvnm;
    
        
    /**
     * 水管单位
     */
                private String waterUnit;
    
        
    /**
     * 人数
     */
                private Integer num;
    
        
    /**
     * 时间 yyyy-mm-dd
     */
                private String date;
    


}
