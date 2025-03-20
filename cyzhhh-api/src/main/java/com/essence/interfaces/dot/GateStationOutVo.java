package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/25 13:43
 */
@Data
public class GateStationOutVo {

    /**
     * 闸坝名称
     */
    private String gateDam;

    /**
     * 水位
     */
    private String level;

    /**
     * 流量
     */
    private String flow;

    /**
     * 相对位置(0-上游;1-下游)
     */
    private String streamLoc;

    /**
     * 状态(多闸孔按分号分割)
     */
    private String state;

    /**
     * 监测时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tm;

}
