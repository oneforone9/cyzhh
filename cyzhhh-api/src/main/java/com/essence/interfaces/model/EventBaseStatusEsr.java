package com.essence.interfaces.model;

import lombok.Data;

@Data
public class EventBaseStatusEsr extends EventBaseEsr{
    /**
     * 工单状态
     */

    private Integer eventStatus;


}
