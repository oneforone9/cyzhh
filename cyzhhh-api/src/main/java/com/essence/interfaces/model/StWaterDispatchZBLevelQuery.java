package com.essence.interfaces.model;


import com.essence.interfaces.vaild.Insert;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchZBLevelQuery {

    /**
     * 测站编码
     */
    @NotNull(groups = Insert.class,message = "主键不能为空")
    private Integer id;
    /**
     * 类型1闸2坝
     */
    @NotEmpty(groups = Insert.class,message = "类型不能为空")
    private String type;

}
