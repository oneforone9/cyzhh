package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-10 17:31:57
 */

@Data
public class StSideRelationEsr extends Esr {

    private static final long serialVersionUID = 886248170508546607L;

    private Integer id;


    /**
     * 编码
     */
    private String stcd;


    /**
     * 闸坝名称
     */
    private String stnm;


    /**
     * 闸坝DD-闸  SB-水坝
     */
    private String sttp;


    /**
     * 站点编码流量站水位站
     */
    private String stcdz;


    /**
     * 站点名称
     */
    private String stnmz;


    /**
     * 流量站ZQ水位站ZZ
     */
    private String sttpz;


}
