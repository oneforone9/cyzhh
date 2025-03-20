package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.common.constant.ItemConstant;
import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 工单基础信息表更新实体
 *
 * @author zhy
 * @since 2022-10-27 15:26:22
 */

@Data
public class WorkorderBaseEsu extends Esu {

    private static final long serialVersionUID = 135820168685960855L;


    /**
     * 主键
     * @mock 1
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 处理人id
     * @mock 1
     */
    @Size(max = 32, message = "处理人id的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "处理人id不能为空")
    private String personId;
    /**
     * 处理人名称
     * @mock 张三
     */
    @Size(max = 20, message = "处理人名称的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "主键不能为空")
    private String personName;
    /**
     * 工单负责人
     * @mock 李四
     */
    @Size(max = 20, message = "工单负责人的最大长度:20")
    private String orderManager;

    /**
     * 工单类型(1巡查 2 保洁 3 绿化 4维保 5运行)
     * @mock 1
     */
    @NotEmpty(groups = Insert.class, message = "工单类型不能为空")
    private String orderType;

    /**
     * 工单编号
     * @mock 20221028XC001
     */
    @Size(max = 20, message = "工单编号的最大长度:20")
    private String orderCode;

    /**
     * 工单来源(1 计划生成 2 巡查上报 3 临时生成)
     * @mock 2
     */
    @Size(max = 1, message = "工单来源的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "工单来源不能为空")
    private String orderSource;

    /**
     * 工单名称
     * @mock 巡河工单一
     */
    @Size(max = 200, message = "工单名称的最大长度:200")
    @NotEmpty(groups = Insert.class, message = "工单名称不能为空")
    private String orderName;

    /**
     * 派发方式(1人工派发 2自动派发)
     * @mock 1
     */
    @Size(max = 1, message = "派发方式的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "派发方式不能为空")
    private String distributeType;

    /**
     * 作业区域
     * @mock 河道一
     */
    @Size(max = 100, message = "作业区域的最大长度:100")
    private String location;

    /**
     *重点巡查位置主键
     */
    private String focusPositionId;
    /**
     * 事件主键
     * @mock 12321
     */
    private String eventId;
    /**
     * 所属单位主键
     * @mock 1
     */
    @Size(max = 32, message = "所属单位主键的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "所属单位主键不能为空")
    private String unitId;

    /**
     * 所属单位名称
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "所属单位名称的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "所属单位名称不能为空")
    private String unitName;

    /**
     * 创建时间
     * @mock 2022-10-28 9:59:00
     */
    @NotNull(groups = Insert.class, message = "创建时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 处理时段（以分钟为单位）
     * @mock 600
     */
    @NotNull(groups = Insert.class, message = "处理时段不能为空")
    private Integer timeSpan;

    /**
     * 截止时间
     * @mock 2022-10-28 19:59:00
     */
    @NotNull(groups = Insert.class, message = "截止时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 工单标签（1市级交办 2区级交办 3局交办）4 自查问题 5、12345上报 6、网格上报
     * @mock 1
     */
    @Size(max = 1, message = "工单标签的最大长度:1")
    private String orderLabel;

    /**
     * 工单描述
     * @mock 工单描述
     */
    @Size(max = 400, message = "备注的最大长度:400")
    private String orderDesc;

    /**
     * 是否关注（1是 0否）
     * @mock 1
     */
    @Size(max = 1, message = "是否关注的最大长度:1")
    private String isAttention;

    /**
     * 经度
     * @mock 123.11
     */
    private Double lgtd;

    /**
     * 纬度
     * @mock 23.111
     */
    private Double lttd;

    /**
     * 是否删除（1是 0否）
     * @ignore
     */
    private String isDelete;

    /**
     * 新增时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 审核时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shsj;

    /**
     * 创建者
     * @ignore
     */
    private String creator;

    /**
     * 更新者
     * @ignore
     */
    private String updater;

    /**
     * 备注
     * @mock 备注
     */
    @Size(max = 1000, message = "备注的最大长度:1000")
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
     * 关联的文件id
     *
     * @mock [1111, 222, 333]
     */
    private List<String> fileIds;

    // ----------------枚举类型字段校验-----------------
    @AssertTrue(message = "工单类型请从以下选择: [巡查,保洁,绿化,维保,运行]")
    private boolean  isOrderType(){
        if (null == this.orderType){
            return true;
        }
        if (ItemConstant.ORDER_TYPE_XC.equals(this.orderType) || ItemConstant.ORDER_TYPE_BJ.equals(this.orderType)|| ItemConstant.ORDER_TYPE_LH.equals(this.orderType)|| ItemConstant.ORDER_TYPE_WB.equals(this.orderType)|| ItemConstant.ORDER_TYPE_YX.equals(this.orderType)){
            return true;
        }
        return false;
    }

    @AssertTrue(message = "是否关注请从以下选择: [是,否]")
    private boolean  isAttention(){
        if (null == this.isAttention){
            return true;
        }
        if (ItemConstant.ORDER_NO_ATTENTION.equals(this.isAttention) || ItemConstant.ORDER_IS_ATTENTION.equals(this.isAttention)){
            return true;
        }
        return false;
    }


    /**
     * 开始巡河时间
     */
    private String startWorkTime;

    /**
     * 工单完成巡查时间
     */
    private String endWorkTime;


    /**
     * 工单状态（2--未开始）
     */
    private String orderStatus;
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
