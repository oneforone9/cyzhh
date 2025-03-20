package com.essence.interfaces.model;

import lombok.Data;

import java.util.List;

/**
 *
 *
 * @author majunjie
 * @since 2023-02-20 14:33:10
 */

@Data
public class StRainDateOutSelect  {

    /**
     * 雨量站编码
     */
    private String stcd;

    /**
     * 雨量数据
     */
    private List<StRainDateOut> rainDate;

}
