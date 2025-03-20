package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频状态记录表更新实体
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */

@Data
public class VideoStatusRecordEsu extends Esu {

    private static final long serialVersionUID = -93495982895799915L;


    /**
     * 主键
     */
    private Integer id;

    /**
     * 视频主键
     */
    private Integer videoId;

    /**
     * 视频状态(0 离线 1 在线)
     */
    private String status;

    /**
     * 时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tm;

    /**
     * 视频来源（HIV 海康 HUAWEI 华为 ZX专线 NW内网）
     */
    private String source;


}
