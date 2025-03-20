package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-03-08 11:32:48
 */

@Data
public class StLevelWaterEsu extends Esu {

    private static final long serialVersionUID = -13108871806135451L;

    /**
     * 数据唯一标识
     */
    private Integer id;

    /**
     * 等级标识
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 积水深度下边界(包含>=)
     */
    private Double lower;

    /**
     * 积水深度上边界（不包含<）
     */
    private Double upper;

    /**
     * 图标路径
     */
    private String fileUrl;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtUpdate;

    /**
     * 修改者
     */
    private String updater;


}
