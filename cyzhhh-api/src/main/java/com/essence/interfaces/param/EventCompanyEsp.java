package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 第三方服务公司人员配置参数实体
 *
 * @author majunjie
 * @since 2023-06-05 11:25:06
 */

@Data
public class EventCompanyEsp extends Esp {

    private static final long serialVersionUID = -27544130324158001L;

    @ColumnMean("id")
    private String id;
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
     * 服务年份
     */

    @ColumnMean("service_year")
    private String serviceYear;
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
     * 数据类型（1-河道绿化保洁  2-闸坝运行养护）
     */
    @ColumnMean("type")
    private Integer type;
    /**
     * 河段id
     */
    @ColumnMean("river_id")
    private String riverId;
    /**
     * 河名称
     */
    @ColumnMean("rvnm")
    private String rvnm;
    /**
     * 处理人
     */
    @ColumnMean("name")
    private String name;
    /**
     * 处理人id
     */
    @ColumnMean("user_id")
    private String userId;
    /**
     * 处理人手机号
     */
    @ColumnMean("phone")
    private String phone;
    /**
     * 是否负责人0否1是
     */
    @ColumnMean("f_type")
    private Integer fType;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;



}
