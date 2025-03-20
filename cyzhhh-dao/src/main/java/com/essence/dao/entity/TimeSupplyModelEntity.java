package com.essence.dao.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/6 14:21
 */
@Data
@HeadStyle(
        wrapped = BooleanEnum.FALSE,
        verticalAlignment = VerticalAlignmentEnum.BOTTOM,
        borderLeft = BorderStyleEnum.THIN,
        borderRight = BorderStyleEnum.THIN,
        borderTop = BorderStyleEnum.THIN,
        borderBottom = BorderStyleEnum.THIN,
        fillForegroundColor = 40
)
@ContentStyle(
        wrapped = BooleanEnum.FALSE,
        verticalAlignment = VerticalAlignmentEnum.BOTTOM,
        borderLeft = BorderStyleEnum.THIN,
        borderRight = BorderStyleEnum.THIN,
        borderTop = BorderStyleEnum.THIN,
        borderBottom = BorderStyleEnum.THIN
)
public class TimeSupplyModelEntity {

    /**
     * 时间序列
     */
    @ColumnWidth(18)
    @ExcelProperty(value = "时间序列", index = 0)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date time;

    /**
     * 补水流量(万m³/日)
     */
    @ColumnWidth(28)
    @ExcelProperty(value = "补水流量(万m³/日)", index = 1)
    BigDecimal supply;
}
