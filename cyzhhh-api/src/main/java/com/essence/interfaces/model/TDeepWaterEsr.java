package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 地下水埋深返回实体
 *
 * @author zhy
 * @since 2023-01-04 14:46:15
 */

@Data
public class TDeepWaterEsr extends Esr {

    private static final long serialVersionUID = -44244857498834374L;

            
    /**
     * 主键
     */
                private String id;
    
        
    /**
     * 年
     */
                private String year;
    
        
    /**
     * 埋 信息
     */
                private String deepInfo;
    /**
     * 深度
     */
    private String deep;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 1 年度 2 月度
     */
    private String type;

}
