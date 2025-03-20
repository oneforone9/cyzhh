package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 返回实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:36
 */

@Data
public class StSceneRelationEsr extends Esr {

    private static final long serialVersionUID = -86059798985223836L;


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
