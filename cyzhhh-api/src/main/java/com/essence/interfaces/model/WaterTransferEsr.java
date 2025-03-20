package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 调水表返回实体
 * @author BINX
 * @since 2023-05-09 10:01:01
 */
@Data
public class WaterTransferEsr extends Esr {

    private static final long serialVersionUID = -67769984337231325L;
    
    /**
     * 唯一标识
     */
    private String id;

    /**
     * 水源
     */
    private String source;

    /**
     * 当日调水量(万m³/日)
     */
    private BigDecimal transfer;

    /**
     * 去水
     */
    private String target;

    /**
     * 调水类型(0 - 调水/1 - 补水/2 - 退水)
     */
    private String type;

    /**
     * 近期调用(0 - 近期/ 1 - 远期)
     */
    private Integer recentTransfer;

    /**
     * 定位id
     */
    private String positionId;

    /**
     * 概化图
     */
    private String image;

    /**
     * 来源补水口id
     */
    private Long sourcePortId;
}
