package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import lombok.Data;

@Data
public class EventBaseStatusEsp extends EventBaseEsp{
    /**
     * 工单状态
     */
    @ColumnMean("event_status")
    private Integer eventStatus;
}
