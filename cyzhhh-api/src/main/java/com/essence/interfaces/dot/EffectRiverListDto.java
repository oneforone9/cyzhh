package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EffectRiverListDto implements Serializable {
    /**
     * 时间 yyyy-MM-dd
     */
    private String date;
    /**
     * 河流名称
     */
    private String riverName;
    /**
     * 河流下的案件类型数据
     */
    private List<EffectCaseStatisticListDto> data;
}
