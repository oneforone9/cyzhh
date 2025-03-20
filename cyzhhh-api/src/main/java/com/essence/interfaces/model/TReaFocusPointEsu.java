package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-05-06 10:02:33
 */

@Data
public class TReaFocusPointEsu extends Esu {

    private static final long serialVersionUID = -50459059488956387L;

    /**
     * 主键
     */
    private String id;

    /**
     * 河道重点位置地理信息表主键id
     */
    private String focusId;

    /**
     * 打卡点位名称
     */
    private String pointName;

    /**
     * 点位-经度
     */
    private BigDecimal lgtd;

    /**
     * 点位-纬度
     */
    private BigDecimal lttd;

    /**
     * 备注
     */
    private String remark;


}
