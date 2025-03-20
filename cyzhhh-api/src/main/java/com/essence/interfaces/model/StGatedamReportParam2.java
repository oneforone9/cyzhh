package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.entity.PaginatorParam;
import lombok.Data;

/**
 * 闸坝运行维保日志上报表更新实体
 *
 * @author liwy
 * @since 2023-03-15 11:56:33
 */

@Data
public class StGatedamReportParam2 extends Esu {

    private static final long serialVersionUID = 143522206881941013L;

    /**
     * 参数
     */
    private PaginatorParam param;

    /**
     * 所属河流id
     */
    private String stBRiverId;
    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */
    private String sttp;

    /**
     * 所属管辖单位
     */
    private String unitId;

    /**
     * 作业类型 (1巡查 2 保洁 3 绿化 4维保 5运行)
     */
    private String workType;

    /**
     * 作业单位
     */
    private String workUnit;

    /**
     * 作业开始时间
     */
    private String startTime;

    /**
     * 作业结束时间
     */
    private String endTime;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称(闸坝名称)
     */
    private String stnm;




}
