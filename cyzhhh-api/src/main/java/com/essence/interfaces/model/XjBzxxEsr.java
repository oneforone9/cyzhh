package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检班组信息返回实体
 *
 * @author majunjie
 * @since 2025-01-08 08:13:27
 */

@Data
public class XjBzxxEsr extends Esr {

    private static final long serialVersionUID = 429995062054820306L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 班组名称
     */
        private String bzmc;
    
    
    /**
     * 生成时间
     */
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    
    
    /**
     * 部门id
     */
        private String bmid;
    
    
    /**
     * 会议室名称
     */
        private String hyName;
    
    
    /**
     * 会议室id
     */
        private String hyId;
    


}
