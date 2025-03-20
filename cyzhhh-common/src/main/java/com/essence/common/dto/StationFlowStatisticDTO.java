package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StationFlowStatisticDTO implements Serializable {
    /**
     * 流速正常数量
     */
    private Integer normalNum;
    /**
     * 流速缓慢数量
     */
    private Integer unNormalNum;
    /**
     * 列表数据
     */
    private List<StationFlowDTO> stationFlowDTOS;
}
