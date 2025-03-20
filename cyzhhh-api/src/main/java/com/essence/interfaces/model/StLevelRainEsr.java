package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-03-08 11:32:26
 */

@Data
public class StLevelRainEsr extends Esr {

    private static final long serialVersionUID = -24764944827083656L;


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
     * 1小时降雨量下边界(包含>=)
     */
    private Double lower1;


    /**
     * 1小时降雨量上边界（不包含<）
     */
    private Double upper1;


    /**
     * 3小时降雨量下边界(包含>=)
     */
    private Double lower3;


    /**
     * 3小时降雨量上边界（不包含<）
     */
    private Double upper3;


    /**
     * 12小时降雨量下边界(包含>=)
     */
    private Double lower12;


    /**
     * 12小时降雨量上边界（不包含<）
     */
    private Double upper12;


    /**
     * 24小时降雨量下边界(包含>=)
     */
    private Double lower24;


    /**
     * 24小时降雨量上边界（不包含<）
     */
    private Double upper24;


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
