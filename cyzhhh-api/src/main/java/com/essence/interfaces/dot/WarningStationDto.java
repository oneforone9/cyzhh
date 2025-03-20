package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警站点列表
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 16:36
 */
@Data
public class WarningStationDto {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 状态(正常/超警戒/溢流)
     */
    private String state;

    /**
     * 所在河系
     */
    private String riverSystem;

    /**
     * 溢流时间 (暂定第一次溢流时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

}
