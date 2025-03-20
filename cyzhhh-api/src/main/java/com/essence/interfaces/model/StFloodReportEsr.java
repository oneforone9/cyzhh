package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


/**
 * 汛情上报表返回实体
 *
 * @author liwy
 * @since 2023-03-13 14:27:13
 */

@Data
public class StFloodReportEsr extends Esr {

    private static final long serialVersionUID = -94739939396700342L;


    /**
     * 主键
     */
    private String id;


    /**
     * 事件描述
     */
    private String eventDescribe;


    /**
     * 所属河流id
     */
    private String stBRiverId;
    /**
     * 所属河道名称
     */
    private String reaName;

    /**
     * 来源
     */
    private String source;


    /**
     * 上报人
     */
    private String reportPerson;


    /**
     * 上报人联系方式
     */
    private String reportPhone;


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
     * 具体位置
     * @mock 北京
     */
    @Size(max = 200, message = "具体位置的最大长度:200")
    private String position;


    /**
     * 上报的图片文件集合
     */
    private List<FileBaseEsr> reportFileList;


}
