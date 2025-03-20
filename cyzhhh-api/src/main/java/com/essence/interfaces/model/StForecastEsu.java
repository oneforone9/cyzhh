package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 预警报表更新实体
 *
 * @author majunjie
 * @since 2023-04-17 19:38:26
 */

@Data
public class StForecastEsu extends Esu {

    private static final long serialVersionUID = 843531888847571478L;

    /**
     * 主键
     */
    @NotNull(groups = Update.class, message = "主键不能为空")
    private String forecastId;

    /**
     * 预警类型预警类型   水位站：1.水位超警戒17设备离线
     * 流量站 17设备离线
     * 闸坝 3阀门超载4阀门欠闸5左荷重超载6右荷重超载7PLC开度超限8PLC超载报警9电压异常10电流异常17设备离线
     * 泵站11设备故障12流量超上限13压力超上限14液位超上限15电能异常16格栅异常17设备离线
     */
    @Size(max = 32,message = "预警类型的最大长度：32")
    @NotEmpty(groups = Insert.class,message = "预警类型不能为空")
    private String forecastType;

    /**
     * 站点编号
     */
    @Size(max = 32,message = "站点编号的最大长度：32")
    @NotEmpty(groups = Insert.class,message = "站点编号不能为空")
    private String stcd;

    /**
     * 站点名称
     */
    @Size(max = 32,message = "站点名称的最大长度：32")
    @NotEmpty(groups = Insert.class,message = "站点名称不能为空")
    private String stnm;

    /**
     * 所属河道
     */
    @Size(max = 50,message = "所属河道的最大长度：50")
    private String rvnm;

    /**
     * 预警来源
     */
    @Size(max = 32,message = "预警来源的最大长度：32")
    private String sourceType;

    /**
     * 现场负责人
     */
    private String xcfzr;

    /**
     * 现场负责人联系方式
     */
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    private String xcfzrUserId;
    /**
     * 报警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(groups = Insert.class, message = "报警时间不能为空")
    private Date policeTime;

    /**
     * 预警状态0待处理1处理中2已处理
     */
    @NotNull(groups = Insert.class,message = "预警状态不能为空")
    private Integer forecastState;
    /**
     * 设备类型1闸坝2泵站3水位站4流量站
     */
    @Size(max = 10,message = "水表编号的最大长度：10")
    @NotEmpty(groups = Insert.class,message = "设备类型不能为空")
    private String deviceType;
    /**
     * 原因描述
     */
    @Size(max = 1000,message = "原因描述的最大长度：1000")
    @NotEmpty(groups = Update.class, message = "原因描述不能为空")
    private String reason;
    /**
     * 设备主键（闸坝才有）
     */
    private Integer stcdId;
    /**
     * 河道编号
     */
    private String river;
    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;

    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;
    /**
     * '处理人'
     */
    @Size(max = 64,message = "处理人最大长度：64")
    private String dispose;
    /**
     * '处理人联系方式'
     */
    @Size(max = 64,message = "处理人联系方式最大长度：64")
    private String disposePhone;
    /**
     * 发送状态0未发送1发送
     */
    private Integer state;
    /**
     * 接收状态0未接收1已接收
     */
    private Integer reception;

    /**
     * 预警处理-文件上传id
     */
    private List<String> file;
}
