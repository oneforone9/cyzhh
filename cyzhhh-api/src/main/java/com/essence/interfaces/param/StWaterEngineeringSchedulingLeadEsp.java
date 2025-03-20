package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 水系联调-工程调度参数实体
 *
 * @author majunjie
 * @since 2023-07-03 17:25:00
 */

@Data
public class StWaterEngineeringSchedulingLeadEsp extends Esp {

    private static final long serialVersionUID = -65088083261704370L;

    @ColumnMean("id")
    private String id;
    /**
     * 所长id
     */
    @ColumnMean("s_user_id")
    private String sUserId;
    /**
     * 所长名称人
     */
    @ColumnMean("sz")
    private String sz;
    /**
     * 联系方式
     */
    @ColumnMean("sz_phone")
    private String szPhone;


}
