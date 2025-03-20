package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchFlow  {

    /**
     * 瞬时流量
     */
    private String momentRate;
    /**
     * 累计流量
     */
    private String addFlowRate;
    /**
     * 时间节点
     */
    private String time;



}
