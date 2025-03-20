package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchDP {
    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;
}
