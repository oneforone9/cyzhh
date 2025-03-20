package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 对接第三方用户基本信息 单点登录返回实体
 *
 * @author zhy
 * @since 2023-04-03 14:31:23
 */

@Data
public class StThirdUserInfoEsr extends Esr {

    private static final long serialVersionUID = -27267346700667255L;

            
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
