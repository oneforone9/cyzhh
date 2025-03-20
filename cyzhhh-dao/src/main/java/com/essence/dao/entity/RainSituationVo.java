package com.essence.dao.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/17 18:09
 */
@Data
public class RainSituationVo {

    /**
     * 站点名称
     */
    String stnm;

    /**
     * 所属区域
     */
    String area;

    /**
     * 所属街道
     */
    String street;

    /**
     * 降雨量
     */
    BigDecimal rain;
}
