package com.essence.interfaces.model;

import lombok.Data;

import java.util.List;

/**
 * 合同管理返回实体
 *
 * @author majunjie
 * @since 2024-09-09 17:48:33
 */

@Data
public class Hthsz {
    /**
     * 合同主键
     */
    private String id;
    /**
     * 用户id(收藏用)
     */
    private String userId;
}
