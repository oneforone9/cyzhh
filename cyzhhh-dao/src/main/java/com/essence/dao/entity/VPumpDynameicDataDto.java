package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "v_pump_dynameic_data")
public class VPumpDynameicDataDto extends Model<VPumpDynameicDataDto> {

    private static final long serialVersionUID = 155941174351148646L;

                /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
                    @TableField("stcd")
    private String stcd;
    
            /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
                    @TableField("stnm")
    private String stnm;
    
            /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
                    @TableField("lgtd")
    private Double lgtd;
    
            /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
                    @TableField("lttd")
    private Double lttd;
    
            /**
     * 河系id
     */
                    @TableField("river_id")
    private Integer riverId;
    
            /**
     * 是否为闸站工程
     */
                    @TableField("gate_project")
    private String gateProject;
    
            /**
     * 闸门型式/泵站类型
     */
                    @TableField("gate_type")
    private String gateType;
    
            /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */
                    @TableField("sttp")
    private String sttp;
    
            /**
     * 工程建设情况
     */
                    @TableField("project_construction")
    private String projectConstruction;
    
            /**
     * 建成时间（年）
     */
                    @TableField("build_time")
    private String buildTime;
    
            /**
     * 启闭型式
     */
                    @TableField("hoist_type")
    private String hoistType;
    
            /**
     * 是否有启闭机房
     */
                    @TableField("hoist_room")
    private String hoistRoom;
    
            /**
     * 是否有备用电源
     */
                    @TableField("spare_power")
    private String sparePower;
    
            /**
     * 闸孔数量（孔）
     */
                    @TableField("holes_number")
    private String holesNumber;
    
            /**
     * 水闸类型/水泵类型
     */
                    @TableField("sluice_type")
    private String sluiceType;
    
            /**
     * 是否进行过抗震复核
     */
                    @TableField("seismic_review")
    private String seismicReview;
    
            /**
     * 是否有水位观测设施
     */
                    @TableField("observe_device")
    private String observeDevice;
    
            /**
     * 是否有视频图像监测设施
     */
                    @TableField("video_cems")
    private String videoCems;
    
            /**
     * 水闸安全鉴定时间
     */
                    @TableField("safety_appraisal_time")
    private String safetyAppraisalTime;
    
            /**
     * 单位id
     */
                    @TableField("unit_id")
    private String unitId;
    
            /**
     * 主键
     */
                    @TableField("id")
    private String id;
    
            /**
     * 设备物理地址
     */
                    @TableField("device_addr")
    private String deviceAddr;
    
            /**
     * 泵站1 反馈
     */
                    @TableField("p1_feed_back")
    private String p1FeedBack;
    
            /**
     * 泵站2 反馈
     */
                    @TableField("p2_feed_back")
    private String p2FeedBack;
    
            /**
     * 流量
     */
                    @TableField("flow_rate")
    private String flowRate;
    
            /**
     * 压力
     */
                    @TableField("pressure")
    private String pressure;
    
            /**
     * 液位
     */
                    @TableField("y_position")
    private String yPosition;
    
            /**
     * 泵站1 计时
     */
                    @TableField("p1_count_time")
    private String p1CountTime;
    
            /**
     * 泵2 计时
     */
                    @TableField("p2_count_time")
    private String p2CountTime;
    
            /**
     * A相电压
     */
                    @TableField("a_voltage")
    private String aVoltage;
    
            /**
     * B相电压
     */
                    @TableField("b_voltage")
    private String bVoltage;
    
            /**
     *  C相电压
     */
                    @TableField("c_voltage")
    private String cVoltage;
    
            /**
     * 电流A
     */
                    @TableField("a_electric")
    private String aElectric;
    
            /**
     * 电流B
     */
                    @TableField("b_electric")
    private String bElectric;
    
            /**
     * 电流C
     */
                    @TableField("c_electric")
    private String cElectric;
    
            /**
     * 电能
     */
                    @TableField("electric")
    private String electric;
    
            /**
     * 累计流量
     */
                    @TableField("add_flow_rate")
    private String addFlowRate;
    
            /**
     * 液位 高
     */
                    @TableField("liquid_high")
    private String liquidHigh;
    
            /**
     * 液位 低
     */
                    @TableField("liquid_low")
    private String liquidLow;
    
            /**
     * 泵1 远程
     */
                    @TableField("p1_remote")
    private String p1Remote;
    
            /**
     * 泵1 运行
     */
                    @TableField("p1_run")
    private String p1Run;
    
            /**
     * 泵1 故障
     */
                    @TableField("p1_hitch")
    private String p1Hitch;
    
            /**
     * 泵2 远程
     */
                    @TableField("p2_remote")
    private String p2Remote;
    
            /**
     * 泵2 运行
     */
                    @TableField("p2_run")
    private String p2Run;
    
            /**
     * 泵2 故障
     */
                    @TableField("p2_hitch")
    private String p2Hitch;
    
                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
            @TableField("date")
    private Date date;

    /**
     * 泵1 远程
     */
    private String p3Remote;
    /**
     * 泵1 运行
     */
    private String p3Run;
    /**
     * 泵1 故障
     */
    private String p3Hitch;
    

}
