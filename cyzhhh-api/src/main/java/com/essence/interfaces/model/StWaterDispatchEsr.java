package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2023-05-08 14:26:29
 */

@Data
public class StWaterDispatchEsr extends Esr {

    private static final long serialVersionUID = -34203132576138433L;

    /**
     * id
     */
    private String id;


    /**
     * 0正常运行工况1特殊运行工况
     */
    private Integer type;

    /**
     * 河道id
     */
    private String riverId;
    /**
     * 河道名称
     */
    private String riverName;
    /**
     * 方案id
     */
    private String caseId;
    /**
     * 文件路径”,“号拆分
     */
    private String file;
    /**
     * 示意图
     */
    private String files;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtUpdate;


}
