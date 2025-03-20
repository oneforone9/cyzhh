package com.essence.common.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class StationFlowDTO implements Serializable {

    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
    private String stcd;
    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;
    /**
     * 河流名称：测站所属河流的中文名称。
     */
    private String rvnm;
    /**
     * 站类（数字字典）：标识测站类型的两位字母代码。测站类型代码由两位大写英文字母组成，第一位固定不变，表示大的测站类型，第二位根据情况可以扩展，表示大的测站类型的细分，如果没有细分的情况下，重复第一位。
     *  ZZ水位的 ZQ流量的
     */
    private String sttp;

    /**
     * 实际水位(水深)
     */
    @ExcelIgnore
    private BigDecimal waterPosition;

    /**
     * 实时流量
     */
    @ExcelIgnore
    private BigDecimal waterRate;
    /**
     * 流量状态 1 正常 2 流速低下
     */
    private Integer flowStatus;
    /**
     * 状态排序 正序 1  逆序 -1
     */
    private Integer sort;
    /**
     * 排序字段名称 status 流速状态  waterRate 流量  waterPosition 水位
     */
    private String field;

    private String geom;
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private BigDecimal lttd;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private BigDecimal lgtd;
    /**
     * 基面高程：测站观测水位时所采用基面高程系的基准面与该水文站所在流域的基准高程系基准面的高差，计量单位为m。
     * 底高程
     */
    private BigDecimal dtmel;
    /**
     * 河流id
     */
    private String rvid;
    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    /**
     * 在线离线状态
     */
    private String onLineStatus;

}
