package com.essence.tcp.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Data
public class ReceivePumpDto implements Serializable {
    /**
     * 每个字的数据
     */
    private Map<Integer,String> data =new HashMap<>();
    /**
     * 数据地址
     */
    private String deviceAddr;
}
