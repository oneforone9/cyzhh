package com.essence.interfaces.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author majunjie
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelData <T> implements Serializable {
    private T data;

    private Integer rowIndex;
}
