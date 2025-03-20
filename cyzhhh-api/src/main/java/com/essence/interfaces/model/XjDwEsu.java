package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检单位更新实体
 *
 * @author majunjie
 * @since 2025-01-09 08:56:30
 */

@Data
public class XjDwEsu extends Esu {

    private static final long serialVersionUID = 638740017550976808L;

    /**
     * 主键
     */        private String id;    

    /**
     * 单位名称
     */        private String dwmc;    

    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;    


}
