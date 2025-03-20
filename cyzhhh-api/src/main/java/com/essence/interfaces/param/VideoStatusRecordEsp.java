package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频状态记录表参数实体
 *
 * @author zhy
 * @since 2022-10-21 16:36:45
 */

@Data
public class VideoStatusRecordEsp extends Esp {

    private static final long serialVersionUID = 890806342619504377L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;

    /**
     * 视频主键
     */
    @ColumnMean("video_id")
    private Integer videoId;

    /**
     * 视频状态(0 离线 1 在线)
     */
    @ColumnMean("status")
    private String status;

    /**
     * 时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("tm")
    private Date tm;


}
