package com.essence.interfaces.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 网格雨量数据
 */
@Getter
@Setter
public class Shiduan implements Serializable {
    /**
     * 等级和 数据值
     */
    private Map<String,String> list;
    /**
     * 步长
     */
    private Integer  t;

}