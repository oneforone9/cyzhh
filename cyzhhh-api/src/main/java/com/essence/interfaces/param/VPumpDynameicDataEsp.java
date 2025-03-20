package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-04-20 17:29:36
 */

@Data
public class VPumpDynameicDataEsp extends Esp {

    private static final long serialVersionUID = 768058513783804745L;

            /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */            @ColumnMean("stcd")
    private String stcd;
        /**
     * 测站名称：测站编码所代表测站的中文名称。
     */            @ColumnMean("stnm")
    private String stnm;
        /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */            @ColumnMean("lgtd")
    private Double lgtd;
        /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */            @ColumnMean("lttd")
    private Double lttd;
        /**
     * 河系id
     */            @ColumnMean("river_id")
    private Integer riverId;
        /**
     * 是否为闸站工程
     */            @ColumnMean("gate_project")
    private String gateProject;
        /**
     * 闸门型式/泵站类型
     */            @ColumnMean("gate_type")
    private String gateType;
        /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */            @ColumnMean("sttp")
    private String sttp;
        /**
     * 工程建设情况
     */            @ColumnMean("project_construction")
    private String projectConstruction;
        /**
     * 建成时间（年）
     */            @ColumnMean("build_time")
    private String buildTime;
        /**
     * 启闭型式
     */            @ColumnMean("hoist_type")
    private String hoistType;
        /**
     * 是否有启闭机房
     */            @ColumnMean("hoist_room")
    private String hoistRoom;
        /**
     * 是否有备用电源
     */            @ColumnMean("spare_power")
    private String sparePower;
        /**
     * 闸孔数量（孔）
     */            @ColumnMean("holes_number")
    private String holesNumber;
        /**
     * 水闸类型/水泵类型
     */            @ColumnMean("sluice_type")
    private String sluiceType;
        /**
     * 是否进行过抗震复核
     */            @ColumnMean("seismic_review")
    private String seismicReview;
        /**
     * 是否有水位观测设施
     */            @ColumnMean("observe_device")
    private String observeDevice;
        /**
     * 是否有视频图像监测设施
     */            @ColumnMean("video_cems")
    private String videoCems;
        /**
     * 水闸安全鉴定时间
     */            @ColumnMean("safety_appraisal_time")
    private String safetyAppraisalTime;
        /**
     * 单位id
     */            @ColumnMean("unit_id")
    private String unitId;
        /**
     * 主键
     */            @ColumnMean("id")
    private String id;
        /**
     * 设备物理地址
     */            @ColumnMean("device_addr")
    private String deviceAddr;
        /**
     * 泵站1 反馈
     */            @ColumnMean("p1_feed_back")
    private String p1FeedBack;
        /**
     * 泵站2 反馈
     */            @ColumnMean("p2_feed_back")
    private String p2FeedBack;
        /**
     * 流量
     */            @ColumnMean("flow_rate")
    private String flowRate;
        /**
     * 压力
     */            @ColumnMean("pressure")
    private String pressure;
        /**
     * 液位
     */            @ColumnMean("y_position")
    private String yPosition;
        /**
     * 泵站1 计时
     */            @ColumnMean("p1_count_time")
    private String p1CountTime;
        /**
     * 泵2 计时
     */            @ColumnMean("p2_count_time")
    private String p2CountTime;
        /**
     * A相电压
     */            @ColumnMean("a_voltage")
    private String aVoltage;
        /**
     * B相电压
     */            @ColumnMean("b_voltage")
    private String bVoltage;
        /**
     *  C相电压
     */            @ColumnMean("c_voltage")
    private String cVoltage;
        /**
     * 电流A
     */            @ColumnMean("a_electric")
    private String aElectric;
        /**
     * 电流B
     */            @ColumnMean("b_electric")
    private String bElectric;
        /**
     * 电流C
     */            @ColumnMean("c_electric")
    private String cElectric;
        /**
     * 电能
     */            @ColumnMean("electric")
    private String electric;
        /**
     * 累计流量
     */            @ColumnMean("add_flow_rate")
    private String addFlowRate;
        /**
     * 液位 高
     */            @ColumnMean("liquid_high")
    private String liquidHigh;
        /**
     * 液位 低
     */            @ColumnMean("liquid_low")
    private String liquidLow;
        /**
     * 泵1 远程
     */            @ColumnMean("p1_remote")
    private String p1Remote;
        /**
     * 泵1 运行
     */            @ColumnMean("p1_run")
    private String p1Run;
        /**
     * 泵1 故障
     */            @ColumnMean("p1_hitch")
    private String p1Hitch;
        /**
     * 泵2 远程
     */            @ColumnMean("p2_remote")
    private String p2Remote;
        /**
     * 泵2 运行
     */            @ColumnMean("p2_run")
    private String p2Run;
        /**
     * 泵2 故障
     */            @ColumnMean("p2_hitch")
    private String p2Hitch;
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("date")
    private Date date;

    /**
     * 泵1 远程
     */            @ColumnMean("p3_remote")
    private String p3Remote;
    /**
     * 泵1 运行
     */            @ColumnMean("p3_run")
    private String p3Run;
    /**
     * 泵1 故障
     */            @ColumnMean("p3_hitch")
    private String p3Hitch;
}
