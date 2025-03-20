package com.essence.interfaces.param;


import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author bird
 * @since 2023-01-04 18:01:14
 */

@Data
public class StWellcollectFeeEsp extends Esp {

    private static final long serialVersionUID = -60242515769472447L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 乡政府名称
     */
    @ColumnMean("town_name")
    private String townName;
    /**
     * 类型 1-乡 2-街道
     */
    @ColumnMean("type")
    private String type;
    /**
     * 乡管机井_实有
     */
    @ColumnMean("xzjj_sy")
    private String xzjjSy;
    /**
     * 乡管机井_装表计量
     */
    @ColumnMean("xzjj_zbjl")
    private String xzjjZbjl;
    /**
     * 乡管机井_实际发生
     */
    @ColumnMean("xzjj_sjfs")
    private Double xzjjSjfs;
    /**
     * 乡管机井_实缴
     */
    @ColumnMean("xzjj_sj")
    private Double xzjjSj;
    /**
     * 社会机井_实有
     */
    @ColumnMean("shdwjj_sy")
    private String shdwjjSy;
    /**
     * 社会机井_装表计量
     */
    @ColumnMean("shdwjj_zbjl")
    private String shdwjjZbjl;
    /**
     * 社会机井_实际发生
     */
    @ColumnMean("shdwjj_sjfs")
    private Double shdwjjSjfs;
    /**
     * 社会机井_实缴
     */
    @ColumnMean("shdwjj_sj")
    private Double shdwjjSj;
    /**
     * 统计年份
     */
    @ColumnMean("tj_time")
    private String tjTime;

    /**
     * 季度
     */
    @ColumnMean("tj_jd")

    private String tjJd;

}
