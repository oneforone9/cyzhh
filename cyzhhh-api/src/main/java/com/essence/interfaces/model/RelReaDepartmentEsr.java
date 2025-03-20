package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 河道部门关系表返回实体
 *
 * @author zhy
 * @since 2022-10-24 17:43:25
 */

@Data
public class RelReaDepartmentEsr extends Esr {

    private static final long serialVersionUID = 450754478753581067L;


    /**
     * 河道主键
     */
    private String reaId;

    /**
     * 部门主键
     */
    private String departmentId;
    /**
     * 河流名称
     */
    private String reaName;

}
