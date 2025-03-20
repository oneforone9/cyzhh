package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
@Data
public class WorkMarkRequestDto implements Serializable {
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 河流id
     */
    private String reaId;
    /**
     * 时间
     */
    private String time;

    /**
     * personId 人的id
     */
    private String personId;
    /**
     * personName 人名
     */
    private String personName;
    /**
     * 工单状态
     */
    private String orderStatus;
}
