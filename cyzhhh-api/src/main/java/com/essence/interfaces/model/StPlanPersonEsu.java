package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 三方养护人员信息表更新实体
 *
 * @author liwy
 * @since 2023-07-17 14:53:49
 */

@Data
public class StPlanPersonEsu extends Esu {

    private static final long serialVersionUID = 537705326509082121L;

    private Integer id;

    /**
     * 养护人员id
     */
    private String personId;

    /**
     * 养护人员
     */
    private String planPerson;

    /**
     * 联系方式
     */
    private String planPhone;

    /**
     * 第三方服务公司主键id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 三方公司负责人
     */
    private String name;

    /**
     * 三方公司负责人id
     */
    private String userId;

    /**
     * 三方公司负责人手机号
     */
    private String phone;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 状态(0启用 1停用)
     */
    private String status;


}
