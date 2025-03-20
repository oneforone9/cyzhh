package com.essence.interfaces.dot;

import lombok.Data;

import java.util.List;

/**
 * 预警统计实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/21 14:31
 */
@Data
public class WaringStatisticDto {

    /**
     * 溢流
     */
    private int overFlow;

    /**
     * 超警戒
     */
    private int overWarning;

    /**
     * 正常
     */
    private int normal;

    /**
     * 预警站点列表
     */
    private List<WarningStationDto> list;
}
