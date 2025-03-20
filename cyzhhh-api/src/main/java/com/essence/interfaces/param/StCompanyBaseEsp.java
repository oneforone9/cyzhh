package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 第三方服务公司基础表参数实体
 *
 * @author zhy
 * @since 2023-02-16 11:58:42
 */

@Data
public class StCompanyBaseEsp extends Esp {

    private static final long serialVersionUID = -32614383383658343L;

                    @ColumnMean("id")
    private String id;
        /**
     * 公司名称
     */            @ColumnMean("company")
    private String company;
        /**
     * 服务年份
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("service_year")
    private Date serviceYear;
        /**
     * 负责人
     */            @ColumnMean("manage_name")
    private String manageName;
        /**
     * 负责人联系方式
     */            @ColumnMean("manage_phone")
    private String managePhone;
        /**
     * 所属单位主键
     */            @ColumnMean("unit_id")
    private String unitId;
        /**
     * 所属单位名称
     */            @ColumnMean("unit_name")
    private String unitName;
        /**
     * 备注
     */            @ColumnMean("remark")
    private String remark;
        /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @ColumnMean("gmt_create")
    private Date gmtCreate;
        /**
     * 创建者
     */            @ColumnMean("creator")
    private String creator;


}
