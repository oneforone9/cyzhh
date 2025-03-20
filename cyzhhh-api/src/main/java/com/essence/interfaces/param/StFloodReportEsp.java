package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 汛情上报表参数实体
 *
 * @author liwy
 * @since 2023-03-13 14:27:06
 */

@Data
public class StFloodReportEsp extends Esp {

    private static final long serialVersionUID = -33048878924674437L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private String id;
    /**
     * 事件描述
     */
    @ColumnMean("event_describe")
    private String eventDescribe;
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
     * 发生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("occur_time")
    private Date occurTime;
    /**
     * 来源
     */
    @ColumnMean("source")
    private String source;
    /**
     * 上报人
     */
    @ColumnMean("report_person")
    private String reportPerson;
    /**
     * 上报人联系方式
     */
    @ColumnMean("report_phone")
    private String reportPhone;
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

    /**
     * 具体位置
     * @mock 北京
     */
    @Size(max = 200, message = "具体位置的最大长度:200")
    private String position;


}
