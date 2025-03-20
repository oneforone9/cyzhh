package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhy
 * @since 2022/10/19 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsBase extends Esr {

    private static final long serialVersionUID = 3023995506959199629L;
    /**
     * 统计类型
     */
    private String type;

    /**
     * 值
     */
    private Object value;
}
