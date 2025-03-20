package com.essence.interfaces.model;

import com.essence.dao.entity.StSceneRelationDto;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 场景基本表返回实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:13
 */

@Data
public class StSceneBaseEsr extends Esr {

    private static final long serialVersionUID = 734729880219565318L;


    /**
     * 主键
     */
    private Integer id;


    /**
     * 场景名称
     */
    private String scene;


    /**
     * 用户ID
     */
    private String userId;


    /**
     * 备注
     */
    private String remark;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    private List<StSceneRelationDto> list;


}
