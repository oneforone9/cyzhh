package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-04-04 14:45:12
 */

@Data
public class StThirdUserRelationEsp extends Esp {



            /**
     * id
     */
            @ColumnMean("id")
    private String id;
        /**
     * 本系统用户
     */
        @ColumnMean("user_id")
    private String userId;
                @ColumnMean("third_user_id")
    private String thirdUserId;

    /**
     * 本系统用户名称
     */
    @ColumnMean("user_name")
    private String userName;
    /**
     * 第三方用户姓名
     */
    @ColumnMean("third_user_name")
    private String thirdUserName;
    /**
     * 第三方用户部门或者角色
     */
    @ColumnMean("third_user_dep")
    private String thirdUserDep;


}
