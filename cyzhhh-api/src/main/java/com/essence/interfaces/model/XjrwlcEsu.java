package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检流程更新实体
 *
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */

@Data
public class XjrwlcEsu extends Esu {

    private static final long serialVersionUID = -25730948850976205L;

    /**
     * 主键
     */        private String id;    

    /**
     * 任务id
     */        private String rwId;    

    /**
     * 描述工单状态
     */        private String ms;    

    /**
     * 操作人
     */        private String czr;    

    /**
     * 创建时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cjsj;    


}
