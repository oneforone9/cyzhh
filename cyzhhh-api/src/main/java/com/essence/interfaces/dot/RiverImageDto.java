package com.essence.interfaces.dot;


import lombok.Data;

import java.io.Serializable;

/**
 * 河流影像表
 */
@Data
public class RiverImageDto implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 河流影像id
     */
    private Integer tRiverEcologyId;
    /**
     * 影像年份
     */
    private String imageYear;
    /**
     * 图片url
     */
    private String imageUrl;
}
