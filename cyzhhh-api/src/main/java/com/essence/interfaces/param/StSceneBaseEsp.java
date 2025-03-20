package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 场景基本表参数实体
 *
 * @author liwy
 * @since 2023-06-01 14:48:10
 */

@Data
public class StSceneBaseEsp extends Esp {

    private static final long serialVersionUID = 810120067551678709L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 场景名称
     */
    @ColumnMean("scene")
    private String scene;
    /**
     * 用户ID
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
