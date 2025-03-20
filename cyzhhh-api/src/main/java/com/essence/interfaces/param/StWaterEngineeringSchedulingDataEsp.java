package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 防汛调度方案指令下发记录参数实体
 *
 * @author majunjie
 * @since 2023-05-14 18:15:51
 */

@Data
public class StWaterEngineeringSchedulingDataEsp extends Esp {

    private static final long serialVersionUID = -51692061817943611L;

    /**
     * 主键
     */
    @ColumnMean("did")
    private String did;
    /**
     * 调度主键
     */
    @ColumnMean("st_id")
    private String stId;
    /**
     * 闸名称
     */
    @ColumnMean("zbmc")
    private String zbmc;
    /**
     * 描述
     */
    @ColumnMean("reason")
    private String reason;
    /**
     * 状态1指令下发2接收3完成
     */
    @ColumnMean("forecast_state")
    private Integer forecastState;
    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;
    /**
     * 现场负责人
     */
    private String xcfzr;
    /**
     * 所属单位 id
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 调度指令id
     */
    @ColumnMean("code_id")
    private String codeId;
    /**
     * 下发级别0逐级1越级
     */
    @ColumnMean("rank")
    private String rank;

}
