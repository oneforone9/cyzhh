package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 水系联调-工程调度-调度预案更新实体
 *
 * @author majunjie
 * @since 2023-06-02 12:39:20
 */

@Data
public class StWaterEngineeringDispatchEsu extends Esu {

    private static final long serialVersionUID = -28482636072980623L;

    private String id;

    /**
     * 调度id
     */
    private String stId;

    /**
     * 汛期日常
     */
    private String floodSeason;

    /**
     * 蓝色预警
     */
    private String bWarning;

    /**
     * 黄色预警
     */
    private String yWarning;

    /**
     * 橙色、红色预警
     */
    private String rWarning;


}
