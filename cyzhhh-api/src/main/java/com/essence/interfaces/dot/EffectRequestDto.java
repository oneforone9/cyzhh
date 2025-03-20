package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class EffectRequestDto implements Serializable {
    /**
     * 时间 yyyy-MM-dd
     */
    private String date;
    /**
     * 1 年  2 月  3 日 4周
     */
    private Integer dateType;
    /**
     * 单位 名称
     */
    private String unitName;
    /**
     * 巡河人员名称
     */
    private String userName;

    private Integer current;

    private Integer size;
    /**
     * 分管河道
     */
    private String reaId;
    /**
     * 周维度开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    /**
     * 周维度结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 事件类型
     */
    private String eventType;
}
