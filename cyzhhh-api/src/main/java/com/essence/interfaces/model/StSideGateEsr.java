package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 边闸基础表返回实体
 *
 * @author zhy
 * @since 2023-01-17 11:05:24
 */

@Data
public class StSideGateEsr extends Esr {

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
     * 站址：测站代表点所在地县级以下详细地址。
     */
    private String stlc;


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
     * 是否为闸站工程
     */
    private String gateProject;


    /**
     * 工程建设情况
     */
    private String projectConstruction;


    /**
     * 建成时间（年）
     */
    private String buildTime;


    /**
     * 闸门型式/泵站类型
     */
    private String gateType;


    /**
     * 启闭型式
     */
    private String hoistType;


    /**
     * 是否有启闭机房
     */
    private String hoistRoom;


    /**
     * 是否有备用电源
     */
    private String sparePower;


    /**
     * 闸孔数量（孔）
     */
    private String holesNumber;


    /**
     * 水闸类型/水泵类型
     */
    private String sluiceType;


    /**
     * 是否进行过抗震复核
     */
    private String seismicReview;


    /**
     * 是否有水位观测设施
     */
    private String observeDevice;


    /**
     * 是否有视频图像监测设施
     */
    private String videoCems;


    /**
     * 监测设施
     */
    private String cems;


    /**
     * 水闸安全鉴定时间
     */
    private String safetyAppraisalTime;


    /**
     * 水闸管理单位名称
     */
    private String managementUnit;


    /**
     * 水闸归口管理部门
     */
    private String managementDept;


    /**
     * 是否完成确权
     */
    private String wcqq;


    /**
     * 是否完成划界
     */
    private String wchj;


    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸
     */
    private String sttp;


    /**
     * 是否有流量监测设施
     */
    private String flowDevice;


    /**
     * 流量监测设施
     */
    private String flow;


    /**
     * 设计过闸流量(m³/s）
     */
    private String designFlow;


    /**
     * 闸底高程（m）
     */
    private String gateBottom;


    /**
     * 闸顶高程（m）
     */
    private String gateTop;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 能否远控 1-能远控
     */
    private String remoteControl;

    /**
     * 闸孔总净宽（米）
     */
    private String holesZjk;

    /**
     * 闸门1宽(m)
     */
    private String holesK;

    /**
     * 闸门1高(m)
     */
    private String holesG;


    /**
     * 目前状态  1-在用  2-停用  3-建成还未移交
     */
    private Integer presentStatus;

    /**
     * 模型是否用到  1-用到  0-没有用到
     */
    private Integer isModelUsed;

    /**
     * 断面名称
     */
    private String sectionName;

    /**
     * 下游断面名称
     */
    private String sectionNameDown;
    /**
     * 坝长（m）
     */
    private String bLong;

    /**
     * 坝高（m）
     */
    private String bHigh;

    /**
     * 序列名称
     */
    private String seriaName;
    /**
     * 调水id
     */
    private Integer transferId;

    /**
     * 高水位(m) [非主汛期、非汛期（8月16日-次年7月19日）]
     */
    private String highWaterLevel;

    /**
     * 中水位(m) [主汛期（7月20日-8月15日）]
     */
    private String middleWaterLevel;
    /**
     * 1 固定值 2 策略控制
     */
    private int controlType = 1;
    /**
     * 1 开 2 关
     */
    private String controlValue;
    /**
     * 起液位
     */
    private String liquidLevel;

    /**
     * 停液位
     */
    private String stopLevel;
    /**
     * 调度状态
     */
    private String dType;
    /**
     * 调度指令
     */
    private String dOrder;

    /**
     * 关联的摄像头code
     */
    private String videoCode;
    /**
     * 是否属于闸坝维护计划 1-属于，需要按照维护计划生成维保工单
     */
    private Integer isPlan;

    private Double jd;

    private Double wd;

}
