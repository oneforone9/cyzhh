package com.essence.interfaces.model;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 第三方服务公司人员配置返回实体
 *
 * @author majunjie
 * @since 2023-06-05 11:25:08
 */

@Data
public class EventCompanyList  {

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
    private List<String> riverIdList;

    /**
     * 河名称
     */
    private List<String> rvnmList;


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

}
