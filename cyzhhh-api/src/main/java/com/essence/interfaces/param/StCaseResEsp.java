package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 方案执行结果表参数实体
 *
 * @author zhy
 * @since 2023-04-18 14:39:15
 */

@Data
public class StCaseResEsp extends Esp {

    private static final long serialVersionUID = 917141460149771134L;

            /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 案件id
     */            @ColumnMean("case_id")
    private String caseId;
        /**
     * 步长结果
     */            @ColumnMean("step")
    private Integer step;
        /**
     * 河流id
     */            @ColumnMean("rv_id")
    private String rvId;

        /**
     * 断面名称
     */            @ColumnMean("section_name")
    private String sectionName;
        /**
     * 河道断面水位
     */            @ColumnMean("river_z")
    private String riverZ;
        /**
     * 河道断面水深
     */            @ColumnMean("river_h")
    private String riverH;
        /**
     * 河道断面流量
     */            @ColumnMean("river_q")
    private String riverQ;
        /**
     * 河道断面过水流速

     */            @ColumnMean("river_v")
    private String riverV;
        /**
     * 河道断面过水面积数组

     */            @ColumnMean("river_a")
    private String riverA;
        /**
     * 河道断面过水宽度

     */            @ColumnMean("river_w")
    private String riverW;
        /**
     * 更新时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("create_time")
    private Date createTime;

    /**
     * 步长时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("step_time")
    private Date stepTime;
    /**
     * 1 数据库设置类型为 入库不展示 2 数据库设置类型为 入库 展示
     */

    private String dataType;
}
