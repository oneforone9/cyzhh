package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 用水户取水量返回实体
 *
 * @author zhy
 * @since 2023-01-04 17:50:32
 */

@Data
public class UserWaterEsr extends Esr {

    private static final long serialVersionUID = -82915629080441867L;

    private String id;


    /**
     * 用户名称
     */
    private String userName;


    /**
     * 取水口
     */
    private String out;


    /**
     * 许可水量
     */
    private String outNum;


    /**
     * 检测水量
     */
    private String mnWater;


    /**
     * 分组类型
     */
    private String fileType;


    /**
     * 年月yyyy-MM
     */
    private String date;

    private String water;


}
