package com.essence.interfaces.model;


import lombok.Data;

/**
 * 第三方服务公司人员配置更新实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:58
 */

@Data
public class EventCompanyPerson {
    /**
     * 名称
     */
    private String name;
    /**
     * id
     */
    private String userId;

    /**
     * 联系方式
     */
    private String phone;
    /**
     * 第三方服务公司主键id
     */
    private String companyId;
}
