package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 抢险队基本信息表参数实体
 *
 * @author liwy
 * @since 2023-04-13 16:15:38
 */

@Data
public class StEmergencyEsp extends Esp {

    private static final long serialVersionUID = 115889867963589927L;

    /**
     * 主键
     */
    @ColumnMean("id")
    private Integer id;
    /**
     * 抢险队名称
     */
    @ColumnMean("emergency_name")
    private String emergencyName;
    /**
     * 负责人
     */
    @ColumnMean("fzr")
    private String fzr;
    /**
     * 联系方式
     */
    @ColumnMean("fzrr_phone")
    private String fzrrPhone;
    /**
     * 管护河道
     */
    @ColumnMean("manage_river")
    private String manageRiver;
    /**
     * 负责类型
     */
    @ColumnMean("type")
    private String type;
    /**
     * 规模
     */
    @ColumnMean("scope")
    private String scope;
    /**
     * 单位id
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 管理单位
     */
    @ColumnMean("unit_name")
    private String unitName;
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


}
