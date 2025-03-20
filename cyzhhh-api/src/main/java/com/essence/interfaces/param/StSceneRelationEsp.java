package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 参数实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:34
 */

@Data
public class StSceneRelationEsp extends Esp {

    private static final long serialVersionUID = 378581304484353629L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 场景基本表id
     */
    @ColumnMean("scene_id")
    private Integer sceneId;
    /**
     * 摄像头主键id
     */
    @ColumnMean("video_id")
    private Integer videoId;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
