package com.essence.interfaces.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class ReaViewStatisticDto implements Serializable {
    /**
     * 河
     */
    private Integer river;
    /**
     * 沟
     */
    private Integer riverG;
    /**
     * 渠
     */
    private Integer riverQ;
    /**
     * 二级河道
     */
    private Integer SecondRiver;
    /**
     * 三级河道
     */
    private Integer ThirdRiver;
    /**
     * 巡河次数
     */
    private Integer portalNum;
    /**
     * 单位id
     */
    private String unitId;
}
