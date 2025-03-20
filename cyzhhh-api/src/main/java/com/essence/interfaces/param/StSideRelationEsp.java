package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2023-05-10 17:31:51
 */

@Data
public class StSideRelationEsp extends Esp {

    private static final long serialVersionUID = 300883143219086971L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 编码
     */
    @ColumnMean("stcd")
    private String stcd;
    /**
     * 闸坝名称
     */
    @ColumnMean("stnm")
    private String stnm;
    /**
     * 闸坝DD-闸  SB-水坝
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 站点编码流量站水位站
     */
    @ColumnMean("stcdz")
    private String stcdz;
    /**
     * 站点名称
     */
    @ColumnMean("stnmz")
    private String stnmz;
    /**
     * 流量站ZQ水位站ZZ
     */
    @ColumnMean("sttpz")
    private String sttpz;


}
