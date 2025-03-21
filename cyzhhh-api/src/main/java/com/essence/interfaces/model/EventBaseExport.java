package com.essence.interfaces.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 事件基础信息表返回实体
 *
 * @author majunjie
 * @since 2023-06-01 18:06:22
 */

@Data
public class EventBaseExport  {

    /**
     * 案件编号
     */
    @ExcelProperty(value="案件编号")
    private String eventCode;
    /**
     * 所属河道
     */
    @ExcelProperty(value="所属河道")
    private String reaName;

    /**
     * 案件渠道(1市级交办 2 区级交办 3 自查问题 4 12345 5 网络上报6摄像头抓拍)
     */
    @ExcelProperty(value="案件渠道")
    private String eventChannel;
    /**
     * 案件分类 其中水环境案件又分为5小类：（11）河道岸坡、水面保洁情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（12）河道绿化养护情况，是否有不符合相关标准和要求的管理行为和环境卫生问题；（13）河道水质是否异常，是否存在污水入河现象；（14）是否有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱倒、乱放行为；（15）管理范围内是否倾倒垃圾和渣土、堆放非防汛物资。
     * 涉河工程和有关活动案件分为3小类：（21）是否有防洪工程设施未经验收，即将建设项目投入生产或者使用的行为；（22）在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，是否经我局批准后开工建设，建成后是否经我局相关河道管理单位验收合格后投入使用；（23）在河道管理范围内是否有法律法规禁止的不当行为；在河湖管理范围、保护范围内是否经我局批准擅自进行的有关活动。
     * 河道附属设施案件分为4小类：（31）水闸设施建筑物是否完好；(32)泵站设施是否正常运行使用；（33）边闸设施是否出现污水入河；（34）其他附属设施是否损坏、构筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损毁护堤护岸林木。
     11: '河道岸坡、水面保洁情况，有不符合相关标准和要求的管理行为和环境卫生问题',
     12: '河道绿化养护情况，有不符合相关标准和要求的管理行为和环境卫生问题',
     13: '河道水质异常，存在污水入河现象',
     14: '有法律法规禁止的有毒、有害垃圾、废弃物、污染物等乱到、乱放行为',
     15: '管理范围内倾倒垃圾和渣土、堆放非防汛物资',
     21: '有防洪工程设施未验收，即将建设项目投入生产或者使用的行为',
     22: '在河湖上新建、扩建以及改建的各类工程和在河湖管理范围、保护范围内的有关建设项目，经我局批准后开工建设，建成后经我局相关河道管理单位验收合格后投入使用',
     23: '在河道管理范围内有法律法规禁止的不当行为; 在河湖管理范围、保护范围内未经我局批准擅自进行的有关活动',
     31: '水闸设施建筑物完好',
     32: '泵站设施正常运行',
     33: '边闸设施出现污水入河',
     34: '其他附属设施出现损坏，建筑物及防汛、水工水文监测和测量、河岸地质监测、通讯、照明、滨河道路以及其他附属设备与设施，损坏堤护岸林木',
     */
    @ExcelProperty(value="案件分类")
    private String eventClass;
    /**
     * 问题描述
     */
    @ExcelProperty(value="问题描述")
    private String problemDesc;
    /**
     * 具体位置
     */
    @ExcelProperty(value="具体位置")
    private String position;
    /**
     * 案件时间
     */

    @ExcelProperty(value="案件时间")
    private String eventTime;

    /**
     * 截止时间
     */
    @ExcelProperty(value="截止时间")
    private String endTime;
    /**
     * 案件状态 0 未办  1 已办
     */
    @ExcelProperty(value="案件状态")
    private String status;

    /**
     * 处理单位名称
     */
    @ExcelProperty(value="处理单位名称")
    private String disposeName;
}
