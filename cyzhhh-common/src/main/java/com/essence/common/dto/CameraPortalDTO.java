package com.essence.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class CameraPortalDTO implements Serializable {
    /**
     * 事件名称
     */

    private String eventName;

    /**
     * 所属河道主键
     */

    private String reaId;

    /**
     * 所属河道名称
     */

    private String reaName;

    /**
     * 管辖单位主键
     */

    private String unitId;

    /**
     * 管辖单位
     */

    private String unitName;

    /**
     * 案件渠道(1市级交办 2 区级交办 3 未诉先办 4 12345 5 网络上报 6 摄像头抓拍)
     */

    private String eventChannel;

    /**
     * 案件编号
     */

    private String eventCode;

    /**
     * 案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件)
     */

    private String eventType;

    /**
     * 案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。
     * 涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。
     * 河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。
     */

    private String eventClass;

    /**
     * 工单主键(生成事件的工单主键)
     */

    private String orderId;

    /**
     * 案件情况
     */

    private String eventSituation;

    /**
     * 问题描述
     */

    private String problemDesc;

    /**
     * 具体位置
     */

    private String position;

    /**
     * 案件时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date eventTime;

    /**
     * 截止时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date endTime;

    /**
     * 处理情况
     */

    private String result;

    /**
     * 经度
     */

    private Double lgtd;

    /**
     * 纬度
     */

    private Double lttd;
    /**
     * 转换之后经度
     */

    private Double changeLgtd;

    /**
     * 转换之后纬度
     */

    private Double changeLttd;

    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    private Date gmtCreate;

    /**
     * 是否已经做了处理 0 未处理 1 已处理
     */

    private String status;
    /**
     * 是否已经做了处理 0 未处理 1 已处理
     */

    private String sendStatus;

    private int dbAlarmID;

    private String fileUrl;
}
