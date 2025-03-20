package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝计划排班信息表参数实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:14
 */

@Data
public class StPlanInfoEsp extends Esp {

    private static final long serialVersionUID = -67155982939621836L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 边闸表的主键
     */
    @ColumnMean("side_gate_id")
    private Integer sideGateId;
    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @ColumnMean("stnm")
    private String stnm;
    /**
     * 设备设施名称
     */
    @ColumnMean("equipment_name")
    private String equipmentName;
    /**
     * 日常维护内容
     */
    @ColumnMean("service_content")
    private String serviceContent;
    /**
     * 日常维护频次
     */
    @ColumnMean("ycwhpc")
    private String ycwhpc;
    /**
     * 河系id
     */
    @ColumnMean("river_id")
    private Integer riverId;
    /**
     * 河道名称
     */
    @ColumnMean("river_name")
    private String riverName;
    /**
     * 第三方服务公司主键id
     */
    @ColumnMean("company_id")
    private String companyId;
    /**
     * 公司名称
     */
    @ColumnMean("company")
    private String company;
    /**
     * 负责人id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 负责人
     */
    @ColumnMean("name")
    private String name;
    /**
     * 负责人手机号
     */
    @ColumnMean("phone")
    private String phone;
    /**
     * 维护日期类型 1-年季度月  2-月/周
     */
    @ColumnMean("type")
    private String type;
    /**
     * 维护日期排班表 （1type   例如0406-0419,0420-0430,0510-0515        2type  05,08,10 ）
     */
    @ColumnMean("wh_time")
    private String whTime;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 所属单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 所属单位名称
     */
    @ColumnMean("unit_name")
    private String unitName;

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


}
