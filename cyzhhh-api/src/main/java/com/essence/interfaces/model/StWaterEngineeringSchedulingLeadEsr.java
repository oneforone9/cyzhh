package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 水系联调-工程调度返回实体
 *
 * @author majunjie
 * @since 2023-07-03 17:25:02
 */

@Data
public class StWaterEngineeringSchedulingLeadEsr extends Esr {

    private static final long serialVersionUID = -32265902668642637L;

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
