package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检会议室信息更新实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */

@Data
public class XjHysxxEsu extends Esu {

    private static final long serialVersionUID = -21354835439152313L;

    /**
     * 主键
     */        private String id;    

    /**
     * 会议室名称
     */        private String mc;    

    /**
     * 生成时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    /**
     * 经度
     */
    private Double lgtd;


    /**
     * 纬度
     */
    private Double lttd;

}
