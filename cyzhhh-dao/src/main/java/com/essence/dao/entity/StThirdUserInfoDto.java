package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对接第三方用户基本信息 单点登录实体
 *
 * @author BINX
 * @since 2023-04-03 14:31:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_third_user_info")
public class StThirdUserInfoDto extends Model<StThirdUserInfoDto> {

    private static final long serialVersionUID = -86544728820257233L;
        
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;
    
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 用户单位
     */
    @TableField("user_dep")
    private String userDep;
    
    /**
     * 手机号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    
    /**
     * 公司名称
     */
    @TableField("company")
    private String company;

}
