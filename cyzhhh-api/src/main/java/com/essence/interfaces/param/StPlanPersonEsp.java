package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 三方养护人员信息表参数实体
 *
 * @author liwy
 * @since 2023-07-17 14:53:56
 */

@Data
public class StPlanPersonEsp extends Esp {

    private static final long serialVersionUID = -43013272245688637L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 养护人员id
     */
    @ColumnMean("person_id")
    private String personId;
    /**
     * 养护人员
     */
    @ColumnMean("plan_person")
    private String planPerson;
    /**
     * 联系方式
     */
    @ColumnMean("plan_phone")
    private String planPhone;
    /**
     * 第三方服务公司主键id
     */
    @ColumnMean("company_id")
    private String companyId;
    /**
     * 公司名称
     */
    @ColumnMean("company")
    private String company;
    /**
     * 所属单位主键
     */
    @ColumnMean("unit_id")
    private String unitId;
    /**
     * 所属单位名称
     */
    @ColumnMean("unit_name")
    private String unitName;
    /**
     * 三方公司负责人
     */
    @ColumnMean("name")
    private String name;
    /**
     * 三方公司负责人id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 三方公司负责人手机号
     */
    @ColumnMean("phone")
    private String phone;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;

    /**
     * 状态(0启用 1停用)
     */
    @ColumnMean("status")
    private String status;


}
