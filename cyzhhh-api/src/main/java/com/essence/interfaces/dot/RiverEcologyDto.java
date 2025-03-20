package com.essence.interfaces.dot;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 河流生态表
 */
@Data
public class RiverEcologyDto implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 河流id
     */
    private String riverId;
    /**
     * 基础信息
     */
    private String baseInfo;
    /**
     * 概化图地址
     */
    private String ghtUrl;

    private List<RiverImageDto> riverImageDtos;
    /**
     * 水位站
     */
    private Integer ZZ;
    /**
     * 流量站
     */
    private Integer ZQ;
    /**
     * 坝
     */
    private Integer SB;
    /**
     * 泵站
     */
    private Integer DP;
    /**
     * 水闸
     */
    private Integer DD;
    /**
     * 功能监控
     */
    private Integer functionMonitor;
    /**
     * 安防监控
     */
    private Integer securityMonitor;
    /**
     * 国考 数量
     */
    private Integer countrySelection = 0;
    /**
     * 国考 断面 名称
     */
    private List<String> countrySelectionNames;
    /**
     * 市考 数量
     */
    private Integer citySelection = 0;
    /**
     * 市考 名称
     */
    private List<String> citySelectionNames;
    /**
     * 乡镇考 数量
     */
    private Integer townSelection = 0;
    /**
     * 乡镇 名称
     */
    private List<String> townSelectionNames;

}
