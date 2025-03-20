package com.essence.interfaces.model;


import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理预审项目返回实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */

@Data
public class HtglysxmEsr extends Esr {

    private static final long serialVersionUID = -20036830523011182L;

        
    /**
     * 主键
     */
        private String id;


    /**
     * 合同主键
     */
    private String htid;
    
    
    /**
     * 审查项目
     */
        private String scxm;
    
    
    /**
     * 具体内容
     */
        private String jtnr;
    
    
    /**
     * 提交时间
     */
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tjsj;
    


}
