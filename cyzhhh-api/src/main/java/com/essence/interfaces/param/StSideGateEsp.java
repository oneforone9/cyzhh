package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 边闸基础表参数实体
 *
 * @author zhy
 * @since 2023-01-17 11:05:23
 */

@Data
public class StSideGateEsp extends Esp {

    private static final long serialVersionUID = -89538683440479092L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 测站编码：是由全国统一编制的，用于标识涉及报送降水、蒸发、河道、水库、闸坝、泵站、潮汐、沙情、冰情、墒情、地下水、水文预报等信息的各类测站的站码。测站编码具有唯一性，由数字和大写字母组成的8位字符串，按《全国水文测站编码》执行。
     */
    @ColumnMean("stcd")
    private String stcd;
    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    @ColumnMean("stnm")
    private String stnm;
    /**
     * 站址：测站代表点所在地县级以下详细地址。
     */
    @ColumnMean("stlc")
    private String stlc;
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
     * 河系id
     */
    @ColumnMean("river_id")
    private Integer riverId;
    /**
     * 是否为闸站工程
     */
    @ColumnMean("gate_project")
    private String gateProject;
    /**
     * 工程建设情况
     */
    @ColumnMean("project_construction")
    private String projectConstruction;
    /**
     * 建成时间（年）
     */
    @ColumnMean("build_time")
    private String buildTime;
    /**
     * 闸门型式/泵站类型
     */
    @ColumnMean("gate_type")
    private String gateType;
    /**
     * 启闭型式
     */
    @ColumnMean("hoist_type")
    private String hoistType;
    /**
     * 是否有启闭机房
     */
    @ColumnMean("hoist_room")
    private String hoistRoom;
    /**
     * 是否有备用电源
     */
    @ColumnMean("spare_power")
    private String sparePower;
    /**
     * 闸孔数量（孔）
     */
    @ColumnMean("holes_number")
    private String holesNumber;
    /**
     * 水闸类型/水泵类型
     */
    @ColumnMean("sluice_type")
    private String sluiceType;
    /**
     * 是否进行过抗震复核
     */
    @ColumnMean("seismic_review")
    private String seismicReview;
    /**
     * 是否有水位观测设施
     */
    @ColumnMean("observe_device")
    private String observeDevice;
    /**
     * 是否有视频图像监测设施
     */
    @ColumnMean("video_cems")
    private String videoCems;
    /**
     * 监测设施
     */
    @ColumnMean("cems")
    private String cems;
    /**
     * 水闸安全鉴定时间
     */
    @ColumnMean("safety_appraisal_time")
    private String safetyAppraisalTime;
    /**
     * 水闸管理单位名称
     */
    @ColumnMean("management_unit")
    private String managementUnit;
    /**
     * 水闸归口管理部门
     */
    @ColumnMean("management_dept")
    private String managementDept;
    /**
     * 是否完成确权
     */
    @ColumnMean("wcqq")
    private String wcqq;
    /**
     * 是否完成划界
     */
    @ColumnMean("wchj")
    private String wchj;
    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    @ColumnMean("sttp")
    private String sttp;
    /**
     * 是否有流量监测设施
     */
    @ColumnMean("flow_device")
    private String flowDevice;
    /**
     * 流量监测设施
     */
    @ColumnMean("flow")
    private String flow;
    /**
     * 设计过闸流量(m³/s）
     */
    @ColumnMean("design_flow")
    private String designFlow;
    /**
     * 闸底高程（m）
     */
    @ColumnMean("gate_bottom")
    private String gateBottom;
    /**
     * 闸顶高程（m）
     */
    @ColumnMean("gate_top")
    private String gateTop;


    /**
     * 单位id
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 能否远控 1-能远控
     */
    @ColumnMean("remote_control")
    private String remoteControl;

    /**
     * 闸孔总净宽（米）
     */
    @ColumnMean("holes_zjk")
    private String holesZjk;

    /**
     * 闸门1宽(m)
     */
    @ColumnMean("holes_k")
    private String holesK;

    /**
     * 闸门1高(m)
     */
    @ColumnMean("holes_g")
    private String holesG;

    /**
     * 目前状态  1-在用  2-停用  3-建成还未移交
     */
    @ColumnMean("present_status")
    private Integer presentStatus;

    /**
     * 模型是否用到  1-用到  0-没有用到
     */
    @ColumnMean("is_model_used")
    private Integer isModelUsed;

    /**
     * 断面名称
     */
    @ColumnMean("section_name")
    private String sectionName;

    /**
     * 下游断面名称
     */
    @ColumnMean("section_name_down")
    private String sectionNameDown;
    /**
     * 坝长（m）
     */
    @ColumnMean("b_long")
    private String bLong;

    /**
     * 坝高（m）
     */
    @ColumnMean("b_high")
    private String bHigh;
    /**
     * 序列名称
     */
    @ColumnMean("seria_name")
    private String seriaName;
    /**
     * 调水id
     */
    @ColumnMean("transfer_id")
    private Integer transferId;

    /**
     * 高水位(m) [非主汛期、非汛期（8月16日-次年7月19日）]
     */
    @ColumnMean("high_water_level")
    private String highWaterLevel;

    /**
     * 中水位(m) [主汛期（7月20日-8月15日）]
     */
    @ColumnMean("middle_water_level")
    private String middleWaterLevel;
    /**
     * 调度状态
     */
    @ColumnMean("d_type")
    private String dType;
    /**
     * 调度指令
     */
    @ColumnMean("d_order")
    private String dOrder;

    /**
     * 关联的摄像头code
     */
    @ColumnMean("video_code")
    private String videoCode;

    /**
     * 是否属于闸坝维护计划 1-属于，需要按照维护计划生成维保工单
     */
    @ColumnMean("is_plan")
    private Integer isPlan;
}
