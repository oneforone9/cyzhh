package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检会议室信息返回实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */

@Data
public class XjHysxxEsr extends Esr {

    private static final long serialVersionUID = 398737543408850635L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 会议室名称
     */
        private String mc;
    
    
    /**
     * 生成时间
     */
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    /**
     * 经度
     */
    private Double lgtd;


    /**
     * 纬度
     */
    private Double lttd;


}
