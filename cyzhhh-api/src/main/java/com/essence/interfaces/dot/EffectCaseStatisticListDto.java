package com.essence.interfaces.dot;

import com.essence.dao.entity.EventBase;
import lombok.Data;

import java.util.List;

@Data
public class EffectCaseStatisticListDto {

    /**
     * 案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )
     */
    private String eventType;
    /**
     * 案件数量
     */
    private Integer caseNumber;
    /**
     * 案件处理数量
     */
    private Integer disposeNumber;
    /**
     * 案件处理率%
     */
    private Double disposeRate;
    /**
     * 责任单位
     */
    private String unitName;

    /**
     * 案件基本信息
     */
    private List<EventBase> eventBaseList;


}
