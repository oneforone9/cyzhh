package com.essence.interfaces.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author majunjie
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelErrorBean {
    /**
     * 行
     */
    private int row;
    /**
     * 列
     */
    private int column;
    /**
     * 错误信息
     */
    private String errorMsg;
}
