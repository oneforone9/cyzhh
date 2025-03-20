package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
@Data
public class PortalUserCountDto implements Serializable {
    /**
     * 巡河人数
     */
    private Integer portalNum = 0;
    /**
     * 完成人数
     */
    private Integer completeNum = 0;
    /**
     * 进行中数量
     */
    private Integer RunningNum = 0;
    /**
     * 未开始工单
     */
    private Integer unStartNum = 0;
}
