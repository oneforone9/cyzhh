package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 闸坝负责人关联表参数实体
 *
 * @author liwy
 * @since 2023-04-13 17:51:24
 */

@Data
public class StSideGateRelationEsp extends Esp {

    private static final long serialVersionUID = 816379002905610932L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 边闸基础表主键id
     */
    @ColumnMean("side_gate_id")
    private Integer sideGateId;
    /**
     * 行政负责人
     */
    @ColumnMean("xzfzr")
    private String xzfzr;
    /**
     * 行政负责人联系方式
     */
    @ColumnMean("xzfzr_phone")
    private String xzfzrPhone;
    /**
     * 现场负责人
     */
    @ColumnMean("xcfzr")
    private String xcfzr;
    /**
     * 现场负责人联系方式
     */
    @ColumnMean("xcfzr_phone")
    private String xcfzrPhone;
    /**
     * 现场负责人需要和用户ID
     */
    @ColumnMean("xcfzr_user_id")
    private String xcfzrUserId;
    /**
     * 技术负责人
     */
    @ColumnMean("jsfzr")
    private String jsfzr;
    /**
     * 技术负责人联系方式
     */
    @ColumnMean("jsfzr_phone")
    private String jsfzrPhone;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
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
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_modified")
    private Date gmtModified;


}
