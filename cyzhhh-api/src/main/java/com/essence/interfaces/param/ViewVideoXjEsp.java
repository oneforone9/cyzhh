package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author majunjie
 * @since 2025-01-08 14:13:47
 */

@Data
public class ViewVideoXjEsp extends Esp {

    private static final long serialVersionUID = 690645069101630507L;

        /**
     * 主键
     */        @ColumnMean("id")
    private Integer id;
    /**
     * 编码
     */
    @ColumnMean("code")
    private String code;
    /**
     * 班组名称
     */  @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 名称
     */
    @ColumnMean("name")
    private String name;
    /**
     * 地址
     */  @ColumnMean("address")
    private String address;
    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;
    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private Double lttd;
    /**
     * 河系名称
     */
    @ColumnMean("river_name")
    private String riverName;
    /**
     * 巡检单位id
     */
    @ColumnMean("xjdw_id")
    private String xjdwId;
    /**
     * 单位名称
     */
    @ColumnMean("dwmc")
    private String dwmc;
    /**
     * 负责人
     */
    @ColumnMean("fzr")
    private String fzr;
    /**
     * 负责人id
     */
    @ColumnMean("fzr_id")
    private String fzrId;
    /**
     * 巡检内容
     */
    @ColumnMean("xjnr")
    private String xjnr;
    /**
     * 巡检日期
     */
    @ColumnMean("xj_rq")
    private String xjRq;
    /**
     * 计划主键
     */
    @ColumnMean("jhid")
    private String jhid;
    /**
     * 班组id
     */
    @ColumnMean("bz_id")
    private String bzId;
    /**
     * 海康HIV,华为HUAWEI,内网NW,萤石云YSYC,YUSY宇视云,专线ZX
     */
    @ColumnMean("source")
    private String source;
}
