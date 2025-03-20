package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 对接第三方用户基本信息 单点登录参数实体
 *
 * @author zhy
 * @since 2023-04-03 14:31:23
 */

@Data
public class StThirdUserInfoEsp extends Esp {

    private static final long serialVersionUID = -99304444293990081L;

            /**
     * id主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 用户名称
     */            @ColumnMean("user_name")
    private String userName;
        /**
     * 用户id
     */            @ColumnMean("user_id")
    private String userId;
        /**
     * 用户单位
     */            @ColumnMean("user_dep")
    private String userDep;
        /**
     * 手机号码
     */            @ColumnMean("phone_number")
    private String phoneNumber;
        /**
     * 公司名称
     */            @ColumnMean("company")
    private String company;


}
