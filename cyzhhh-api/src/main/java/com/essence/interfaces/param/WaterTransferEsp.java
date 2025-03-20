package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 调水表参数实体
 *
 * @author BINX
 * @since 2023-05-09 10:00:58
 */
@Data
public class WaterTransferEsp extends Esp {

    private static final long serialVersionUID = 984537938658069956L;

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
    private Long id;

    /**
     * 批量操作
     */
    private List<Long> ids;

    /**
     * 水源
     */
    @ColumnMean("source")
    private String source;

    /**
     * 当日调水量(万m³/日)
     */
    @ColumnMean("transfer")
    private BigDecimal transfer;

    /**
     * 去水
     */
    @ColumnMean("target")
    private String target;

    /**
     * 调水类型(0 - 调水/1 - 补水/2 - 退水)
     */
    @ColumnMean("type")
    private String type;

    /**
     * 近期调用(0 - 近期/ 1 - 远期)
     */
    @ColumnMean("recent_transfer")
    private Integer recentTransfer;

    /**
     * 定位id
     */
    @ColumnMean("position_id")
    private String positionId;

    /**
     * 概化图
     */
    @ColumnMean("image")
    private String image;

    /**
     * 来源补水口id
     */
    @ColumnMean("source_port_id")
    private Long sourcePortId;

}
