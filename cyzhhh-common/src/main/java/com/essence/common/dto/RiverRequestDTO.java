package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 河道请求参数
 */
@Data
public class RiverRequestDTO implements Serializable {
    /**
     * 河道id
     */
    private String riverId;
    /**
     * 测站名称
     */
    private String stnm;
}
