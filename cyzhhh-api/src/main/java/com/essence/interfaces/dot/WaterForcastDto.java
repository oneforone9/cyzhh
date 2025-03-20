package com.essence.interfaces.dot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预报水位返回实体
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/23 16:03
 */
@Data
public class WaterForcastDto {

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
     * 河流名称
     */
    private String rvName;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 预报水位
     */
    private String riverZ;

    /**
     * 河道断面水深
     */
    private String riverH;

    /**
     * 预报流量
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
     * 调度时间
     */
    private String schedulingTime;
    /**
     * 调度类型
     */
    private String schedulingType;
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
}
