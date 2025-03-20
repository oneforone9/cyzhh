package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 第三方服务公司基础表更新实体
 *
 * @author zhy
 * @since 2023-02-16 11:58:35
 */

@Data
public class StCompanyBaseEsu<T> extends Esu {

    private static final long serialVersionUID = -22001492381131643L;

    private String id;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 服务年份
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date serviceYear;

    /**
     * 负责人
     */
    private String manageName;

    /**
     * 负责人联系方式
     */
    private String managePhone;

    /**
     * 所属单位主键
     */
    private String unitId;

    /**
     * 所属单位名称
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

    /**
     * 服务类型
     */
    private List<StCompanyBaseRelationEsu> serviceType;

    /**
     * 管护河段
     */
    private List<StCompanyBaseRelationEsu> manageRiver;


}
