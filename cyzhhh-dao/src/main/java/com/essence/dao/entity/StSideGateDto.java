package com.essence.dao.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 边闸基础表实体
 *
 * @author BINX
 * @since 2023-01-17 11:05:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_side_gate")
public class StSideGateDto extends Model<StSideGateDto> {

    private static final long serialVersionUID = -95379598252892200L;
        
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
        
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
     * 站址：测站代表点所在地县级以下详细地址。
     */
    @TableField("stlc")
    private String stlc;
    
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
     * 闸门型式/泵站类型
     */
    @TableField("gate_type")
    private String gateType;
    
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
     * 监测设施
     */
    @TableField("cems")
    private String cems;
    
    /**
     * 水闸安全鉴定时间
     */
    @TableField("safety_appraisal_time")
    private String safetyAppraisalTime;
    
    /**
     * 水闸管理单位名称
     */
    @TableField("management_unit")
    private String managementUnit;
    
    /**
     * 水闸归口管理部门
     */
    @TableField("management_dept")
    private String managementDept;
    
    /**
     * 是否完成确权
     */
    @TableField("wcqq")
    private String wcqq;
    
    /**
     * 是否完成划界
     */
    @TableField("wchj")
    private String wchj;
    
    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    @TableField("sttp")
    private String sttp;
    
    /**
     * 是否有流量监测设施
     */
    @TableField("flow_device")
    private String flowDevice;
    
    /**
     * 流量监测设施
     */
    @TableField("flow")
    private String flow;
    
    /**
     * 设计过闸流量(m³/s）
     */
    @TableField("design_flow")
    private String designFlow;
    
    /**
     * 闸底高程（m）
     */
    @TableField("gate_bottom")
    private String gateBottom;
    
    /**
     * 闸顶高程（m）
     */
    @TableField("gate_top")
    private String gateTop;

    /**
     * 单位id
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 能否远控 1-能远控
     */
    @TableField("remote_control")
    private String remoteControl;

    /**
     * 闸孔总净宽（米）
     */
    @TableField("holes_zjk")
    private String holesZjk;

    /**
     * 闸门1宽(m)
     */
    @TableField("holes_k")
    private String holesK;

    /**
     * 闸门1高(m)
     */
    @TableField("holes_g")
    private String holesG;

    /**
     * 目前状态  1-在用  2-停用  3-建成还未移交
     */
    @TableField("present_status")
    private Integer presentStatus;

    /**
     * 模型是否用到  1-用到  0-没有用到
     */
    @TableField("is_model_used")
    private Integer isModelUsed;

    /**
     * 断面名称
     */
    @TableField("section_name")
    private String sectionName;

    /**
     * 断面名称
     */
    @TableField("section_name2")
    private String sectionName2;

    /**
     * 下游断面名称
     */
    @TableField("section_name_down")
    private String sectionNameDown;
    /**
     * 坝长（m）
     */
    @TableField("b_long")
    private String bLong;

    /**
     * 坝高（m）
     */
    @TableField("b_high")
    private String bHigh;


    /**
     * 序列名称
     */
    @TableField("seria_name")
    private String seriaName;

    /**
     * 调水id
     */
    @TableField("transfer_id")
    private Integer transferId;

    /**
     * 高水位(m) [非主汛期、非汛期（8月16日-次年7月19日）]
     */
    @TableField("high_water_level")
    private String highWaterLevel;

    /**
     * 中水位(m) [主汛期（7月20日-8月15日）]
     */
    @TableField("middle_water_level")
    private String middleWaterLevel;
    /**
     * 起液位
     */
    @TableField("liquid_level")
    private String liquidLevel;

    /**
     * 停液位
     */
    @TableField("stop_level")
    private String stopLevel;
    /**
     * 调度状态
     */
    @TableField("d_type")
    private String dType;
    /**
     * 调度指令
     */
    @TableField("d_order")
    private String dOrder;
    @TableField("b_default")
    private String bDefault;


    /**
     * 关联的摄像头code
     */
    @TableField("video_code")
    private String videoCode;

    /**
     * 警戒水位
     */
    @TableField("wrz")
    private BigDecimal wrz;

    /**
     * 最高水位
     */
    @TableField("bhtz")
    private BigDecimal bhtz;

    /**
     * 是否属于闸坝维护计划 1-属于，需要按照维护计划生成维保工单
     */
    @TableField("is_plan")
    private BigDecimal isPlan;

    private String lot;

    /**
     * 负责人a
     */
    @ExcelIgnore
    private String namea;
    /**
     * 负责人b
     */
    @ExcelIgnore
    private String nameb;
    /**
     * 负责人c
     */
    @ExcelIgnore
    private String namec;
}
