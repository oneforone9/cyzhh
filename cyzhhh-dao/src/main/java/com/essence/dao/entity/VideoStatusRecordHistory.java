package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频状态记录表(VideoStatusRecord)实体类
 *
 * @author zhy
 * @since 2022-10-21 16:36:44
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "video_status_record_history")
public class VideoStatusRecordHistory extends Model<VideoStatusRecordHistory> {

    private static final long serialVersionUID = -31731545469466557L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频主键
     */
    @TableField("video_id")
    private Integer videoId;

    /**
     * 视频状态(0 离线 1 在线)
     */
    @TableField("status")
    private String status;

    /**
     * 时间
     */

    @TableField("tm")
    private String tm;

}
