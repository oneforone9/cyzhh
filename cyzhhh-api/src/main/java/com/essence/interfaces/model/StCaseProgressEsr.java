package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 方案执行进度表返回实体
 *
 * @author zhy
 * @since 2023-04-18 17:03:07
 */

@Data
public class StCaseProgressEsr extends Esr {

    private static final long serialVersionUID = 774706250702254520L;

                        private String id;
    
        
    /**
     * 方案id
     */
                private String caseId;
    
        
    /**
     * 进度百分比
     */
                private BigDecimal progress;
    
        
    /**
     * 备注
     */
                private String message;
    
        
    /**
     * 新增或者更新
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date updateTime;

    /**
     * 方案状态 1 未开始 2 进行中 3 计算失败 4 计算完成
     */
    private Integer status;
    /**
     * 方案名称
     */
    private String caseName;

    /**
     * 模型类型  1 防汛调度  2 水资源调度
     */
    private Integer modelType;
}
