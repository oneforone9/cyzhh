package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 接诉即办理返回实体
 *
 * @author zhy
 * @since 2023-01-04 10:39:21
 */

@Data
public class TRewardDealEsr extends Esr {

    private static final long serialVersionUID = -92357572737309858L;

            
    /**
     * 主键
     */
                private String id;
    
        
    /**
     * 来电时间
     */
                private String callTime;
    
        
    /**
     * 大类
     */
                private String bigKind;
    
        
    /**
     * 来源渠道
     */
                private String source;
    
        
    /**
     * 小类
     */
                private String smallKind;
    
        
    /**
     * 细类
     */
                private String detailKind;
    


}
