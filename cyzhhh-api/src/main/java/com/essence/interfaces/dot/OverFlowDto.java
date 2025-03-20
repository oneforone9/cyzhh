package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 溢流断面信息实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/5 9:47
 */
@Data
public class OverFlowDto {

    /**
     * 断面名称
     */
    String sectionName;

    /**
     * 预报最高水位
     */
    BigDecimal highest;

    /**
     * 溢流开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date startTime;

    /**
     * 溢流时长 (分钟)
     */
    long overFlowTime;

}
