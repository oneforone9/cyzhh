package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 科室基础表更新实体
 *
 * @author liwy
 * @since 2023-03-29 14:21:04
 */

@Data
public class StOfficeBaseEsu extends Esu {

    private static final long serialVersionUID = -33376017373151308L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 科室名
     */
    private String deptName;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类别 1-朝阳区水务局 2 -日常服务
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;


}
