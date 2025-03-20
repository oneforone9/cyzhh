package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 水位流量站(对外)返回实体
 * @author BINX
 * @since 2023-05-11 10:32:18
 */
@Data
public class StationOutEsr extends Esr {

    private static final long serialVersionUID = -80584028344680844L;

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 监测点描述
     */
    private String monitor;

    /**
     * 所属河道id
     */
    private Integer riverId;
}
