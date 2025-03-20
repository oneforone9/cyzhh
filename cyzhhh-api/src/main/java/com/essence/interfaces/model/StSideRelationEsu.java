package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author majunjie
 * @since 2023-05-10 17:31:46
 */

@Data
public class StSideRelationEsu extends Esu {

    private static final long serialVersionUID = 475373896402728278L;

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
