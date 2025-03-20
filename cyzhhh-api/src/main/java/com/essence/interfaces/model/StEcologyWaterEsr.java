package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-02-21 16:34:42
 */

@Data
public class StEcologyWaterEsr extends Esr {

    private static final long serialVersionUID = 587245150348872791L;

                        private Integer id;
    
        
    /**
     * 河道名称
     */
                private String riverName;
    
        
    /**
     * 初始水量
     */
                private Double startCount;
    
        
    /**
     * 工况1(6、7、8月)
     */
                private Double countOne;
    
        
    /**
     * 工况2(3、4、10、11月)
     */
                private Double countTwo;
    
        
    /**
     * 工况3((5、9月)
     */
                private Double countThree;
    
        
    /**
     * 地区（1-北部，2-中部，3-南部）
     */
                private String area;
    
        
    /**
     * 创建者
     */
                private String creator;
    
        
    /**
     * 服务类型或管护河段名称
     */
                private String dataName;
    


}
