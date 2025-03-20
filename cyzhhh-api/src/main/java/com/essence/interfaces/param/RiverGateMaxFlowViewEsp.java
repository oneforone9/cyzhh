package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 参数实体
 *
 * @author BINX
 * @since 2023-04-25 13:38:51
 */
@Data
public class RiverGateMaxFlowViewEsp extends Esp {

    private static final long serialVersionUID = -15416953588858023L;

    /**
     * 页码 默认 1
     * @mock 1
     */
    private int currentPage = 1;

    /**
     * 个数 默认 10
     * @mock 10
     */
    private int pageSize = 10;

    /**
     * 查询条件
     */
    private List<Criterion> conditions;
    
    /**
     * 排序条件
     */
    private List<Criterion> orders;

    /**
     * 共通查询条件
     * 加载or关键字前，建议后端追加参数使用此集合，不暴露给前端
     * @ignore
     */
    private List<Criterion> currency;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;

    /**
     * 河道名称
     */
    @ColumnMean("river_name")
    private String riverName;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @ColumnMean("stnm")
    private String stnm;

    /**
     * 断面名称
     */
    @ColumnMean("section_name")
    private String sectionName;

    /**
     * 案件id
     */
    @ColumnMean("case_id")
    private String caseId;

    /**
     * 洪峰流量(m³/s)
     */
    @ColumnMean("max_q")
    private BigDecimal maxQ;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("create_time")
    private Date createTime;
}
