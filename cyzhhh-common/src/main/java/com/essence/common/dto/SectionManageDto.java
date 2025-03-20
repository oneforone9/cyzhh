package com.essence.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SectionManageDto implements Serializable {
    /**
     * 断面名称
     */
    private String sectionName;
    /**
     * 断面 类型
     */
    private Integer type;
    /**
     * 断面 备注
     */
    private List<String> level;
    /**
     * 平均水质类别
     */
    private Integer avgLevel;
    /**
     * 更新时间
     */
    private String updateTime;
}
