package com.essence.interfaces.model;

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
public class WorkorderNewestEsu extends WorkorderBaseEsu {

    private static final long serialVersionUID = -219747642142694635L;
    /**
     * 当前工单处理过程主键
     */
    private String recordId;
    /**
     * 工单当前状态
     */
    private String orderStatus;
    /**
     * 当前处理人主键
     */
    private String personId;
    /**
     * 当前处理人名称
     */
    private String personName;
    /**
     * 经办人主键集
     */
    private String operatorIds;
    /**
     * 开始巡河时间
     */

    private String startWorkTime;
    /**
     * 操作
     */
    private String operation;

    /**
     * 创建者
     */
    private String creator;
    /**
     * 第三方服务公司主键id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String company;
    /**
     * 交办至
     */

    private String sendTo;

    /**
     * 河流id
     */
    private String realId;
    /**
     * 河流名称
     */
    private String realName;

    /**
     * 公司负责人审批意见
     */
    private String opinion;

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
     */ @ColumnMean("deal_unit")
    private String dealUnit;
    /**
     * 处理描述
     */@ColumnMean("clms")
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
     */@ColumnMean("jjfashsj")
    private String jjfashsj ;
}
