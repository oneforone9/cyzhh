package com.essence.common.dto;

import com.alibaba.excel.annotation.ExcelIgnore;

import lombok.Data;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

@Data
public class StationStatusRequestDTO implements Serializable {

    private List<String> videoTypeList;
    /**
     * 日期字符串
     */
    private String dateStr;
    /**
     * 当前页码
     */
    @ExcelIgnore
    private Integer current;
    /**
     * 大小
     */
    @ExcelIgnore
    private Integer size;
    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
    @ExcelIgnore
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
     * 日期
     */
    private Date date;
    /**
     * 日期类型
     */
    private Integer dateType;
    /**
     * 检查状态  1 在线  2 不在线
     */
    private Integer checked;


}
