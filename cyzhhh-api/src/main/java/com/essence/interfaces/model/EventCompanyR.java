package com.essence.interfaces.model;


import lombok.Data;

/**
 * 第三方服务公司人员配置返回实体
 * @author liwy
 * @since 2023-07-13 14:40:38
 */

@Data
public class EventCompanyR {

    /**
     * 负责人
     */
    private String name;


    /**
     * 负责人id
     */
    private String userId;


    /**
     * 负责人手机号
     */
    private String phone;

}
