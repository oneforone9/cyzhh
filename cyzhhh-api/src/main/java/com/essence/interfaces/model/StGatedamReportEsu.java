package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 闸坝运行维保日志上报表更新实体
 *
 * @author liwy
 * @since 2023-03-15 11:56:33
 */

@Data
public class StGatedamReportEsu extends Esu {

    private static final long serialVersionUID = 143522206881941013L;

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
     * 作业类型（ 养维护修  运行巡查）
     */
    private String workType;

    /**
     * 站点类型 BZ-边闸  DP-泵站 DD-闸  SB-水坝
     */
    private String sttp;
    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称(闸坝名称)
     */
    private String stnm;

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
     * 检查结论
     */
    private String conclusion;


    /**
     * 作业内容集合list
     */
    private List<StGatedamReportRelationEsu> workList;

    /**
     * 关联的文件id
     *
     * @mock [1111, 222, 333]
     */
    private List<String> fileIds;



}
