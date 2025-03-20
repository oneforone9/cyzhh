package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 防汛调度方案指令下发记录更新实体
 *
 * @author majunjie
 * @since 2023-05-14 18:15:45
 */

@Data
public class StWaterEngineeringSchedulingDataEsu extends Esu {

    private static final long serialVersionUID = -31589691808038742L;

    /**
     * 主键
     */
    private String did;

    /**
     * 调度主键
     */
    private String stId;

    /**
     * 闸名称
     */
    private String zbmc;

    /**
     * 描述
     */
    private String reason;

    /**
     * 状态1指令下发2接收3完成
     */
    private Integer forecastState;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    /**
     * 现场负责人
     */
    private String xcfzr;
    /**
     * 所属单位 id
     */
    private String unitId;
    /**
     * 调度指令id
     */
    private String codeId;
    /**
     * 下发级别0逐级1越级
     */
    private String rank;
}
