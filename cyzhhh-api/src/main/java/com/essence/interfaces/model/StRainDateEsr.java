package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-02-20 14:33:11
 */

@Data
public class StRainDateEsr extends Esr {

    private static final long serialVersionUID = 622466803388455123L;

            
    /**
     * 主键
     */
                private String id;
    
        
    /**
     * 时间
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date date;
    
        
    /**
     * 雨量站id
     */
                private String stationId;
    
        
    /**
     * 小时雨量
     */
                private String hhRain;
    


}
