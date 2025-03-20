package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-02-16 12:01:47
 */

@Data
public class StCompanyBaseRelationEsp extends Esp {

    private static final long serialVersionUID = 953214322852376357L;

    @ColumnMean("id")
    private Integer id;
    /**
     * 第三方服务公司主键id
     */
    @ColumnMean("st_company_base_id")
    private String stCompanyBaseId;
    /**
     * 数据类型（1-服务类型  2-管护河道）
     */
    @ColumnMean("type")
    private String type;
    /**
     * 服务类型或管护河段id
     */
    @ColumnMean("data_id")
    private String dataId;
    /**
     * 备注
     */
    @ColumnMean("remark")
    private String remark;
    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("gmt_create")
    private Date gmtCreate;
    /**
     * 创建者
     */
    @ColumnMean("creator")
    private String creator;

    /**
     * 服务类型或管护河段名称
     */
    private String dataName;


}
