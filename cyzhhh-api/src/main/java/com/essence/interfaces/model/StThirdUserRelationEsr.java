package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-04-04 14:45:12
 */

@Data
public class StThirdUserRelationEsr extends Esr {

            
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
