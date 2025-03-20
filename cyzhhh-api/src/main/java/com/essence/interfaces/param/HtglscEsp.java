package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 合同管理收藏参数实体
 *
 * @author majunjie
 * @since 2024-09-13 16:18:31
 */

@Data
public class HtglscEsp extends Esp {

    private static final long serialVersionUID = -16294415915047570L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 合同编号
     */        @ColumnMean("htid")
    private String htid;
    /**
     * 用户id
     */        @ColumnMean("user_id")
    private String userId;


}
