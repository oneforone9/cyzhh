package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 事件基础信息表参数实体
 *
 * @author zhy
 * @since 2022-10-30 18:06:22
 */

@Data
public class EventBaseEsp extends Esp {

    private static final long serialVersionUID = 613599406043397894L;


    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;

    /**
     * 事件名称
     */
    @ColumnMean("event_name")
    private String eventName;

    /**
     * 所属河道主键
     */
    @ColumnMean("rea_id")
    private String reaId;

    /**
     * 所属河道名称
     */
    @ColumnMean("rea_name")
    private String reaName;

    /**
     * 管辖单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;

    /**
     * 管辖单位
     */
    @ColumnMean("unit_name")
    private String unitName;

    /**
     * 案件渠道(1市级交办 2 区级交办 3 未诉先办 4 12345 5 网络上报)
     */
    @ColumnMean("event_channel")
    private String eventChannel;

    /**
     * 案件编号
     */
    @ColumnMean("event_code")
    private String eventCode;

    /**
     * 案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )
     */
    @ColumnMean("event_type")
    private String eventType;

    /**
     * 案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。
     * 涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。
     * 河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。
     */
    @ColumnMean("event_class")
    private String eventClass;

    /**
     * 工单主键(生成事件的工单主键)
     */
    @ColumnMean("order_id")
    private String orderId;

    /**
     * 案件情况
     */
    @ColumnMean("event_situation")
    private String eventSituation;

    /**
     * 问题描述
     */
    @ColumnMean("problem_desc")
    private String problemDesc;

    /**
     * 具体位置
     */
    @ColumnMean("position")
    private String position;

    /**
     * 案件时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("event_time")
    private Date eventTime;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("end_time")
    private Date endTime;

    /**
     * 处理情况
     */
    @ColumnMean("result")
    private String result;

    /**
     * 经度（小程序端上传火星坐标系)
     */
    @ColumnMean("lgtd")
    private Double lgtd;
    /**
     * 纬度（小程序端上传火星坐标系)
     */
    @ColumnMean("lttd")
    private Double lttd;
    /**
     * 转换之后经度(pc展示84坐标系)
     */
    @ColumnMean("change_lgtd")
    private Double changeLgtd;

    /**
     * 转换之后纬度(pc展示84坐标系)
     */
    @ColumnMean("change_lttd")
    private Double changeLttd;


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
     * 是否已经做了处理 0 未处理 1 已处理
     */
    @ColumnMean("status")
    private String status;
    /**
     * 是否已经做了处理 0 未处理 1 已处理
     * 事件是否完成
     */
    @ColumnMean("send_status")
    private String sendStatus;
    /**
     * 工单负责人
     */
    @ColumnMean("order_manager")
    private String orderManager;

    /**
     * 处理单位名称
     */
    @ColumnMean("dispose_name")
    private String disposeName;
    /**
     * 处理单位（1-第三方公司 2-水环境组 3-闸坝设施组）
     */
    @ColumnMean("deal_unit")
    private Integer dealUnit;
    /**
     * 处理人
     */
    @ColumnMean("dispose_person")
    private String disposePerson;
    /**
     * 处理人id
     */
    @ColumnMean("dispose_id")
    private String disposeId;
    /**
     * 处理人手机号
     */
    @ColumnMean("dispose_phone")
    private String disposePhone;
    /**
     * 类型(1巡查 2 保洁 3 绿化 4维保 5运行)
     */
    @ColumnMean("order_type")
    private String orderType;
    /**
     * 处理时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disposeTime;
}
