package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
public class WatchAreaDTO implements Serializable {
    /**
     * id
     */
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
     * 文件路径
     */
    private List<String> imageUrls;

    /**
     * 文件标记 1-图片 2-视频
     */
    private Integer imageFlag;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;
}
