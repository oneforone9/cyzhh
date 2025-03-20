package com.essence.common.dto.health;

import lombok.Data;

import java.io.Serializable;
@Data
public class HealthRequestDto implements Serializable {
    /**
     * 标志 1 区政 2 管属
     */
    private Integer type;
    /**
     * 年月
     */
    private String ym;

    private String rvid;
}
