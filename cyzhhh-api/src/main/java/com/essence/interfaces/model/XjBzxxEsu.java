package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检班组信息更新实体
 *
 * @author majunjie
 * @since 2025-01-08 08:13:26
 */

@Data
public class XjBzxxEsu extends Esu {

    private static final long serialVersionUID = 185384600754432953L;

    /**
     * 主键
     */        private String id;    

    /**
     * 班组名称
     */        private String bzmc;    

    /**
     * 生成时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;    

    /**
     * 部门id
     */        private String bmid;

    /**
     * 会议室名称
     */        private String hyName;    

    /**
     * 会议室id
     */        private String hyId;    


}
