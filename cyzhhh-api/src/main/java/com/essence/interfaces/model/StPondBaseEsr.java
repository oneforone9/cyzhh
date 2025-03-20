package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 易积滞水点基础表返回实体
 *
 * @author liwy
 * @since 2023-04-03 14:45:35
 */

@Data
public class StPondBaseEsr extends Esr {

    private static final long serialVersionUID = -19307005282968026L;


    /**
     * 主键
     */
    private Integer id;


    /**
     * 积水点名称
     */
    private String pondName;


    /**
     * 积水类别
     */
    private String pondType;


    /**
     * 积水面积(㎡)
     */
    private String pondArea;


    /**
     * 积水面积(㎡)
     */
    private String pondDepth;


    /**
     * 消除时间（h）
     */
    private String removeTime;


    /**
     * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
     */
    private Double lgtd;


    /**
     * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
     */
    private Double lttd;


    /**
     * 所在区域
     */
    private String area;


    /**
     * 措施类别
     */
    private String type;


    /**
     * 备注
     */
    private String remark;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 创建者
     */
    private String creator;

    /**
     * 抢险队负责人
     */
    private String qxdfzr;
    /**
     *抢险队负责人电话
     */
    private String qxdfzrdh;

    /**
     *责任单位1
     */
    private String zrdw1;
    /**
     *责任单位负责人1
     */
    private String zrdwfzr1;
    /**
     * 责任单位负责人电话1
     */
    private String zrdwdh1;
    /**
     * 责任单位2
     */
    private String zrdw2;

    /**
     * 责任单位负责人2
     */
    private String zrdwfzr2;
    /**
     * 责任单位负责人电话2
     */
    private String zrdwdh2;
    /**
     * 责任单位3
     */
    private String zrdw3;
    /**
     * 责任单位负责人3
     */
    private String zrdwfzr3;

    /**
     * 责任单位负责人电话3
     */
    private String zrdwdh3;


}
