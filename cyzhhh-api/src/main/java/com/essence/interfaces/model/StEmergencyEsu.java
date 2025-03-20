package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 抢险队基本信息表更新实体
 *
 * @author liwy
 * @since 2023-04-13 16:15:36
 */

@Data
public class StEmergencyEsu extends Esu {

    private static final long serialVersionUID = -70957594514759185L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 抢险队名称
     */
    private String emergencyName;

    /**
     * 负责人
     */
    private String fzr;

    /**
     * 联系方式
     */
    private String fzrrPhone;

    /**
     * 管护河道
     */
    private String manageRiver;

    /**
     * 负责类型
     */
    private String type;

    /**
     * 规模
     */
    private String scope;

    /**
     * 单位id
     */
    private String unitId;

    /**
     * 管理单位
     */
    private String unitName;

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


}
