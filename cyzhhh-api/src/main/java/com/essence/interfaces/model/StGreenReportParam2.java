package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 绿化保洁工作日志上报表更新实体
 *
 * @author liwy
 * @since 2023-03-14 15:36:46
 */

@Data
public class StGreenReportParam2 extends Esu {

    private static final long serialVersionUID = 536394831120020646L;

    /**
     * 所属河流id
     */
    private String stBRiverId;

    /**
     * 作业类型 (1巡查 2 保洁 3 绿化 4维保 5运行)
     */
    private String workType;

    /**
     * 作业单位
     */
    private String workUnit;

    /**
     * 作业开始时间
     */
    private String startTime;

    /**
     * 作业结束时间
     */
    private String endTime;




}
