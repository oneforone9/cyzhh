package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 河系分配表返回实体
 *
 * @author majunjie
 * @since 2025-01-09 09:08:05
 */

@Data
public class StBRiverEsr extends Esr {

    private static final long serialVersionUID = 293064757068833050L;

                private Integer id;
    
    
    /**
     * 河系名称
     */
        private String riverName;
    
    
    /**
     * 管护单位
     */
        private String managementUnit;
    
    
    /**
     * 长度（km）
     */
        private Double selfLength;
    
    
    /**
     * 平均宽度（m）
     */
        private Double averageWidth;
    
    
    /**
     * 河流类型
     */
        private String riverType;
    
    
    /**
     * 备注：用于记载该条记录的一些描述性的文字，最长不超过100个汉字。
     */
        private String comments;
    


}
