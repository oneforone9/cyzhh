package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 返回实体
 * @author BINX
 * @since 2023-04-25 13:38:56
 */
@Data
public class RiverGateMaxFlowViewEsr extends Esr {

    private static final long serialVersionUID = -43155985047043642L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 河道名称
     */
    private String riverName;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 案件id
     */
    private String caseId;

    /**
     * 洪峰流量(m³/s)
     */
    private BigDecimal maxQ;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
