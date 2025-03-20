package com.essence.service.impl.alg;

import lombok.Data;

/**
 * @author majunjie
 **/
@Data
public class ModelProceeFolder {
    /**
     * 持续时间秒
     */

    private long duration;
    /**
     * 开始日期月日年
     */

    private String startDate;
    /**
     * 开始时间时分秒
     */

    private String startTime;
    /**
     * 结束日期月日年
     */

    private String endDate;
    /**
     * 结束时间时分秒
     */

    private String endTime;
}
