package com.essence.interfaces.model;


import lombok.Data;

/**
 * 预设调度方案返回实体
 *
 * @author majunjie
 * @since 2023-04-24 11:21:24
 */

@Data
public class StForeseeProjectSelect {

    /**
     * 水闸DD坝SB
     */
    private String sttp;

    /**
     * 方案id
     */
    private String caseId;
}
