package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 设备巡检摄像头巡检计划日期返回实体
 *
 * @author majunjie
 * @since 2025-01-08 17:53:37
 */

@Data
public class XjSxtJhTimeEsr extends Esr {

    private static final long serialVersionUID = 684711229552755264L;

        
    /**
     * 主键
     */
        private String id;
    /**
     * 周计划时间id
     */
    private String zjhId;
    /**
     * 计划id
     */
    private String jhId;
    


}
