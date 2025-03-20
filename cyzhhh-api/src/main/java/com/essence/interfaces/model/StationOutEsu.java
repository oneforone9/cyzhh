package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 水位流量站(对外)更新实体
 *
 * @author BINX
 * @since 2023-05-11 10:32:17
 */
@Data
public class StationOutEsu extends Esu {

    private static final long serialVersionUID = 546744840085011323L;

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
