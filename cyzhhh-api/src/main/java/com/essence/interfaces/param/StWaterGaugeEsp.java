package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 电子水尺积水台账
 *
 * @author liwy
 * @since 2023-05-11 18:40:00
 */

@Data
public class StWaterGaugeEsp extends Esp {

    private static final long serialVersionUID = 504309982865111393L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 水务感知码（32位）
     */
    @ColumnMean("water_code")
    private String waterCode;

    /**
     * 站点名称
     */
    @ColumnMean("name")
    private String name;
    /**
     * 测点名称
     */
    @ColumnMean("pond_name")
    private String pondName;
    /**
     * 积水类别
     */
    @ColumnMean("pond_type")
    private String pondType;
    /**
     * 运行状态（良好/待改造/报废）
     */
    @ColumnMean("run_state")
    private String runState;
    /**
     * 感知对象名称（注：水利工程或水利设施的名称）
     */
    @ColumnMean("object_name")
    private String objectName;
    /**
     * 感知位置（注：测站相对感知对象的位置，如xx闸上游）
     */
    @ColumnMean("location")
    private String location;
    /**
     * 建设时间
     */
    @ColumnMean("build_time")
    private String buildTime;
    /**
     * 站点地址
     */
    @ColumnMean("site_address")
    private String siteAddress;
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
     * 测量方式
     */
    @ColumnMean("guage_way")
    private String guageWay;

    /**
     * 信息传输方式
     */
    @ColumnMean("info_method")
    private String infoMethod;

    /**
     * 管理单位名称
     */
    @ColumnMean("manage_unit")
    private String manageUnit;

    /**
     * 管理人员
     */
    @ColumnMean("manage_name")
    private String manageName;

    /**
     * 联系方式
     */
    @ColumnMean("manage_number")
    private String manageNumber;


}
