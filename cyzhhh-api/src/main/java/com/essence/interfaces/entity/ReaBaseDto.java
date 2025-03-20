package com.essence.interfaces.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liwy
 */
@Data
public class ReaBaseDto<T> implements Serializable {

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

    /**
     * 类型数据
     */
    private List<T> data;

}
