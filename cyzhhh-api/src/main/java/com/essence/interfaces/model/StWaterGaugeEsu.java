package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 电子水尺积水台账
 *
 * @author liwy
 * @since 2023-05-11 18:39:52
 */

@Data
public class StWaterGaugeEsu extends Esu {

    private static final long serialVersionUID = 583249982838310647L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 水务感知码（32位）
     */
    private String waterCode;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 测点名称
     */
    private String pondName;

    /**
     * 积水类别
     */
    private String pondType;

    /**
     * 运行状态（良好/待改造/报废）
     */
    private String runState;

    /**
     * 感知对象名称（注：水利工程或水利设施的名称）
     */
    private String objectName;

    /**
     * 感知位置（注：测站相对感知对象的位置，如xx闸上游）
     */
    private String location;

    /**
     * 建设时间
     */
    private String buildTime;

    /**
     * 站点地址
     */
    private String siteAddress;

    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;

    /**
     * 测量方式
     */
    private String guageWay;

    /**
     * 信息传输方式
     */
    private String infoMethod;

    /**
     * 管理单位名称
     */
    private String manageUnit;

    /**
     * 管理人员
     */
    private String manageName;

    /**
     * 联系方式
     */
    private String manageNumber;


}
