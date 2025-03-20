package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 用水户取水量参数实体
 *
 * @author zhy
 * @since 2023-01-04 17:50:31
 */

@Data
public class UserWaterEsp extends Esp {

    private static final long serialVersionUID = -87665515511869256L;

    @ColumnMean("id")
    private String id;
    /**
     * 用户名称
     */
    @ColumnMean("user_name")
    private String userName;
    /**
     * 取水口
     */
    @ColumnMean("out_num")
    private String outNum;
    /**
     * 许可水量
     */
    @ColumnMean("water")
    private String water;
    /**
     * 检测水量
     */
    @ColumnMean("mn_water")
    private String mnWater;
    /**
     * 年月yyyy-MM
     */
    @ColumnMean("date")
    private String date;
    /**
     * 分组类型
     */
    @ColumnMean("file_type")
    private String fileType;


}
