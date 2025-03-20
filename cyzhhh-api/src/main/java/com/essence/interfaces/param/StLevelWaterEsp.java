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
 * @since 2023-03-08 11:32:51
 */

@Data
public class StLevelWaterEsp extends Esp {

    private static final long serialVersionUID = 349247258600678135L;

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
     * 积水深度下边界(包含>=)
     */            @ColumnMean("lower")
    private Double lower;
        /**
     * 积水深度上边界（不包含<）
     */            @ColumnMean("upper")
    private Double upper;
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
