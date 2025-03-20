package com.essence.interfaces.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bird
 */
@Data
public class PieChartDtoRes<T> implements Serializable {

    /**
     * 类型名称
     */
    private String type;

    /**
     * 类型总条数
     */
    private Integer number;
    /**
     * 类型在线条数
     */
    private Integer onlineNum;

    private Integer sort;

}
