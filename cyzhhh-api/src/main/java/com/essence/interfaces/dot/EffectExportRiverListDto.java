package com.essence.interfaces.dot;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EffectExportRiverListDto implements Serializable {
    /**
     * 时间 yyyy-MM-dd
     */
    @ExcelProperty("时间")
    private String date;
    /**
     * 河流名称
     */
    @ExcelProperty("河流名称")
    private String riverName;
    /**
     * 案件类型
     */
    @ExcelProperty("案件类型")
    private String eventType;
    /**
     * 案件数量
     */
    @ExcelProperty("案件数量")
    private Integer eventNum;
    /**
     * 案件处理数量
     */
    @ExcelProperty("案件处理数量")
    private Integer eventDeal;
    /**
     * 责任单位
     */
    @ExcelProperty("责任单位")
    private String unitName;
}
