package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 方案执行结果表更新实体
 *
 * @author zhy
 * @since 2023-04-18 14:39:14
 */

@Data
public class StCaseResEsu extends Esu {

    private static final long serialVersionUID = -62249277732519225L;

    /**
     * 主键
     */
    private String id;

    /**
     * 案件id
     */
    private String caseId;

    /**
     * 步长结果
     */
    private Integer step;

    /**
     * 河流id
     */
    private String rvId;

    /**
     * /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 河道断面水位
     */
    private String riverZ;

    /**
     * 河道断面水深
     */
    private String riverH;

    /**
     * 河道断面流量
     */
    private String riverQ;

    /**
     * 河道断面过水流速
     */
    private String riverV;

    /**
     * 河道断面过水面积数组
     */
    private String riverA;

    /**
     * 河道断面过水宽度
     */
    private String riverW;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 步长时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stepTime;

    /**
     * 1 数据库设置类型为 入库不展示 2 数据库设置类型为 入库 展示
     */

    private String dataType;
}
