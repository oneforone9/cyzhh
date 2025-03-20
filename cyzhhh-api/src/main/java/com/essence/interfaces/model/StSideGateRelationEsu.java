package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝负责人关联表更新实体
 *
 * @author liwy
 * @since 2023-04-13 17:51:21
 */

@Data
public class StSideGateRelationEsu extends Esu {

    private static final long serialVersionUID = 555687527180037780L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 边闸基础表主键id
     */
    private Integer sideGateId;

    /**
     * 行政负责人
     */
    private String xzfzr;

    /**
     * 行政负责人联系方式
     */
    private String xzfzrPhone;

    /**
     * 现场负责人
     */
    private String xcfzr;

    /**
     * 现场负责人联系方式
     */
    private String xcfzrPhone;

    /**
     * 现场负责人需要和用户ID
     */
    private String xcfzrUserId;

    /**
     * 技术负责人
     */
    private String jsfzr;

    /**
     * 技术负责人联系方式
     */
    private String jsfzrPhone;

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
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;


}
