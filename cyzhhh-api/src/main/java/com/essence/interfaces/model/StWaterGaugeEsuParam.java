package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author liwy
 * @since 2023-05-11 18:39:52
 */

@Data
public class StWaterGaugeEsuParam extends Esu {

    private static final long serialVersionUID = 583249982838310647L;


    /**
     * 水务感知码（32位）
     */
    private String waterCode;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;





}
