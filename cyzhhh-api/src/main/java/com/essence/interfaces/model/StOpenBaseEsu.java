package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-08-23 14:29:20
 */

@Data
public class StOpenBaseEsu extends Esu {

    private static final long serialVersionUID = -19576017732863560L;

    private Integer id;

    /**
     * 用户的openid
     */
    private String openId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 微信获取的jsCode
     */
    private String jsCode;


}
