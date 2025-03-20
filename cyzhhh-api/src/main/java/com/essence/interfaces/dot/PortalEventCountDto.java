package com.essence.interfaces.dot;

import lombok.Data;

@Data
public class PortalEventCountDto {
    /**
     * 河道id
     */
    private String reaId;
    /**
     * 河道名称
     */
    private String reaName;
    /**
     * geom
     */
    private String geom;
    /**
     * 数量
     */
    private Integer num;
}
