package com.essence.interfaces.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 河道信息
 */
@Data
public class ReaBaseDTO implements Serializable {

    /**
     * 主键 河道id
     */

    private String id;

    /**
     * 河道名称
     */

    private String reaName;

    /**
     * 事件详情
     */
    private List<EventBaseDTO> eventBaseDTOList;


}
