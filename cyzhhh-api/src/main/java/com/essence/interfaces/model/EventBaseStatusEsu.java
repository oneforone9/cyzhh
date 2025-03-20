package com.essence.interfaces.model;

import lombok.Data;

@Data
public class EventBaseStatusEsu extends EventBaseEsu{
    /**
     * 工单状态
     */
    private Integer eventStatus;
    /**
     * 是否删除（1是 0否）
     * @ignore
     */
    private String isDelete;
}
