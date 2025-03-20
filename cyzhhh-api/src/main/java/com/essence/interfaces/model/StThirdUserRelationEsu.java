package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-04-04 14:45:11
 */

@Data
public class StThirdUserRelationEsu extends Esu {



                /**
     * id
     */
    private String id;
        
            /**
     * 本系统用户
     */
    private String userId;
    /**
     * 第三方用户id
     */
    private String thirdUserId;
    /**
     * 本系统用户名称
     */
    private String userName;
    /**
     * 第三方用户姓名
     */
    private String thirdUserName;
    /**
     * 第三方用户部门或者角色
     */
    private String thirdUserDep;


}
