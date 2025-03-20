package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 用水量返回实体
 *
 * @author zhy
 * @since 2023-01-04 17:18:05
 */

@Data
public class UseWaterEsr extends Esr {

    private static final long serialVersionUID = 513351870961721766L;

                        private String id;
    
        
    /**
     * 日期
     */
                private String date;
    
        
    /**
     * 用水量
     */
                private String useInfo;
    /**
     * 更新时间
     */
    private String updateTime;
    


}
