package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-02-20 14:33:10
 */

@Data
public class StRainDateEsu extends Esu {

    private static final long serialVersionUID = 217725583657152328L;

                /**
     * 主键
     */            private String id;
        
            /**
     * 时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date date;
        
            /**
     * 雨量站id
     */            private String stationId;
        
            /**
     * 小时雨量
     */            private String hhRain;
        


}
