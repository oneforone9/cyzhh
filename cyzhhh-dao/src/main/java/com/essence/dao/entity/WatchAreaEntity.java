package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 观鸟区
 */
@Data
@TableName("t_watch_area")
public class WatchAreaEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 来源 1 人工上传 2 网格获取 3 AI 识别
     */
    private Integer source;
    /**
     * 观景区 1 北小河河段 2 坝河 3 马家湾河段  4 萧太后河段
     */
    private Integer watchArea;

    /**
     * 文件标记 1-图片 2-视频
     */
    private Integer imageFlag;
    /**
     * 文件路径
     */
    private String imageUrls;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;
}
