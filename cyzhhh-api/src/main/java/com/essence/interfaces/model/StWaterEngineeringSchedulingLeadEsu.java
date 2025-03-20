package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 水系联调-工程调度更新实体
 *
 * @author majunjie
 * @since 2023-07-03 17:24:56
 */

@Data
public class StWaterEngineeringSchedulingLeadEsu extends Esu {

    private static final long serialVersionUID = 132927955248504819L;

    private String id;

    /**
     * 所长id
     */
    private String sUserId;

    /**
     * 所长名称人
     */
    private String sz;

    /**
     * 联系方式
     */
    private String szPhone;


}
