package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author liwy
 * @since 2023-08-23 14:29:29
 */

@Data
public class StOpenBaseEsp extends Esp {

    private static final long serialVersionUID = -76292035983379068L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 用户的openid
     */
    @ColumnMean("open_id")
    private String openId;
    /**
     * 用户id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 用户名
     */
    @ColumnMean("name")
    private String name;
    /**
     * 手机号
     */
    @ColumnMean("phone")
    private String phone;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;


}
