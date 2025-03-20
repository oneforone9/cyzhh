package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 绿化保洁工作日志上报表更新实体
 *
 * @author liwy
 * @since 2023-03-14 15:36:46
 */

@Data
public class StGreenReportEsu extends Esu {

    private static final long serialVersionUID = 536394831120020646L;

    /**
     * 主键
     */
    private String id;

    /**
     * 所属河流id
     */
    private String stBRiverId;

    /**
     * 所属河道名称
     */
    private String reaName;

    /**
     * 作业类型 (1巡查 2 保洁 3 绿化 4维保 5运行)
     */
    private String workType;

    /**
     * 人员投入
     */
    private Integer workNum;

    /**
     * 作业单位
     */
    private String workUnit;

    /**
     * 负责人
     */
    private String workManage;

    /**
     * 负责人联系方式
     */
    private String managePhone;

    /**
     * 具体位置（位置信息）
     */
    private String position;

    /**
     * 经度（小程序端上传火星坐标系)
     */
    private Double lgtd;

    /**
     * 纬度（小程序端上传火星坐标系)
     */
    private Double lttd;

    /**
     * 转换之后的经度(pc展示84坐标系)
     */
    private Double changeLgtd;

    /**
     * 转换之后的纬度(pc展示84坐标系)
     */
    private Double changeLttd;

    /**
     * 是否删除（1是 0否）
     */
    private String isDelete;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 备注
     */
    private String remark;

    /**
     * 作业内容集合list
     */
    private List<StGreenReportRelationEsu> workList;


}
