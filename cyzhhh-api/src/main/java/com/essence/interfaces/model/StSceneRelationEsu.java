package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:31
 */

@Data
public class StSceneRelationEsu extends Esu {

    private static final long serialVersionUID = 696546396802965305L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 场景基本表id
     */
    private Integer sceneId;

    /**
     * 摄像头主键id
     */
    private Integer videoId;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


}
