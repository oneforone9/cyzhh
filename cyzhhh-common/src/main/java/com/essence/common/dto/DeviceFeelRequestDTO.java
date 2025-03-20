package com.essence.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
public class DeviceFeelRequestDTO implements Serializable {
    /**
     *  ZZ  ZQ  VIDEO
     */
    private String type;
    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    /**
     * 1 年  2 月  3 日
     */
    private String dateType;
    /**
     * 测站id
     */
    private String stcd;
    /**
     * 日期字符串
     */
    private String dateStr;
    /**
     *  时间排序标记 desc为倒序 其他值为正序
     */
    private String flag;
}
