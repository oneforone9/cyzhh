package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-01-05 14:59:50
 */

@Data
public class SectionDataViewEsr extends Esr {

    private static final long serialVersionUID = -35561021139666888L;

                        private Integer id;
    
        
    /**
     * 断面名称
     */
                private String sectionName;
    
        
    /**
     * 断面类型(0 国考 1 市考 2区考)
     */
                private String sectionType;
    
        
    /**
     * 河系id
     */
                private Integer riverId;
    
        
    /**
     * 经度
     */
                private Double lgtd;
    
        
    /**
     * 纬度
     */
                private Double lttd;
    
        
    /**
     * 断面类型
     */
                private String type;
    
                    private String qualityId;
    
        
    /**
     * 断面主键
     */
                private Integer sectionId;
    
        
    /**
     * 水质期间(年月)
     */
                private String qualityPeriod;
    
        
    /**
     * 水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;6 无水/结冰；)
     */
                private Integer qualityLevel;
    
        
    /**
     * 创建者
     */
                private String creator;
    
        
    /**
     * 更新者
     */
                private String updater;
    
        
    /**
     * 备注(水质等级，无水还是结冰)
     */
                private String levelRemark;
    
        
    /**
     * 平均水质等级(1 Ⅰ；2 Ⅱ；3 Ⅲ；4 Ⅳ；5 Ⅴ;)
     */
                private Integer averageLevel;
    
        
    /**
     * 河道名称
     */
                private String reaName;
    


}
