package com.essence.interfaces.model;


import lombok.Data;

/**
 * 更新实体
 *
 * @author bird
 * @since 2023-01-04 18:01:13
 */

@Data
public class StWellcollectFeeEsuVo  {



    /**
     * 总实缴
     */
    private String sumPayment;

    /**
     * 乡实缴
     */
    private String xSumPayment;

    /**
     *街道实缴
     */
    private String jSumPayment;

    /**
     * 乡管机井实缴
     */
    private String xgPayment;

    /**
     * 乡管社会单位机井实缴
     */
    private String xgShPayment;
    /**
     * 社会单位机井实缴
     */
    private String shPayment;

    private String update_time;

}
