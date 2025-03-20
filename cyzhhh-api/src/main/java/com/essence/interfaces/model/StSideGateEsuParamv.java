package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.entity.PaginatorParam;
import lombok.Data;

/**
 * 边闸基础表更新实体
 *
 * @author zhy
 * @since 2023-01-17 11:05:23
 */

@Data
public class StSideGateEsuParamv extends Esu {

    private static final long serialVersionUID = 488637167806596045L;

    /**
     * 参数
     */
    private PaginatorParam paginatorParam;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;

    /**
     * 河系id
     */
    private Integer riverId;

    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    private String sttp;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 采集时间
     */
    private String ctime;



}
