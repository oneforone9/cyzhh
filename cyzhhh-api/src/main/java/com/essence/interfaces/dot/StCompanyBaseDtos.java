package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class StCompanyBaseDtos implements Serializable {


    /**
     * 标记  1- 查询绿化保洁的   2-查询运行维保的
     */
    private String flag;

    /**
     * 时间 yyyy-MM-dd
     */
    private String date;
    /**
     * 1 年  2 月  3 日 4周
     */
    private Integer dateType;
    /**
     * 单位 id
     */
    private String unitId;
    /**
     * 巡河人员名称
     */
    private String userName;

    /**
     * 公司名称
     */
    private String company;

    private Integer current;

    private Integer size;
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
}
