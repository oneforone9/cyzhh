package com.essence.interfaces.model;


import lombok.Data;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchSelect  {

    /**
     * 时间区间0（8月16-次年7月19）1（7月20-8月15）
     */
    private String dataType;
    /**
     * 泵站编码
     */
    private  String stcd;
}
