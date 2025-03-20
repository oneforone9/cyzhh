package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 河道部门关系表更新实体
 *
 * @author zhy
 * @since 2022-10-24 17:43:24
 */

@Data
public class RelReaDepartmentEsu extends Esu {

    private static final long serialVersionUID = 342419673859328182L;


    /**
     * 河道主键
     *
     * @mock 12
     */
    @NotEmpty(message = "河道主键不能为空")
    private String reaId;

    /**
     * 部门主键
     *
     * @mock 1
     */
    @NotEmpty(message = "部门主键不能为空")
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
