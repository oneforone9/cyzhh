package com.essence.interfaces.model;

import lombok.Data;

import java.util.List;

/**
 * 合同管理更新实体
 *
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */

@Data
public class HtUserData {
    /**
     * userid
     */
    private String useId;
    /**
     * 名称+职务
     */
    private String name;
    /**
     * 科室
     */
    private String ks;
}
