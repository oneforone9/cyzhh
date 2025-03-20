package com.essence.entity;

import lombok.Data;

/**
 * @author zhy
 * @since 2022/9/23 11:44
 */
@Data
public class MessageBean {
    /**
     * 编码 0 系统接受 1 发pc 2发小程序
     */
    private Integer code;

    /**
     * 内容
     */
    private String content;

    private String userId;
}
