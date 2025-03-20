package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设计雨型更新实体
 *
 * @author majunjie
 * @since 2023-04-24 09:57:39
 */

@Data
public class StDesigRainPatternEsu extends Esu {

    private static final long serialVersionUID = 436463734605907941L;

    /**
     * 雨型ID
     */
    private String designRainPatternId;

    /**
     * 雨型名称
     */
    private String designRainPatternName;

    /**
     * 小时数
     */
    private Integer hourCount;

    /**
     * 时间间隔（分钟）
     */
    private Integer timeInterval;

    /**
     * 雨型参数（参数之间用英文逗号隔开）
     */
    private String param;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 备注
     */
    private String remark;


}
