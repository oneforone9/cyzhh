package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esu;
import lombok.Data;

import java.util.Date;

/**
 * 第三方服务公司人员配置更新实体
 *
 * @author majunjie
 * @since 2023-06-05 11:24:58
 */

@Data
public class EventCompanyEsu extends Esu {

    private static final long serialVersionUID = 850013259682721587L;

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
     * 所属管辖单位
     */

    private String unitName;

    /**
     * 服务类型（1-河道绿化保洁  2-闸坝运行养护）
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
     * 处理人联系方式
     */

    private String phone;

    /**
     * 是否负责人0否1是
     */
    private Integer fType;

    /**
     * 新增时间
     */
    private Date gmtCreate;



}
