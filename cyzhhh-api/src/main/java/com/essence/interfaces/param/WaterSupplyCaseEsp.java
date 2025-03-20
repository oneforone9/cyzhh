package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 补水口案件计算入参表参数实体
 *
 * @author BINX
 * @since 2023-05-04 19:16:23
 */
@Data
public class WaterSupplyCaseEsp extends Esp {

    private static final long serialVersionUID = -75398759118028894L;

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
     * 唯一标识
     */
    @ColumnMean("id")
    private Integer id;

    /**
     * 批量操作
     */
    private List<Integer> ids;

    /**
     * 案件id
     */
    @ColumnMean("case_id")
    private String caseId;

    /**
     * 河道名称
     */
    @ColumnMean("river_name")
    private String riverName;

    /**
     * 补水口名称
     */
    @ColumnMean("water_port_name")
    private String waterPortName;

    /**
     * 供水能力（万m3/日）
     */
    @ColumnMean("supply")
    private BigDecimal supply;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @ColumnMean("lgtd")
    private BigDecimal lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ColumnMean("lttd")
    private BigDecimal lttd;

    /**
     * 断面名称
     */
    @ColumnMean("section_name")
    private String sectionName;

    /**
     * 序列名称
     */
    @ColumnMean("seria_name")
    private String seriaName;

    /**
     * 补水方式 (0 - 默认固定流量; 1 - 时间序列)
     */
    @ColumnMean("supply_way")
    private int supplyWay;

    /**
     * 时间序列流量
     */
    @ColumnMean("time_supply")
    private String timeSupply;

}
