package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OnlineDeviceDetailDVo{

    /**
     * 场站名称
     */
    @ExcelProperty(value="设备名称")
    private String stnm;
    /**
     * 河流名称：测站所属河流的中文名称。
     */
    @ExcelProperty(value="所属河道")
    private String rvnm;

    /**
     * 实际在线数量
     */
    @ExcelProperty(value="实际在线时长(h)")
    private Integer factNum;
    /**
     * 总计
     */
    @ExcelProperty(value="总考核时长(h)")
    private Integer total;
    /**
     * 百分比
     */
    @ExcelProperty(value="在线时长占比")
    private BigDecimal percent;
    /**
     * 1.达标 2未达标
     */
    @ExcelProperty(value="达标状态")
    private String checked;
    /**
     * 场站类型
     */
    @ExcelProperty(value="设备类型")
    private String sttp;
}
