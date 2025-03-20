package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * 闸坝运行维保日志上报表返回实体
 *
 * @author liwy
 * @since 2023-03-15 11:56:50
 */

@Data
public class StGatedamReportEsr extends Esr {

    private static final long serialVersionUID = 448256340527107379L;


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
     * 上报的图片文件集合
     */
    private List<FileBaseEsr> reportFileList;


    /**
     * 作业内容集合list
     */
    private  List<StGatedamReportRelationEsr> stGatedamReportRelationEsrList;

    /**
     * 累计巡查次数
     */
    private String sumCount;
    /**
     * 所属管辖单位id
     */
    private String unitId;
    /**
     * 所属管辖单位
     */
    private String unitName;
    /**
     *最新作业内容
     */
    private String workContent;


}
