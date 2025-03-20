package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 *绿化保洁工作日志上报表参数实体
 *
 * @author liwy
 * @since 2023-03-14 15:36:50
 */

@Data
public class StGreenReportEsp extends Esp {

    private static final long serialVersionUID = 252645974345360858L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 所属河流id
     */
    @ColumnMean("st_b_river_id")
    private String stBRiverId;
    /**
     * 所属河道名称
     */
    @ColumnMean("rea_name")
    private String reaName;
    /**
     * 作业类型
     */
    @ColumnMean("work_type")
    private String workType;
    /**
     * 人员投入
     */
    @ColumnMean("work_num")
    private Integer workNum;
    /**
     * 作业单位
     */
    @ColumnMean("work_unit")
    private String workUnit;
    /**
     * 负责人
     */
    @ColumnMean("work_manage")
    private String workManage;
    /**
     * 负责人联系方式
     */
    @ColumnMean("manage_phone")
    private String managePhone;
    /**
     * 具体位置（位置信息）
     */
    @ColumnMean("position")
    private String position;
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
     * 转换之后的经度(pc展示84坐标系)
     */
    @ColumnMean("change_lgtd")
    private Double changeLgtd;
    /**
     * 转换之后的纬度(pc展示84坐标系)
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


}
