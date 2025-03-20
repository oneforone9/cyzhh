package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-03-08 11:32:18
 */

@Data
public class StLevelRainEsp extends Esp {

    private static final long serialVersionUID = 731935878790978699L;

            /**
     * 数据唯一标识
     */            @ColumnMean("id")
    private Integer id;
        /**
     * 等级标识
     */            @ColumnMean("level")
    private Integer level;
        /**
     * 等级名称
     */            @ColumnMean("level_name")
    private String levelName;
        /**
     * 1小时降雨量下边界(包含>=)
     */            @ColumnMean("lower1")
    private Double lower1;
        /**
     * 1小时降雨量上边界（不包含<）
     */            @ColumnMean("upper1")
    private Double upper1;
        /**
     * 3小时降雨量下边界(包含>=)
     */            @ColumnMean("lower3")
    private Double lower3;
        /**
     * 3小时降雨量上边界（不包含<）
     */            @ColumnMean("upper3")
    private Double upper3;
        /**
     * 12小时降雨量下边界(包含>=)
     */            @ColumnMean("lower12")
    private Double lower12;
        /**
     * 12小时降雨量上边界（不包含<）
     */            @ColumnMean("upper12")
    private Double upper12;
        /**
     * 24小时降雨量下边界(包含>=)
     */            @ColumnMean("lower24")
    private Double lower24;
        /**
     * 24小时降雨量上边界（不包含<）
     */            @ColumnMean("upper24")
    private Double upper24;
        /**
     * 图标路径
     */            @ColumnMean("file_url")
    private String fileUrl;
        /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_create")
    private Date gmtCreate;
        /**
     * 创建者
     */            @ColumnMean("creator")
    private String creator;
        /**
     * 修改时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_update")
    private Date gmtUpdate;
        /**
     * 修改者
     */            @ColumnMean("updater")
    private String updater;


}
