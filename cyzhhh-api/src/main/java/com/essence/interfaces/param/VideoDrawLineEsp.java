package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频管理线保护线表参数实体
 *
 * @author zhy
 * @since 2023-02-03 14:51:28
 */

@Data
public class VideoDrawLineEsp extends Esp {

    private static final long serialVersionUID = -44146840863333912L;

                    @ColumnMean("id")
    private Integer id;
        /**
     * 视频编码code
     */            @ColumnMean("video_code")
    private String videoCode;
        /**
     * 管理范围保护范围画线数据
     */            @ColumnMean("geom")
    private String geom;
        /**
     * 备注
     */            @ColumnMean("remark")
    private String remark;
        /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_create")
    private Date gmtCreate;
        /**
     * 创建者
     */            @ColumnMean("creator")
    private String creator;


}
