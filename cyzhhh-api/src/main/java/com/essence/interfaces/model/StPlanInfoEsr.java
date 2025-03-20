package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 闸坝计划排班信息表返回实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:18
 */

@Data
public class StPlanInfoEsr extends Esr {

    private static final long serialVersionUID = -83559748789308836L;

    private Integer id;


    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    private String sttp;


    /**
     * 边闸表的主键
     */
    private Integer sideGateId;


    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;


    /**
     * 设备设施名称
     */
    private String equipmentName;


    /**
     * 日常维护内容
     */
    private String serviceContent;


    /**
     * 日常维护频次
     */
    private String ycwhpc;


    /**
     * 河系id
     */
    private Integer riverId;

    /**
     * 河道名称
     */
    private String riverName;

    /**
     * 第三方服务公司主键id
     */
    private String companyId;


    /**
     * 公司名称
     */
    private String company;


    /**
     * 负责人id
     */
    private String userId;


    /**
     * 负责人
     */
    private String name;


    /**
     * 负责人手机号
     */
    private String phone;


    /**
     * 维护日期类型 1-年季度月  2-月/周
     */
    private String type;


    /**
     * 维护日期排班表 （1type   例如0406-0419,0420-0430,0510-0515        2type  05,08,10 ）
     */
    private String whTime;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;


}
