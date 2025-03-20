package com.essence.dao.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/9 10:22
 */
@Data
public class WaterTransferFlowDto extends WaterTransferDto {

    /**
     * 累计调水量
     */
    private BigDecimal sumFlow;

    /**
     * 泵站名称
     */
    private String pumpStnm;

    /**
     * 泵站经度
     */
    private String pumpLgtd;

    /**
     * 泵站纬度
     */
    private String pumpLttd;

    /**
     * 是否可以远传 1-能远传
     */
    private String remoteControl;
}
