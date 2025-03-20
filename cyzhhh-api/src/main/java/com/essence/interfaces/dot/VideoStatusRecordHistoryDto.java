package com.essence.interfaces.dot;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;


import java.io.Serializable;

@Data
public class VideoStatusRecordHistoryDto implements Serializable {

    /**
     * 视频名称
     */
    @ExcelProperty("视频名称")
    private String videoName;

    /**
     * 视频状态(0 离线 1 在线)
     */
    @ExcelProperty("视频状态")
    private String status;

    /**
     * 时间
     */
    @ExcelProperty("时间")
    private String tm;
}
