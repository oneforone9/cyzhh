package com.essence.interfaces.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.interfaces.annotation.ColumnMean;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhy
 * @since 2022/10/28 14:28
 */
@Data
public class WorkorderNewestEsp extends WorkorderBaseEsp {
    private static final long serialVersionUID = 6657146095372665463L;
    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 工单负责人
     */
    @ColumnMean("order_manager")
    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5维修)
     */
    @ColumnMean("order_type")
    private String orderType;

    /**
     * 工单编号
     */
    @ColumnMean("order_code")
    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报)
     */
    @ColumnMean("order_source")
    private String orderSource;

    /**
     * 工单名称
     */
    @ColumnMean("order_name")
    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     */
    @ColumnMean("distribute_type")
    private String distributeType;

    /**
     * 作业区域
     */
    @ColumnMean("location")
    private String location;
    /**
     * 事件主键
     *
     * @mock 12321
     */
    @ColumnMean("event_id")
    private String eventId;

    /**
     * 所属单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 所属单位名称
     */
    @ColumnMean("unit_name")
    private String unitName;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("start_time")
    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     */
    @ColumnMean("time_span")
    private Integer timeSpan;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("end_time")
    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）
     */
    @ColumnMean("order_label")
    private String orderLabel;

    /**
     * 工单描述
     */
    @ColumnMean("order_desc")
    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     */
    @ColumnMean("is_attention")
    private String isAttention;

    /**
     * 经度
     */
    @ColumnMean("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @ColumnMean("lttd")
    private Double lttd;

    /**
     * 是否删除（1是 0否）
     */
    @ColumnMean("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 更新者
     */
    @ColumnMean("updater")
    private String updater;

    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 当前工单处理过程主键
     */
    @ColumnMean("record_id")
    private String recordId;
    /**
     * 工单当前状态
     */
    @ColumnMean("order_status")
    private String orderStatus;
    /**
     * 当前处理人主键
     */
    @ColumnMean("person_id")
    private String personId;
    /**
     * 当前处理人名称
     */
    @ColumnMean("person_name")
    private String personName;
    /**
     * 经办人主键集
     */
    @ColumnMean("operator_ids")
    private String operatorIds;
    /**
     * 开始巡河时间
     */
    @ColumnMean("start_work_time")
    private String startWorkTime;

    /**
     *工单完成巡查时间
     */
    @ColumnMean("end_work_time")
    private String endWorkTime;
    /**
     * 操作
     */
    @ColumnMean("operation")
    private String operation;
    /**
     * 第三方服务公司主键id
     */
    @ColumnMean("company_id")
    private String companyId;

    /**
     * 公司名称
     */
    @ColumnMean("company")
    private String company;
    /**
     * 交办至
     */
    @ColumnMean("send_to")
    private String sendTo;


    /**
     * 河流id
     */
    @ColumnMean("real_id")
    private String realId;
    /**
     * 河流名称
     */
    @ColumnMean("real_name")
    private String realName;

    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @ColumnMean("sttp")
    private String sttp;

    /**
     * 设备设施名称
     */
    @ColumnMean("equipment_name")
    private String equipmentName;

    /**
     * 公司负责人审批意见
     */
    @ColumnMean("opinion")
    private String opinion;

    /**
     * 处理时效
     */
    @ColumnMean("deal_effect")
    private String dealEffect;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("reject_time")
    private Date rejectTime;


    /**
     * 驳回工单开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("reject_start_time")
    private Date rejectStartTime;
    /**
     * '是否发送公众 1 是 2否'
     */ @ColumnMean("sffsgz")
    private Integer sffsgz;
    /**
     * 河管单位意见'
     */ @ColumnMean("hgdwyj")
    private String hgdwyj;
    /**
     * '案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )'
     */ @ColumnMean("eventType")
    private String eventType;
    /**
     * '案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。\\r\\n   涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。\\r\\n    河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。''',
     */ @ColumnMean("eventClass")
    private String eventClass;
    /**
     * '处理前图片',
     */ @ColumnMean("clqtp")
    private String clqtp;
    /**
     * '处理后图片',
     */ @ColumnMean("clhtp")
    private String clhtp;
    /**
     * 是否需要电话回访
     */ @ColumnMean("sfxydhhf")
    private Integer sfxydhhf;
    /**
     * '上报人姓名'
     */ @ColumnMean("sbrxm")
    private String sbrxm;
    /**
     * 上报人手机号
     */ @ColumnMean("sbrsjh")
    private String sbrsjh;
    /**
     * 问题描述
     */ @ColumnMean("wtms")
    private String wtms;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("shsj")
    private Date shsj;
    /**
     * 是否可派发 1 是 2 否
     */
    private String sfkpf;
    /**
     * 处理单位
     */ @ColumnMean("deal_unit")
    private String dealUnit;
    /**
     * 处理描述
     */@ColumnMean("clms")
    private String clms;
    /**
     * 审核意见 1 同意 2 拒绝
     */@ColumnMean("shyj")
    private Integer shyj;
    /**
     * 回退原因
     */@ColumnMean("htyy")
    private String htyy;
    /**
     * 解决方案审核时间
     */@ColumnMean("jjfashsj")
    private String jjfashsj ;
}
