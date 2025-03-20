package com.essence.interfaces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 *
 * @author majunjie
 * @since 2023-02-20 14:33:10
 */

@Data
public class StRainDateOut {

    /**
     * 雨量站编码
     */
    private String stcd;
    /**
     * 雨量值
     */
    private String rain;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tm;

}
