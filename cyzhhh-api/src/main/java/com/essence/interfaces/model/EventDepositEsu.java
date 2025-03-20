package com.essence.interfaces.model;

import com.essence.common.constant.ItemConstant;
import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 事件基础信息表更新实体
 *
 * @author zhy
 * @since 2022-10-30 18:06:22
 */

@Data
public class EventDepositEsu extends Esu {

    private static final long serialVersionUID = 840081124620833221L;


    /**
     * 主键
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 事件名称
     * @mock xxx事件
     */
    @Size(max = 100, message = "事件名称的最大长度:100")
    @NotEmpty(groups = Insert.class, message = "事件名称不能为空")
    private String eventName;

    /**
     * 所属河道主键
     * @mock 1
     */
    @NotEmpty(groups = Insert.class, message = "所属河道主键不能为空")
    private String reaId;

    /**
     * 所属河道名称
     * @mock 朝阳干渠
     */
    @Size(max = 50, message = "所属河道名称的最大长度:50")
    @NotEmpty(groups = Insert.class, message = "所属河道名称不能为空")
    private String reaName;

    /**
     * 管辖单位主键
     * @mock 1
     */
    @Size(max = 32, message = "管辖单位主键的最大长度:32")
    @NotEmpty(groups = Insert.class, message = "管辖单位主键不能为空")
    private String unitId;

    /**
     * 管辖单位
     * @mock 河道管理一所
     */
    @Size(max = 20, message = "管辖单位的最大长度:20")
    @NotEmpty(groups = Insert.class, message = "管辖单位不能为空")
    private String unitName;

    /**
     * 案件渠道(1市级交办 2 区级交办 3 未诉先办 4 12345 5 网络上报)
     * @mock 3
     */
    @Size(max = 1, message = "案件渠道的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "案件渠道不能为空")
    private String eventChannel;

    /**
     * 案件编号
     * @mock 202221001
     */
    @Size(max = 40, message = "案件编号的最大长度:40")
    private String eventCode;

    /**
     * 案件类型(1 水环境案件 2 涉河工程和有关活动案件 3 河道附属设施案件 )
     * @mock 1
     */
    @Size(max = 1, message = "备注的最大长度:1")
    @NotEmpty(groups = Insert.class, message = "案件类型不能为空")
    private String eventType;

    /**
     * 案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。
     * 涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。
     * 河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。
     * @mock 21
     */
    @Size(max = 2, message = "备注的最大长度:2")
    @NotEmpty(groups = Insert.class, message = "案件分类不能为空")
    private String eventClass;

    /**
     * 工单主键(生成事件的工单主键)
     * @mock  72305610752
     */
    private String orderId;

    /**
     * 案件情况
     * @mock 案件情况xxxx
     */
    @Size(max = 400, message = "案件情况的最大长度:400")
    private String eventSituation;

    /**
     * 问题描述
     * @mock 问题一
     */
    @Size(max = 400, message = "问题描述的最大长度:400")
    private String problemDesc;

    /**
     * 具体位置
     * @mock 北京
     */
    @Size(max = 200, message = "具体位置的最大长度:200")
    private String position;

    /**
     * 案件时间
     * @mock 2022-10-31 10:55:00
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventTime;

    /**
     * 截止时间
     * @mock 2022-11-01 10:55:00
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 处理情况
     * @mock 完成
     */
    @Size(max = 400, message = "处理情况的最大长度:400")
    private String result;

    /**
     * 经度 （小程序端上传火星坐标系)
     * @mock 121.11
     */
    private Double lgtd;

    /**
     * 纬度 （小程序端上传火星坐标系)
     * @mock 11.11
     */
    private Double lttd;
    /**
     * 转换之后经度(pc展示84坐标系)
     */
    private Double changeLgtd;

    /**
     * 转换之后纬度(pc展示84坐标系)
     */
    private Double changeLttd;

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
     * 关联的文件id
     *
     * @mock [1111, 222, 333]
     */
    private List<String> fileIds;




    @AssertTrue(message = "案件渠道请从以下选择[市级交办,区级交办,未诉先办,12345,网络上报]")
    private boolean isEventChannel() {
        if (null == this.eventChannel) {
            return true;
        }
        if (ItemConstant.EVENT_CHANNEL_CITY.equals(this.eventChannel) ||
                ItemConstant.EVENT_CHANNEL_COUNTY.equals(this.eventChannel) ||
                ItemConstant.EVENT_CHANNEL_NO_APPEAL.equals(this.eventChannel) ||
                ItemConstant.EVENT_CHANNEL_12345.equals(this.eventChannel) ||
                ItemConstant.EVENT_CHANNEL_NETWORK.equals(this.eventChannel)) {
            return true;
        }
        return false;
    }
    @AssertTrue(message = "案件类型请从以下选择[水环境案件,涉河工程和有关活动案件,河道附属设施案件]")
    private boolean isEventType() {
        if (null == this.eventType) {
            return true;
        }
        if (ItemConstant.EVENT_TYPE_SHJ.equals(this.eventType) ||
                ItemConstant.EVENT_TYPE_SHGC.equals(this.eventType) ||
                ItemConstant.EVENT_TYPE_HDSS.equals(this.eventType)) {
            return true;
        }
        return false;
    }
    @AssertTrue(message = "案件分类选择错误")
    private boolean isEventClass() {
        if (null == this.eventClass) {
            return true;
        }
        if (ItemConstant.EVENT_CLASS_11.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_12.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_13.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_14.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_15.equals(this.eventClass) ||

                ItemConstant.EVENT_CLASS_21.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_22.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_23.equals(this.eventClass) ||

                ItemConstant.EVENT_CLASS_31.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_32.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_33.equals(this.eventClass) ||
                ItemConstant.EVENT_CLASS_34.equals(this.eventClass)) {
            return true;
        }
        return false;
    }
}
