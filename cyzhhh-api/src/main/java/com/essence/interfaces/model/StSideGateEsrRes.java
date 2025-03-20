package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import lombok.Data;

import java.util.List;

/**
 * 边闸基础表返回实体
 *
 * @author zhy
 * @since 2023-01-17 11:05:24
 */

@Data
public class StSideGateEsrRes extends Esr {

    private static final long serialVersionUID = -58077454984394602L;


    /**
     * 主键
     */
    private Integer id;

    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
    private String stcd;

    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;


    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;


    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;


    /**
     * 河系id
     */
    private Integer riverId;


    /**
     * 水闸安全鉴定时间
     */
    private String safetyAppraisalTime;


    /**
     * 水闸管理单位名称
     */
    private String managementUnit;


    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    private String sttp;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 能否远控 1-能远控
     */
    private String remoteControl;

    /**
     * 目前状态  1-在用  2-停用  3-建成还未移交
     */
    private Integer presentStatus;

    /**
     * 所属河道
     */
    private String rvnm;

    /**
     * 上报的图片文件集合
     */
    private StSideGateRelationEsr stSideGateRelationEsr;


}
