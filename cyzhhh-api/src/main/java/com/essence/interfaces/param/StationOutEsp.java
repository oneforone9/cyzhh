package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

import java.util.List;

/**
 * 水位流量站(对外)参数实体
 *
 * @author BINX
 * @since 2023-05-11 10:32:15
 */
@Data
public class StationOutEsp extends Esp {

    private static final long serialVersionUID = 181391223202737647L;

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
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 监测点描述
     */
    private String monitor;

    /**
     * 所属河道id
     */
    private Integer riverId;

}
