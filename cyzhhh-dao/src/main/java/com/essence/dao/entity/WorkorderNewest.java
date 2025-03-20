package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhy
 * @since 2022/10/28 14:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "v_workorder_newest")
public class WorkorderNewest extends Model<WorkorderNewest> {


    private static final long serialVersionUID = -872046236909588801L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 工单负责人
     */
    @TableField("order_manager")
    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5维修)
     */
    @TableField("order_type")
    private String orderType;

    /**
     * 工单编号
     */
    @TableField("order_code")
    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报)
     */
    @TableField("order_source")
    private String orderSource;

    /**
     * 工单名称
     */
    @TableField("order_name")
    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     */
    @TableField("distribute_type")
    private String distributeType;

    /**
     * 作业区域
     */
    @TableField("location")
    private String location;
    /**
     * 事件主键
     */
    @TableField("event_id")
    private String eventId;

    /**
     * 所属单位主键
     */
    @TableField("unit_id")
    private String unitId;

    /**
     * 所属单位名称
     */
    @TableField("unit_name")
    private String unitName;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     */
    @TableField("time_span")
    private Integer timeSpan;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）
     */
    @TableField("order_label")
    private String orderLabel;

    /**
     * 工单描述
     */
    @TableField("order_desc")
    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     */
    @TableField("is_attention")
    private String isAttention;

    /**
     * 经度
     */
    @TableField("lgtd")
    private Double lgtd;

    /**
     * 纬度
     */
    @TableField("lttd")
    private Double lttd;

    /**
     * 是否删除（1是 0否）
     */
    @TableField("is_delete")
    private String isDelete;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 当前工单处理过程主键
     */
    @TableField("record_id")
    private String recordId;

    /**
     * 工单当前状态
     */
    @TableField("order_status")
    private String orderStatus;
    /**
     * 当前处理人主键
     */
    @TableField("person_id")
    private String personId;
    /**
     * 当前处理人名称
     */
    @TableField("person_name")
    private String personName;
    /**
     * 经办人主键集
     */
    @TableField("operator_ids")
    private String operatorIds;
    /**
     * 开始巡河时间
     */
    @TableField("start_work_time")
    private String startWorkTime;

    /**
     *工单完成巡查时间
     */
    @TableField("end_work_time")
    private String endWorkTime;
    /**
     * 操作
     */

    private String operation;
    /**
     * 第三方服务公司主键id
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("company")
    private String company;
    /**
     * 交办至
     */
    @TableField("send_to")
    private String sendTo;

    /**
     * 河流id
     */
    @TableField("real_id")
    private String realId;
    /**
     * 河流名称
     */
    @TableField("real_name")
    private String realName;

    /**
     * 处理时效
     */
    @TableField("deal_effect")
    private String dealEffect;

    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    @TableField("sttp")
    private String sttp;

    /**
     * 设备设施名称
     */
    @TableField("equipment_name")
    private String equipmentName;

    /**
     *日常维护内容
     */
    @TableField("service_content")
    private String serviceContent;

    /**
     * 公司负责人审批意见
     */
    @TableField("opinion")
    private String opinion;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("reject_time")
    private Date rejectTime;

    /**
     * 驳回工单开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("reject_start_time")
    private Date rejectStartTime;
    /**
     * 整改驳回时效
     */
    private String rejectEffect;
    /**
     * '是否发送公众 1 是 2否'
     */
    private Integer sffsgz;
    /**
     * 河管单位意见'
     */
    private String hgdwyj;
    /**
     * '案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )'
     */
    private String eventType;
    /**
     * '案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。\\r\\n   涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。\\r\\n    河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。''',
     */
    private String eventClass;
    /**
     * '处理前图片',
     */
    private String clqtp;
    /**
     * '处理后图片',
     */
    private String clhtp;
    /**
     * 是否需要电话回访
     */
    private Integer sfxydhhf;
    /**
     * '上报人姓名'
     */
    private String sbrxm;
    /**
     * 上报人手机号
     */
    private String sbrsjh;
    /**
     * 问题描述
     */
    private String wtms;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("shsj")
    private Date shsj;
    /**
     * 是否可派发 1 是 2 否
     */
    private String sfkpf;
    /**
     * 处理单位
     */ @TableField("deal_unit")
    private String dealUnit;
    /**
     * 处理描述
     */
    private String clms;
    /**
     * 审核意见 1 同意 2 拒绝
     */
    private Integer shyj;
    /**
     * 回退原因
     */
    private String htyy;
    /**
     * 解决方案审核时间
     */
    private String jjfashsj ;
}
