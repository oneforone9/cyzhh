package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频状态记录表返回实体
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */

@Data
public class VideoStatusRecordEsr extends Esr {

    private static final long serialVersionUID = -55719591724400433L;


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


}
