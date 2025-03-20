package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 水系联调-工程调度-调度预案返回实体
 *
 * @author majunjie
 * @since 2023-06-02 12:39:31
 */

@Data
public class StWaterEngineeringDispatchEsr extends Esr {

    private static final long serialVersionUID = 466115856753846692L;

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
