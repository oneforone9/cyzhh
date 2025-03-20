package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 第三方服务公司人员配置返回实体
 *
 * @author majunjie
 * @since 2023-06-05 11:25:08
 */

@Data
public class EventCompanyEsr extends Esr {

    private static final long serialVersionUID = -68390950195701623L;

    private String id;


    /**
     * 第三方服务公司主键id
     */
    private String companyId;


    /**
     * 公司名称
     */
    private String company;


    /**
     * 服务年份
     */

    private String serviceYear;


    /**
     * 所属单位主键
     */
    private String unitId;


    /**
     * 所属单位名称
     */
    private String unitName;


    /**
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    private Integer type;


    /**
     * 河段id
     */
    private String riverId;


    /**
     * 河名称
     */
    private String rvnm;


    /**
     * 处理人
     */
    private String name;


    /**
     * 处理人id
     */
    private String userId;


    /**
     * 处理人手机号
     */
    private String phone;


    /**
     * 是否负责人0否1是
     */
    private Integer fType;


    /**
     * 新增时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 服务类型
     */
    private List<StCompanyBaseRelationEsu> serviceType;

    /**
     * 管护河段
     */
    private List<StCompanyBaseRelationEsu> manageRiver;
    private String manageName;
    private String managePhone;
}
