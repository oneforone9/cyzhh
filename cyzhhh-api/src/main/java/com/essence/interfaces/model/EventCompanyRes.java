package com.essence.interfaces.model;


import com.essence.dao.entity.EventCompanyDto;
import lombok.Data;

import java.util.List;

/**
 * 第三方服务公司人员配置返回实体
 * @author liwy
 * @since 2023-07-13 14:40:38
 */

@Data
public class EventCompanyRes {

    /**
     * 公司id
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
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    private Integer type;

    /**
     * 负责人
     */
    private List<EventCompanyR> companUserList;


}
