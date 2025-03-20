package com.essence.interfaces.model;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单基础信息表返回实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:24
 */

@Data
public class WorkorderBaseEsr extends Esr {

    private static final long serialVersionUID = -50577681575542846L;


    /**
     * 主键
     */
    private String id;

    /**
     * 工单负责人
     */
    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5维修 6养护)
     */
    private String orderType;

    /**
     * 工单编号
     */
    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报)
     */
    private String orderSource;

    /**
     * 工单名称
     */
    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     */
    private String distributeType;

    /**
     * 作业区域
     */
    private String location;
    /**
     * 事件主键
     * @mock 12321
     */
    private String eventId;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 创建时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     */
    private Integer timeSpan;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）
     */
    private String orderLabel;

    /**
     * 工单描述
     */
    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     */
    private String isAttention;

    /**
     * 经度
     */
    private Double lgtd;

    /**
     * 纬度
     */
    private Double lttd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 交办至 1 北京金都园林绿化有限公司 2、北京金河生态科技有限公司 3、北京鸿运通生态科技有限公司 4、北京朝水环境治理有限公司 5、河道管理一所 6、河道管理二所
     */
    private String sendTo;
    /**
     * 所属河道id
     */
    private String realId;
    /**
     * 所属河道id
     */
    private String realName;

    /**
     * 开始巡河时间
     */
    private String startWorkTime;

    /**
     * 工单完成巡查时间
     */
    private String endWorkTime;

    /**
     * 第三方服务公司主键id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 处理时效
     */
    private String dealEffect;

    /**
     * 类型 DP-泵站 DD-水闸 SB-水坝 BZ-边闸
     */
    private String sttp;
    /**
     * 设备设施名称
     */
    private String equipmentName;
    /**
     *日常维护内容
     */
    private String serviceContent;
    /**
     * 创建者
     * @ignore
     */
    private String creator;

    /**
     *测站名称
     */
    private String stnm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date rejectTime;

    /**
     * 驳回工单开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

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
    /**
     * 审核时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shsj;

    /**
     * 是否可派发 1 是 2 否
     */
    private String sfkpf;

    private String dealUnit;
    /**
     * 处理描述
     */
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
     */
    private String jjfashsj ;
}
