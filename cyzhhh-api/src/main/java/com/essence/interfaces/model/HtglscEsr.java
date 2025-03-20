package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 合同管理收藏返回实体
 *
 * @author majunjie
 * @since 2024-09-13 16:18:31
 */

@Data
public class HtglscEsr extends Esr {

    private static final long serialVersionUID = 597539582520185442L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 合同编号
     */
        private String htid;
    
    
    /**
     * 用户id
     */
        private String userId;
    


}
