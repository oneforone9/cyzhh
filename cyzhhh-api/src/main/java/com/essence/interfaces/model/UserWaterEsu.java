package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 用水户取水量更新实体
 *
 * @author zhy
 * @since 2023-01-04 17:50:31
 */

@Data
public class UserWaterEsu extends Esu {

    private static final long serialVersionUID = -63295624902923852L;

    private String id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 取水口
     */
    private String outNum;

    /**
     * 许可水量
     */
    private String water;

    /**
     * 检测水量
     */
    private String mnWater;

    /**
     * 年月yyyy-MM
     */
    private String date;

    /**
     * 分组类型
     */
    private String fileType;


}
