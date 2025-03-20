package com.essence.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HumanHotsDto implements Serializable {
    /**
     *  "300101099"
     */
    private String AREAID ;

    private String  INDEX ;
    /**
     *
     *         "亮马河"
     */
    private String  VALUE;
    /**
     *    "朝阳公园"
     */
    private String VALUE1;

    /**
     *
     *         "北运河管理处"
     */
    private String VALUE2;
    /**
     *
     * 数量        "61754"
     */
    private String VALUE7;
    /**
     *
     * 日期        "2023-01-29"
     */
    private String VALUE4;
    /**
     *
     * 区域名称        "朝阳区"
     */
    private String VALUE5;

}
