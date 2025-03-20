package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 参数实体
 *
 * @author liwy
 * @since 2023-05-06 10:02:57
 */

@Data
public class TReaFocusPointEsp extends Esp {

    private static final long serialVersionUID = 769579265330713005L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 河道重点位置地理信息表主键id
     */
    @ColumnMean("focus_id")
    private String focusId;
    /**
     * 打卡点位名称
     */
    @ColumnMean("point_name")
    private String pointName;
    /**
     * 点位-经度
     */
    @ColumnMean("lgtd")
    private BigDecimal lgtd;
    /**
     * 点位-纬度
     */
    @ColumnMean("lttd")
    private BigDecimal lttd;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;


}
