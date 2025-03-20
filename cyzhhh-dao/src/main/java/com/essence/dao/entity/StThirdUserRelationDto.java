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
 * 实体
 *
 * @author BINX
 * @since 2023-04-04 14:45:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_third_user_relation")
public class StThirdUserRelationDto extends Model<StThirdUserRelationDto> {

        
    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
        
    /**
     * 本系统用户
     */
    @TableField("user_id")
    private String userId;
    
    @TableField("third_user_id")
    private String thirdUserId;
    /**
     * 本系统用户名称
     */
    @TableField("user_name")
    private String userName;
    /**
     * 第三方用户姓名
     */
    @TableField("third_user_name")
    private String thirdUserName;
    /**
     * 第三方用户部门或者角色
     */
    @TableField("third_user_dep")
    private String thirdUserDep;

}
