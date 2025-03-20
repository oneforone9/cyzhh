package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.essence.interfaces.vaild.Insert;
import com.essence.interfaces.vaild.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 工单巡查轨迹更新实体
 *
 * @author zhy
 * @since 2022-11-09 17:49:06
 */

@Data
public class WorkorderTrackEsu extends Esu {

    private static final long serialVersionUID = -83292880446034666L;


    /**
     * 主键
     * @mock 123asdqw1
     */
    @NotEmpty(groups = Update.class, message = "主键不能为空")
    private String id;

    /**
     * 经度（小程序端上传火星坐标系)
     * @mock 111.11
     */
    @NotNull(groups = Insert.class, message = "经度（小程序端上传火星坐标系)不能为空")
    private Double lgtd;

    /**
     * 纬度（小程序端上传火星坐标系)
     * @mock 11.11
     */
    @NotNull(groups = Insert.class, message = "纬度（小程序端上传火星坐标系)不能为空")
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
     * 工单主键
     * @mock 123asdqwe
     */
    @NotEmpty(groups = Insert.class, message = "工单主键不能为空")
    private String orderId;

    /**
     * 新增时间
     * @ignore
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 速度
     */
    private Double speed;


}
