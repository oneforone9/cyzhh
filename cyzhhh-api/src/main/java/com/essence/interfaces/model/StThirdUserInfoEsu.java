package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 对接第三方用户基本信息 单点登录更新实体
 *
 * @author zhy
 * @since 2023-04-03 14:31:22
 */

@Data
public class StThirdUserInfoEsu extends Esu {

    private static final long serialVersionUID = 488202289902548142L;

    /**
     * id主键
     */
    private String id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户单位
     */
    private String userDep;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 公司名称
     */
    private String company;


}
