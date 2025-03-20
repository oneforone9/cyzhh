package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 易积滞水点基础表参数实体
 *
 * @author liwy
 * @since 2023-04-03 14:45:34
 */

@Data
public class StPondBaseEsp extends Esp {

    private static final long serialVersionUID = -26460376169965210L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 积水点名称
     */
    @ColumnMean("pond_name")
    private String pondName;
    /**
     * 积水类别
     */
    @ColumnMean("pond_type")
    private String pondType;
    /**
     * 积水面积(㎡)
     */
    @ColumnMean("pond_area")
    private String pondArea;
    /**
     * 积水面积(㎡)
     */
    @ColumnMean("pond_depth")
    private String pondDepth;
    /**
     * 消除时间（h）
     */
    @ColumnMean("remove_time")
    private String removeTime;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    @ColumnMean("lgtd")
    private Double lgtd;
    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    @ColumnMean("lttd")
    private Double lttd;
    /**
     * 所在区域
     */
    @ColumnMean("area")
    private String area;
    /**
     * 措施类别
     */
    @ColumnMean("type")
    private String type;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 抢险队负责人
     */
    @ColumnMean("qxdfzr")
    private String qxdfzr;
    /**
     *抢险队负责人电话
     */
    @ColumnMean("qxdfzrdh")
    private String qxdfzrdh;

    /**
     *责任单位1
     */
    @ColumnMean("zrdw1")
    private String zrdw1;
    /**
     *责任单位负责人1
     */
    @ColumnMean("zrdwfzr1")
    private String zrdwfzr1;
    /**
     * 责任单位负责人电话1
     */
    @ColumnMean("zrdwdh1")
    private String zrdwdh1;
    /**
     * 责任单位2
     */
    @ColumnMean("zrdw2")
    private String zrdw2;

    /**
     * 责任单位负责人2
     */
    @ColumnMean("zrdwfzr2")
    private String zrdwfzr2;
    /**
     * 责任单位负责人电话2
     */
    @ColumnMean("zrdwdh2")
    private String zrdwdh2;
    /**
     * 责任单位3
     */
    @ColumnMean("zrdw3")
    private String zrdw3;
    /**
     * 责任单位负责人3
     */
    @ColumnMean("zrdwfzr3")
    private String zrdwfzr3;

    /**
     * 责任单位负责人电话3
     */
    @ColumnMean("zrdwdh3")
    private String zrdwdh3;


}
