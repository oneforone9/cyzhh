package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员部门关系表更新实体
 *
 * @author zhy
 * @since 2022-10-24 17:42:55
 */

@Data
public class RelPersonDepartmentEsu extends Esu {

    private static final long serialVersionUID = 699095264230348162L;


    /**
     * 人员主键
     */
    private String personId;

    /**
     * 部门主键
     */
    private String departmentId;

    /**
     * 新增时间（不传）
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 创建者（不传）
     */
    private String creator;


}
